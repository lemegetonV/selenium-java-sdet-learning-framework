package com.learning.tests.learning;

import java.nio.file.Path;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Introduces date picker strategies and table row extraction/action patterns.
 */
public class _18_CalendarAndWebTableTest {

    @Test
    public void selectsDateUsingInputAndCalendarButton() {
        WebDriver driver = createChromeDriver();

        try {
            driver.get(module07FixtureUrl("advanced-interactions.html"));

            WebElement dateInput = driver.findElement(By.id("appointment-date"));

            /*
             * Native date inputs store ISO dates, but typing can be interpreted
             * through the browser locale. JavaScriptExecutor makes the stored ISO
             * value explicit for this learning fixture.
             */
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].value = '2026-05-08'; arguments[0].dispatchEvent(new Event('change'));",
                    dateInput
            );
            Assert.assertEquals(dateInput.getAttribute("value"), "2026-05-08");
            Assert.assertEquals(driver.findElement(By.id("selected-date")).getText(), "Selected date: 2026-05-08");

            /*
             * Many real applications use custom calendar widgets, so this test also
             * clicks a visible date button that updates the same input.
             */
            dateInput.clear();
            driver.findElement(By.id("date-2026-05-08")).click();

            Assert.assertEquals(dateInput.getAttribute("value"), "2026-05-08");
            Assert.assertEquals(driver.findElement(By.id("selected-date")).getText(), "Selected date: 2026-05-08");
        } finally {
            driver.quit();
        }
    }

    @Test
    public void readsRowsFromTheInternetTable() {
        WebDriver driver = createChromeDriver();

        try {
            driver.get("https://the-internet.herokuapp.com/tables");

            List<WebElement> rows = driver.findElements(By.cssSelector("#table1 tbody tr"));
            Assert.assertEquals(rows.size(), 4);

            WebElement doeRow = rows.stream()
                    .filter(row -> row.getText().contains("Doe"))
                    .findFirst()
                    .orElseThrow();

            Assert.assertTrue(doeRow.getText().contains("jdoe@hotmail.com"));
        } finally {
            driver.quit();
        }
    }

    @Test
    public void clicksActionInMatchingFixtureRow() {
        WebDriver driver = createChromeDriver();

        try {
            driver.get(module07FixtureUrl("advanced-interactions.html"));

            List<WebElement> rows = driver.findElements(By.cssSelector("#people-table tbody tr"));
            WebElement doeRow = rows.stream()
                    .filter(row -> row.getText().contains("Doe"))
                    .findFirst()
                    .orElseThrow();

            /*
             * The Internet table has edit/delete links, but this local fixture makes
             * the row-action result visible so the beginner assertion is explicit.
             */
            doeRow.findElement(By.cssSelector("button.select-person")).click();
            Assert.assertTrue(doeRow.getAttribute("class").contains("selected-row"));
            Assert.assertEquals(driver.findElement(By.id("selected-person")).getText(), "Selected person: Jane Doe");
        } finally {
            driver.quit();
        }
    }

    @Test
    public void sortsTableAndReadsColumnOrder() {
        WebDriver driver = createChromeDriver();

        try {
            driver.get(module07FixtureUrl("advanced-interactions.html"));

            driver.findElement(By.id("sort-by-name")).click();

            List<String> names = driver.findElements(By.cssSelector("#sortable-table tbody tr td:first-child"))
                    .stream()
                    .map(WebElement::getText)
                    .toList();

            Assert.assertEquals(names, List.of("Alice", "Bob", "Charlie"));
            Assert.assertEquals(driver.findElement(By.id("sort-result")).getText(), "Sorted by name: ascending");
            Assert.assertEquals(driver.findElement(By.id("sort-by-name")).getAttribute("aria-sort"), "ascending");
        } finally {
            driver.quit();
        }
    }

    private String module07FixtureUrl(String fileName) {
        return Path.of("src/test/resources/module07", fileName).toUri().toString();
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
