package com.learning.framework.pages.saucedemo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.learning.framework.actions.ElementActions;
import com.learning.framework.config.ConfigReader;
import com.learning.framework.waits.WaitUtils;

/**
 * Page Object for the SauceDemo login page.
 *
 * A Page Object hides page-specific locators behind public actions. Tests
 * should describe user intent, such as logging in, instead of repeating
 * username/password/button locators in every test method.
 */
public class LoginPage {

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
    private final ElementActions actions;
    private final WaitUtils waits;

    /**
     * The test owns the browser lifecycle through BaseTest. The Page Object only
     * receives framework services so it can interact with its page without
     * owning browser lifecycle.
     */
    public LoginPage(WebDriver driver, ElementActions actions, WaitUtils waits) {
        this.driver = driver;
        this.actions = actions;
        this.waits = waits;
    }

    public LoginPage open() {
        driver.get(ConfigReader.getBaseUrl());
        waits.waitForVisible(LOGIN_BUTTON);
        return this;
    }

    /**
     * Successful login navigates from the login page to the products page, so
     * this method returns ProductsPage. That return type documents the expected
     * page transition in Java code.
     */
    public ProductsPage loginAs(String username, String password) {
        enterCredentials(username, password);
        actions.click(LOGIN_BUTTON);
        return new ProductsPage(actions, waits).waitUntilLoaded();
    }

    /**
     * Negative login stays on the same page. Returning this page object keeps
     * the test flow honest: the user is still looking at the login page.
     */
    public LoginPage loginExpectingError(String username, String password) {
        enterCredentials(username, password);
        actions.click(LOGIN_BUTTON);
        waits.waitForVisible(LOGIN_ERROR);
        return this;
    }

    public String getErrorMessage() {
        return actions.getText(LOGIN_ERROR);
    }

    private void enterCredentials(String username, String password) {
        actions.type(USERNAME_INPUT, username);
        actions.type(PASSWORD_INPUT, password);
    }
}
