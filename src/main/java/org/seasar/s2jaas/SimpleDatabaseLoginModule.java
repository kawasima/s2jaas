/**
 *
 */
package org.seasar.s2jaas;

import java.util.List;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.util.StringConversionUtil;

/**
 * @author kawasima
 *
 */
public class SimpleDatabaseLoginModule implements LoginModule {
	private static Log log = LogFactory.getLog(SimpleDatabaseLoginModule.class);
	protected JdbcManager jdbcManager;

	private SimpleCallbackHandler handler;

	private String sql;

	public boolean abort() throws LoginException {
		return true;
	}

	public boolean commit() throws LoginException {
		return true;
	}

	public void initialize(Subject subject, CallbackHandler callbackHandler,
			Map<String, ?> sharedState, Map<String, ?> options) {
		jdbcManager = SingletonS2Container.getComponent(JdbcManager.class);
		handler = (SimpleCallbackHandler)callbackHandler;
		sql = StringConversionUtil.toString(options.get("sql"));
	}

	public boolean login() throws LoginException {
		log.info(sql);
		List<BeanMap> userList = jdbcManager
			.selectBySql(BeanMap.class, sql, handler.getName(), handler.getPassword())
			.getResultList();
		log.info(handler.getName()+":"+ handler.getPassword());

		return userList.size() > 0;
	}

	public boolean logout() throws LoginException {
		return false;
	}

}
