package com.sriram.spring.calculator.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.StringTokenizer;

/**
 * @author guduri.sriram
 */
@Slf4j
@WebFilter(
        urlPatterns = { "/*" },
        initParams = {
                @WebInitParam(name = "username", value = "user"),
                @WebInitParam(name = "password", value = "pass")
        }
)
public class AuthenticationFilter implements Filter {
    private String username = "";

    private String password = "";

    private String realm = "Protected";

    @Value("${filter.enable.password.auth}")
    private boolean enablePasswordAuth;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        username = filterConfig.getInitParameter("username");
        password = filterConfig.getInitParameter("password");
        String paramRealm = filterConfig.getInitParameter("realm");
        if (paramRealm != null && paramRealm.length() > 0) {
            realm = paramRealm;
        }
        log.debug("AuthenticationFilter initialized.");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        log.debug("Inside auth filter.");
        if (!enablePasswordAuth) {
            log.debug("Password auth disabled. Passing through the filter.");
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;

            String authHeader = request.getHeader("Authorization");
            if (authHeader != null) {
                StringTokenizer st = new StringTokenizer(authHeader);
                if (st.hasMoreTokens()) {
                    String basic = st.nextToken();

                    if (basic.equalsIgnoreCase("Basic")) {
                        try {
                            String credentials = new String(Base64.getDecoder().decode(st.nextToken()));
                            log.debug("Credentials: " + credentials);
                            System.out.println("credentials: " + credentials);
                            int p = credentials.indexOf(":");
                            if (p != -1) {
                                String _username = credentials.substring(0, p).trim();
                                String _password = credentials.substring(p + 1).trim();

                                if (!username.equals(_username) || !password.equals(_password)) {
                                    unauthorized(response, "Bad credentials");
                                }

                                filterChain.doFilter(servletRequest, servletResponse);
                            } else {
                                unauthorized(response, "Invalid authentication token");
                            }
                        } catch (UnsupportedEncodingException e) {
                            throw new Error("Couldn't retrieve authentication", e);
                        }
                    }
                }
            } else {
                unauthorized(response);
            }
        }
    }

    @Override
    public void destroy() {
    }

    private void unauthorized(HttpServletResponse response, String message) throws IOException {
        response.setHeader(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"" + realm + "\"");
        response.setHeader(HttpHeaders.CONNECTION, "keep-alive");
        response.sendError(401, message);
    }

    private void unauthorized(HttpServletResponse response) throws IOException {
        log.error("Unauthorized request.");
        unauthorized(response, "Unauthorized");
    }

}
