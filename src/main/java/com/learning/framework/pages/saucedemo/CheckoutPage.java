package com.learning.framework.pages.saucedemo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page Object for the SauceDemo checkout flow.
 *
 * SauceDemo splits checkout across information, overview, and complete screens.
 * Module 09 models the first checkout screen only so the first POM lesson stays
 * focused on page boundaries. Later modules can expand this class or split the
 * checkout flow when action wrappers and richer diagnostics exist.
 */
public class CheckoutPage {

    private static final By PAGE_TITLE = By.cssSelector("[data-test='title']");
    private static final By FIRST_NAME_INPUT = By.cssSelector("[data-test='firstName']");

    private final WebDriver driver;
    private final WebDriverWait wait;

    public CheckoutPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public CheckoutPage waitForInformationStep() {
        wait.until(ExpectedConditions.textToBe(PAGE_TITLE, "Checkout: Your Information"));
        wait.until(ExpectedConditions.elementToBeClickable(FIRST_NAME_INPUT));
        return this;
    }

    public String getTitle() {
        return driver.findElement(PAGE_TITLE).getText();
    }

    public boolean isCustomerInformationFormDisplayed() {
        /*
         * Page Objects can expose page state without forcing the test to know the
         * locator. Module 10 will centralize this wait/find/display pattern.
         */
        return wait.until(ExpectedConditions.visibilityOfElementLocated(FIRST_NAME_INPUT)).isDisplayed();
    }
}
