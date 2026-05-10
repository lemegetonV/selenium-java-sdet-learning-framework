package com.learning.framework.pages.saucedemo;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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

    private final WebDriver driver;
    private final WebDriverWait wait;

    public CartPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public CartPage waitUntilLoaded() {
        wait.until(ExpectedConditions.textToBe(PAGE_TITLE, "Your Cart"));
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
        driver.findElement(CHECKOUT_BUTTON).click();
        return new CheckoutPage(driver, wait).waitForInformationStep();
    }

    private List<WebElement> findCartItems() {
        return driver.findElements(CART_ITEMS);
    }
}
