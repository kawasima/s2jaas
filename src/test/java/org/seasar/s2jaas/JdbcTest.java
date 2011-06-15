package org.seasar.s2jaas;

import javax.security.auth.Subject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.unit.Seasar2;
import org.seasar.framework.unit.TestContext;

import static org.junit.Assert.*;

@RunWith(Seasar2.class)
public class JdbcTest {
	private TestContext ctx;

	S2LoginContext loginContext;

	public void beforeLoginJdbc() {
		ctx.include("s2jaas-jdbc.dicon");
	}

	@Test
	public void loginJdbc() {
		JdbcManager jdbcManager = ctx.getComponent(JdbcManager.class);
		try {
			jdbcManager.updateBySql("create table users(account varchar(20), password varchar(20))").execute();
			jdbcManager.updateBySql("INSERT INTO users values('user', 'password')").execute();
			loginContext.login("user", "password");
			Subject subject =loginContext.getSubject();
			assertNotNull(subject);
		} finally {
			jdbcManager.updateBySql("drop table users").execute();
		}
	}

	public void beforeFailureLoginJdbc() {
		ctx.include("s2jaas-jdbc.dicon");
	}

	@Test(expected=LoginRuntimeException.class)
	public void failureLoginJdbc() {
		JdbcManager jdbcManager = ctx.getComponent(JdbcManager.class);
		try {
			jdbcManager.updateBySql("create table users(account varchar(20), password varchar(20))").execute();
			jdbcManager.updateBySql("INSERT INTO users values('user2', 'password')").execute();
			loginContext.login("user", "password");
		} finally {
			jdbcManager.updateBySql("drop table users").execute();
		}
	}
}
