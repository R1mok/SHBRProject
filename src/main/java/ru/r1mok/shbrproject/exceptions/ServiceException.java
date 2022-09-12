package ru.r1mok.shbrproject.exceptions;

import lombok.Getter;

import java.util.function.Supplier;

@Getter
public class ServiceException extends RuntimeException implements Supplier<ServiceException> {
    private String message;

    private int code;
    public ServiceException() {}

    public ServiceException(String message, int code) {
        this.message = message;
        this.code = code;
    }

    @Override
    public ServiceException get() {
        return this;
    }
}
