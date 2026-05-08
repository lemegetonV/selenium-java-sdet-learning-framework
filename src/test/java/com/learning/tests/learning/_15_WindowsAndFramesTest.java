package com.learning.tests.learning;

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
            driver.get("https://the-internet.herokuapp.com/windows");

            String originalWindow = driver.getWindowHandle();

            /*
             * getWindowHandles returns all known top-level browser contexts.
             * Capturing the set before the click lets the test identify only the
             * handle that appears after the new window opens.
             */
            Set<String> windowsBeforeClick = driver.getWindowHandles();

            driver.findElement(By.linkText("Click Here")).click();

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.numberOfWindowsToBe(windowsBeforeClick.size() + 1));

            /*
             * The stream keeps the handle that was not present before the click.
             * orElseThrow is deliberate because the test cannot continue without
             * a new window context.
             */
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
            Assert.assertEquals(driver.findElement(By.tagName("h3")).getText(), "New Window");

            /*
             * close() closes the selected window only. The original window still
             * exists, so Selenium must switch back before the next assertion.
             */
            driver.close();
            driver.switchTo().window(originalWindow);
            Assert.assertTrue(driver.getCurrentUrl().contains("/windows"));
        } finally {
            driver.quit();
        }
    }

    @Test
    public void switchesIntoChildAndNestedFrames() {
        WebDriver driver = createChromeDriver();

        try {
            driver.get("https://the-internet.herokuapp.com/nested_frames");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            /*
             * A frame is its own browsing context. Elements inside it are not
             * searchable until Selenium switches into that frame.
             */
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("frame-top"));
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("frame-left"));
            Assert.assertEquals(driver.findElement(By.tagName("body")).getText().trim(), "LEFT");

            /*
             * parentFrame moves one level up from frame-left to frame-top. It is
             * different from defaultContent, which returns to the top page.
             */
            driver.switchTo().parentFrame();
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("frame-middle"));
            Assert.assertEquals(driver.findElement(By.id("content")).getText().trim(), "MIDDLE");

            // Return to the top-level page before making page-level assertions.
            driver.switchTo().defaultContent();
            Assert.assertTrue(driver.getCurrentUrl().contains("/nested_frames"));
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
