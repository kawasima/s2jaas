package org.seasar.s2jaas;

import javax.security.auth.login.LoginException;

import org.seasar.framework.exception.SRuntimeException;

public class LoginRuntimeException extends SRuntimeException {
	private static final long serialVersionUID = 1L;

	public LoginRuntimeException(LoginException e) {
		super("EJAA0001", new Object[]{e}, e);
	}
}
