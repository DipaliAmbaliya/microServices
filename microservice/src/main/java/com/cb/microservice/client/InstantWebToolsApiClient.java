package com.cb.microservice.client;

import com.cb.microservice.client.config.CustomFeignConfiguration;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(value = "${app.feign.config.name}", url = "${app.feign.config.url}", configuration = CustomFeignConfiguration.class)
@FeignClient(name = "product-services", configuration = CustomFeignConfiguration.class) // used eureka discovery to connect product service directly
public interface InstantWebToolsApiClient {

    Logger log = LoggerFactory.getLogger(InstantWebToolsApiClient.class);

    @GetMapping(value = "/single-product-name/{theproductId}")
    @Retry(name = "productProxyRetry")
    @CircuitBreaker(name = "productProxyCircuitBreaker", fallbackMethod = "serviceFallbackMethod")
    String readreadProductById(@PathVariable int theproductId);

    default String serviceFallbackMethod(Throwable exception) {
        log.error(
                "Data server is either unavailable or malfunctioned due to {}", exception.getMessage());
        throw new RuntimeException(exception.getMessage());
    }

}
