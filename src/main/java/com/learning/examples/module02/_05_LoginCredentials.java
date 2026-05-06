package com.learning.examples.module02;

/**
 * Encapsulates username and password test data.
 *
 * The class validates data at construction time so bad test data fails early.
 * Later modules will move login data into TestNG DataProviders and external
 * files, but the OOP principle stays the same: data shape and validation
 * should be explicit.
 */
public class _05_LoginCredentials {

    private final String username;
    private final String password;

    public _05_LoginCredentials(String username, String password) {
        if (isBlank(username)) {
            throw new _04_InvalidTestDataException("Username must not be blank");
        }
        if (isBlank(password)) {
            throw new _04_InvalidTestDataException("Password must not be blank");
        }

        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    /**
     * Passwords should not be printed directly in logs or reports.
     */
    public String getMaskedPassword() {
        return "*".repeat(password.length());
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
