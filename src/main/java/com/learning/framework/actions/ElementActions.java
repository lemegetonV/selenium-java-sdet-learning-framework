package com.learning.framework.actions;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.learning.framework.waits.WaitUtils;

/**
 * First wrapper layer around common Selenium element commands.
 *
 * Page objects should describe page behavior, not repeat the same find, wait,
 * click, type, and text-reading mechanics. This class keeps the raw Selenium
 * command visible while centralizing the repeated pattern.
 */
public class ElementActions {

    private final WebDriver driver;
    private final WaitUtils waits;

    public ElementActions(WebDriver driver, WaitUtils waits) {
        this.driver = driver;
        this.waits = waits;
    }

    public void click(By locator) {
        waits.waitForClickable(locator).click();
    }

    public void clickInside(WebElement parent, By childLocator) {
        /*
         * This scoped action keeps the Module 09 row/card lookup lesson intact:
         * first find the correct container, then click inside that container.
         */
        parent.findElement(childLocator).click();
    }

    public void type(By locator, String value) {
        WebElement element = waits.waitForVisible(locator);
        element.clear();
        element.sendKeys(value);
    }

    public String getText(By locator) {
        return waits.waitForVisible(locator).getText();
    }

    public boolean isDisplayed(By locator) {
        return waits.waitForVisible(locator).isDisplayed();
    }

    public int getElementCount(By locator) {
        return driver.findElements(locator).size();
    }

    public List<WebElement> findAll(By locator) {
        return driver.findElements(locator);
    }

    public void selectByVisibleText(By locator, String visibleText) {
        Select select = new Select(waits.waitForVisible(locator));
        select.selectByVisibleText(visibleText);
    }
}
