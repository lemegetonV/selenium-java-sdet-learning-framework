package com.learning.examples.module01;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Runnable entrypoint for Module 01.
 *
 * The demo wires together the small learning classes so the learner can see
 * objects, method calls, collections, loops, and maps working in one place.
 * Later modules replace these simulated automation ideas with real Selenium
 * WebDriver code.
 */
public class Module01Demo {

    public static void main(String[] args) {
        /*
         * BrowserSession is an automation-style object, but not a real browser.
         * It prepares the mental model for WebDriver objects in Selenium.
         */
        BrowserSession session = new BrowserSession("https://www.saucedemo.com");
        session.open();

        /*
         * LoginAttempt represents test input. Later this kind of data will flow
         * into a LoginPage method instead of being printed by a demo.
         */
        LoginAttempt loginAttempt = new LoginAttempt("standard_user", "secret_sauce");

        /*
         * TestCaseSummary shows an object owning a mutable list. The list is
         * updated through methods so the class controls its own state.
         */
        TestCaseSummary testCase = new TestCaseSummary("valid login");
        testCase.addStep("Open browser");
        testCase.addStep("Navigate to SauceDemo");
        testCase.addStep("Enter username and password");
        testCase.addStep("Submit login form");

        /*
         * Lists are common in automation: test names, element collections,
         * product names, table rows, and data-driven records all use the same
         * collection concept.
         */
        List<String> smokeTests = List.of("valid login", "product list loads", "cart opens");

        /*
         * This map previews future config files where values such as browser,
         * base URL, and headless mode are read from outside the code.
         */
        Map<String, String> environment = new LinkedHashMap<>();
        environment.put("browser", BrowserSession.DEFAULT_BROWSER);
        environment.put("baseUrl", "https://www.saucedemo.com");

        printSection("Browser Session");
        System.out.println(session.getSessionSummary());
        System.out.println("Sessions created: " + BrowserSession.getCreatedSessionCount());

        printSection("Login Attempt");
        System.out.println("Username: " + loginAttempt.getUsername());
        System.out.println("Masked password: " + loginAttempt.getMaskedPassword());
        System.out.println("Ready to submit: " + loginAttempt.isReadyToSubmit());

        printSection("Test Case");
        System.out.println(testCase.describe());
        for (String step : testCase.getSteps()) {
            System.out.println("- " + step);
        }

        printSection("Smoke Tests");
        for (int index = 0; index < smokeTests.size(); index++) {
            System.out.println((index + 1) + ". " + smokeTests.get(index));
        }

        printSection("Environment");
        for (Map.Entry<String, String> entry : environment.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }

        session.close();
    }

    /**
     * Keeps repeated console formatting in one method.
     *
     * In later modules, this kind of repeated support behavior moves into
     * framework utilities or reporting/logging helpers.
     */
    private static void printSection(String title) {
        System.out.println();
        System.out.println("== " + title + " ==");
    }
}
