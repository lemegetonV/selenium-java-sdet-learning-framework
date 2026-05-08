package com.learning.tests.learning;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Demonstrates implicit wait setup and a controlled explicit wait timeout.
 */
public class _10_ImplicitWaitAndTimeoutTest {

    @Test
    public void usesShortImplicitWaitForElementLookup() {
        WebDriver driver = createChromeDriver();

        try {
            /*
             * Implicit wait is global driver state for future element lookups.
             * The module keeps it short because long implicit waits can make
             * framework timing hard to diagnose when explicit waits are also used.
             */
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));

            driver.get("https://the-internet.herokuapp.com/");

            Assert.assertTrue(driver.findElement(By.linkText("Dynamic Loading")).isDisplayed());
        } finally {
            driver.quit();
        }
    }

    @Test
    public void observesTimeoutWhenConditionNeverBecomesTrue() {
        WebDriver driver = createChromeDriver();

        try {
            driver.get("https://the-internet.herokuapp.com/dynamic_loading/1");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));

            /*
             * The lambda delays the wait call so TestNG's expectThrows can verify
             * the TimeoutException. The test is intentionally green while still
             * teaching what a timeout failure looks like.
             */
            TimeoutException timeout = Assert.expectThrows(
                    TimeoutException.class,
                    () -> wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("does-not-exist")))
            );

            Assert.assertTrue(timeout.getMessage().contains("Expected condition failed"));
        } finally {
            driver.quit();
        }
    }

    @Test
    public void observesStaleElementWhenDomNodeIsReplaced() {
        WebDriver driver = createChromeDriver();

        try {
            driver.get("https://the-internet.herokuapp.com/dynamic_controls");

            WebElement originalCheckbox = driver.findElement(By.cssSelector("#checkbox input"));
            driver.findElement(By.cssSelector("#checkbox-example button")).click();

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            /*
             * stalenessOf waits until the saved WebElement no longer points to a
             * live DOM node. The locator may be reusable, but this object reference
             * is now stale.
             */
            Assert.assertTrue(wait.until(ExpectedConditions.stalenessOf(originalCheckbox)));

            StaleElementReferenceException staleElement = Assert.expectThrows(
                    StaleElementReferenceException.class,
                    /*
                     * Method reference syntax points to isSelected on the old
                     * WebElement. Calling it after removal demonstrates that the
                     * saved object is stale even though a locator may work later.
                     */
                    originalCheckbox::isSelected
            );
            Assert.assertNotNull(staleElement.getMessage());
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
