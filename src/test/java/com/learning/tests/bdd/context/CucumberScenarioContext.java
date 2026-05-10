package com.learning.tests.bdd.context;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.learning.framework.actions.ElementActions;
import com.learning.framework.config.ConfigReader;
import com.learning.framework.driver.DriverFactory;
import com.learning.framework.waits.WaitUtils;

/**
 * Scenario-scoped service holder for Cucumber glue code.
 *
 * TestNG tests inherit BaseTest, but Cucumber step classes are created by
 * Cucumber's object factory. This context gives hooks and step definitions the
 * same framework services while keeping each scenario isolated through
 * ThreadLocal storage.
 */
public final class CucumberScenarioContext {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
    private static final ThreadLocal<WebDriverWait> WAIT = new ThreadLocal<>();
    private static final ThreadLocal<WaitUtils> WAITS = new ThreadLocal<>();
    private static final ThreadLocal<ElementActions> ELEMENT_ACTIONS = new ThreadLocal<>();

    private CucumberScenarioContext() {
        // Utility class: Cucumber scenarios use the static scenario context.
    }

    public static void openBrowser() {
        DriverFactory.createDriver();
        WebDriver currentDriver = DriverFactory.getDriver();
        WebDriverWait currentWait = new WebDriverWait(
                currentDriver,
                Duration.ofSeconds(ConfigReader.getExplicitWaitSeconds())
        );
        WaitUtils currentWaits = new WaitUtils(currentWait);
        ElementActions currentActions = new ElementActions(currentDriver, currentWaits);

        DRIVER.set(currentDriver);
        WAIT.set(currentWait);
        WAITS.set(currentWaits);
        ELEMENT_ACTIONS.set(currentActions);
    }

    public static boolean hasDriver() {
        return DRIVER.get() != null;
    }

    public static WebDriver driver() {
        return DRIVER.get();
    }

    public static WaitUtils waits() {
        return WAITS.get();
    }

    public static ElementActions elementActions() {
        return ELEMENT_ACTIONS.get();
    }

    public static void closeBrowser() {
        if (hasDriver()) {
            DriverFactory.quitDriver();
            DRIVER.remove();
            WAIT.remove();
            WAITS.remove();
            ELEMENT_ACTIONS.remove();
        }
    }
}
