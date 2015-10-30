package org.qame.scenario;

import static org.junit.Assert.*;

import org.qame.Checkpoint;
import org.qame.Config;
import org.qame.Driver;
import org.qame.RunState;
import org.qame.Scenario;
import org.qame.State;

public class Checkout implements Scenario {

	@Config public String[] items;
	@Config public String promoCode;
	
	@State public double price;
	public Checkpoint<Checkout> afterPurchaise;
	
	public void run(RunState state) throws Exception {
		// Do things here...
		price = 99.0;
		Driver.verify(state,this,afterPurchaise);
	}
	
	public static Checkpoint<Checkout> expectPrice( final double price ) {
		return new Checkpoint<Checkout>() {
			public void verify(RunState state, Checkout scenario) {
				System.out.println("checkpoint - expect price");
				assertEquals("Expect price to be "+price,price,scenario.price,0.001);
			}
		};
	}
}
