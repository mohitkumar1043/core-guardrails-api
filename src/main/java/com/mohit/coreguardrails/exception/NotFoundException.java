package com.mohit.coreguardrails.exception;

@SuppressWarnings("serial")
public class NotFoundException extends RuntimeException {
	public NotFoundException(String message) {
        super(message);
    }
}
