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
 * Introduces textboxes, textareas, and simple button clicks.
 *
 * The fixture is local so the learner can connect Selenium actions to the
 * exact HTML controls behind them without network variability.
 */
public class _11_TextboxTextareaButtonTest {

    @Test
    public void typesIntoTextboxAndTextarea() {
        WebDriver driver = createChromeDriver();

        try {
            driver.get(module06FixtureUrl());

            WebElement displayName = driver.findElement(By.id("display-name"));
            WebElement notes = driver.findElement(By.id("notes"));

            /*
             * clear() prevents sendKeys from appending to existing text. This is
             * the raw version of what a future typeInto wrapper should do when a
             * field must contain an exact value.
             */
            displayName.clear();
            displayName.sendKeys("Selenium learner");

            /*
             * A textarea is still a WebElement. Selenium types into it with
             * sendKeys just like a regular text input.
             */
            notes.sendKeys("Practicing text input controls.");

            Assert.assertEquals(displayName.getAttribute("value"), "Selenium learner");
            Assert.assertEquals(notes.getAttribute("value"), "Practicing text input controls.");
        } finally {
            driver.quit();
        }
    }

    @Test
    public void clicksButtonAndReadsUpdatedPageText() {
        WebDriver driver = createChromeDriver();

        try {
            driver.get(module06FixtureUrl());

            WebElement displayName = driver.findElement(By.id("display-name"));
            WebElement notes = driver.findElement(By.id("notes"));
            WebElement emailRadio = driver.findElement(By.id("contact-email"));
            WebElement saveButton = driver.findElement(By.id("save-profile"));

            displayName.sendKeys("Module 06");
            notes.sendKeys("Saved from a Selenium button test.");
            emailRadio.click();

            /*
             * click() only performs the button action. The important assertion is
             * the application state after the click: the saved summary should
             * reflect the values the test entered.
             */
            saveButton.click();

            Assert.assertEquals(driver.findElement(By.id("save-status")).getText(), "Saved profile");
            Assert.assertEquals(driver.findElement(By.id("saved-name")).getText(), "Module 06");
            Assert.assertEquals(
                    driver.findElement(By.id("saved-notes")).getText(),
                    "Saved from a Selenium button test."
            );
            Assert.assertEquals(driver.findElement(By.id("saved-contact")).getText(), "Email");
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
