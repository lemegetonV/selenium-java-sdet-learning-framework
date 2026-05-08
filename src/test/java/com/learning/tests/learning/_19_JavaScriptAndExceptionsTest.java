package com.learning.tests.learning;

import java.nio.file.Path;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Introduces JavaScriptExecutor, broken image checks, and advanced exceptions.
 */
public class _19_JavaScriptAndExceptionsTest {

    @Test
    public void readsBrowserStateWithJavaScriptExecutor() {
        WebDriver driver = createChromeDriver();

        try {
            driver.get("https://the-internet.herokuapp.com/");

            /*
             * The variable is typed as WebDriver, so Java needs this cast before
             * executeScript is available on the reference.
             */
            JavascriptExecutor javascript = (JavascriptExecutor) driver;

            /*
             * JavaScriptExecutor runs script inside the current page. Use it
             * selectively when Selenium's normal WebElement API cannot expose the
             * browser state you need.
             */
            String title = (String) javascript.executeScript("return document.title;");

            /*
             * JavaScript numbers come back through WebDriver as Java numeric types.
             * This count is an integer-sized browser result, so Selenium maps it to Long.
             */
            Long linkCount = (Long) javascript.executeScript("return document.querySelectorAll('a').length;");

            Assert.assertEquals(title, "The Internet");
            Assert.assertTrue(linkCount > 20);
        } finally {
            driver.quit();
        }
    }

    @Test
    public void detectsBrokenImageWithJavaScriptProperties() {
        WebDriver driver = createChromeDriver();

        try {
            driver.get("https://the-internet.herokuapp.com/broken_images");

            WebElement brokenImage = driver.findElements(By.cssSelector(".example img")).get(0);
            JavascriptExecutor javascript = (JavascriptExecutor) driver;

            /*
             * complete means the browser finished loading the image request.
             * naturalWidth is zero when the loaded result has no image pixels.
             */
            Boolean isBroken = (Boolean) javascript.executeScript(
                    "return arguments[0].complete && arguments[0].naturalWidth === 0;",
                    brokenImage
            );

            Assert.assertTrue(isBroken);
        } finally {
            driver.quit();
        }
    }

    @Test
    public void observesAdvancedSeleniumExceptions() {
        WebDriver driver = createChromeDriver();

        try {
            driver.get(module07FixtureUrl("advanced-interactions.html"));

            /*
             * expectThrows is used here as a teaching tool: each lambda deliberately
             * performs one bad Selenium action so the exception category is visible.
             */
            Assert.expectThrows(
                    NoSuchFrameException.class,
                    () -> driver.switchTo().frame("missing-frame")
            );

            Assert.expectThrows(
                    ElementNotInteractableException.class,
                    () -> driver.findElement(By.id("hidden-button")).click()
            );

            Assert.expectThrows(
                    NoSuchWindowException.class,
                    () -> driver.switchTo().window("missing-window-handle")
            );
        } finally {
            driver.quit();
        }
    }

    private String module07FixtureUrl(String fileName) {
        return Path.of("src/test/resources/module07", fileName).toUri().toString();
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
