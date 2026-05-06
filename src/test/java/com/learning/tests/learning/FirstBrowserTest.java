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
public class FirstBrowserTest {

    @Test
    public void opensTheInternetHomePage() {
        WebDriver driver = createChromeDriver();

        try {
            driver.get("https://the-internet.herokuapp.com/");

            Assert.assertEquals(driver.getTitle(), "The Internet");
            Assert.assertTrue(
                    driver.getCurrentUrl().contains("the-internet.herokuapp.com"),
                    "The browser should stay on The Internet test site"
            );
        } finally {
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
        ChromeOptions options = new ChromeOptions();

        if (Boolean.parseBoolean(System.getProperty("headless", "true"))) {
            options.addArguments("--headless=new");
        }

        options.addArguments("--window-size=1440,900");
        return new ChromeDriver(options);
    }
}
