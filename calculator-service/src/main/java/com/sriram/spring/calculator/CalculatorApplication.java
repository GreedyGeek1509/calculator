package com.sriram.spring.calculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class CalculatorApplication {

    public static void main(String[] args) {
        System.setProperty("java.security.auth.login.config",
                "/home/guduri.sriram/workspace/calculator/calculator-service/src/main/resources/jaas.conf");
        SpringApplication.run(CalculatorApplication.class, args);
    }
}
