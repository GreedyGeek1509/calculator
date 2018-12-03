package com.sriram.spring.calculator;

import com.sriram.spring.calculator.api.TCalculatorService;
import com.sriram.spring.calculator.dto.TAuthException;
import com.sriram.spring.calculator.dto.TDivisionByZeroException;
import com.sriram.spring.calculator.dto.TOperation;
import org.apache.commons.codec.binary.Base64;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.Oid;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import java.io.UnsupportedEncodingException;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;

/**
 * @author guduri.sriram
 */
public class CalculatorClient {
    public static void main(String[] args) throws TException, TDivisionByZeroException, TAuthException, LoginException, UnsupportedEncodingException, GSSException, PrivilegedActionException {

        THttpClient transport = new THttpClient("http://localhost:9090/calculator");
        //transport.setCustomHeader("Authorization", "Basic dXNlcjpwYXNz");
        transport.setCustomHeader("Authorization", "Negotiate " + getTGS("SVC/calculator-service"));
        transport.setConnectTimeout(300000);
        TProtocol protocol = new TBinaryProtocol(transport);
        TCalculatorService.Client calculatorClient = new TCalculatorService.Client(protocol);

        System.out.println(calculatorClient.calculate(1, 2, TOperation.MULTIPLY));
    }


    private static String getTGS(String servicePrincipal) throws LoginException, PrivilegedActionException {
        System.setProperty( "java.security.auth.login.config", "/home/sriram/workspace/calculator/calculator-service/src/main/resources/jaas.conf");
        LoginContext loginCtx = new LoginContext("ClientConf");
        loginCtx.login();
        String token = Subject.doAs(loginCtx.getSubject(), (PrivilegedAction<String>) () -> {
            try {
                byte[] byteToken = new byte[0];
                GSSManager manager = GSSManager.getInstance();
                Oid krb5Oid = new Oid("1.2.840.113554.1.2.2");
                GSSName serverName = manager.createName(servicePrincipal, GSSName.NT_USER_NAME);
                final GSSContext context = manager.createContext(serverName, krb5Oid, null, GSSContext.DEFAULT_LIFETIME);
                context.requestMutualAuth( true);
                context.requestCredDeleg( false);
                context.requestConf(true);
                byte[] secToken = context.initSecContext(byteToken, 0, byteToken.length);
                byte[] encodedToken = Base64.encodeBase64(secToken);
                return new String(encodedToken, "UTF-8");
            } catch (GSSException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        });
        return token;
    }
}
