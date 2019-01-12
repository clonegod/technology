package spring.el.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import clonegod.spider.common.util.SpelEvaluation;

public class SPELTest {
	
	ExpressionParser parser = new SpelExpressionParser();
	
	@Test
	public void testSingleQuote() {
		// To put a single quote itself in a string, use two single quote characters.
		Expression expr = parser.parseExpression("'It''s very easy!'");
		String value = expr.getValue("", String.class);
		System.out.println(value);
	}
	
	@Test
	public void testMap1() {
		Map<String,String> rootObj = new HashMap<>();
		rootObj.put("name", "zhangsan");
		StandardEvaluationContext mapContext = new StandardEvaluationContext(rootObj);
		
		Expression expr = parser.parseExpression("'Your name is:'+['name']");
		String value = expr.getValue(mapContext, String.class);
		System.out.println(value);
		
	}
	
	@Test
	public void testFormatStringWithMapContext() {
		// 注意：使用单引号将普通字符串或符号括起来，这样springEL引擎会按普通字符串进行处理，不会对其进行计算！
		String expressionString = "spel:'javascript:strEncode('''+['bh5']+''','''+['fMcM3']+''','''+['TjXjbs4']+''','''+['fEAI5']+''');'";
		System.out.println("expressionString="+expressionString);
		
		Map<String,String> rootObj = new HashMap<>();
		rootObj.put("bh5", "62187979321788392187");
		rootObj.put("fMcM3", "pdcss123");
		rootObj.put("TjXjbs4", "css11q1a");
		rootObj.put("fEAI5", "co1qacq11");
		
		Object value = SpelEvaluation.getValue(rootObj, expressionString);
		System.out.println(value);
		
	}
	
	@Test
	public void testFormatUrl() {
		Map<String,List<String>> rootObj = new HashMap<>();
		List<String> items = new ArrayList<>();
		items.add("123");
		items.add("abc");
		rootObj.put("loop:mainRecord", items);
		
		String url = "spel:'http://www.bjgjj.gov.cn/wsyw/wscx/'+['loop:mainRecord']['%s']";
		
		for(int i = 0; i < 2; i++) {
			String newUrl = String.format(url, i);
			System.out.println(newUrl);
			
			Object value = SpelEvaluation.getValue(rootObj, newUrl);
			System.out.println(value);
		}
		
		
	}
	
}
