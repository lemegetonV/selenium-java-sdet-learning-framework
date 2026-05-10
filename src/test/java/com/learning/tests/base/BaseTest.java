package com.learning.tests.base;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.learning.framework.actions.ElementActions;
import com.learning.framework.waits.WaitUtils;

/**
 * First reusable TestNG base class for framework-style tests.
 *
 * Module 08 intentionally keeps driver creation simple: Chrome is still created
 * directly here so the learner can see exactly what was repeated in Modules
 * 03-07. Module 11 will move this responsibility into DriverFactory after the
 * need for external browser configuration is clear.
 */
public class BaseTest {

    private static final int DEFAULT_TIMEOUT_SECONDS = 10;

    /*
     * protected is the framework compromise for this module: child test classes
     * can use driver and wait directly, while non-child classes cannot access
     * them as public global state.
     */
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected WaitUtils waits;
    protected ElementActions elementActions;

    /**
     * TestNG runs this method before every @Test method in child classes.
     *
     * A fresh browser per test gives isolation: cookies, local storage, current
     * URL, windows, and logged-in state from one test do not leak into the next.
     */
    @BeforeMethod(alwaysRun = true)
    public void setUpBrowser() {
        ChromeOptions options = new ChromeOptions();

        if (Boolean.parseBoolean(System.getProperty("headless", "true"))) {
            options.addArguments("--headless=new");
        }

        options.addArguments("--window-size=1440,900");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));

        /*
         * Module 10 introduces wrapper services after the learner has already
         * seen repeated raw Selenium calls inside page objects.
         */
        waits = new WaitUtils(wait);
        elementActions = new ElementActions(driver, waits);
    }

    /**
     * TestNG runs this method after every @Test method, even when the test fails.
     *
     * quit() ends the complete browser session. That is stronger than close(),
     * which only closes the currently selected tab or window.
     */
    @AfterMethod(alwaysRun = true)
    public void tearDownBrowser() {
        if (driver != null) {
            driver.quit();
            driver = null;
            wait = null;
            waits = null;
            elementActions = null;
        }
    }
}
