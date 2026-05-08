package com.learning.examples.module01;

/**
 * Represents the data a user would type into a login form.
 *
 * This is intentionally not a Selenium class. It prepares the learner for
 * future test data and page object methods where username/password values are
 * passed into a login workflow.
 */
public class _02_LoginAttempt {

    /*
     * The fields are private and final to show encapsulation and immutability:
     * after a _02_LoginAttempt is created, outside code can read safe values through
     * methods but cannot rewrite the stored credentials.
     */
    private final String username;
    private final String password;

    /**
     * Creates one login attempt with the values a test would submit.
     */
    public _02_LoginAttempt(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Keeps the username validation rule in one place.
     */
    public boolean hasUsername() {
        /*
         * Check null before isBlank(). Calling a method on null would throw
         * NullPointerException, so defensive validation starts with the null
         * check and only then inspects the string contents.
         */
        return username != null && !username.isBlank();
    }

    /**
     * Keeps the password validation rule in one place.
     */
    public boolean hasPassword() {
        return password != null && !password.isBlank();
    }

    /**
     * Combines smaller validation methods into a readable business-style rule.
     *
     * Future page objects will use the same idea: a method name should
     * communicate intent instead of forcing callers to read every low-level
     * detail.
     */
    public boolean isReadyToSubmit() {
        return hasUsername() && hasPassword();
    }

    /**
     * Masks the password before printing demo output.
     *
     * This introduces a framework habit early: sensitive data should not be
     * exposed in logs, reports, screenshots, or console output.
     */
    public String getMaskedPassword() {
        if (!hasPassword()) {
            return "";
        }
        /*
         * repeat(...) creates one mask character per password character. The
         * real value stays private, which previews later logging/reporting rules
         * where secrets must not appear in output.
         */
        return "*".repeat(password.length());
    }

    /**
     * Exposes the username because it is safe to display in this learning demo.
     */
    public String getUsername() {
        return username;
    }
}
