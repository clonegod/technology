package clonegod.spider.core.parser;

import java.io.Serializable;

public enum PageParserType implements Serializable {
	
	HTML(HtmlPageParser.class), 
	
	
	JSON(JsonPageParsrer.class), 
	
	
	TEXT(TextPageParser.class), 
	
	
	IMAGE(ImagePageParser.class), 
	
	
	PDF(PdfPageParser.class);

	Class<? extends PageParser> parser;

	PageParserType(Class<? extends PageParser> clazz) {
		this.parser = clazz;
	}
	
	public PageParser getParser() {
		PageParser parser = null;
		try {
			parser = this.parser.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parser;
	}
}
