package clonegod.spider.core.engine;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import clonegod.spider.common.constant.SpiderConst;
import clonegod.spider.common.util.ImageUtil;
import clonegod.spider.common.util.SwingDialogUI;
import clonegod.spider.core.page.PageContext;
import clonegod.spider.core.page.PageTask;

public class SpiderMain implements Serializable {
	
	private static final long serialVersionUID = -6466829882164115827L;

	Logger logger = LoggerFactory.getLogger(SpiderMain.class);
	
	PageContext pageContext;
	
	Scheduler scheduler;

	public SpiderMain(PageContext pageContext, Map<String, PageTask> taskMap) {
		this.pageContext = pageContext;
		initScheduler(taskMap);
	}
	
	private void initScheduler(Map<String, PageTask> taskMap) {
		LinkedList<PageTask> taskQueue = new LinkedList<>();
		Collection<PageTask> tasks = taskMap.values();
		taskQueue.addAll(tasks);
		Collections.sort(taskQueue);
		this.scheduler = new Scheduler(pageContext, taskQueue, taskMap);
	}
	
	public void run() {
		PageTask pageTask = scheduler.start();
		if(pageTask == null) {
			logger.info("All cachedArgs={}", pageContext.getCachedArgs());
			logger.info("SpiderMain's job done, all task have been processed!");
		} else {
			if(pageTask.isSuccess() && pageTask.isPauseNext()) {
				parsePausedPage(pageTask);
				logger.info("page request paused, current page name={}, all cachedArgs={}", pageTask.getTaskName(), pageContext.getCachedArgs());
			} else {
				logger.warn("page request failed, message={}", pageTask.getPageExtract().getFailedFields());
			}
		}
	}
	
	/**
	 * 从暂停页面中解析数据
	 * 
	 * @param pageTask
	 */
	private void parsePausedPage(PageTask pageTask) {
		Map<String,Object> cachedArgs = pageContext.getCachedArgs();
		Assert.notNull(cachedArgs, "Paused page should contains args for reset page");
		if(cachedArgs.containsKey(SpiderConst.IMAGE_CODE)) {
			BufferedImage bufferedImage = (BufferedImage) cachedArgs.get(SpiderConst.IMAGE_CODE);
			String imageCode = SwingDialogUI.display(bufferedImage);
			File file = new File(SpiderConst.TEMP_DIR+"/image", pageContext.getSessionId()+"_"+imageCode+".png");
			ImageUtil.bufferedImageToFile(bufferedImage, file, "png");
			cachedArgs.replace(SpiderConst.IMAGE_CODE, imageCode);
		}
	}

	public PageContext getPageContext() {
		return pageContext;
	}

	public Scheduler getScheduler() {
		return scheduler;
	}

	public void releaseUselessDataBeforeStore() {
		this.scheduler.getExecutionEngine().setWebClient(null);
		this.scheduler.clearPageTaskResponse();
	}
}
