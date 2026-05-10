package com.learning.framework.driver;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import com.learning.framework.config.ConfigReader;

/**
 * Owns WebDriver creation, access, and cleanup.
 *
 * Module 11 moves browser construction out of BaseTest. DriverFactory is also
 * the first place where ThreadLocal appears. ThreadLocal prepares the framework
 * for future parallel execution by keeping one driver per executing thread.
 */
public final class DriverFactory {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverFactory() {
        // Utility class: do not instantiate.
    }

    public static void createDriver() {
        if (DRIVER.get() != null) {
            return;
        }

        WebDriver driver = switch (ConfigReader.getBrowser()) {
            case "chrome" -> createChromeDriver();
            case "firefox" -> createFirefoxDriver();
            case "edge" -> createEdgeDriver();
            default -> throw new IllegalArgumentException(
                    "Unsupported browser: " + ConfigReader.getBrowser()
                            + ". Supported values: chrome, firefox, edge."
            );
        };

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigReader.getPageLoadTimeoutSeconds()));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getImplicitWaitSeconds()));

        DRIVER.set(driver);
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("Driver has not been created. Call DriverFactory.createDriver() first.");
        }
        return driver;
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }

    private static WebDriver createChromeDriver() {
        ChromeOptions options = new ChromeOptions();

        if (ConfigReader.isHeadless()) {
            options.addArguments("--headless=new");
        }

        options.addArguments(windowSizeArgument());
        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxDriver() {
        FirefoxOptions options = new FirefoxOptions();

        if (ConfigReader.isHeadless()) {
            options.addArguments("--headless");
        }

        WebDriver driver = new FirefoxDriver(options);
        driver.manage().window().setSize(configuredWindowSize());
        return driver;
    }

    private static WebDriver createEdgeDriver() {
        EdgeOptions options = new EdgeOptions();

        if (ConfigReader.isHeadless()) {
            options.addArguments("--headless=new");
        }

        options.addArguments(windowSizeArgument());
        return new EdgeDriver(options);
    }

    private static String windowSizeArgument() {
        return "--window-size=" + ConfigReader.getWindowWidth() + "," + ConfigReader.getWindowHeight();
    }

    private static org.openqa.selenium.Dimension configuredWindowSize() {
        return new org.openqa.selenium.Dimension(
                ConfigReader.getWindowWidth(),
                ConfigReader.getWindowHeight()
        );
    }
}
