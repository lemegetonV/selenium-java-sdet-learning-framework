package com.learning.examples.module02;

/**
 * Learning interface that models the role Selenium's WebDriver will play
 * later.
 *
 * An interface defines what an object can do without saying how each browser
 * does it. Module 02 uses this contract to make polymorphism visible before
 * real Selenium browser classes are introduced.
 */
public interface BrowserDriver {

    /**
     * Returns the browser name used in demo output.
     */
    String getBrowserName();

    /**
     * Opens the browser-like object against a target URL.
     *
     * Later, this maps to driver.get(baseUrl) on a real WebDriver object.
     */
    void open(String baseUrl);

    /**
     * Closes the browser-like object.
     *
     * Later, this maps to driver.quit() during Selenium cleanup.
     */
    void close();

    /**
     * Lets callers ask for state without reading implementation fields.
     */
    boolean isOpen();
}
