package clonegod.spider.core.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.Page;

import clonegod.spider.common.util.CommonUtil;
import clonegod.spider.core.filter.UrlFilter;

/**
 * 为每个请求的页面封装相关的数据
 * 
 */
public class PageTask implements Comparable<PageTask>, Serializable {
	
	private static final long serialVersionUID = -1217401249193534898L;

	String taskName;
	
	String dynamicTaskName;
	
	int taskOrder;
	
	private PageRequest pageRequest = new PageRequest();
	
	private UrlFilter urlFilter = new UrlFilter(); // 模式匹配，凡是匹配的资源都会被拦截
	
	private PageWebClientOption webClientOption = new PageWebClientOption(); // 每个页面都可以单独定制webclient的某些属性
	
	private PageExtract pageExtract = new PageExtract();
	
	private boolean pauseNext; // 是否在当前页面请求执行结束后暂停后续任务：需要图片验证码，短信验证码
	
	private PageTask prev; // 每次请求当前页时都需要访问的前置页面.prevTask的order配置为-1
	
	private PageTask parentTask;
	
	private List<PageTask> subTaskList = new ArrayList<PageTask>(); // 请求该页面之后的进行的循环子任务（动态产生）
	
	private String subPageTaskName; // 标识该页面是否为某个页面的子任务
	
	private Page resPage;
	
	private boolean success; // 页面是否清楚成功
	
	private int reTryCount;
	
	
	public PageTask clone() {
		return (PageTask) CommonUtil.clone(this);
	}
	
	@Override
	public int compareTo(PageTask other) {
		return this.taskOrder - other.taskOrder;
	}
	
	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("taskOrder=").append(taskOrder).append(", ");
		buf.append("taskName=").append(taskName);
		if(dynamicTaskName != null) {
			buf.append(", dynamicTaskName=").append(dynamicTaskName);
		}
		return buf.toString();
	}

	
	public String getTaskName() {
		if(dynamicTaskName != null) {
			return dynamicTaskName;
		}
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public int getTaskOrder() {
		return taskOrder;
	}

	public void setTaskOrder(int taskOrder) {
		this.taskOrder = taskOrder;
	}

	public PageRequest getPageRequest() {
		return pageRequest;
	}

	public void setPageRequest(PageRequest pageRequest) {
		this.pageRequest = pageRequest;
	}

	public PageWebClientOption getWebClientOption() {
		return webClientOption;
	}

	public void setWebClientOption(PageWebClientOption webClientOption) {
		this.webClientOption = webClientOption;
	}

	public PageExtract getPageExtract() {
		return pageExtract;
	}

	public void setPageExtract(PageExtract pageExtract) {
		this.pageExtract = pageExtract;
	}

	public boolean isPauseNext() {
		return pauseNext;
	}

	public void setPauseNext(boolean pauseNext) {
		this.pauseNext = pauseNext;
	}

	public PageTask getPrev() {
		return prev;
	}

	public void setPrev(PageTask prev) {
		this.prev = prev;
	}

	public Page getResPage() {
		return resPage;
	}

	public void setResPage(Page resPage) {
		this.resPage = resPage;
	}
	
	public UrlFilter getUrlFilter() {
		return urlFilter;
	}

	public void setUrlFilter(UrlFilter urlFilter) {
		this.urlFilter = urlFilter;
	}
	
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}


	public int getReTryCount() {
		return reTryCount;
	}

	public void setReTryCount(int reTryCount) {
		this.reTryCount = reTryCount;
	}

	public List<PageTask> getSubTaskList() {
		return subTaskList;
	}

	public void setSubTaskList(List<PageTask> subTaskList) {
		this.subTaskList = subTaskList;
	}

	public String getSubPageTaskName() {
		return subPageTaskName;
	}

	public void setSubPageTaskName(String subPageTaskName) {
		this.subPageTaskName = subPageTaskName;
	}

	public PageTask getParentTask() {
		return parentTask;
	}

	public void setParentTask(PageTask parentTask) {
		this.parentTask = parentTask;
	}

	public void setDynamicTaskName(String dynamicTaskName) {
		this.dynamicTaskName = dynamicTaskName;
	}

}
