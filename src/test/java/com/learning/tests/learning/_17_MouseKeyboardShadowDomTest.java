package com.learning.tests.learning;

import java.nio.file.Path;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Introduces Selenium Actions, keyboard input, drag/drop, and Shadow DOM.
 */
public class _17_MouseKeyboardShadowDomTest {

    @Test
    public void performsMouseHover() {
        WebDriver driver = createChromeDriver();

        try {
            driver.get("https://the-internet.herokuapp.com/hovers");

            WebElement hoverCard = driver.findElement(By.cssSelector(".figure"));

            // Actions builds user-like mouse and keyboard gestures.
            new Actions(driver).moveToElement(hoverCard).perform();

            Assert.assertTrue(driver.findElement(By.cssSelector(".figcaption h5")).isDisplayed());
        } finally {
            driver.quit();
        }
    }

    @Test
    public void sendsKeyboardInputAndSpecialKeys() {
        WebDriver driver = createChromeDriver();

        try {
            driver.get("https://the-internet.herokuapp.com/key_presses");

            WebElement keyboardInput = driver.findElement(By.id("target"));
            keyboardInput.click();

            /*
             * sendKeys can type normal text and special keyboard constants. Later
             * framework code may wrap this, but the raw API is visible here.
             */
            keyboardInput.sendKeys("abc");
            keyboardInput.sendKeys(Keys.ESCAPE);

            Assert.assertEquals(keyboardInput.getAttribute("value"), "abc");
            Assert.assertEquals(driver.findElement(By.id("result")).getText(), "You entered: ESCAPE");
        } finally {
            driver.quit();
        }
    }

    @Test
    public void dragsElementToDropTarget() {
        WebDriver driver = createChromeDriver();

        try {
            driver.get(module07FixtureUrl("advanced-interactions.html"));

            WebElement source = driver.findElement(By.id("drag-source"));
            WebElement target = driver.findElement(By.id("drop-target"));

            new Actions(driver)
                    .clickAndHold(source)
                    .moveToElement(target)
                    .release()
                    .perform();

            Assert.assertEquals(driver.findElement(By.id("drag-result")).getText(), "Dropped: Drag source");
            Assert.assertTrue(driver.findElement(By.id("drop-target")).getAttribute("class").contains("drop-ready"));
        } finally {
            driver.quit();
        }
    }

    @Test
    public void findsElementInsideOpenShadowRoot() {
        WebDriver driver = createChromeDriver();

        try {
            driver.get(module07FixtureUrl("advanced-interactions.html"));

            WebElement shadowHost = driver.findElement(By.id("shadow-host"));

            /*
             * Elements inside Shadow DOM are not found by normal page-level
             * findElement calls. For an open shadow root, Selenium can enter the
             * shadow root and search from there.
             */
            SearchContext shadowRoot = shadowHost.getShadowRoot();
            shadowRoot.findElement(By.id("shadow-button")).click();

            Assert.assertEquals(shadowRoot.findElement(By.id("shadow-result")).getText(), "Shadow clicked");
        } finally {
            driver.quit();
        }
    }

    private String module07FixtureUrl(String fileName) {
        return Path.of("src/test/resources/module07", fileName).toUri().toString();
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
