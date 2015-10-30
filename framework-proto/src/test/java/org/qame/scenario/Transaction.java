package org.qame.scenario;

import org.qame.Driver;
import org.qame.RunState;
import org.qame.Scenario;

public class Transaction implements Scenario {
	
	public Login login;
	public Checkout checkout; 
	
	public void run(RunState state) throws Exception {
		Driver.run(state, login);
		Driver.run(state, checkout);
	}

}
