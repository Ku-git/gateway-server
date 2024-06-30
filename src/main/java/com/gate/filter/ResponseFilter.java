package com.gate.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

@Configuration
public class ResponseFilter {

    private static final Logger logger = LoggerFactory.getLogger(ResponseFilter.class);

    @Bean
    public GlobalFilter postGlobalFilter() {
        GlobalFilter globalFilter = (exchange, chain) -> {
            Mono<Void> monoResponse = chain.filter(exchange).then(Mono.fromRunnable(() -> {
                HttpHeaders headers = exchange.getRequest().getHeaders();
                String correlationId = FilterUtils.getCorrelationId(headers);
                logger.debug("adding the correlationId to the outbound headers. {}", correlationId);

                exchange.getResponse().getHeaders()
                        .add(FilterUtils.CORRELATION_ID, correlationId);
                logger.debug("completing outgoing request for {}", exchange.getRequest().getURI());
            }));
            return monoResponse;
        };
        return globalFilter;
    }

}
