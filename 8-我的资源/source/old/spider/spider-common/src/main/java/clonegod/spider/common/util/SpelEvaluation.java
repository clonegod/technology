package clonegod.spider.common.util;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class SpelEvaluation {
	
	private static final String SPEL_PREFIX = "spel:";
	
	private static final ExpressionParser spelParser = new SpelExpressionParser();
	
	/**
	 * 表达式必须以"spel:"为前缀，否则不进行解析。
	 * 
	 * @param rootObject
	 * @param expressionString
	 * @param clazz
	 * @return
	 */
	public static Object getValue(Object rootObject, String expressionString) {
		Object result = null;
		try {
			if(rootObject==null) {
				throw new NullPointerException("root object cann't be null");
			}
			if(! expressionString.toLowerCase().startsWith(SPEL_PREFIX)) {
				return expressionString;
			}
			expressionString = expressionString.replaceFirst(SPEL_PREFIX, "");
			Expression expression = spelParser.parseExpression(expressionString);
			EvaluationContext context = new StandardEvaluationContext(rootObject);
			result = expression.getValue(context);
		} catch (EvaluationException e) {
			throw new RuntimeException("spel execution error!", e);
		}
		return result;
	}
}
