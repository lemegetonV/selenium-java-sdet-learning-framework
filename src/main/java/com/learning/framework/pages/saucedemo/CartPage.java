package com.learning.framework.pages.saucedemo;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.learning.framework.actions.ElementActions;
import com.learning.framework.waits.WaitUtils;

/**
 * Page Object for the SauceDemo cart page.
 *
 * The cart page exposes cart-level questions and actions, such as whether a
 * product is present and whether the user can move into checkout.
 */
public class CartPage {

    private static final By PAGE_TITLE = By.cssSelector("[data-test='title']");
    private static final By CART_ITEMS = By.cssSelector("[data-test='inventory-item']");
    private static final By CHECKOUT_BUTTON = By.id("checkout");

    private final ElementActions actions;
    private final WaitUtils waits;

    public CartPage(ElementActions actions, WaitUtils waits) {
        this.actions = actions;
        this.waits = waits;
    }

    public CartPage waitUntilLoaded() {
        waits.waitForText(PAGE_TITLE, "Your Cart");
        return this;
    }

    public boolean containsProduct(String productName) {
        return findCartItems().stream()
                .anyMatch(item -> item.getText().contains(productName));
    }

    public int getCartItemCount() {
        return findCartItems().size();
    }

    public CheckoutPage checkout() {
        actions.click(CHECKOUT_BUTTON);
        return new CheckoutPage(actions, waits).waitForInformationStep();
    }

    private List<WebElement> findCartItems() {
        return actions.findAll(CART_ITEMS);
    }
}
