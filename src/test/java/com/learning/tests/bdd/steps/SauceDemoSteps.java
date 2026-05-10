package com.learning.tests.bdd.steps;

import java.util.List;
import java.util.Map;

import org.testng.Assert;

import com.learning.framework.pages.saucedemo.CartPage;
import com.learning.framework.pages.saucedemo.CheckoutPage;
import com.learning.framework.pages.saucedemo.LoginPage;
import com.learning.framework.pages.saucedemo.ProductsPage;
import com.learning.tests.bdd.context.CucumberScenarioContext;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * Step definitions translate Gherkin steps into framework calls.
 *
 * Keep step definitions thin: they coordinate pages and assertions, while
 * Selenium locators and browser commands remain inside Page Objects and wrapper
 * services. This is what lets Cucumber sit above the existing framework.
 */
public class SauceDemoSteps {

    private LoginPage loginPage;
    private ProductsPage productsPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;

    @Given("the SauceDemo login page is open")
    public void theSauceDemoLoginPageIsOpen() {
        loginPage = new LoginPage(
                CucumberScenarioContext.driver(),
                CucumberScenarioContext.elementActions(),
                CucumberScenarioContext.waits()
        ).open();
    }

    @When("I login as {string} with password {string}")
    public void iLoginAsWithPassword(String username, String password) {
        /*
         * This step represents a successful business action, so it stores the
         * ProductsPage returned by the Page Object transition.
         */
        productsPage = loginPage.loginAs(username, password);
    }

    @When("I submit login for {string} with password {string}")
    public void iSubmitLoginForWithPassword(String username, String password) {
        /*
         * Negative login does not navigate away from the login page. The Page
         * Object return type makes that browser behavior explicit.
         */
        loginPage = loginPage.loginExpectingError(username, password);
    }

    @When("I add the following products to the cart:")
    public void iAddTheFollowingProductsToTheCart(DataTable productTable) {
        List<Map<String, String>> rows = productTable.asMaps();
        for (Map<String, String> row : rows) {
            productsPage.addProductToCart(row.get("product"));
        }
    }

    @When("I open the cart")
    public void iOpenTheCart() {
        cartPage = productsPage.openCart();
    }

    @When("I start checkout")
    public void iStartCheckout() {
        checkoutPage = cartPage.checkout();
    }

    @Then("the products page should show title {string}")
    public void theProductsPageShouldShowTitle(String expectedTitle) {
        Assert.assertEquals(productsPage.getTitle(), expectedTitle);
    }

    @Then("the product catalog should contain {int} items")
    public void theProductCatalogShouldContainItems(int expectedCount) {
        Assert.assertEquals(productsPage.getInventoryItemCount(), expectedCount);
    }

    @Then("the login error should contain {string}")
    public void theLoginErrorShouldContain(String expectedMessage) {
        Assert.assertTrue(
                loginPage.getErrorMessage().contains(expectedMessage),
                "Login error should contain expected business message."
        );
    }

    @Then("the cart badge should show {string}")
    public void theCartBadgeShouldShow(String expectedBadgeCount) {
        Assert.assertEquals(productsPage.getCartBadgeCount(), expectedBadgeCount);
    }

    @Then("the cart should contain {string}")
    public void theCartShouldContain(String productName) {
        Assert.assertTrue(cartPage.containsProduct(productName), "Cart should contain selected product.");
    }

    @Then("the checkout title should be {string}")
    public void theCheckoutTitleShouldBe(String expectedTitle) {
        Assert.assertEquals(checkoutPage.getTitle(), expectedTitle);
    }

    @Then("the customer information form should be displayed")
    public void theCustomerInformationFormShouldBeDisplayed() {
        Assert.assertTrue(checkoutPage.isCustomerInformationFormDisplayed());
    }
}
