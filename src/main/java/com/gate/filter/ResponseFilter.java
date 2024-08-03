package com.gate.filter;

import brave.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

@Configuration
public class ResponseFilter {

    private static final Logger logger = LoggerFactory.getLogger(ResponseFilter.class);

    private final Tracer tracer;

    public ResponseFilter(Tracer tracer) {
        this.tracer = tracer;
    }

    @Bean
    public GlobalFilter postGlobalFilter() {
        GlobalFilter globalFilter = (exchange, chain) -> {
            Mono<Void> monoResponse = chain.filter(exchange).then(Mono.fromRunnable(() -> {

                String traceId = tracer.nextSpan()
                        .context()
                        .traceIdString();
                logger.debug("adding the correlationId to the outbound headers. {}", traceId);

                exchange.getResponse().getHeaders()
                        .add(FilterUtils.CORRELATION_ID, traceId);
                logger.debug("completing outgoing request for {}", exchange.getRequest().getURI());
            }));
            return monoResponse;
        };
        return globalFilter;
    }

}
