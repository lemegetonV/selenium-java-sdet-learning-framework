package com.learning.tests.learning;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * First real Selenium test in the learning path.
 *
 * The setup and teardown are intentionally inside this class. Module 03 should
 * make the raw WebDriver lifecycle visible before Module 08 extracts shared
 * setup into BaseTest.
 */
public class _01_FirstBrowserTest {

    @Test
    public void opensTheInternetHomePage() {
        /*
         * WebDriver is the Selenium interface, just like Module 02's
         * _01_BrowserDriver interface. The actual object created inside the
         * helper is ChromeDriver, but the test talks through common browser
         * behavior.
         */
        WebDriver driver = createChromeDriver();

        try {
            // get() loads a full page and waits for Selenium's normal page-load completion signal.
            driver.get("https://the-internet.herokuapp.com/");

            // getTitle() and getCurrentUrl() read browser state without needing element locators.
            Assert.assertEquals(driver.getTitle(), "The Internet");
            Assert.assertTrue(
                    driver.getCurrentUrl().contains("the-internet.herokuapp.com"),
                    "The browser should stay on The Internet test site"
            );
        } finally {
            /*
             * quit() ends the whole browser session even if an assertion fails.
             * This is the raw version of cleanup that later moves into TestNG
             * @AfterMethod.
             */
            driver.quit();
        }
    }

    /**
     * Creates a ChromeDriver through the WebDriver interface.
     *
     * This is the real Selenium version of the Module 02 polymorphism idea:
     * WebDriver is the interface, ChromeDriver is the concrete class.
     */
    private WebDriver createChromeDriver() {
        // ChromeOptions configures browser startup before ChromeDriver is created.
        ChromeOptions options = new ChromeOptions();

        if (Boolean.parseBoolean(System.getProperty("headless", "true"))) {
            /*
             * System.getProperty reads the -Dheadless value from Maven. The
             * default is true so normal test runs are fast and CI-friendly.
             */
            options.addArguments("--headless=new");
        }

        options.addArguments("--window-size=1440,900");
        // Selenium Manager resolves the matching ChromeDriver automatically in modern Selenium.
        return new ChromeDriver(options);
    }
}
