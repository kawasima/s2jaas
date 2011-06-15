package org.seasar.s2jaas;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;

public class S2LoginContext {
	private LoginContext loginContext;
	private CallbackHandler callbackHandler;
	private Boolean initialized = false;
	private Subject subject;

	@Binding(bindingType=BindingType.MAY)
	public Configuration configuration;

	public void init() {
		subject = new Subject();
		if(callbackHandler == null) {
			callbackHandler = new SimpleCallbackHandler();
		}
		try {
			loginContext = new LoginContext("Test", subject, callbackHandler, configuration);
		} catch(LoginException e) {
			throw new LoginRuntimeException(e);
		}
		initialized = true;
	}

	public Subject getSubject() {
		return subject;
	}
	public void login(String name, String password) {
		if(!initialized)
			init();
		((SimpleCallbackHandler)callbackHandler).setName(name);
		((SimpleCallbackHandler)callbackHandler).setPassword(password);
		try {
			loginContext.login();
		} catch(LoginException e) {
			throw new LoginRuntimeException(e);
		}
	}
}
