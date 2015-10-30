package org.qame.gen;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RuleDef {
	private String expression;
	private String filters = "";
	private String defaultValue = "";
	
	public String getExpression() {
		return expression;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}
	public String getFilters() {
		return filters;
	}
	public void setFilters(String filters) {
		this.filters = filters;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	private static final Pattern rxPath = Pattern.compile("^(\\s+)([\\w\\.]+)?(\\[(\\d+|\\+|\\-)?\\])?$");
	public Rule toRule( Stack<Rule> rules, Filters filters ) throws Exception{
		if (expression == null) throw new Exception("Expression is required");
		Matcher m = rxPath.matcher(expression);
		if (m.matches()) {
			String indent = m.group(1);
			String jspath = m.group(2);
			boolean isArray = m.group(3) != null;
			String arrayExpr = m.group(4);
		}
	}
}
