package com.learning.tests.learning;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Opens the main application under test without interacting with elements yet.
 *
 * Module 03 only proves that Selenium can launch a browser and load SauceDemo.
 * Locator-based login interactions are intentionally deferred to Module 04.
 */
public class _03_SauceDemoPageLoadTest {

    @Test
    public void opensSauceDemoLoginPage() {
        WebDriver driver = createChromeDriver();

        try {
            driver.get("https://www.saucedemo.com/");

            Assert.assertEquals(driver.getTitle(), "Swag Labs");
            Assert.assertTrue(
                    driver.getCurrentUrl().contains("saucedemo.com"),
                    "The browser should load the SauceDemo application"
            );
        } finally {
            driver.quit();
        }
    }

    private WebDriver createChromeDriver() {
        ChromeOptions options = new ChromeOptions();

        if (Boolean.parseBoolean(System.getProperty("headless", "true"))) {
            options.addArguments("--headless=new");
        }

        options.addArguments("--window-size=1440,900");
        return new ChromeDriver(options);
    }
}
