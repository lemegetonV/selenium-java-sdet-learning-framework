package com.learning.tests.learning;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Demonstrates first WebElement commands on the SauceDemo login page.
 */
public class _03_WebElementCommandTest {

    @Test
    public void typesClearsClicksAndReadsElementState() {
        WebDriver driver = createChromeDriver();

        try {
            driver.get("https://www.saucedemo.com/");

            WebElement usernameInput = driver.findElement(By.id("user-name"));
            WebElement passwordInput = driver.findElement(By.id("password"));
            WebElement loginButton = driver.findElement(By.id("login-button"));

            // sendKeys types into an editable element, similar to real keyboard input.
            usernameInput.sendKeys("temporary_user");
            Assert.assertEquals(usernameInput.getAttribute("value"), "temporary_user");

            // clear removes the current field value before entering the intended test data.
            usernameInput.clear();
            usernameInput.sendKeys("locked_out_user");

            passwordInput.sendKeys("secret_sauce");

            // click performs the element's default click action.
            loginButton.click();

            WebElement errorMessage = driver.findElement(By.cssSelector("[data-test='error']"));

            // getText reads visible text; getAttribute reads a specific DOM attribute value.
            Assert.assertTrue(errorMessage.getText().contains("locked out"));
            Assert.assertEquals(errorMessage.getAttribute("data-test"), "error");
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
