package org.qame;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("checkpoints")
public class Checkpoints<S extends Scenario> implements Checkpoint<S> {
	private List<Checkpoint<? super S>> checkpoints;
	private boolean verifyAll = false;
	
	public void verify(RunState state, S scenario) {
		for (Checkpoint<? super S> cp : checkpoints) {
			try {
				cp.verify(state, scenario);
			} catch (Exception e) {
				if (!verifyAll) throw new java.lang.AssertionError(e);
			}
		}
	}
	
	public List<Checkpoint<? super S>> getCheckpoints() {
		return checkpoints;
	}
	
	public void setCheckpoints(List<Checkpoint<? super S>> checkpoints ) {
		this.checkpoints = checkpoints;
	}	
}
