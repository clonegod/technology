package clonegod.spider.core.parser;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.Page;

import clonegod.spider.core.page.Field;

public abstract class PageParser {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	protected Page page;
	protected Field field;
	protected Map<String, Object> cachedArgs;
	
	public abstract Object parse();

	public void setPage(Page page) {
		this.page = page;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public void setCachedArgs(Map<String, Object> cachedArgs) {
		this.cachedArgs = cachedArgs;
	}
	
}
