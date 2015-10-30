package org.qame;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.qame.Driver;
import org.qame.RunState;
import org.qame.impl.CheckpointFactory;
import org.qame.impl.ReflectedCheckpointFactory;
import org.qame.scenario.BasicLogin;
import org.qame.scenario.Checkout;
import org.qame.scenario.OAuthLogin;
import org.qame.scenario.TestLoginCheckpoint;
import org.qame.scenario.Transaction;

import static org.junit.Assert.*; 

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;

public class SanityTest {

	@Test @SuppressWarnings("unchecked")
	public void testCompile() throws Exception {
		
		Transaction test = new Transaction() {{
			login = new BasicLogin() {{
				loginUri = new java.net.URI("http://www.contoso.com/login");
				userName = "user";
				password = "password";
				before = Driver.checkAll(BasicLogin.expectConsistentParameters());
				after = BasicLogin.expectSuccess();
			}};
			checkout = new Checkout() {{
				items = new String[] { "Oban 1.75l", "Grey Goose 1.0l" };
				promoCode = "HAPPYMONDAY50OFF";
				afterPurchaise = Checkout.expectPrice(99.00);
			}};
		}};
		Driver.run( new RunState(), test);
	}
	
	@Test
	public void testDriverFeatures() throws Exception {
		@SuppressWarnings("serial")
		final Map<String,Object> config = new HashMap<String,Object>(){{
			put("login.loginUri", new java.net.URI("http://www.contoso.com/login"));
			put("login.userName","user");
			put("login.password","password");
			put("checkout.items",new String[] { "Oban 1.75l", "Grey Goose 1.0l" });
			put("checkout.promoCode","HAPPYMONDAY50OFF");
		}};
		Transaction test = new Transaction() {{
			login = Driver.apply(new OAuthLogin(), config, "login.*"); 
			checkout = Driver.apply(new Checkout(), config, "checkout.*");
		}};
		Driver.run( new RunState(), test);
	}
	
	@Test
	public void testApplyJson() throws Exception {
		@SuppressWarnings("unchecked")
		Transaction test = new Transaction();
		test.login = new BasicLogin();
		test.checkout = new Checkout();
		/* {{
			login = new BasicLogin() {{
				before = Driver.checkAll(BasicLogin.expectConsistentParameters());
				after = BasicLogin.expectSuccess();
			}};
			checkout = new Checkout() {{
				afterPurchaise = Checkout.expectPrice(99.99);
			}};
		}};*/
		
		String json = 
		"{"+
				"\"login\": {"+
					"\"xtype\":\"org.qame.scenario.BasicLogin\","+
					"\"loginUri\":\"http://www.contoso.com/login\","+
					"\"userName\":\"user\","+
					"\"password\":\"password\","+
					"\"before\": {"+
						"\"xtype\": \"org.qame.scenario.TestLoginCheckpoint\","+
						"\"test1\": \"Value 1\","+
						"\"test2\": \"Value 2\""+
					"}"+
				"},"+
				"\"checkout\": {"+
					"\"xtype\":\"org.qame.scenario.Checkout\","+
					"\"items\":[\"Oban 1.75l\",\"Grey Goose 1.0l\"],"+
					"\"promoCode\":\"HAPPYMONDAY50OFF\","+
					"\"afterPurchaise\":{"+
						"\"xtype\":\"call\","+
						"\"function\":\"org.qame.scenario.Checkout#expectPrice\","+
						"\"args\":[99.0]"+
					"}"+
				"}"+
		"}";
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule( new org.qame.impl.jackson.JacksonSerializerExtensions());
		test = mapper.readerForUpdating(test).readValue(json);
		assertNotNull(test);
		Driver.run( new RunState(), test);
	}
	
	@Test
	public void testReflectiveCheckpoint() throws ReflectiveOperationException {
		CheckpointFactory factory = 
				new org.qame.impl.ReflectedCheckpointFactory()
				.setArgs((Object)99.0)
				.setExpression("org.qame.scenario.Checkout#expectPrice",-1);
		Checkpoint<Checkout> c1 = factory.createCheckpoint();
		assertNotNull(c1);
		c1.verify(new RunState(), new Checkout(){{ price = 99.0; }});
	}
}
