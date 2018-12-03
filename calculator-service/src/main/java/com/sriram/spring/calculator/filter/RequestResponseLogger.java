package com.sriram.spring.calculator.filter;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.util.CollectionUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * @author guduri.sriram
 */
@Slf4j
@WebFilter(
        servletNames = {"calculator", "ping"}
        //urlPatterns = {"/ping", "/calculator"}
)
public class RequestResponseLogger implements Filter {

    private ServletContext context;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.context = filterConfig.getServletContext();
        log.debug("RequestResponseLogger initialized.");
    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // pass the request along the filter chain
        chain.doFilter(request, response);
    }
}
