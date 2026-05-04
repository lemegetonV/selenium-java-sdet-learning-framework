package com.learning.examples.module02;

import java.util.List;

/**
 * Runnable entrypoint for Module 02.
 *
 * The demo shows OOP concepts that will appear in Selenium framework code:
 * interfaces, concrete classes, polymorphism, inheritance, abstraction,
 * encapsulation, custom exceptions, and collections.
 */
public class Module02Demo {

    private static final String SAUCE_DEMO_URL = "https://www.saucedemo.com";

    public static void main(String[] args) {
        /*
         * The list type is BrowserDriver, so it can hold any object that
         * implements the interface. This mirrors future cross-browser Selenium
         * code that talks to ChromeDriver or FirefoxDriver through WebDriver.
         */
        List<BrowserDriver> browsers = List.of(
                new ChromeBrowserDriver(),
                new FirefoxBrowserDriver()
        );

        LoginCredentials credentials = new LoginCredentials("standard_user", "secret_sauce");

        for (BrowserDriver browser : browsers) {
            printSection("Learning test on " + browser.getBrowserName());
            SauceDemoLoginLearningTest learningTest =
                    new SauceDemoLoginLearningTest(browser, SAUCE_DEMO_URL, credentials);
            learningTest.run();
        }

        printSection("Invalid test data example");
        showInvalidDataHandling();
    }

    private static void showInvalidDataHandling() {
        try {
            new LoginCredentials("standard_user", "");
        } catch (InvalidTestDataException exception) {
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
