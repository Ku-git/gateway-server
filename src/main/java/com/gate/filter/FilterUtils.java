package com.gate.filter;


import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

public class FilterUtils {

    public static final String CORRELATION_ID = "txn-correlation-id";
    public static final String AUTH_TOKEN = "txn-auth-token";
    public static final String USER_ID = "txn-user-id";
    public static final String ORGANIZATION_ID = "txn-org-id";
    public static final String PRE_FILTER_TYPE = "pre";
    public static final String POST_FILTER_TYPE = "post";
    public static final String ROUTE_FILTER_TYPE = "route";

    public static String getCorrelationId(HttpHeaders requestHeaders) {

        if (requestHeaders.get(CORRELATION_ID) != null) {
            List<String> header = requestHeaders.get(CORRELATION_ID);
            return header.stream().findFirst().get();
        }
        return null;
    }

    public static ServerWebExchange setRequestHeader(ServerWebExchange exchange, String name, String value) {
        var serverWebExchange = exchange.mutate().request(
                exchange.getRequest().mutate()
                        .header(name, value)
                        .build()).build();
        return serverWebExchange;
    }

    public static ServerWebExchange setCorrelationId(ServerWebExchange exchange, String correlationId) {
        return setRequestHeader(exchange, CORRELATION_ID, correlationId);
    }

}
