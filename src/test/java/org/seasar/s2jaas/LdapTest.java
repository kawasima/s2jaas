package org.seasar.s2jaas;

import static org.apache.directory.server.integ.ServerIntegrationUtils.getWiredContext;
import static org.junit.Assert.assertEquals;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;

import org.apache.directory.server.annotations.CreateLdapServer;
import org.apache.directory.server.annotations.CreateTransport;
import org.apache.directory.server.core.integ.AbstractLdapTestUnit;
import org.apache.directory.server.core.integ.FrameworkRunner;
import org.apache.directory.server.ldap.LdapServer;
import org.apache.directory.server.protocol.shared.transport.Transport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;
import org.seasar.framework.unit.TestContext;

@RunWith(FrameworkRunner.class)
@CreateLdapServer(
    transports =
      {
        @CreateTransport(protocol = "LDAP", port=389)
      })
public class LdapTest extends AbstractLdapTestUnit {
	public static LdapServer ldapServer;
	private TestContext context;

	@Before
	public void beforeLoginLdap() throws Exception {
        S2Container container = S2ContainerFactory.create("s2junit4.dicon");
        context = (TestContext) container.getComponent(TestContext.class);
        context.include("s2jaas-ldap.dicon");

        LdapContext ldapContext = ( LdapContext ) getWiredContext(ldapServer).lookup("ou=system");
        for(Transport t : ldapServer.getTransports()) {
            System.err.println("ldapPort ==="+t.getAddress() +":"+t.getPort());
        }
        Attribute objclass = new BasicAttribute("objectClass");
        objclass.add("top");
        objclass.add("person");
        objclass.add("inetOrgPerson");
        Attributes attrs = new BasicAttributes(true);
        attrs.put(objclass);
        attrs.put(new BasicAttribute("cn", "kawasima"));
        attrs.put(new BasicAttribute("sn", "kawasima"));
        attrs.put(new BasicAttribute("userPassword", "password"));
        ldapContext.createSubcontext("uid=kawasima,ou=users", attrs);
	}

	@Test
	public void loginLdap() throws Exception {
        LdapContext ldapContext = ( LdapContext ) getWiredContext(ldapServer).lookup("ou=system");
        SearchControls controls = new SearchControls();
        controls.setSearchScope( SearchControls.ONELEVEL_SCOPE );
        controls.setReturningAttributes( new String[]
            { "+", "*" } );

        NamingEnumeration<SearchResult> res = ldapContext.search( "ou=users", "(ObjectClass=*)", controls );
        while ( res.hasMoreElements() )
        {
            SearchResult result = ( SearchResult ) res.next();

            System.out.println( result.getName() );
        }


		S2LoginContext loginContext = (S2LoginContext) context.getComponent(S2LoginContext.class);
		loginContext.login("kawasima", "password");

		try {
			loginContext.login("kawasima1", "password");
		} catch(Exception e) {
			assertEquals("login failure", LoginRuntimeException.class, e.getClass());
		}

		try {
			loginContext.login("kawasima", "password1");
		} catch(Exception e) {
			assertEquals("login failure", LoginRuntimeException.class, e.getClass());
		}

	}
}
