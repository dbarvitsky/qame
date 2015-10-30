package org.qame.scenario;

import org.qame.Checkpoint;
import org.qame.Config;
import org.qame.RunState;

public class TestLoginCheckpoint implements Checkpoint<BasicLogin>{
	public @Config String test1 = null; 
	public @Config String test2 = null;
	public void verify(RunState state, BasicLogin scenario) {
		System.out.println("test 1:"+test1);
		System.out.println("test 2:"+test2);
	}
}
