package org.qame.scenario;

import static org.junit.Assert.*;

import org.qame.Checkpoint;
import org.qame.Config;
import org.qame.Driver;
import org.qame.RunState;
import org.qame.State;

public class BasicLogin implements Login
{
	public @Config java.net.URI loginUri;
	public @Config String userName;
	public @Config String password;
	
	public @State int httpCode;
	public @State String message;
	public @State(key="auth-token") String cookie;
	
	public Checkpoint<BasicLogin> before;
	public Checkpoint<BasicLogin> after;
	
	public void run(RunState state) {
		Driver.verify(state, this, before);
		// Do HTTP gets and posts
		httpCode = 200;
		message = "Welcome";
		cookie = "blablabla";
		Driver.verify(state, this, after);
	}
	
	public static Checkpoint<BasicLogin> expectSuccess() {
		return new Checkpoint<BasicLogin>() {
			public void verify(RunState state, BasicLogin login) {
				assertNotNull(login.cookie);
				assertEquals("Expect HTTP status 200",200,login.httpCode);
			}
		};
	}
	
	public static Checkpoint<BasicLogin> expectFailure( final int code ) {
		return new Checkpoint<BasicLogin>() {
			public void verify(RunState state, BasicLogin login) {
				assertNull(login.cookie);
				assertEquals("Expect HTTP error "+code,code,login.httpCode);
			}
		};
	}
	
	public static Checkpoint<BasicLogin> expectConsistentParameters() {
		return new Checkpoint<BasicLogin>() {
			public void verify(RunState state, BasicLogin login) {
				assertNotNull("URL provided",login.loginUri);
				assertNotNull("User name provided",login.userName);
				assertNotNull("Password provided",login.password);
			}
		};
	}
}