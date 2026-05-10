package com.learning.framework.pages.saucedemo;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page Object for the SauceDemo products page.
 *
 * Module 09 keeps raw Selenium calls visible inside page classes. Module 10
 * will introduce wrapper methods so page objects no longer call
 * driver.findElement directly for every action.
 */
public class ProductsPage {

    private static final By PAGE_TITLE = By.cssSelector("[data-test='title']");
    private static final By INVENTORY_ITEMS = By.cssSelector("[data-test='inventory-item']");
    private static final By CART_LINK = By.cssSelector("[data-test='shopping-cart-link']");
    private static final By CART_BADGE = By.cssSelector("[data-test='shopping-cart-badge']");

    private final WebDriver driver;
    private final WebDriverWait wait;

    public ProductsPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public ProductsPage waitUntilLoaded() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(PAGE_TITLE));
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(INVENTORY_ITEMS, 0));
        return this;
    }

    public String getTitle() {
        return driver.findElement(PAGE_TITLE).getText();
    }

    public int getInventoryItemCount() {
        return driver.findElements(INVENTORY_ITEMS).size();
    }

    public ProductsPage addProductToCart(String productName) {
        WebElement productCard = findProductCard(productName);

        /*
         * This is row/card-scoped lookup. Selenium searches inside the matching
         * product card, so the test clicks the button for the intended product
         * rather than the first matching button on the page.
         */
        productCard.findElement(By.cssSelector("button.btn_inventory")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(CART_BADGE));
        return this;
    }

    public String getCartBadgeCount() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(CART_BADGE)).getText();
    }

    public CartPage openCart() {
        driver.findElement(CART_LINK).click();
        return new CartPage(driver, wait).waitUntilLoaded();
    }

    private WebElement findProductCard(String productName) {
        List<WebElement> products = driver.findElements(INVENTORY_ITEMS);
        return products.stream()
                .filter(product -> product.getText().contains(productName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productName));
    }
}
