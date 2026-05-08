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
            Assert.assertEquals(
                    driver.findElement(By.id("contact-result")).getText(),
                    "Preferred contact: Email"
            );

            phoneRadio.click();
            Assert.assertTrue(phoneRadio.isSelected());
            Assert.assertFalse(emailRadio.isSelected());
            Assert.assertEquals(
                    driver.findElement(By.id("contact-result")).getText(),
                    "Preferred contact: Phone"
            );
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
            /*
             * The image is validated through attributes in this module. The alt
             * text describes meaning/accessibility; src confirms which image the
             * browser was asked to load. Broken-image checks are saved for Module 07.
             */
            Assert.assertEquals(logo.getAttribute("alt"), "Sample inline logo");
            Assert.assertTrue(logo.getAttribute("src").startsWith("data:image/svg+xml"));

            WebElement detailsLink = driver.findElement(By.id("details-link"));
            Assert.assertEquals(detailsLink.getText(), "View details");
            Assert.assertTrue(detailsLink.getAttribute("href").endsWith("#details"));

            /*
             * A normal hyperlink follows its href. Here the href is a fragment,
             * so the URL changes to #details instead of loading a new page.
             */
            detailsLink.click();
            Assert.assertTrue(driver.getCurrentUrl().endsWith("#details"));
            Assert.assertEquals(
                    driver.findElement(By.id("details-result")).getText(),
                    "Details section opened"
            );
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
