package org.qame;

import java.util.HashMap;
import java.util.Map;

public class RunState {
	private Map<String,Object> data;
	private RunState parent;

	public RunState( RunState parent, Map<String, Object> data ) {
		if (data == null) data = new HashMap<String,Object>();
		this.data = data;
		this.parent = parent;
	}
	
	public RunState(Map<String,Object> data) {
		this(null,data);
	}
	
	public RunState() {
		this(null,null);
	}
	
	@SuppressWarnings("unchecked")
	public <E> E get( String key ) {
		Object t = data.get(key);
		if (t == null && parent != null) t = parent.get(key);
		return (E)t;
	}
	
	public RunState set(String key, Object value ) {
		data.put(key, value);
		return this;
	}
	
	public RunState override() {
		return new RunState(this,null);
	}
	
	public boolean has( String key ) {
		return data.containsKey(key) || (parent != null && parent.has(key));
	}
	
	public Map<String,Object> getData() {
		return data;
	}

	public void setData(Map<String,Object> data) {
		this.data = data;
	}
	
	public RunState getParent() {
		return parent;
	}

	public void setParent(RunState parent) {
		this.parent = parent;
	}	
}
