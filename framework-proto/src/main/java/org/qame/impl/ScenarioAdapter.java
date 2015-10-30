package org.qame.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.qame.Config;
import org.qame.RunState;
import org.qame.Scenario;
import org.qame.State;

import com.fasterxml.jackson.databind.node.ObjectNode;

import static org.junit.Assert.*;

public class ScenarioAdapter {
	private Class<?> type;
	private Constructor<?> constructor;
	private Map<String,Field> config = new HashMap<String,Field>();
	private Map<String,Field> runstate = new HashMap<String,Field>();
	
	public ScenarioAdapter( Class<?> type ) {
		this.type = type;
	}
	
	public Class<?> getScenarioType() {
		return type;
	}
	
	public Scenario newInstance() {
		try {
			return (Scenario) constructor.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void applyConfig(Scenario s, String name, Object value) {
		Field f = config.get(name);
		if (f != null) 
			try {
				f.set(s, value);
			} catch (Exception e) {
				fail(e.toString());
			}
	}
	
	public void applyConfig(Scenario s, ObjectNode json ) {
	}

	public void applyState(Scenario s, RunState r) {
		for ( Entry<String,Field> t : runstate.entrySet()) {
			try {
				Object v = r.get(t.getKey());
				t.getValue().set(s,v);
			} catch (Exception e) {
				fail(e.toString());
			}
		}
	}
	
	public void updateState(Scenario s, RunState r) {
		for ( Entry<String,Field> t : runstate.entrySet()) {
			try {
				r.set(t.getKey(), t.getValue().get(s));
			} catch (Exception e) {
				fail(e.toString());
			}
		}
	}
	
	protected void scanConfig() throws NoSuchMethodException, SecurityException {
		constructor = type.getConstructor( new Class<?>[]{});
		for( Field field : type.getFields())
		{
			Config conf = field.getAnnotation(Config.class);
			if (conf != null) {
				config.put(field.getName(), field);
			}
			State state = field.getAnnotation(State.class);
			if (state != null) {
				String key = state.key();
				if (key==null || "".equals(key)) key = field.getName();
				runstate.put(state.key(), field);
			}
		}
	}
}
