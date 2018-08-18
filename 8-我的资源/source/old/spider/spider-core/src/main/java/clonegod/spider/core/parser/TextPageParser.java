package clonegod.spider.core.parser;

import clonegod.spider.common.util.RegexUtil;

public class TextPageParser extends PageParser {

	@Override
	public Object parse() {
		Object result = null;
		
		String text = page.getWebResponse().getContentAsString();

		if(field.getName().startsWith("list")) {
			result = RegexUtil.matchList(text, field.getExpression());
		} else {
			result = RegexUtil.match(text, field.getExpression());
		}
		
		logger.info("parse field: name={}, value={}", field.getName(), result);
		
		return result;
	}

}
