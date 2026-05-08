package com.learning.tests.learning;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Introduces checkbox state checks and Selenium's Select helper for dropdowns.
 */
public class _13_CheckboxDropdownTest {

    @Test
    public void togglesCheckboxesAndReadsSelectedState() {
        WebDriver driver = createChromeDriver();

        try {
            driver.get("https://the-internet.herokuapp.com/checkboxes");

            WebElement firstCheckbox = driver.findElement(By.cssSelector("#checkboxes input:nth-of-type(1)"));
            WebElement secondCheckbox = driver.findElement(By.cssSelector("#checkboxes input:nth-of-type(2)"));

            /*
             * The test asserts starting state because checkbox clicks toggle.
             * Without knowing the initial state, a click could accidentally
             * create the opposite of the state the test intended.
             */
            Assert.assertFalse(firstCheckbox.isSelected(), "First checkbox starts unchecked");
            Assert.assertTrue(secondCheckbox.isSelected(), "Second checkbox starts checked");

            /*
             * Checkbox click toggles selected state. Tests should assert state
             * with isSelected instead of trusting that the click worked.
             */
            firstCheckbox.click();
            secondCheckbox.click();

            Assert.assertTrue(firstCheckbox.isSelected());
            Assert.assertFalse(secondCheckbox.isSelected());
        } finally {
            driver.quit();
        }
    }

    @Test
    public void selectsDropdownOptionsByVisibleTextAndValue() {
        WebDriver driver = createChromeDriver();

        try {
            driver.get("https://the-internet.herokuapp.com/dropdown");

            WebElement dropdownElement = driver.findElement(By.id("dropdown"));

            /*
             * Select is Selenium's helper for real HTML <select> elements. It is
             * not used for custom dropdowns built with div/li markup.
             */
            Select dropdown = new Select(dropdownElement);
            Assert.assertFalse(dropdown.isMultiple(), "The Internet dropdown is single-select");

            dropdown.selectByVisibleText("Option 1");
            Assert.assertEquals(dropdown.getFirstSelectedOption().getText(), "Option 1");

            /*
             * selectByValue targets the option value attribute, not the visible
             * label. Both strategies are useful when the HTML has stable values.
             */
            dropdown.selectByValue("2");
            Assert.assertEquals(dropdown.getFirstSelectedOption().getText(), "Option 2");
        } finally {
            driver.quit();
        }
    }

    private WebDriver createChromeDriver() {
        ChromeOptions options = new ChromeOptions();

        if (Boolean.parseBoolean(System.getProperty("headless", "true"))) {
            options.addArguments("--headless=new");
        }

        options.addArguments("--window-size=1440,900");
        return new ChromeDriver(options);
    }
}
