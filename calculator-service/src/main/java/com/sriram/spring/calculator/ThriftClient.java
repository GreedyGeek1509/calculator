package com.sriram.spring.calculator;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.apache.http.client.HttpClient;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransportException;

import java.util.Map;

/**
 * @author guduri.sriram
 */
@Slf4j
public class ThriftClient extends THttpClient {

    private final String service;
    private final Map<String, String> tokenStore;

    private static final String SCHEME = "Negotiate";

    public ThriftClient(String url, @NonNull String service, @NonNull Map<String, String> tokenStore) throws TTransportException {
        super(url);
        this.service = service;
        this.tokenStore = tokenStore;
    }

    public ThriftClient(String url, HttpClient client, @NonNull String service, @NonNull Map<String, String> tokenStore) throws TTransportException {
        super(url, client);
        this.service = service;
        this.tokenStore = tokenStore;
    }

    @Override
    public void flush() throws TTransportException {
        log.debug("Inside ThriftClient.flush method.");
        System.out.println("Inside ThriftClient.flush() method.");
        String token = tokenStore.get(service);
        if (token != null) {
            StringBuilder sb = new StringBuilder(SCHEME);
            sb.append(" ");
            sb.append(token);
            setCustomHeader(HttpHeaders.AUTHORIZATION, sb.toString());
        }
        super.flush();
    }
}
