# Calendars, Tables, JavaScript, and Images

## Files In This Topic

```text
src/test/java/com/learning/tests/learning/_18_CalendarAndWebTableTest.java
src/test/java/com/learning/tests/learning/_19_JavaScriptAndExceptionsTest.java
src/test/resources/module07/advanced-interactions.html
```

This topic uses The Internet for web table reading, JavaScript page-state
reading, and broken images:

```text
https://the-internet.herokuapp.com/tables
https://the-internet.herokuapp.com/
https://the-internet.herokuapp.com/broken_images
```

The local fixture remains for calendar/date-picker behavior, visible row
actions, and deterministic sorting.

## Calendar And Date Picker Widgets

Calendars are not one Selenium API. They are usually one of these:

- native `<input type="date">`.
- custom HTML widgets with buttons/cells.
- third-party JavaScript components.

Module 07 uses a local fixture here because The Internet does not provide a
calendar/date-picker page for this lesson. The fixture shows two strategies.

For a native date input, the test sets the stored ISO value explicitly:

```java
((JavascriptExecutor) driver).executeScript(
        "arguments[0].value = '2026-05-08'; arguments[0].dispatchEvent(new Event('change'));",
        dateInput
);
```

Why not only `sendKeys`? Browser locale can affect how typed date text is
interpreted. The first verification run caught exactly this issue, so the
module now teaches the safer stored-value approach.

For a custom calendar control, the test clicks a visible date button:

```java
driver.findElement(By.id("date-2026-05-08")).click();
```

## Web Tables

Module 07 introduces table extraction on The Internet:

```java
List<WebElement> rows = driver.findElements(By.cssSelector("#table1 tbody tr"));
```

Then it finds a row by cell text:

```java
WebElement doeRow = rows.stream()
        .filter(row -> row.getText().contains("Doe"))
        .findFirst()
        .orElseThrow();
```

The Internet table is good for reading cells, but its row actions do not
produce a beginner-friendly visible result. The local fixture adds a visible
row action result:

```java
doeRow.findElement(By.cssSelector("button.select-person")).click();
```

This is the raw version of a pattern future helper methods can centralize:

- find row by cell value.
- read related cells.
- click row-level action.

## Sortable Tables

The fixture has a sortable table. The test clicks the header button and then
reads the first column:

```java
List<String> names = driver.findElements(By.cssSelector("#sortable-table tbody tr td:first-child"))
        .stream()
        .map(WebElement::getText)
        .toList();
```

This prepares for later collection and assertion patterns in framework tests.

## JavaScriptExecutor

`JavascriptExecutor` runs script inside the current page. Module 07 reads
simple browser state from The Internet:

```java
JavascriptExecutor javascript = (JavascriptExecutor) driver;
String title = (String) javascript.executeScript("return document.title;");
```

Use it selectively. Prefer normal WebDriver APIs first because they better
represent user behavior.

Good uses:

- reading browser state not exposed by WebElement APIs.
- setting up difficult fixture state.
- inspecting image properties.

Avoid using JavaScript to bypass every click or type action. That hides real
user interaction problems.

## Broken Images

Selenium can locate an image and read attributes. Module 07 uses The Internet
broken images page. To check whether the browser loaded the image, the test
reads browser image properties:

```java
return arguments[0].complete && arguments[0].naturalWidth === 0;
```

This is intentionally a basic browser-side check. HTTP status-code validation
for image URLs can be added later if the framework needs it.
