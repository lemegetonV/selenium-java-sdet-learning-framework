package com.learning.tests.learning;

import java.nio.file.Path;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Introduces radio buttons, hyperlinks, and image element checks.
 */
public class _12_RadioImageHyperlinkTest {

    @Test
    public void selectsOneRadioButtonFromAGroup() {
        WebDriver driver = createChromeDriver();

        try {
            driver.get(module06FixtureUrl());

            WebElement emailRadio = driver.findElement(By.id("contact-email"));
            WebElement phoneRadio = driver.findElement(By.id("contact-phone"));

            /*
             * Radio buttons with the same name behave as a group: selecting one
             * option clears the other option in that group.
             */
            emailRadio.click();
            Assert.assertTrue(emailRadio.isSelected());
            Assert.assertFalse(phoneRadio.isSelected());

            phoneRadio.click();
            Assert.assertTrue(phoneRadio.isSelected());
            Assert.assertFalse(emailRadio.isSelected());
        } finally {
            driver.quit();
        }
    }

    @Test
    public void readsImageAndHyperlinkAttributes() {
        WebDriver driver = createChromeDriver();

        try {
            driver.get(module06FixtureUrl());

            WebElement logo = driver.findElement(By.id("sample-logo"));
            Assert.assertTrue(logo.isDisplayed());
            Assert.assertEquals(logo.getAttribute("alt"), "Sample inline logo");
            Assert.assertTrue(logo.getAttribute("src").startsWith("data:image/svg+xml"));

            WebElement detailsLink = driver.findElement(By.id("details-link"));
            Assert.assertEquals(detailsLink.getText(), "View details");
            Assert.assertTrue(detailsLink.getAttribute("href").endsWith("#details"));

            // Hyperlinks navigate through their href when clicked.
            detailsLink.click();
            Assert.assertTrue(driver.getCurrentUrl().endsWith("#details"));
        } finally {
            driver.quit();
        }
    }

    private String module06FixtureUrl() {
        return Path.of("src/test/resources/module06/form-controls.html").toUri().toString();
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
