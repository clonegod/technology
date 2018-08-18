package clonegod.spider.core.engine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.Page;

import clonegod.spider.common.constant.SpiderConst;
import clonegod.spider.common.util.SpelEvaluation;
import clonegod.spider.core.page.PageContext;
import clonegod.spider.core.page.PageTask;
import clonegod.spider.core.parser.PageResultParser;
import clonegod.spider.core.pipeline.PipeLineChain;

/**
 * 任务调度器
 *	从任务队列取出任务
 *	如果有前置任务，先执行前置任务
 *	对请求参数进行赋值（之前页面中提取到的参数值）
 *	执行当前任务
 *	执行页面请求
 *	保存页面源码
 *	从当前页面提取下一个请求需要的参数
 *	添加动态生成的新任务到任务队列（多账号、分页）
 *	是否需要暂停？是，则返回当前页面，提取验证码返回给客户端
 *	否则，获取下一个任务继续执行...
 */
public class Scheduler implements Serializable {
	
	private static final long serialVersionUID = -1520069437650839360L;

	private static final Logger logger = LoggerFactory.getLogger(Scheduler.class);

	private PageContext pageContext;
	
	private LinkedList<PageTask> taskQueue;
	
	private final Map<String, PageTask> taskMapConfig;
	
	private int nextTaskIndex = 0; // 当前任务索引号
	
	private PageTask currentTask;
	
	ExecutionEngine executionEngine;

	public Scheduler(PageContext pageContext, LinkedList<PageTask> taskQueue, Map<String, PageTask> taskMapConfig) {
		this.pageContext = pageContext;
		this.taskQueue = taskQueue;
		this.taskMapConfig = taskMapConfig;
		executionEngine = new ExecutionEngine(pageContext);
	}
	
	/**
	 * 调度页面请求<br/>
	 * 如果需要进行二次交互，则暂停请求。	
	 * @return 1.当需要暂停请求时则返回当前页面；2. 若任务全部执行完成，返回null 
	 */
	public PageTask start() {
		this.currentTask = getNextTask(null);
		while (currentTask != null) {
			doTask(currentTask);
			nextTaskIndex += 1;
			savePageTaskResponseAsync(currentTask.clone());
			if(! currentTask.isSuccess()) {
				break;
			}
			extractExtArgs();
			extractBusinessData();
			tryInsertDynamicTasks();
			if (currentTask.isPauseNext()) {
				break;
			} else {
				currentTask = getNextTask(currentTask);
			}
		}
		// 一旦任务退出，立即关闭js引擎，释放线程
		getExecutionEngine().getWebClient().shutdownJavascriptEngine();
		
		return currentTask;
	}

	private PageTask getNextTask(PageTask pageTask) {
		PageTask nextTask = null;
		if(pageTask == null) {
			pageTask = taskQueue.get(nextTaskIndex);
		}
		
		if(pageTask.getSubTaskList().size() > 0) {
			//TODO 任务如何递归？
			for(Iterator<PageTask> iter = pageTask.getSubTaskList().listIterator(); iter.hasNext(); ) {
				nextTask = iter.next();
				iter.remove();
				taskQueue.add(nextTaskIndex, nextTask);
				break;
			}
		}
		if (nextTaskIndex < taskQueue.size()) {
			nextTask = taskQueue.get(nextTaskIndex);
		}
		if(nextTask == null && pageTask.getParentTask() != null) {
			nextTask = getNextTask(pageTask.getParentTask());
		}
		return nextTask;
	}
	
	/**
	 * 执行每个页面请求任务，并提取下一个请求页面需要的参数
	 * 
	 * @param task
	 */
	private void doTask(PageTask pageTask) {
		if (pageTask.getPrev() != null) {
			doTask(pageTask.getPrev());
		}
		
		logger.info("------>processing task:{}", pageTask.toString());

		bindArgs();

		// download the page
		Page page = executionEngine.execute(pageTask);
		pageTask.setResPage(page);
		logger.info(page.getWebResponse().getContentAsString());
		
		checkPageFeature(pageTask);
	}
	
	private void checkPageFeature(PageTask pageTask) {
		boolean success = false;
		
		Map<String, Object> successMap = new PageResultParser(pageContext.getCachedArgs(), currentTask).parseSuccessFeature();
		for(Entry<String, Object> entry : successMap.entrySet()) {
			if(entry.getValue() != null) {
				success = true;
				break;
			}
		}
		if(! success) {
			Map<String, Object> failedMap = new PageResultParser(pageContext.getCachedArgs(), currentTask).parseFailedFeature();
			logger.info("PageTask failed! failed map={}", failedMap);
		}
		pageTask.setSuccess(success);
	}

	/**
	 * 对请求参数赋值
	 */
	private void bindArgs() {
		String value;
		
		// 将当前页面配置的静态参数加入到缓存中
		pageContext.getCachedArgs().putAll(currentTask.getPageRequest().getCachedArgs());
		
		// bing value to url
		String evaluatedUrl = (String) SpelEvaluation.getValue(pageContext.getCachedArgs(), currentTask.getPageRequest().getUrl());
		currentTask.getPageRequest().setUrl(evaluatedUrl);
		
		// bind value to request parameters
		if(currentTask.getPageRequest().isRawBody()) {
			value = (String) SpelEvaluation.getValue(pageContext.getCachedArgs(), currentTask.getPageRequest().getBody());
			currentTask.getPageRequest().setBody(value);
		} else {
			for(Entry<String, String> entry : currentTask.getPageRequest().getParameters().entrySet()) {
				value = (String) SpelEvaluation.getValue(pageContext.getCachedArgs(), entry.getValue());
				entry.setValue(value);
			}
		}
		
	}
	
	/**
	 * parse params for next page
	 * @param page
	 * @param pageTask
	 */
	private void extractExtArgs() {
		Map<String, Object> args = new PageResultParser(pageContext.getCachedArgs(), currentTask).parseExtArgs();
		pageContext.getCachedArgs().putAll(args);
		logger.info("page={}, extract args for next page:{}", currentTask.getTaskName(), args);
	}
	
	/**
	 * 解析页面业务数据---由于页面已经异步保存下来，所以提取业务数据可以其它系统（抓们做页面数据提取）完成此操作。
	 */
	private void extractBusinessData() {
		Map<String, Object> args = new PageResultParser(pageContext.getCachedArgs(), currentTask).parseBusinessData();
		logger.info("page={}, extract business data:{}", currentTask.getTaskName(), args);
	}

	/**
	 * 保存页面源码到本地文件/数据库
	 * 
	 * @param pageTask
	 */
	private void savePageTaskResponseAsync(PageTask pageTask) {
		SpiderThreadPool.submitCommonTask(new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				PipeLineChain.getPipelineChain().propcess(pageContext.getSessionId(), pageTask);
				return null;
			}
		});
	}

	/**
	 * 动态生成新的任务---比如分页查询时，按总页数生成新的任务（建议设置每次动态生成任务的上限）
	 */
	private void tryInsertDynamicTasks() {
		// 当前任务是否有子任务
		PageTask subTask = taskMapConfig.get(currentTask.getSubPageTaskName());
		if(subTask == null) {
			return;
		}
		
		// 子任务循环次数
		String keyName = SpiderConst.LOOP_PREFIX + currentTask.getTaskName() + ":" +  currentTask.getSubPageTaskName();
		Object object = pageContext.getCachedArgs().get(keyName);
		// 确保子任务至少被执行1次---子任务只有一个的情况，不需要多次循环
		if(object == null) {
			object = new ArrayList<>(); 
		}
		
		
		// 删除未填充参数的源task
		taskQueue.remove(subTask);
		List<?> loopItems = (List<?>) object;
		for(int index = 0; index < Math.max(loopItems.size(), 1); index++) {
			int newTaskOrder = subTask.getTaskOrder() + index;
			// 如果每次循环涉及多个参数，则拼接在一个字符串中，使用时再进行字符串切割
			PageTask dynamicTask = subTask.clone();
			dynamicTask.setTaskOrder(newTaskOrder);
			dynamicTask.setDynamicTaskName(String.format("%s_%s#%s", currentTask.getTaskName(), currentTask.getSubPageTaskName(), index+1));
			//TODO how to bind loop item to each task ? ---> bind loop item index to each task is the answer!
			formatLoopItemIndex(dynamicTask, index);
			dynamicTask.setParentTask(currentTask);
			currentTask.getSubTaskList().add(dynamicTask);
			//taskQueue.add(newTaskOrder - 1, dynamicTask); //　按序插入新任务到队列中
			logger.info("Add new dynamic task to task queue, taskName={}, taskOrder={}", dynamicTask.getTaskName(), dynamicTask.getTaskOrder());
		}
		// update order of rest task
		for(int i=currentTask.getTaskOrder()+loopItems.size(); i<taskQueue.size(); i++) {
			PageTask task = taskQueue.get(i);
			task.setTaskOrder(task.getTaskOrder() + loopItems.size() - 1);
		}
		
	}
	
	/**
	 * 设置PageTask中循环参数变量的索引位
	 * @param index
	 */
	private void formatLoopItemIndex(PageTask pageTask, int itemIndexInLoop) {
		pageTask.getPageRequest().setUrl(String.format(pageTask.getPageRequest().getUrl(), itemIndexInLoop));
		if(pageTask.getPageRequest().isRawBody()) {
			pageTask.getPageRequest().setBody(String.format(pageTask.getPageRequest().getBody(), itemIndexInLoop));
		} else {
			for(Entry<String, String> entry : pageTask.getPageRequest().getParameters().entrySet()) {
				entry.setValue(String.format(entry.getValue(), itemIndexInLoop));
			}
		}
	}
	

	public ExecutionEngine getExecutionEngine() {
		return executionEngine;
	}

	public void clearPageTaskResponse() {
		taskQueue.stream().forEach(pageTask -> pageTask.setResPage(null));
	}
}
