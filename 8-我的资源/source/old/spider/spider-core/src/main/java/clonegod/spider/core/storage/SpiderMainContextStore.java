package clonegod.spider.core.storage;

import clonegod.spider.core.engine.SpiderMain;

public interface SpiderMainContextStore {
	
	public void store(SpiderMain spiderMain);
	
	public SpiderMain reload(String sessionId);
	
}
