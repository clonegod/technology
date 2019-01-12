package clonegod.spider.core.page;

import java.io.Serializable;
import java.util.LinkedList;

import clonegod.spider.core.parser.PageParserType;

public class Field implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1442311356994087298L;

	private String name;
	
	private PageParserType parserType;

	private String expression;

	private LinkedList<Function> extFuncs = new LinkedList<>(); // 对结果进一步进行清洗、转换
	
	private Object value;
	
	public Field() {
		super();
	}

	public Field(String fieldName, PageParserType parserType, String expression, String...funcExprs) {
		super();
		this.name = fieldName;
		this.parserType = parserType;
		this.expression = expression;
		for(String expr : funcExprs) {
			this.extFuncs.add(new Function(expr));
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PageParserType getParserType() {
		return parserType;
	}

	public void setParserType(PageParserType parserType) {
		this.parserType = parserType;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public LinkedList<Function> getExtFuncs() {
		return extFuncs;
	}

	public void setExtFuncs(LinkedList<Function> extFuncs) {
		this.extFuncs = extFuncs;
	}
	
	public LinkedList<Function> addExtFunc(Function func) {
		extFuncs.add(func);
		return this.extFuncs;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Field [name=" + name + ", value=" + value + "]";
	}

}
