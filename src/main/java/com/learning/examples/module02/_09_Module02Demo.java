package com.learning.examples.module02;

import java.util.List;

/**
 * Runnable entrypoint for Module 02.
 *
 * The demo shows OOP concepts that will appear in Selenium framework code:
 * interfaces, concrete classes, polymorphism, inheritance, abstraction,
 * encapsulation, custom exceptions, and collections.
 */
public class _09_Module02Demo {

    private static final String SAUCE_DEMO_URL = "https://www.saucedemo.com";

    public static void main(String[] args) {
        /*
         * The list type is _01_BrowserDriver, so it can hold any object that
         * implements the interface. This mirrors future cross-browser Selenium
         * code that talks to ChromeDriver or FirefoxDriver through WebDriver.
         */
        List<_01_BrowserDriver> browsers = List.of(
                new _02_ChromeBrowserDriver(),
                new _03_FirefoxBrowserDriver()
        );

        _05_LoginCredentials credentials = new _05_LoginCredentials("standard_user", "secret_sauce");

        for (_01_BrowserDriver browser : browsers) {
            printSection("Learning test on " + browser.getBrowserName());
            _08_SauceDemoLoginLearningTest learningTest =
                    new _08_SauceDemoLoginLearningTest(browser, SAUCE_DEMO_URL, credentials);
            learningTest.run();
        }

        printSection("Invalid test data example");
        showInvalidDataHandling();
    }

    private static void showInvalidDataHandling() {
        try {
            new _05_LoginCredentials("standard_user", "");
        } catch (_04_InvalidTestDataException exception) {
            System.out.println("Rejected invalid login data: " + exception.getMessage());
        }
    }

    /**
     * Keeps demo output readable while logging/reporting are still deferred.
     */
    private static void printSection(String title) {
        System.out.println();
        System.out.println("== " + title + " ==");
    }
}
