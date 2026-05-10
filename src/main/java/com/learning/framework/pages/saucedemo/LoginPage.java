package com.learning.framework.pages.saucedemo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page Object for the SauceDemo login page.
 *
 * A Page Object hides page-specific locators behind public actions. Tests
 * should describe user intent, such as logging in, instead of repeating
 * username/password/button locators in every test method.
 */
public class LoginPage {

    private static final String LOGIN_URL = "https://www.saucedemo.com/";

    /*
     * Locators are private because outside classes should not depend on this
     * page's HTML details. If the AUT changes an id, this class should absorb
     * that change without forcing every test to change.
     */
    private static final By USERNAME_INPUT = By.id("user-name");
    private static final By PASSWORD_INPUT = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login-button");
    private static final By LOGIN_ERROR = By.cssSelector("[data-test='error']");

    private final WebDriver driver;
    private final WebDriverWait wait;

    /**
     * The test owns the browser lifecycle through BaseTest. The Page Object only
     * receives the active driver and wait so it can interact with its page.
     */
    public LoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public LoginPage open() {
        driver.get(LOGIN_URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(LOGIN_BUTTON));
        return this;
    }

    /**
     * Successful login navigates from the login page to the products page, so
     * this method returns ProductsPage. That return type documents the expected
     * page transition in Java code.
     */
    public ProductsPage loginAs(String username, String password) {
        enterCredentials(username, password);
        driver.findElement(LOGIN_BUTTON).click();
        return new ProductsPage(driver, wait).waitUntilLoaded();
    }

    /**
     * Negative login stays on the same page. Returning this page object keeps
     * the test flow honest: the user is still looking at the login page.
     */
    public LoginPage loginExpectingError(String username, String password) {
        enterCredentials(username, password);
        driver.findElement(LOGIN_BUTTON).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(LOGIN_ERROR));
        return this;
    }

    public String getErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(LOGIN_ERROR)).getText();
    }

    private void enterCredentials(String username, String password) {
        driver.findElement(USERNAME_INPUT).clear();
        driver.findElement(USERNAME_INPUT).sendKeys(username);
        driver.findElement(PASSWORD_INPUT).clear();
        driver.findElement(PASSWORD_INPUT).sendKeys(password);
    }
}
