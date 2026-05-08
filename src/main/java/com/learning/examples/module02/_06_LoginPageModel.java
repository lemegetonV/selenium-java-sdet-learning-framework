package com.learning.examples.module02;

import java.util.List;

/**
 * Page-style model for the SauceDemo login page.
 *
 * This is not a Selenium Page Object yet. It teaches the Page Object direction:
 * keep page details private and expose a public action that describes what the
 * user wants to do.
 */
public class _06_LoginPageModel {

    /*
     * These strings stand in for future Selenium locators. They are private
     * because tests should not couple themselves directly to page internals.
     */
    private final String usernameField = "username field";
    private final String passwordField = "password field";
    private final String loginButton = "login button";

    /**
     * Returns simulated action steps for a login workflow.
     *
     * Later this public method maps to a real LoginPage method that uses
     * WebDriver or wrapper methods internally.
     */
    public List<String> loginWith(_05_LoginCredentials credentials) {
        /*
         * The method returns steps because this module is still simulated. In a
         * real page object, the same public method would call Selenium or wrapper
         * methods internally and the test would not know the locator details.
         */
        return List.of(
                "Type " + credentials.getUsername() + " into " + usernameField,
                "Type " + credentials.getMaskedPassword() + " into " + passwordField,
                "Click " + loginButton
        );
    }
}
