package com.learning.tests.learning;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Demonstrates link text locators on The Internet home page.
 */
public class _02_LinkLocatorTest {

    @Test
    public void findsLinksByExactAndPartialText() {
        WebDriver driver = createChromeDriver();

        try {
            driver.get("https://the-internet.herokuapp.com/");

            // linkText must match the full visible link text.
            WebElement checkboxesLink = driver.findElement(By.linkText("Checkboxes"));
            Assert.assertEquals(checkboxesLink.getText(), "Checkboxes");
            checkboxesLink.click();
            Assert.assertTrue(driver.getCurrentUrl().contains("/checkboxes"));

            driver.navigate().back();

            // partialLinkText can match part of the visible link text, but it can become ambiguous.
            WebElement dropdownLink = driver.findElement(By.partialLinkText("Dropdown"));
            dropdownLink.click();
            Assert.assertTrue(driver.getCurrentUrl().contains("/dropdown"));
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
