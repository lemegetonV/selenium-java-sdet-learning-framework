package com.learning.examples.module02;

import java.util.List;

/**
 * Page-style model for the SauceDemo login page.
 *
 * This is not a Selenium Page Object yet. It teaches the Page Object direction:
 * keep page details private and expose a public action that describes what the
 * user wants to do.
 */
public class LoginPageModel {

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
    public List<String> loginWith(LoginCredentials credentials) {
        return List.of(
                "Type " + credentials.getUsername() + " into " + usernameField,
                "Type " + credentials.getMaskedPassword() + " into " + passwordField,
                "Click " + loginButton
        );
    }
}
