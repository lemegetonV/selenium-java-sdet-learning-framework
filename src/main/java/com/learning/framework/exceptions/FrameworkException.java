package com.learning.framework.exceptions;

/**
 * Runtime exception for failures caused by framework setup or framework
 * services, rather than by a product assertion.
 *
 * Module 13 introduces this so future utilities can fail with a clear
 * framework-level vocabulary. For example, "configuration file is missing" and
 * "screenshot could not be saved" are framework failures. "Products page title
 * was wrong" is still a test assertion failure.
 */
public class FrameworkException extends RuntimeException {

    public FrameworkException(String message) {
        super(message);
    }

    public FrameworkException(String message, Throwable cause) {
        super(message, cause);
    }
}
