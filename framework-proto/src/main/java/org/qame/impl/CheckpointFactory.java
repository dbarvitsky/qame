package org.qame.impl;

import org.qame.Checkpoint;
import org.qame.Scenario;

public interface CheckpointFactory {
	<S extends Scenario> Checkpoint<S> createCheckpoint() throws ReflectiveOperationException;
}
