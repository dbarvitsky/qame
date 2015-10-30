package org.qame.gen;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Filters {

	private ObjectMapper mapper;
	private List<DataSource> dataSources;
	
	public List<Filter> parse( String expr ) throws Exception {
		String[] filters = expr.split("\\|");
		List<Filter> result = new ArrayList<Filter>();
		for (String fexpr : filters) {
			if (fexpr == null) continue;
			fexpr = fexpr.trim().toLowerCase();
			if (fexpr.length() == 0) continue;
			Filter f = null;
			if ("json".equals(fexpr)) f = json();
			else if ("load".equals(fexpr)) f = load();
			else if ("non-null".equals(fexpr)) f= notNull();
			else if ("string".equals(fexpr)) f=string();
			else throw new Exception("Invalid filter name "+fexpr);
			
			result.add(f);
		}
		return result;
	}
	
	public Filter json()
	{
		return new Filter() {
			public Object filter(Object value) throws Exception {
				if (value instanceof ObjectNode) return value;
				else if (value instanceof String) return mapper.reader().readTree((String)value);
				else return mapper.reader().readTree(mapper.writer().writeValueAsString(value));
			}

			public boolean isAccepted(Object value) {
				return true;
			}
		};
	}
	public Filter load()
	{
		return new Filter() {
			public Object filter(Object value) throws Exception {
				java.net.URI uri = null;
				if (value instanceof java.net.URI) uri = (java.net.URI)value;
				else if (value instanceof String) uri = java.net.URI.create((String)value).normalize();
				else throw new Exception("String or URI is expected");
				for (DataSource ds : dataSources ) {
					if (uri.getScheme().equals(ds.getSchema())) {
						return ds.load(uri);
					}
				}
				throw new Exception("No data sources support schema "+uri.getScheme()+", cannot load "+uri.toString());
			}

			public boolean isAccepted(Object value) {
				return true;
			}
		};		
	}
	
	public Filter notNull()
	{
		return new Filter() {
			public Object filter(Object value) throws Exception {
				return value;
			}

			public boolean isAccepted(Object value) {
				return !(value instanceof String && ((String)value).length() == 0) || value != null;
			}
		};		
	}
	
	public Filter string()
	{
		return new Filter() {
			public Object filter(Object value) throws Exception {
				if (value instanceof String) return value;
				else return mapper.writer().writeValueAsString(value);
			}

			public boolean isAccepted(Object value) {
				return true;
			}
		};		
	}
	
	public class EvaluationResult {
		public Object value = null;
		public boolean isRejected = false;
	}
	
	public EvaluationResult evaluate( List<Filter> filters, Object value ) throws Exception {
		EvaluationResult result = new EvaluationResult();
		result.value = value;

		for (Filter f : filters ) {
			if (!f.isAccepted(result.value)) {
				result.isRejected = true;
			} else {
				result.value = f.filter(result.value);
			}
		}
		
		return result;
	}
	
}
