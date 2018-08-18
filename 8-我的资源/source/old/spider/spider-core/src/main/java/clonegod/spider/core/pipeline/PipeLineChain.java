package clonegod.spider.core.pipeline;

import clonegod.spider.core.page.PageTask;

public class PipeLineChain {
	private final Pipeline firstPipeline;
	
	private Pipeline filePipeline = new FilePipeline();
	private Pipeline mongoPipeline = new MongoPipeline();
	private Pipeline mySqlPipeline = new MySqlPipeline();

	private PipeLineChain() {
		firstPipeline = filePipeline; // set the start pipeline
		firstPipeline.next(mySqlPipeline).next(mongoPipeline); // set order of the pipeline chain
	}
	
	private static class PipelineChainHolder {
		private static PipeLineChain INSTANCE = new PipeLineChain();
	}
	
	public static PipeLineChain getPipelineChain() {
		return PipelineChainHolder.INSTANCE;
	}
	
	/**
	 * 启动Pipeline
	 * 
	 * @param pageTask 需要进行持久化操作的页面对象
	 */
	public void propcess(String sessionId, PageTask pageTask) {
		firstPipeline.process(sessionId, pageTask);
	}

}
