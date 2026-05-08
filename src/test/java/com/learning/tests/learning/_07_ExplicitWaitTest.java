package com.learning.tests.learning;

import java.time.Duration;

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
 * Demonstrates explicit waits on The Internet dynamic loading page.
 *
 * Explicit waits are introduced before framework wait utilities so the learner
 * can see the raw Selenium API and the timing problem it solves.
 */
public class _07_ExplicitWaitTest {

    @Test
    public void waitsUntilHiddenTextBecomesVisible() {
        WebDriver driver = createChromeDriver();

        try {
            driver.get("https://the-internet.herokuapp.com/dynamic_loading/1");

            driver.findElement(By.cssSelector("#start button")).click();

            /*
             * WebDriverWait polls the browser until the ExpectedCondition returns
             * a successful value or the timeout expires. This is state-based
             * synchronization, not a fixed sleep.
             */
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            /*
             * visibilityOfElementLocated returns the WebElement after it both
             * exists and is displayed. The returned element can be asserted
             * immediately because the condition already proved visibility.
             */
            WebElement finishMessage = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.id("finish"))
            );

            Assert.assertEquals(finishMessage.getText(), "Hello World!");
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
