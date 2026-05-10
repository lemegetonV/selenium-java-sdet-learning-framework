package com.learning.framework.driver;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.learning.framework.config.ConfigReader;
import com.learning.framework.exceptions.FrameworkException;

/**
 * Owns WebDriver creation, access, and cleanup.
 *
 * Module 11 moves browser construction out of BaseTest. Module 15 now uses
 * ThreadLocal for real controlled parallel execution and adds a Grid execution
 * path through RemoteWebDriver.
 */
public final class DriverFactory {

    private static final Logger LOGGER = LogManager.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverFactory() {
        // Utility class: do not instantiate.
    }

    public static void createDriver() {
        if (DRIVER.get() != null) {
            return;
        }

        WebDriver driver = switch (ConfigReader.getExecutionMode()) {
            case "local" -> createLocalDriver();
            case "grid" -> createRemoteDriver();
            default -> throw new FrameworkException(
                    "Unsupported executionMode: " + ConfigReader.getExecutionMode()
                            + ". Supported values: local, grid."
            );
        };

        configureDriver(driver);
        DRIVER.set(driver);
        LOGGER.info("Created {} {} browser session on thread {} with window {}x{}",
                ConfigReader.getExecutionMode(),
                ConfigReader.getBrowser(),
                Thread.currentThread().threadId(),
                ConfigReader.getWindowWidth(),
                ConfigReader.getWindowHeight());
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            throw new FrameworkException("Driver has not been created. Call DriverFactory.createDriver() first.");
        }
        return driver;
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            LOGGER.info("Quitting browser session on thread {}", Thread.currentThread().threadId());
            driver.quit();
            DRIVER.remove();
        }
    }

    private static WebDriver createLocalDriver() {
        return switch (ConfigReader.getBrowser()) {
            case "chrome" -> createChromeDriver();
            case "firefox" -> createFirefoxDriver();
            case "edge" -> createEdgeDriver();
            default -> throw new FrameworkException(
                    "Unsupported browser: " + ConfigReader.getBrowser()
                            + ". Supported values: chrome, firefox, edge."
            );
        };
    }

    private static WebDriver createRemoteDriver() {
        URL remoteUrl = gridUrl();
        LOGGER.info("Connecting to Selenium Grid at {}", remoteUrl);

        return switch (ConfigReader.getBrowser()) {
            case "chrome" -> new RemoteWebDriver(remoteUrl, chromeOptions());
            case "firefox" -> new RemoteWebDriver(remoteUrl, firefoxOptions());
            case "edge" -> new RemoteWebDriver(remoteUrl, edgeOptions());
            default -> throw new FrameworkException(
                    "Unsupported browser for Grid: " + ConfigReader.getBrowser()
                            + ". Supported values: chrome, firefox, edge."
            );
        };
    }

    private static WebDriver createChromeDriver() {
        return new ChromeDriver(chromeOptions());
    }

    private static WebDriver createFirefoxDriver() {
        return new FirefoxDriver(firefoxOptions());
    }

    private static WebDriver createEdgeDriver() {
        return new EdgeDriver(edgeOptions());
    }

    private static ChromeOptions chromeOptions() {
        ChromeOptions options = new ChromeOptions();
        if (ConfigReader.isHeadless()) {
            options.addArguments("--headless=new");
        }
        options.addArguments(windowSizeArgument());
        return options;
    }

    private static FirefoxOptions firefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();
        if (ConfigReader.isHeadless()) {
            options.addArguments("--headless");
        }
        return options;
    }

    private static EdgeOptions edgeOptions() {
        EdgeOptions options = new EdgeOptions();
        if (ConfigReader.isHeadless()) {
            options.addArguments("--headless=new");
        }
        options.addArguments(windowSizeArgument());
        return options;
    }

    private static void configureDriver(WebDriver driver) {
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigReader.getPageLoadTimeoutSeconds()));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getImplicitWaitSeconds()));
        driver.manage().window().setSize(configuredWindowSize());
    }

    private static URL gridUrl() {
        try {
            return URI.create(ConfigReader.getGridUrl()).toURL();
        } catch (IllegalArgumentException | MalformedURLException exception) {
            throw new FrameworkException("Invalid Selenium Grid URL: " + ConfigReader.getGridUrl(), exception);
        }
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
