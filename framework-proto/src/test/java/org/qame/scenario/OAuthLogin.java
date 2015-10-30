package org.qame.scenario;

import org.qame.Checkpoint;
import org.qame.Config;
import org.qame.Driver;
import org.qame.RunState;
import org.qame.State;

public class OAuthLogin implements Login {

	public @Config java.net.URI loginUri;
	
	@State(key="auth-token") public String oAuthToken;
	
	public Checkpoint<OAuthLogin> before;
	public Checkpoint<OAuthLogin> after;	
	
	public void run(RunState state) throws Exception {
		Driver.verify(state, this, before);
		oAuthToken = "12345";
		Driver.verify(state, this, after);
	}
}
