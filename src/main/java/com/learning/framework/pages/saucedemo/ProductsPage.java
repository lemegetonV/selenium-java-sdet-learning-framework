package com.learning.framework.pages.saucedemo;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.learning.framework.actions.ElementActions;
import com.learning.framework.waits.WaitUtils;

/**
 * Page Object for the SauceDemo products page.
 *
 * Module 10 routes common find/wait/action mechanics through ElementActions
 * and WaitUtils so this page can focus on product-page behavior.
 */
public class ProductsPage {

    private static final By PAGE_TITLE = By.cssSelector("[data-test='title']");
    private static final By INVENTORY_ITEMS = By.cssSelector("[data-test='inventory-item']");
    private static final By CART_LINK = By.cssSelector("[data-test='shopping-cart-link']");
    private static final By CART_BADGE = By.cssSelector("[data-test='shopping-cart-badge']");
    private static final By PRODUCT_ADD_BUTTON = By.cssSelector("button.btn_inventory");

    private final ElementActions actions;
    private final WaitUtils waits;

    public ProductsPage(ElementActions actions, WaitUtils waits) {
        this.actions = actions;
        this.waits = waits;
    }

    public ProductsPage waitUntilLoaded() {
        waits.waitForVisible(PAGE_TITLE);
        waits.waitForMoreThan(INVENTORY_ITEMS, 0);
        return this;
    }

    public String getTitle() {
        return actions.getText(PAGE_TITLE);
    }

    public int getInventoryItemCount() {
        return actions.getElementCount(INVENTORY_ITEMS);
    }

    public ProductsPage addProductToCart(String productName) {
        WebElement productCard = findProductCard(productName);

        /*
         * This is row/card-scoped lookup. Selenium searches inside the matching
         * product card, so the test clicks the button for the intended product
         * rather than the first matching button on the page.
         */
        actions.clickInside(productCard, PRODUCT_ADD_BUTTON);
        waits.waitForVisible(CART_BADGE);
        return this;
    }

    public String getCartBadgeCount() {
        return actions.getText(CART_BADGE);
    }

    public CartPage openCart() {
        actions.click(CART_LINK);
        return new CartPage(actions, waits).waitUntilLoaded();
    }

    private WebElement findProductCard(String productName) {
        List<WebElement> products = actions.findAll(INVENTORY_ITEMS);
        return products.stream()
                .filter(product -> product.getText().contains(productName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productName));
    }
}
