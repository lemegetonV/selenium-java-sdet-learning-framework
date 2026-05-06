package com.learning.tests.learning;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Demonstrates basic browser navigation commands.
 *
 * The duplication with _01_FirstBrowserTest is intentional. Later modules will
 * remove this repetition only after the learner has seen why it hurts.
 */
public class _02_NavigationTest {

    @Test
    public void navigatesBackAndForwardBetweenPages() {
        WebDriver driver = createChromeDriver();

        try {
            driver.get("https://the-internet.herokuapp.com/");
            String homeUrl = driver.getCurrentUrl();

            // navigate().to() is another way to load a URL and starts a browser-history entry.
            driver.navigate().to("https://the-internet.herokuapp.com/login");
            Assert.assertTrue(driver.getCurrentUrl().contains("/login"));

            // back() and forward() use browser history, just like the browser toolbar buttons.
            driver.navigate().back();
            Assert.assertEquals(driver.getCurrentUrl(), homeUrl);

            driver.navigate().forward();
            Assert.assertTrue(driver.getCurrentUrl().contains("/login"));

            // refresh() reloads the current page; it does not create a new test or browser session.
            driver.navigate().refresh();
            Assert.assertEquals(driver.getTitle(), "The Internet");
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
