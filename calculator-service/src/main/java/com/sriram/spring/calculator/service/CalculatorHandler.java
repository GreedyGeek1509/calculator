package com.sriram.spring.calculator.service;

import com.sriram.spring.calculator.AuthContext;
import com.sriram.spring.calculator.api.TCalculatorService;
import com.sriram.spring.calculator.dto.TAuthException;
import com.sriram.spring.calculator.dto.TOperation;
import com.sriram.spring.calculator.dto.TDivisionByZeroException;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author guduri.sriram
 */

@Component
public class CalculatorHandler implements TCalculatorService.Iface {

    @Autowired
    CalculatorService calculatorService;

    @Override
    public int calculate(int i1, int i2, TOperation operation) throws TDivisionByZeroException, TAuthException {
        switch (operation) {
            case ADD:
                if (AuthContext.AUTH_USER.get() != "sriram") {
                    throwAuthException(operation);
                }
                return calculatorService.add(i1, i2);
            case SUBTRACT:
                return calculatorService.subtract(i1, i2);
            case MULTIPLY:
                return calculatorService.multiply(i1, i2);
            case DIVIDE:
                return calculatorService.divice(i1, i2);
        }
        return 0;
    }


    private void throwAuthException(TOperation operation) throws TAuthException {
        StringBuilder sb = new StringBuilder("User : ");
        sb.append(AuthContext.AUTH_USER.get());
        sb.append(" is not authorized to execute operation : ");
        sb.append(operation);
        TAuthException authException = new TAuthException();
        authException.setErrorCode((short) 2);
        authException.setMessage(sb.toString());
        throw authException;
    }
}
