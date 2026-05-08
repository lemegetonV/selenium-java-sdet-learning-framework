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
            driver.get(module07FixtureUrl("advanced-interactions.html"));

            JavascriptExecutor javascript = (JavascriptExecutor) driver;

            /*
             * JavaScriptExecutor runs script inside the current page. Use it
             * selectively when Selenium's normal WebElement API cannot expose the
             * browser state you need.
             */
            String title = (String) javascript.executeScript("return document.title;");
            Long sectionCount = (Long) javascript.executeScript("return document.querySelectorAll('section').length;");

            Assert.assertEquals(title, "Module 07 Advanced Interactions");
            Assert.assertTrue(sectionCount >= 8);
        } finally {
            driver.quit();
        }
    }

    @Test
    public void detectsBrokenImageWithJavaScriptProperties() {
        WebDriver driver = createChromeDriver();

        try {
            driver.get(module07FixtureUrl("advanced-interactions.html"));

            WebElement brokenImage = driver.findElement(By.id("broken-image"));
            JavascriptExecutor javascript = (JavascriptExecutor) driver;

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
