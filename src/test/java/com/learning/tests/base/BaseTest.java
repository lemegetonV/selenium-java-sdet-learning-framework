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
     * Module 15 moves these from simple fields to ThreadLocal values. TestNG
     * can run methods from the same test class instance on different threads,
     * so normal instance fields can be overwritten by a parallel test method.
     */
    private final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private final ThreadLocal<WebDriverWait> wait = new ThreadLocal<>();
    private final ThreadLocal<WaitUtils> waits = new ThreadLocal<>();
    private final ThreadLocal<ElementActions> elementActions = new ThreadLocal<>();

    /**
     * TestNG runs this method before every @Test method in child classes.
     *
     * A fresh browser per test gives isolation: cookies, local storage, current
     * URL, windows, and logged-in state from one test do not leak into the next.
     */
    @BeforeMethod(alwaysRun = true)
    public void setUpBrowser() {
        DriverFactory.createDriver();
        WebDriver currentDriver = DriverFactory.getDriver();
        WebDriverWait currentWait = new WebDriverWait(
                currentDriver,
                Duration.ofSeconds(ConfigReader.getExplicitWaitSeconds())
        );

        /*
         * Module 10 introduces wrapper services after the learner has already
         * seen repeated raw Selenium calls inside page objects.
         */
        WaitUtils currentWaits = new WaitUtils(currentWait);
        ElementActions currentElementActions = new ElementActions(currentDriver, currentWaits);

        driver.set(currentDriver);
        wait.set(currentWait);
        waits.set(currentWaits);
        elementActions.set(currentElementActions);
    }

    /**
     * TestNG runs this method after every @Test method, even when the test fails.
     *
     * quit() ends the complete browser session. That is stronger than close(),
     * which only closes the currently selected tab or window.
     */
    @AfterMethod(alwaysRun = true)
    public void tearDownBrowser() {
        if (driver.get() != null) {
            DriverFactory.quitDriver();
            driver.remove();
            wait.remove();
            waits.remove();
            elementActions.remove();
        }
    }

    protected WebDriver driver() {
        return driver.get();
    }

    protected WaitUtils waits() {
        return waits.get();
    }

    protected ElementActions elementActions() {
        return elementActions.get();
    }
}
