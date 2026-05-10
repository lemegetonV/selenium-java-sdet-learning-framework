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

/**
 * First SauceDemo tests that read through Page Objects.
 *
 * Tests now describe user-level workflow and assertions. Page-specific Selenium
 * locators live in page classes under com.learning.framework.pages.saucedemo.
 */
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
    public void standardUserCanReachProductsPage() {
        ProductsPage productsPage = new LoginPage(driver, wait)
                .open()
                .loginAs(standardUser, password);

        Assert.assertEquals(productsPage.getTitle(), "Products");
        Assert.assertEquals(productsPage.getInventoryItemCount(), 6);
    }

    @Test(groups = "regression")
    public void lockedOutUserSeesErrorMessage() {
        LoginPage loginPage = new LoginPage(driver, wait)
                .open()
                .loginExpectingError(lockedOutUser, password);

        Assert.assertTrue(
                loginPage.getErrorMessage().contains("Sorry, this user has been locked out."),
                "Locked-out login should explain why access was denied."
        );
    }

    @Test(groups = "regression")
    public void standardUserCanStartCheckoutForSingleProduct() {
        ProductsPage productsPage = new LoginPage(driver, wait)
                .open()
                .loginAs(standardUser, password)
                .addProductToCart("Sauce Labs Backpack");

        Assert.assertEquals(productsPage.getCartBadgeCount(), "1");

        CartPage cartPage = productsPage.openCart();
        Assert.assertEquals(cartPage.getCartItemCount(), 1);
        Assert.assertTrue(cartPage.containsProduct("Sauce Labs Backpack"));

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
