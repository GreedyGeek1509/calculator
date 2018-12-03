package com.sriram.spring.calculator.kerberos;

/**
 * @author guduri.sriram
 */
public class AuthContext {
    public static final ThreadLocal<String> AUTH_USER = ThreadLocal.withInitial(() -> "");
}
