package clonegod.spider.core.page;

import static clonegod.spider.common.constant.SpiderConst.*;

import java.io.Serializable;

import clonegod.spider.common.util.RegexUtil;
import clonegod.spider.common.util.SpelEvaluation;

public class Function implements Serializable {
	
	private static final long serialVersionUID = -5894542447035612402L;

	String expr; // spring expression language
	
	public Function(String expr) {
		super();
		this.expr = expr;
	}

	public Object execute(Object source) {
		Object value = "";
		if(source == null) {
			return value;
		}
		if(expr.toLowerCase().startsWith(SPEL_PREFIX)) {
			value = SpelEvaluation.getValue(source, expr);
		} else if(expr.toLowerCase().startsWith(REGEX_PREFIX)) {
			value = RegexUtil.matchList(source.toString(), expr.replaceFirst(REGEX_PREFIX, ""));
		} else {
			throw new RuntimeException("Cann't execute function of expr " +  expr + ", because the function type is neither [spel] nor [regex]");
		}
		return value;
	}
}
