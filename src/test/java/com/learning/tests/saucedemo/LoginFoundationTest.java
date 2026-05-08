package com.learning.tests.saucedemo;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.learning.tests.base.BaseTest;

/**
 * First framework-style SauceDemo tests built on BaseTest.
 *
 * The locators still live in the test class on purpose. Module 09 will move
 * them into Page Objects after this module teaches why shared browser setup is
 * useful on its own.
 */
public class LoginFoundationTest extends BaseTest {

    private static final By USERNAME_INPUT = By.id("user-name");
    private static final By PASSWORD_INPUT = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login-button");
    private static final By PRODUCTS_TITLE = By.cssSelector("[data-test='title']");
    private static final By INVENTORY_ITEMS = By.cssSelector("[data-test='inventory-item']");
    private static final By LOGIN_ERROR = By.cssSelector("[data-test='error']");

    private String loginUrl;

    /**
     * @BeforeClass runs once before this class's test methods.
     *
     * It is useful for class-level data that does not need a browser. Browser
     * setup stays in BaseTest's @BeforeMethod so each test gets isolation.
     */
    @BeforeClass(alwaysRun = true)
    public void setUpClassData() {
        loginUrl = "https://www.saucedemo.com/";
    }

    @Test(groups = {"smoke", "regression"})
    public void standardUserCanReachProductsPage() {
        driver.get(loginUrl);

        loginAs("standard_user", "secret_sauce");

        wait.until(ExpectedConditions.urlContains("/inventory.html"));
        Assert.assertEquals(driver.findElement(PRODUCTS_TITLE).getText(), "Products");
        Assert.assertEquals(driver.findElements(INVENTORY_ITEMS).size(), 6);
    }

    @Test(groups = "regression")
    public void lockedOutUserSeesErrorMessage() {
        driver.get(loginUrl);

        loginAs("locked_out_user", "secret_sauce");

        String errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(LOGIN_ERROR)).getText();
        Assert.assertTrue(
                errorMessage.contains("Sorry, this user has been locked out."),
                "Locked-out login should explain why access was denied."
        );
    }

    /**
     * This local helper removes duplication inside this class only.
     *
     * It is not a Page Object yet because it still knows raw locators and uses
     * WebDriver directly. Module 09 will move this behavior into LoginPage.
     */
    private void loginAs(String username, String password) {
        driver.findElement(USERNAME_INPUT).sendKeys(username);
        driver.findElement(PASSWORD_INPUT).sendKeys(password);
        driver.findElement(LOGIN_BUTTON).click();
    }

    /**
     * @AfterClass runs once after all tests in this class.
     *
     * There is no browser cleanup here because BaseTest already owns per-test
     * browser lifecycle through @AfterMethod.
     */
    @AfterClass(alwaysRun = true)
    public void clearClassData() {
        loginUrl = null;
    }
}
