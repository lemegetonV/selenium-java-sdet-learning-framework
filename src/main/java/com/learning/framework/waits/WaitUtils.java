package com.learning.framework.waits;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Central wait helper for framework code.
 *
 * Module 10 starts with a small wrapper around Selenium's ExpectedConditions.
 * The goal is not to hide waits completely; the goal is to give page objects a
 * single place for common wait decisions so every page does not repeat the same
 * wait.until(...) calls.
 */
public class WaitUtils {

    private final WebDriverWait wait;

    public WaitUtils(WebDriverWait wait) {
        this.wait = wait;
    }

    public WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public boolean waitForText(By locator, String expectedText) {
        return wait.until(ExpectedConditions.textToBe(locator, expectedText));
    }

    public boolean waitForUrlContains(String expectedUrlPart) {
        return wait.until(ExpectedConditions.urlContains(expectedUrlPart));
    }

    public List<WebElement> waitForMoreThan(By locator, int minimumCount) {
        return wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(locator, minimumCount));
    }
}
