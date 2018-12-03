package com.sriram.spring.calculator;

import com.sriram.spring.calculator.api.TCalculatorService;
import com.sriram.spring.calculator.dto.TDivisionByZeroException;
import com.sriram.spring.calculator.dto.TOperation;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@Configuration
@RunWith(SpringRunner.class)
@SpringBootTest
public class CalculatorApplicationTests {
//
//    @Autowired
//    protected TProtocolFactory protocolFactory;
//    @Value("${server.port}")
//    protected int port;
//
//    protected TCalculatorService.Client client;
//
//    @Before
//    public void setUp() throws Exception {
//        TTransport transport = new THttpClient("http://localhost:" + port + "/calculator/");
//        TProtocol protocol = protocolFactory.getProtocol(transport);
//        client = new TCalculatorService.Client(protocol);
//    }
//
//    @Test
//    public void testAdd() throws Exception {
//        assertEquals(5, client.calculate(2, 3, TOperation.ADD));
//    }
//
//    @Test
//    public void testSubtract() throws Exception {
//        assertEquals(3, client.calculate(5, 2, TOperation.SUBTRACT));
//    }
//
//    @Test
//    public void testMultiply() throws Exception {
//        assertEquals(10, client.calculate(5, 2, TOperation.MULTIPLY));
//    }
//
    @Test
    public void testDivide() throws Exception {
        //assertEquals(2, client.calculate(10, 5, TOperation.DIVIDE));
    }
//
//    @Test(expected = TDivisionByZeroException.class)
//    public void testDivisionByZero() throws Exception {
//        client.calculate(10, 0, TOperation.DIVIDE);
//    }

}
