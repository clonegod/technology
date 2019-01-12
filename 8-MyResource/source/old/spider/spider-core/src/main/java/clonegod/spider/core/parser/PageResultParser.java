package clonegod.spider.core.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import clonegod.spider.core.page.Field;
import clonegod.spider.core.page.Function;
import clonegod.spider.core.page.PageTask;

public class PageResultParser {
	private Logger logger = LoggerFactory.getLogger(PageResultParser.class);
	
	private Map<String, Object> cachedArgs;
	
	private PageTask pageTask;
	
	public PageResultParser(Map<String,Object> cachedArgs, PageTask pageTask) {
		this.cachedArgs = cachedArgs;
		this.pageTask = pageTask;
	}

	/**
	 * 根据配置的页面特性字段，判断请求是否成功
	 * @return
	 */
	public Map<String, Object> parseSuccessFeature() {
		return parse(pageTask.getPageExtract().getSuccessFields());
	}
	
	/**
	 * 根据配置的页面特征字段，从页面提取错误提示
	 * @return
	 */
	public Map<String, Object> parseFailedFeature() {
		return parse(pageTask.getPageExtract().getFailedFields());
	}
	
	/**
	 * 根据页面配置，从响应结果中提取辅助字段
	 * @return
	 */
	public Map<String, Object> parseExtArgs() {
		return parse(pageTask.getPageExtract().getExtFields());
	}
	
	/**
	 * 根据页面配置，从响应结果中提取业务数据字段
	 * @return
	 */
	public Map<String, Object> parseBusinessData() {
		return parse(pageTask.getPageExtract().getBusinessDataFields());
	}
	
	/**
	 * 仅提取请求后续页面时需要的参数。
	 * 业务数据在抓取完成后使用多线程批量解析提取^_^
	 * @return 
	 */
	private Map<String, Object> parse(List<Field> fields) {
		Map<String, Object> argsMap = new HashMap<>();
		try {
			Object value = null;
			for(Field field : fields) {
				PageParser parser = getParser(field);
				value = parser.parse();
				if(value != null) {
					// 使用spel对对value进一步处理
					List<Function> funcs = field.getExtFuncs();
					for(Function func : funcs) {
						value = func.execute(value);
					}
				}
				argsMap.put(field.getName(), value);
			}
		} catch (Exception e) {
			logger.error("parse page result error!", e);
		}
		return argsMap;
	}

	private PageParser getParser(Field field) {
		PageParserType parserType = PageParserType.valueOf(field.getParserType().name());
		PageParser parser = parserType.getParser();
		parser.setPage(pageTask.getResPage());
		parser.setField(field);
		parser.setCachedArgs(cachedArgs);
		return parser;
	}
}
