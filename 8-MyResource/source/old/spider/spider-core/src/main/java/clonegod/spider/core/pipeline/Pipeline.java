package clonegod.spider.core.pipeline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import clonegod.spider.core.page.PageTask;

abstract class Pipeline {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private Pipeline next;

	protected final Pipeline next(Pipeline pipeline) {
		this.next = pipeline;
		return next;
	}
	
	public final void process(String sessionId, PageTask pageTask) {
		logger.info("Pipeline:{} persistence page: {}", this.getClass().getSimpleName(), pageTask.getTaskName());
		boolean doNext = execute(sessionId, pageTask);
		if(doNext && next != null) {
			next.process(sessionId, pageTask);
		}
	}
	
	protected abstract boolean execute(String sessionId, PageTask pageTask);
	
}
