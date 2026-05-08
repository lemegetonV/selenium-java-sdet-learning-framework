package com.learning.tests.learning;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Demonstrates FluentWait for customized polling behavior.
 */
public class _09_FluentWaitTest {

    @Test
    public void usesCustomPollingForDynamicLoading() {
        WebDriver driver = createChromeDriver();

        try {
            driver.get("https://the-internet.herokuapp.com/dynamic_loading/2");
            driver.findElement(By.cssSelector("#start button")).click();

            /*
             * FluentWait is the configurable parent concept behind WebDriverWait.
             * This example customizes polling and ignores NoSuchElementException
             * while the dynamic element is not in the DOM yet.
             */
            FluentWait<WebDriver> wait = new FluentWait<>(driver)
                    .withTimeout(Duration.ofSeconds(10))
                    .pollingEvery(Duration.ofMillis(250))
                    .ignoring(NoSuchElementException.class);

            WebElement finishMessage = wait.until(currentDriver -> {
                /*
                 * This lambda is the custom wait condition. Returning the element
                 * means success; returning null means "keep polling" until the
                 * timeout is reached.
                 */
                WebElement element = currentDriver.findElement(By.id("finish"));
                return element.isDisplayed() ? element : null;
            });

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
