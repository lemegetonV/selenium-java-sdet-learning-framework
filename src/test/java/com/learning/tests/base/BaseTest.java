package com.learning.tests.base;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.learning.framework.actions.ElementActions;
import com.learning.framework.config.ConfigReader;
import com.learning.framework.driver.DriverFactory;
import com.learning.framework.waits.WaitUtils;

/**
 * First reusable TestNG base class for framework-style tests.
 *
 * BaseTest owns test lifecycle. Module 11 delegates browser construction to
 * DriverFactory and reads timeout settings from ConfigReader.
 */
public class BaseTest {

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
        DriverFactory.createDriver();
        driver = DriverFactory.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWaitSeconds()));

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
            DriverFactory.quitDriver();
            driver = null;
            wait = null;
            waits = null;
            elementActions = null;
        }
    }
}
