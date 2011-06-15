package org.seasar.s2jaas;

import java.util.List;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;

public class SimpleJdbcLoginModule implements LoginModule {
	@Binding(bindingType=BindingType.MUST)
	protected JdbcManager jdbcManager;

	private String sql;
	private Subject subject;
	private SimpleCallbackHandler callbackHandler;

	public void initialize(Subject subject, CallbackHandler callbackHandler,
			Map<String, ?> sharedState, Map<String, ?> options) {
		this.subject = subject;
		this.callbackHandler = (SimpleCallbackHandler) callbackHandler;
	}

	public boolean login() throws LoginException {
		List<BeanMap> userList = jdbcManager.selectBySql(BeanMap.class, sql, callbackHandler.getName(), callbackHandler.getPassword())
			.getResultList();
		return userList.size() > 0;
	}

	public boolean commit() throws LoginException {
		return true;
	}

	public boolean abort() throws LoginException {
		return true;
	}

	public boolean logout() throws LoginException {
		return true;
	}

}
