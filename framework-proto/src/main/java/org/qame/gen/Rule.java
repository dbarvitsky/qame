package org.qame.gen;

import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class Rule {
	private List<Filter> filters;
	private List<Rule> nested;
	
	public Rule( List<Filter> filters ) {
		this.filters = filters;
	}
	
	public List<Rule> getNested() {
		return nested;
	}

	public void setNested(List<Rule> nested) {
		this.nested = nested;
	}

	public ObjectNode execute( ObjectNode current, String value ) throws Exception {
		Object result = value;
		boolean isAccepted = true;
		if (filters != null) {
			for ( Filter f : filters) {
				isAccepted = f.isAccepted(result);
				if (!isAccepted) return current; 
				result = f.filter(result);
			}
		}
		if (isAccepted) {
			return applyValue(current);
		}
		return current;
	}
	
	protected ObjectNode applyValue( ObjectNode current ) {
		return current;
	}
}
