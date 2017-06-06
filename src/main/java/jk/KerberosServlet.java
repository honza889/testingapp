package jk;

import org.wildfly.security.auth.callback.CredentialCallback;
import org.wildfly.security.auth.client.AuthenticationConfiguration;
import org.wildfly.security.auth.client.AuthenticationContext;
import org.wildfly.security.auth.client.AuthenticationContextConfigurationClient;
import org.wildfly.security.auth.server.SecurityDomain;
import org.wildfly.security.auth.server.SecurityIdentity;
import org.wildfly.security.credential.GSSKerberosCredential;

import javax.security.auth.callback.CallbackHandler;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

// http://localhost:8080/testingapp/KerberosServlet

public class KerberosServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getOutputStream().println("This is testing servlet");

        // Alternative solution to get GSSCredential

        SecurityIdentity securityIdentity = SecurityDomain.getCurrent().getCurrentSecurityIdentity();
        resp.getOutputStream().println("securityIdentity = "+securityIdentity);

        GSSKerberosCredential gssKerberosCredential2 = securityIdentity.getPrivateCredentials().getCredential(GSSKerberosCredential.class);
        resp.getOutputStream().println("gssKerberosCredential2 = "+gssKerberosCredential2);

        resp.getOutputStream().println("gssKerberosCredential2.getGssCredential = "+(gssKerberosCredential2 != null ? gssKerberosCredential2.getGssCredential() : null));

        AuthenticationContextConfigurationClient configClient = new AuthenticationContextConfigurationClient();
        resp.getOutputStream().println("configClient = "+configClient);

        AuthenticationContext authenticationContext = AuthenticationContext.captureCurrent();
        resp.getOutputStream().println("authenticationContext = "+authenticationContext);

        AuthenticationConfiguration configuration = configClient.getAuthenticationConfiguration(URI.create("http://localhost"), authenticationContext);
        resp.getOutputStream().println("configuration = "+configuration);

        CallbackHandler callbackHandler = configClient.getCallbackHandler(configuration);
        resp.getOutputStream().println("callbackHandler = "+callbackHandler);

        CredentialCallback credentialCallback = new CredentialCallback(GSSKerberosCredential.class);
        resp.getOutputStream().println("credentialCallback = "+credentialCallback);
    }

}
