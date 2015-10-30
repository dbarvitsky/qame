package org.qame;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.qame.impl.ScenarioAdapter;

public class Driver {
	private static final Map<Class<?>,ScenarioAdapter> adapters
		= new HashMap<Class<?>,ScenarioAdapter>();
	
	protected static ScenarioAdapter getAdapter( Class<?> type ) {
		ScenarioAdapter adapter = adapters.get(type);
		if (adapter == null) {
			adapter = new ScenarioAdapter(type);
			adapters.put(type, adapter);
		}
		return adapter;
	}
	
	public static RunState run( RunState r, Scenario s ) throws Exception {
		if (s != null) {
			ScenarioAdapter adapter = getAdapter(s.getClass());
			adapter.applyState(s, r);
			try {
				s.run(r);
			} finally {
				adapter.updateState(s, r);
			}
		}
		return r;
	}
	
	public static <S extends Scenario> S apply( S scenario, Map<String,Object> config, String... selectors ) {
		ScenarioAdapter adapter = getAdapter(scenario.getClass());
		for (Entry<String,Object> e : config.entrySet()) {
			if (selectors != null) {
				for (String s : selectors) {
					if (!e.getKey().startsWith(s)) continue;
				}
			}
			adapter.applyConfig(scenario, e.getKey(), e.getValue());
		}
		return scenario;
	}
	
	public static <S extends Scenario> RunState verify( RunState r, S s, Checkpoint<? super S> c) {
		if ( c != null) {
			RunState checkScope = r.override();
			c.verify(checkScope, s);
		}
		return r;
	}
	
	public static <S extends Scenario> Checkpoint<S> checkAll( 
			final Checkpoint<? super S> ... checkpoints) {
		return new Checkpoint<S>() {

			public void verify(RunState state, S scenario) {
				for( Checkpoint<? super S> c : checkpoints) {
					c.verify(state, scenario);
				}
			}
		};
	}
}
