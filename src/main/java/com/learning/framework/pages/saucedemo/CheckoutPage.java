package com.learning.framework.pages.saucedemo;

import org.openqa.selenium.By;

import com.learning.framework.actions.ElementActions;
import com.learning.framework.waits.WaitUtils;

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

    private final ElementActions actions;
    private final WaitUtils waits;

    public CheckoutPage(ElementActions actions, WaitUtils waits) {
        this.actions = actions;
        this.waits = waits;
    }

    public CheckoutPage waitForInformationStep() {
        waits.waitForText(PAGE_TITLE, "Checkout: Your Information");
        waits.waitForClickable(FIRST_NAME_INPUT);
        return this;
    }

    public String getTitle() {
        return actions.getText(PAGE_TITLE);
    }

    public boolean isCustomerInformationFormDisplayed() {
        /*
         * Page Objects can expose page state without forcing the test to know the
         * locator. Module 10 will centralize this wait/find/display pattern.
         */
        return actions.isDisplayed(FIRST_NAME_INPUT);
    }
}
