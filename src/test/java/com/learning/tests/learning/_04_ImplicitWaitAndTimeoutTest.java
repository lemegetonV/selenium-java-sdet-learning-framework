package com.learning.tests.learning;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Demonstrates implicit wait setup and a controlled explicit wait timeout.
 */
public class _04_ImplicitWaitAndTimeoutTest {

    @Test
    public void usesShortImplicitWaitForElementLookup() {
        WebDriver driver = createChromeDriver();

        try {
            // Implicit wait applies to future findElement/findElements calls for this driver.
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

            // This test catches TimeoutException intentionally to teach timeout behavior.
            TimeoutException timeout = Assert.expectThrows(
                    TimeoutException.class,
                    () -> wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("does-not-exist")))
            );

            Assert.assertTrue(timeout.getMessage().contains("Expected condition failed"));
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
