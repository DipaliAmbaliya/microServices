package com.cb.microservice.exception;

public class FeignClientException extends JavatoDevGlobalException {

    public FeignClientException(String message, Long code) {
        super(message, code);
    }

}
