package org.qame.impl;

import org.qame.Checkpoint;
import org.qame.RunState;
import org.qame.Scenario;

public class IndirectCheckpoint <S extends Scenario> implements Checkpoint<S> {
	private Checkpoint<S> inner = null;

	public void verify(RunState state, S scenario) {
		if (getInner() != null) getInner().verify(state, scenario);
	}

	public Checkpoint<S> getInner() {
		return inner;
	}

	public void setInner(Checkpoint<S> inner) {
		this.inner = inner;
	}
}
