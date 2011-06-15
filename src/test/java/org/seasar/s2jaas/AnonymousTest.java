package org.seasar.s2jaas;

import static org.junit.Assert.assertNotNull;

import javax.security.auth.Subject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seasar.framework.unit.Seasar2;
import org.seasar.framework.unit.TestContext;

@RunWith(Seasar2.class)
public class AnonymousTest {
	private TestContext ctx;

	S2LoginContext loginContext;

	public void beforeLoginAnonymous() {
		ctx.include("s2jaas-anonymous.dicon");
	}

	@Test
	public void loginAnonymous() {
		loginContext.login("user", "password");
		Subject subject =loginContext.getSubject();
		assertNotNull(subject);
	}


}
