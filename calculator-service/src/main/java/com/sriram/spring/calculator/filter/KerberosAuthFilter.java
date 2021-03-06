package com.sriram.spring.calculator.filter;

import com.sriram.spring.calculator.kerberos.AuthContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;
import org.springframework.beans.factory.annotation.Value;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.stream.Collectors;

/**
 * @author guduri.sriram
 */
@Slf4j
@WebFilter(
        urlPatterns = "/*"
)
public class KerberosAuthFilter implements Filter {

    private static final String FILTER_NAME = KerberosAuthFilter.class.getSimpleName();
    private static final String AUTHORIZATION_SCHEME = "Negotiate";

    private LoginContext loginContext;
    private GSSManager gssManager;
    private Base64 base64;

    @Value("${filter.login.conf}")
    private String loginConf;

    @Value("${filter.enable.kerberos.auth}")
    private boolean enableKerberosFilter;

    @Value("${login.config.file}")
    private String loginConfFile;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.debug("Using login conf file {}, with config : {}", loginConfFile, loginConf);
        System.setProperty("java.security.auth.login.config", loginConfFile);
//        System.setProperty( "sun.security.krb5.debug", "true");
//        System.setProperty( "java.security.krb5.realm", "DEV.SRIRAM.COM");
//        System.setProperty( "java.security.krb5.kdc", "localhost");
//        System.setProperty( "javax.security.auth.useSubjectCredsOnly", "true");
        try {
            loginContext = new LoginContext(loginConf);
            loginContext.login();
            log.debug("Service Principals : {}", loginContext.getSubject().getPrincipals());
            log.debug("Service Names : {}", loginContext.getSubject().getPrincipals().stream().map(Principal::getName).collect(Collectors.toList()));
            gssManager = Subject.doAs(loginContext.getSubject(), (PrivilegedAction<GSSManager>) () -> GSSManager.getInstance());
            base64 = new Base64();
        } catch (LoginException e) {
            throw  new ServletException(e);
        }
        log.debug("{} initialized.", FILTER_NAME);
    }

    @Override
    public void destroy() {
        log.debug("{} destroyed.", FILTER_NAME);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.debug("Inside {}.", FILTER_NAME);
        if (enableKerberosFilter) {
            log.debug("Kerberos auth enabled. Authenticating request");
            //authenticateRequest((HttpServletRequest)request, (HttpServletResponse)response, chain);
            authRequest((HttpServletRequest)request, (HttpServletResponse)response, chain);
        } else {
            log.debug("Kerberos auth disabled. Passing request along filter chain.");
            chain.doFilter(request, response);
        }
    }

    private void authenticateRequest(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException {
        byte[] authToken = getAuthToken(request);
        log.debug("Query String : {}", request.getQueryString());
        if (authToken == null) {
            requestAuthentication(response,null);
        } else {
            try {
                String user = Subject.doAs(loginContext.getSubject(), (PrivilegedExceptionAction<String>) () -> {
                    GSSContext gssContext = null;
                    String user1 = null;
                    try {
                        gssContext = gssManager.createContext((GSSCredential) null);
                        //gssContext.requestMutualAuth(true);
                        //gssContext.requestInteg(true);
                        byte[] serverToken = gssContext.acceptSecContext(authToken, 0, authToken.length);
                        if (!gssContext.isEstablished()) {
                            // Spnego need more challenges
                            requestAuthentication(response, serverToken);
                        } else {
                            if (serverToken != null) {
                                // context established, but this needs to be
                                // passed to client in response for context
                                // establishment at client side
//                            ThreadLocalContext.setKey(HttpHeaders.Names.WWW_AUTHENTICATE.toString(),
//                                    base64.encodeAsString(serverToken));
                            }
                            String principal = gssContext.getSrcName().toString();
                            user1 = principal;
                            AuthContext.AUTH_USER.set(user1);
                            log.debug("Client principal : {}", principal);
                        }
                        chain.doFilter(request, response);
                        gssContext.dispose();
                    } catch (GSSException gsse) {
                        log.error("Caught exception", gsse);
                        response.sendError(HttpStatus.SC_FORBIDDEN, "Forbidden");
                    } finally {
                        if (gssContext != null) {
                            gssContext.dispose();
                        }
                    }
                    return user1;
                });
            } catch (PrivilegedActionException e) {
                throw new RuntimeException(e.getException());
            }
        }
    }

    private void authRequest(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException {
        byte[] authToken = getAuthToken(request);
        if (authToken == null) {
            requestAuthentication(response,null);
        } else {
            try {
                String user = Subject.doAs(loginContext.getSubject(), (PrivilegedExceptionAction<String>) () -> {
                    try {
                        GSSContext context = gssManager.createContext( (GSSCredential) null);
                        context.acceptSecContext(authToken, 0, authToken.length);
                        String clientPrincipal = context.getSrcName().toString();
                        log.debug("client principal : {}", clientPrincipal);
                        AuthContext.AUTH_USER.set(clientPrincipal);
                        chain.doFilter(request, response);
                        context.dispose();
                        return clientPrincipal;
                    }
                    catch ( Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                });
            } catch (PrivilegedActionException e) {
                throw new RuntimeException(e.getException());
            }
        }
    }


    protected byte[] getAuthToken(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null || !authorization.startsWith(AUTHORIZATION_SCHEME)) {
            return null;
        }
        // get auth to= authorization.substring(AUTHORIZATION_SCHEME.length()).trim();
        return base64.decode(authorization.substring(AUTHORIZATION_SCHEME.length()).trim());
    }

    private void requestAuthentication(HttpServletResponse response, byte[] authToken) throws IOException {
        String tokenStr = authToken == null ? "" : base64.encodeAsString(authToken);
        response.setHeader(HttpHeaders.WWW_AUTHENTICATE, String.format("%s %s", AUTHORIZATION_SCHEME, tokenStr));
        response.setHeader(HttpHeaders.CONNECTION, "keep-alive");
        response.sendError(HttpStatus.SC_UNAUTHORIZED, "Unauthorized");
    }
}
