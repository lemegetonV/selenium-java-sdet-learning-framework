package com.learning.tests.saucedemo;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.learning.framework.pages.saucedemo.CartPage;
import com.learning.framework.pages.saucedemo.CheckoutPage;
import com.learning.framework.pages.saucedemo.LoginPage;
import com.learning.framework.pages.saucedemo.ProductsPage;
import com.learning.tests.base.BaseTest;

import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;

/**
 * First SauceDemo tests that read through Page Objects.
 *
 * Tests now describe user-level workflow and assertions. Page-specific Selenium
 * locators live in page classes under com.learning.framework.pages.saucedemo.
 */
@Epic("SauceDemo UI")
@Feature("Page object workflows")
public class SauceDemoPageObjectTest extends BaseTest {

    private String standardUser;
    private String lockedOutUser;
    private String password;

    @BeforeClass(alwaysRun = true)
    public void setUpClassData() {
        standardUser = "standard_user";
        lockedOutUser = "locked_out_user";
        password = "secret_sauce";
    }

    @Test(groups = {"smoke", "regression"})
    @Story("Standard user login")
    @Severity(SeverityLevel.CRITICAL)
    public void standardUserCanReachProductsPage() {
        Allure.step("Open login page and login as standard user");
        ProductsPage productsPage = new LoginPage(driver(), elementActions(), waits())
                .open()
                .loginAs(standardUser, password);

        Allure.step("Verify product page title and inventory count");
        Assert.assertEquals(productsPage.getTitle(), "Products");
        Assert.assertEquals(productsPage.getInventoryItemCount(), 6);
    }

    @Test(groups = "regression")
    @Story("Locked-out user login")
    @Severity(SeverityLevel.NORMAL)
    public void lockedOutUserSeesErrorMessage() {
        Allure.step("Open login page and login as locked-out user");
        LoginPage loginPage = new LoginPage(driver(), elementActions(), waits())
                .open()
                .loginExpectingError(lockedOutUser, password);

        Allure.step("Verify locked-out error message");
        Assert.assertTrue(
                loginPage.getErrorMessage().contains("Sorry, this user has been locked out."),
                "Locked-out login should explain why access was denied."
        );
    }

    @Test(groups = "regression")
    @Story("Checkout start")
    @Severity(SeverityLevel.CRITICAL)
    public void standardUserCanStartCheckoutForSingleProduct() {
        Allure.step("Login and add one product to cart");
        ProductsPage productsPage = new LoginPage(driver(), elementActions(), waits())
                .open()
                .loginAs(standardUser, password)
                .addProductToCart("Sauce Labs Backpack");

        Allure.step("Verify cart badge and cart contents");
        Assert.assertEquals(productsPage.getCartBadgeCount(), "1");

        CartPage cartPage = productsPage.openCart();
        Assert.assertEquals(cartPage.getCartItemCount(), 1);
        Assert.assertTrue(cartPage.containsProduct("Sauce Labs Backpack"));

        Allure.step("Open checkout and verify customer information form");
        CheckoutPage checkoutPage = cartPage.checkout();

        Assert.assertEquals(checkoutPage.getTitle(), "Checkout: Your Information");
        Assert.assertTrue(checkoutPage.isCustomerInformationFormDisplayed());
    }

    @AfterClass(alwaysRun = true)
    public void clearClassData() {
        standardUser = null;
        lockedOutUser = null;
        password = null;
    }
}
