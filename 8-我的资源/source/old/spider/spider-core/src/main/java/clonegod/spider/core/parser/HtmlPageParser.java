package clonegod.spider.core.parser;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.imageio.ImageReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import clonegod.spider.common.constant.SpiderConst;
import clonegod.spider.common.util.SpelEvaluation;
import clonegod.spider.core.page.Field;

public class HtmlPageParser extends PageParser {
	
	Logger logger = LoggerFactory.getLogger(HtmlPageParser.class);

	@Override
	public Object parse() {
		Object value = null;
		if(! page.isHtmlPage()) {
			throw new RuntimeException("Page type error, not html page!");
		}
		
		HtmlPage htmlPage = (HtmlPage) page;
		if(Objects.equals(field.getName(), SpiderConst.IMAGE_CODE)) {
			value = getImage(htmlPage, field);
		} else if(field.getExpression().startsWith(SpiderConst.SPEL_PREFIX)) {
			value = SpelEvaluation.getValue(cachedArgs, field.getExpression());
			if(value != null && value.toString().startsWith(SpiderConst.JAVASCRIPT_PREFIX)) {
				String jsCode = value.toString().replaceFirst(SpiderConst.JAVASCRIPT_PREFIX, "");
				Object jsResult = htmlPage.executeJavaScript(jsCode).getJavaScriptResult();
				value = jsResult;
			}
		}else{
			// SgmlPage -> HTML or XML
			List<DomNode> domNodes = ((SgmlPage)page).getByXPath(field.getExpression());
			if(! CollectionUtils.isEmpty(domNodes)) {
				List<String> dataList = new ArrayList<String>(domNodes.size());
				for (DomNode domNode : domNodes) {
					if(domNode instanceof HtmlInput) {
						dataList.add(((HtmlInput) domNode).getValueAttribute());
					} else {
						dataList.add(domNode.getTextContent());
					}
				}
				value = dataList;
				if(dataList.size() == 1) {
					value = dataList.get(0);
				}
			}
		}
		
		return value;
	}

	private BufferedImage getImage(HtmlPage htmlPage, Field field) {
		BufferedImage bufferedImage = null;
		try {
			HtmlImage image = (HtmlImage) htmlPage.getFirstByXPath(field.getExpression());
			ImageReader reader = image.getImageReader();
			int index = 0;
			while(true) {
				try {
					bufferedImage = reader.read(index++);
				} catch (Exception e) {
					break;
				}
			}
		} catch (IOException e) {
			throw new RuntimeException("获取图片验证码异常", e);
		}
		return bufferedImage;
	}

}
