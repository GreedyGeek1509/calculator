package com.sriram.spring.calculator.servlet;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author guduri.sriram
 */
@Slf4j
@WebServlet(name = "ping", urlPatterns = "/ping")
public class PingServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        log.debug("PingServlet initialized.");
        super.init();
    }

    @Override
    public void destroy() {
        log.debug("PingServlet destroyed.");
        super.destroy();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("Inside ping servlet");
        OutputStream outputStream = resp.getOutputStream();
        ((ServletOutputStream) outputStream).print("pong");
        outputStream.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}

