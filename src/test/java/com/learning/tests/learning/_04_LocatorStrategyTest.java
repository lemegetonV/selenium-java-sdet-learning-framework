package com.learning.tests.learning;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Demonstrates Selenium locator strategies on stable SauceDemo markup.
 *
 * Module 04 keeps locator usage raw so the learner can see exactly how
 * WebDriver finds elements before page objects and wrapper methods exist.
 */
public class _04_LocatorStrategyTest {

    @Test
    public void findsSauceDemoElementsWithDifferentLocatorStrategies() {
        WebDriver driver = createChromeDriver();

        try {
            driver.get("https://www.saucedemo.com/");

            // By.id is usually the strongest beginner locator when the id is stable and unique.
            WebElement usernameInput = driver.findElement(By.id("user-name"));
            Assert.assertEquals(usernameInput.getAttribute("placeholder"), "Username");

            // By.name is useful for form fields when the name attribute is stable.
            WebElement passwordInput = driver.findElement(By.name("password"));
            Assert.assertEquals(passwordInput.getAttribute("placeholder"), "Password");

            // By.className accepts one class token, not a space-separated compound class value.
            WebElement loginLogo = driver.findElement(By.className("login_logo"));
            Assert.assertEquals(loginLogo.getText(), "Swag Labs");

            // findElements returns a list and does not fail when no elements match.
            List<WebElement> inputElements = driver.findElements(By.tagName("input"));
            Assert.assertTrue(inputElements.size() >= 3, "SauceDemo login page should have at least three inputs");

            // CSS selectors are flexible and are often preferred for stable data-test attributes.
            WebElement loginButtonByCss = driver.findElement(By.cssSelector("input[data-test='login-button']"));
            Assert.assertEquals(loginButtonByCss.getAttribute("value"), "Login");

            // XPath is powerful, but this project uses it selectively when simpler locators are weaker.
            WebElement loginButtonByXpath = driver.findElement(By.xpath("//input[@id='login-button']"));
            Assert.assertTrue(loginButtonByXpath.isDisplayed(), "Login button should be visible");
        } finally {
            driver.quit();
        }
    }

    @Test
    public void scopesLocatorSearchInsideAParentElement() {
        WebDriver driver = createChromeDriver();

        try {
            driver.get("https://www.saucedemo.com/");

            WebElement loginContainer = driver.findElement(By.id("login_button_container"));

            /*
             * A chained locator searches from a parent WebElement instead of the
             * whole page. This is useful when repeated cards, rows, or forms have
             * similar child elements.
             */
            WebElement usernameInput = loginContainer.findElement(By.cssSelector("input[data-test='username']"));
            WebElement passwordInput = loginContainer.findElement(By.cssSelector("input[data-test='password']"));

            usernameInput.sendKeys("standard_user");
            passwordInput.sendKeys("secret_sauce");

            Assert.assertEquals(usernameInput.getAttribute("value"), "standard_user");
            Assert.assertEquals(passwordInput.getAttribute("value"), "secret_sauce");
        } finally {
            driver.quit();
        }
    }

    @Test
    public void locatesRelatedCellsWithXpathAxes() {
        WebDriver driver = createChromeDriver();

        try {
            driver.get("https://the-internet.herokuapp.com/tables");

            /*
             * XPath axes describe relationships in the DOM. This locator finds the
             * row containing "Smith" and then reads cells related to that row.
             */
            WebElement smithFirstName = driver.findElement(
                    By.xpath("//table[@id='table1']//td[normalize-space()='Smith']/following-sibling::td[1]")
            );
            WebElement smithRow = driver.findElement(
                    By.xpath("//table[@id='table1']//td[normalize-space()='Smith']/ancestor::tr")
            );

            Assert.assertEquals(smithFirstName.getText(), "John");
            Assert.assertTrue(smithRow.getText().contains("jsmith@gmail.com"));
        } finally {
            driver.quit();
        }
    }

    @Test
    public void observesCommonLocatorExceptions() {
        WebDriver driver = createChromeDriver();

        try {
            driver.get("https://www.saucedemo.com/");

            // NoSuchElementException means the locator was valid, but matched no element.
            NoSuchElementException missingElement = Assert.expectThrows(
                    NoSuchElementException.class,
                    () -> driver.findElement(By.id("missing-login-field"))
            );
            Assert.assertNotNull(missingElement.getMessage());

            // InvalidSelectorException means the selector syntax itself is not valid.
            InvalidSelectorException invalidSelector = Assert.expectThrows(
                    InvalidSelectorException.class,
                    () -> driver.findElement(By.xpath("//*["))
            );
            Assert.assertNotNull(invalidSelector.getMessage());
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
