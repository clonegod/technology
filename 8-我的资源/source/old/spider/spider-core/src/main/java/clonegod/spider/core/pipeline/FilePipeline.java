package clonegod.spider.core.pipeline;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import clonegod.spider.common.constant.SpiderConst;
import clonegod.spider.core.page.PageTask;

class FilePipeline extends Pipeline {
	
	@Override
	public boolean execute(String sessionId, PageTask pageTask) {
		File file = new File(SpiderConst.TEMP_DIR+"/html",  sessionId+"_"+pageTask.getTaskName().concat(".html"));
		file.getParentFile().mkdirs();
		
		try {
			FileUtils.write(file, 
					pageTask.getResPage().getWebResponse().getContentAsString(), 
					pageTask.getPageRequest().getCharset());
			logger.info("Save file to {}", file.getAbsolutePath());
		} catch (IOException e) {
			logger.error("Write page source to file failed", e);
		}
		
		return true;
	}

}
