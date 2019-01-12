package clonegod.spider.core.pipeline;

import clonegod.spider.core.page.PageTask;

class MongoPipeline extends Pipeline {
	
	@Override
	public boolean execute(String sessionId, PageTask pageTask) {
		
		return true;
	}


}
