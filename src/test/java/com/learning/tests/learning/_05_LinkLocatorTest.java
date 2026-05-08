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
public class _05_LinkLocatorTest {

    @Test
    public void findsLinksByExactAndPartialText() {
        WebDriver driver = createChromeDriver();

        try {
            driver.get("https://the-internet.herokuapp.com/");

            /*
             * linkText matches the complete visible text of an anchor element.
             * It is readable, but it couples the test to user-facing copy.
             */
            WebElement checkboxesLink = driver.findElement(By.linkText("Checkboxes"));
            Assert.assertEquals(checkboxesLink.getText(), "Checkboxes");
            checkboxesLink.click();
            Assert.assertTrue(driver.getCurrentUrl().contains("/checkboxes"));

            driver.navigate().back();

            /*
             * partialLinkText is convenient for demos, but it can become
             * ambiguous if multiple links contain the same word. Mature tests
             * should prefer more stable locators when ambiguity is possible.
             */
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
