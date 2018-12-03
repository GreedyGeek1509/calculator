package com.sriram.spring.calculator.module;

import com.sriram.spring.calculator.api.TCalculatorService;
import com.sriram.spring.calculator.service.CalculatorHandler;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author guduri.sriram
 */
@Configuration
public class CalculatorModule {

    @Bean
    public TProtocolFactory protocolFactory() {
        return new TBinaryProtocol.Factory();
    }

    @Bean
    public TProcessor calculatorProcessor(CalculatorHandler calculatorHandler) {
        return new TCalculatorService.Processor<>(calculatorHandler);
    }
//
//
//    @Bean
//    public Servlet calculator(TProcessor calculatorProcessor, TProtocolFactory protocolFactory) {
//        return new DelegateServlet(calculatorProcessor, protocolFactory);
//    }
}
