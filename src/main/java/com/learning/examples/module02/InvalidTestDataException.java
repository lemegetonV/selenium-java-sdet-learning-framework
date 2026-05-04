package com.learning.examples.module02;

/**
 * Custom exception for invalid learning test data.
 *
 * A named exception explains the failure category better than a generic
 * RuntimeException. Later framework modules use the same idea for invalid
 * config, unsupported browsers, wait failures, and reporting problems.
 */
public class InvalidTestDataException extends RuntimeException {

    public InvalidTestDataException(String message) {
        super(message);
    }
}
