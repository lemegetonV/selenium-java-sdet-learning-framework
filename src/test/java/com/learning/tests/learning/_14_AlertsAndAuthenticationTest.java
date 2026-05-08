package com.learning.tests.learning;

import java.time.Duration;

import org.openqa.selenium.Alert;
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
 * Introduces JavaScript alerts and a simple form authentication flow.
 */
public class _14_AlertsAndAuthenticationTest {

    @Test
    public void acceptsJavaScriptAlert() {
        WebDriver driver = createChromeDriver();

        try {
            driver.get("https://the-internet.herokuapp.com/javascript_alerts");

            driver.findElement(By.xpath("//button[normalize-space()='Click for JS Alert']")).click();

            /*
             * switchTo().alert() changes Selenium's focus from the page to the
             * browser alert dialog so the test can read and accept it.
             */
            Alert alert = driver.switchTo().alert();
            Assert.assertEquals(alert.getText(), "I am a JS Alert");
            alert.accept();

            Assert.assertEquals(driver.findElement(By.id("result")).getText(), "You successfully clicked an alert");
        } finally {
            driver.quit();
        }
    }

    @Test
    public void dismissesJavaScriptConfirm() {
        WebDriver driver = createChromeDriver();

        try {
            driver.get("https://the-internet.herokuapp.com/javascript_alerts");

            driver.findElement(By.xpath("//button[normalize-space()='Click for JS Confirm']")).click();

            Alert confirm = driver.switchTo().alert();
            Assert.assertEquals(confirm.getText(), "I am a JS Confirm");
            confirm.dismiss();

            Assert.assertEquals(driver.findElement(By.id("result")).getText(), "You clicked: Cancel");
        } finally {
            driver.quit();
        }
    }

    @Test
    public void typesIntoJavaScriptPrompt() {
        WebDriver driver = createChromeDriver();

        try {
            driver.get("https://the-internet.herokuapp.com/javascript_alerts");

            driver.findElement(By.xpath("//button[normalize-space()='Click for JS Prompt']")).click();

            Alert prompt = driver.switchTo().alert();
            /*
             * sendKeys on Alert types into the browser prompt dialog, not into a
             * DOM input. After accept(), Selenium returns to normal page context.
             */
            prompt.sendKeys("Module 06 prompt");
            prompt.accept();

            Assert.assertEquals(driver.findElement(By.id("result")).getText(), "You entered: Module 06 prompt");
        } finally {
            driver.quit();
        }
    }

    @Test
    public void logsIntoFormAuthenticationPage() {
        WebDriver driver = createChromeDriver();

        try {
            driver.get("https://the-internet.herokuapp.com/login");

            WebElement username = driver.findElement(By.id("username"));
            WebElement password = driver.findElement(By.id("password"));
            WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));

            /*
             * Credentials are visible in beginner code for learning only. Later
             * data-driven and logging modules must avoid printing or exposing
             * secrets in reports.
             */
            username.sendKeys("tomsmith");
            password.sendKeys("SuperSecretPassword!");
            loginButton.click();

            /*
             * Form submission can navigate asynchronously. Module 06 reuses the
             * explicit wait concept from Module 05 instead of racing the page.
             */
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.urlContains("/secure"));
            WebElement flashMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("flash")));

            Assert.assertTrue(driver.getCurrentUrl().contains("/secure"));
            Assert.assertTrue(flashMessage.getText().contains("You logged into a secure area!"));
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
