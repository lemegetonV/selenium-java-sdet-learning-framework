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

            /*
             * navigate().to() also loads a URL, but using navigate() here makes
             * the browser-history lesson explicit because the next commands are
             * back(), forward(), and refresh().
             */
            driver.navigate().to("https://the-internet.herokuapp.com/login");
            Assert.assertTrue(driver.getCurrentUrl().contains("/login"));

            /*
             * back() depends on a previous browser-history entry. The test stores
             * homeUrl before navigating so it can prove the browser returned to
             * the original page instead of assuming the command worked.
             */
            driver.navigate().back();
            Assert.assertEquals(driver.getCurrentUrl(), homeUrl);

            driver.navigate().forward();
            Assert.assertTrue(driver.getCurrentUrl().contains("/login"));

            /*
             * refresh() reloads the current page in the same session. It can
             * replace DOM nodes, which becomes important when stale elements are
             * introduced in the waits module.
             */
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
