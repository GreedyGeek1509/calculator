package com.sriram.spring.calculator.service;

import com.sriram.spring.calculator.dto.TDivisionByZeroException;
import org.springframework.stereotype.Component;

/**
 * @author guduri.sriram
 */
@Component
public class CalculatorService {
    public int add(int i1, int i2) {
        return i1 + i2;
    }

    public int subtract(int i1, int i2) {
        return i1 - i2;
    }

    public int multiply(int i1, int i2) {
        return i1 * i2;
    }

    public int divice(int i1, int i2) throws TDivisionByZeroException {
        if (i2 == 0) {
            throw new TDivisionByZeroException();
        }
        return i1 / i2;
    }
}
