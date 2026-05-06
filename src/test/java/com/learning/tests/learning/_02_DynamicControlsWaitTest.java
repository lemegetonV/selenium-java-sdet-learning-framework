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
 * Demonstrates waits for elements being removed, added, and enabled.
 */
public class _02_DynamicControlsWaitTest {

    @Test
    public void waitsForCheckboxRemovalAndInputEnablement() {
        WebDriver driver = createChromeDriver();

        try {
            driver.get("https://the-internet.herokuapp.com/dynamic_controls");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            By checkbox = By.id("checkbox");
            By checkboxButton = By.cssSelector("#checkbox-example button");
            driver.findElement(checkboxButton).click();

            // invisibilityOfElementLocated is useful when the element disappears from the page.
            Assert.assertTrue(wait.until(ExpectedConditions.invisibilityOfElementLocated(checkbox)));
            Assert.assertEquals(driver.findElement(By.id("message")).getText(), "It's gone!");

            // The page reuses the same button; wait until it has changed from Remove to Add.
            wait.until(ExpectedConditions.textToBePresentInElementLocated(checkboxButton, "Add"));
            driver.findElement(checkboxButton).click();

            // The checkbox returns with the same id but a different DOM shape, so the id locator is safer.
            WebElement restoredCheckbox = wait.until(ExpectedConditions.visibilityOfElementLocated(checkbox));
            Assert.assertTrue(restoredCheckbox.isDisplayed());

            By inputField = By.cssSelector("#input-example input");
            driver.findElement(By.cssSelector("#input-example button")).click();

            // elementToBeClickable waits for visibility and enabled state before typing.
            WebElement enabledInput = wait.until(ExpectedConditions.elementToBeClickable(inputField));
            enabledInput.sendKeys("waited value");

            Assert.assertEquals(enabledInput.getAttribute("value"), "waited value");
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
