package com.sriram.spring.calculator.servlet;

import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author guduri.sriram
 */
@Slf4j
@WebServlet(name = "calculator", urlPatterns = "/calculator")
public class CalculatorServlet extends TServlet {

    @Override
    public void init() throws ServletException {
        log.debug("CalculatorServlet initialized.");
        super.init();
    }

    @Override
    public void destroy() {
        log.debug("CalculatorServlet destroyed.");
        super.destroy();
    }

    public CalculatorServlet(TProcessor processor, TProtocolFactory protocolFactory) {
        super(processor, protocolFactory);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Inside doPost");
        super.doPost(request, response);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Inside doGet");
        super.doGet(request, response);
    }
}
