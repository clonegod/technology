package spidermain.test;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.reflect.TypeToken;

import clonegod.spider.common.util.CommonUtil;
import clonegod.spider.common.util.JsonUtil;
import clonegod.spider.core.engine.MyWebClient;
import clonegod.spider.core.engine.SpiderMain;
import clonegod.spider.core.engine.SpiderThreadPool;
import clonegod.spider.core.page.PageContext;
import clonegod.spider.core.page.PageTask;
import clonegod.spider.core.storage.SpiderMainContextStore;
import clonegod.spider.core.storage.SpiderMainStoreLocalFile;

/**
 * 测试数据抓取
 * 
 * @author clonegod@163.com
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SpiderMainTest {
	static Logger logger = LoggerFactory.getLogger(SpiderMainTest.class);
	
	static String sessionId = "JKLSDFJ324189FSDJKL12";
	
	@SuppressWarnings("all")
	public static void main(String[] args) throws Exception {
		String taskJson = configPageTask();
		Map<String, PageTask> tasks = JsonUtil.fromJson(taskJson, new TypeToken<Map<String, PageTask>>() {});
		System.out.println(tasks);
		
		PageContext pageContext = new PageContext(sessionId);
		
		SpiderMain spiderMain = new SpiderMain(pageContext, tasks);
		spiderMain.run();
		
		// 保存cookie
		MyWebClient webClient = spiderMain.getScheduler().getExecutionEngine().getWebClient();
		spiderMain.getPageContext().setWebClinetCookies(webClient.getCookieManager().getCookies());
		// 清除不需要的对象-有效减小持久化对象的size
		spiderMain.releaseUselessDataBeforeStore();
		logger.info("SpiderThreadPool.getUnCompleteTaskCount()="+SpiderThreadPool.getUnCompleteTaskCount());
		while(SpiderThreadPool.getUnCompleteTaskCount() > 0) {
			logger.info("Wait thread pool to complete task, sleep 1 second");
			CommonUtil.threadSleep(1000);
		}
		
		SpiderMainContextStore spiderMainStore = new SpiderMainStoreLocalFile();
		spiderMainStore.store(spiderMain);	
		
		// 客户端第二次发起请求时，加载之前的环境（cookie，参数等）
		SpiderMain spiderMainV2 = spiderMainStore.reload(sessionId);
		
		spiderMainV2.run();
		
		SpiderThreadPool.shutdown();
		
		System.out.println("exit");
		
	}
	
	private static String configPageTask() throws Exception {
		Map<String, PageTask> pages = new LinkedHashMap<>();
		
		PageTask indexTask = PageTaskFactory.createIndexPage();
		PageTask lkTask = PageTaskFactory.createLkPage();
		PageTask loginTask = PageTaskFactory.createLoginPage();
		PageTask mainRecordTask = PageTaskFactory.createMainRecordPage();
		PageTask detailRecordTask = PageTaskFactory.createMainDetailPage();
		
		pages.put(indexTask.getTaskName(), indexTask);
		pages.put(lkTask.getTaskName(), lkTask);
		pages.put(loginTask.getTaskName(), loginTask);
		pages.put(mainRecordTask.getTaskName(), mainRecordTask);
		pages.put(detailRecordTask.getTaskName(), detailRecordTask);
		
		String taskJson = JsonUtil.toJson(pages);
		return taskJson;
	}

	
}
