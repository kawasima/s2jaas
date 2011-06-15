package org.seasar.s2jaas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;
import javax.security.auth.login.Configuration;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;

public class SimpleConfiguration extends Configuration{
	@Binding(bindingType=BindingType.MAY)
	private String controlFlag;

	@Binding(bindingType=BindingType.MAY)
	private String loginModule;

	@Binding(bindingType=BindingType.MAY)
	private Map<String, Object> options;

	@Override
	public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
		List<AppConfigurationEntry> configEntries = new ArrayList<AppConfigurationEntry>();
		configEntries.add(new AppConfigurationEntry(getLoginModuleName(), getControlFlag(), getOptions()));
		return configEntries.toArray(new AppConfigurationEntry[0]);
	}

	private LoginModuleControlFlag getControlFlag() {
		if(controlFlag == null || controlFlag.equalsIgnoreCase("REQUIRED")) {
			return LoginModuleControlFlag.REQUIRED;
		} else if (controlFlag.equalsIgnoreCase("REQUISITE")) {
			return LoginModuleControlFlag.REQUISITE;
		} else if (controlFlag.equalsIgnoreCase("SUFFICIENT")) {
			return LoginModuleControlFlag.SUFFICIENT;
		} else if (controlFlag.equalsIgnoreCase("OPTIONAL")) {
			return LoginModuleControlFlag.OPTIONAL;
		}

		return LoginModuleControlFlag.REQUIRED;
	}

	private String getLoginModuleName() {
		if(loginModule == null)
			loginModule = "org.seasar.s2jaas.AnonymousLoginModule";
		return loginModule;
	}

	public String getLoginModule() {
		return loginModule;
	}

	public void setLoginModule(String loginModule) {
		this.loginModule = loginModule;
	}

	public void setControlFlag(String controlFlag) {
		this.controlFlag = controlFlag;
	}

	public Map<String, Object> getOptions() {
		if(options == null)
			options = new HashMap<String, Object>();
		return options;
	}

	public void setOptions(Map<String, Object> options) {
		this.options = options;
	}
}
