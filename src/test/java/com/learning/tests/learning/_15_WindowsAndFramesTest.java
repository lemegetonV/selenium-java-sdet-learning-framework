package com.learning.tests.learning;

import java.nio.file.Path;
import java.time.Duration;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Introduces browser window handles and frame context switching.
 */
public class _15_WindowsAndFramesTest {

    @Test
    public void switchesToNewWindowAndBackToOriginalWindow() {
        WebDriver driver = createChromeDriver();

        try {
            driver.get(module07FixtureUrl("advanced-interactions.html"));

            String originalWindow = driver.getWindowHandle();
            Set<String> windowsBeforeClick = driver.getWindowHandles();

            driver.findElement(By.id("new-window-link")).click();

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.numberOfWindowsToBe(windowsBeforeClick.size() + 1));

            String newWindow = driver.getWindowHandles().stream()
                    .filter(windowHandle -> !windowsBeforeClick.contains(windowHandle))
                    .findFirst()
                    .orElseThrow();

            /*
             * Selenium commands run against the currently selected window. After a
             * link opens a new tab/window, switchTo().window(handle) makes that
             * new browser context active.
             */
            driver.switchTo().window(newWindow);
            Assert.assertEquals(driver.findElement(By.id("window-heading")).getText(), "New Window Loaded");

            driver.close();
            driver.switchTo().window(originalWindow);
            Assert.assertEquals(driver.findElement(By.tagName("h1")).getText(), "Module 07 Advanced Interactions");
        } finally {
            driver.quit();
        }
    }

    @Test
    public void switchesIntoChildAndNestedFrames() {
        WebDriver driver = createChromeDriver();

        try {
            driver.get(module07FixtureUrl("advanced-interactions.html"));

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            /*
             * A frame is its own browsing context. Elements inside it are not
             * searchable until Selenium switches into that frame.
             */
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("profile-frame")));
            Assert.assertEquals(driver.findElement(By.id("frame-heading")).getText(), "Inside child frame");

            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("nested-frame")));
            Assert.assertEquals(driver.findElement(By.id("nested-frame-message")).getText(), "Inside nested frame");

            driver.switchTo().defaultContent();
            Assert.assertEquals(driver.findElement(By.tagName("h1")).getText(), "Module 07 Advanced Interactions");
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
