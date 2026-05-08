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
actions, selected-row highlighting, and deterministic sorting with visible sort
status.

## Calendar And Date Picker Widgets

Calendars are not one Selenium API. They are usually one of these:

- native `<input type="date">`.
- custom HTML widgets with buttons/cells.
- third-party JavaScript components.

Module 07 uses a local fixture here because The Internet does not provide a
calendar/date-picker page for this lesson. The fixture shows two strategies.

This is also why the fixture is intentionally visible and deterministic. A
learner should be able to open the HTML file, pick the same date manually, and
see the same "Selected date" text that the test asserts.

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

The script also dispatches a `change` event. Many applications update visible
UI only when the browser fires that event, so setting the value without the
event can leave the page in an unrealistic state.

For a custom calendar control, the test clicks a visible date button:

```java
driver.findElement(By.id("date-2026-05-08")).click();
```

Both paths update the same visible result:

```java
Assert.assertEquals(driver.findElement(By.id("selected-date")).getText(), "Selected date: 2026-05-08");
```

That matters because a learning fixture should not make the native input path
look disconnected from the custom date-button path.

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
row action result and a selected-row highlight:

```java
doeRow.findElement(By.cssSelector("button.select-person")).click();
Assert.assertTrue(doeRow.getAttribute("class").contains("selected-row"));
Assert.assertEquals(driver.findElement(By.id("selected-person")).getText(), "Selected person: Jane Doe");
```

This is the raw version of a pattern future helper methods can centralize:

- find row by cell value.
- read related cells.
- click row-level action.

The important technique is row-scoped lookup. Once `doeRow` is found, the test
searches for the button inside that row, not across the entire page. This keeps
the action tied to the matching record.

## Sortable Tables

The fixture has a separate sortable table so row selection and sorting are not
mixed into one lesson. The test clicks the header button and then reads the
first column:

```java
List<String> names = driver.findElements(By.cssSelector("#sortable-table tbody tr td:first-child"))
        .stream()
        .map(WebElement::getText)
        .toList();
```

This prepares for later collection and assertion patterns in framework tests.
The fixture also reports the active sort direction:

```java
Assert.assertEquals(driver.findElement(By.id("sort-result")).getText(), "Sorted by name: ascending");
Assert.assertEquals(driver.findElement(By.id("sort-by-name")).getAttribute("aria-sort"), "ascending");
```

## JavaScriptExecutor

`JavascriptExecutor` runs script inside the current page. Module 07 reads
simple browser state from The Internet:

```java
JavascriptExecutor javascript = (JavascriptExecutor) driver;
String title = (String) javascript.executeScript("return document.title;");
```

Use it selectively. Prefer normal WebDriver APIs first because they better
represent user behavior.

`executeScript` returns `Object` because JavaScript can return many different
types. The test casts the result to the Java type it expects, such as `String`,
`Long`, or `Boolean`.

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

## Java Syntax To Notice

```java
List<String> names = driver.findElements(...)
        .stream()
        .map(WebElement::getText)
        .toList();
```

This stream converts a list of `WebElement` objects into a list of visible text
values. `WebElement::getText` is a method reference, equivalent to calling
`element.getText()` for each element.

```java
JavascriptExecutor javascript = (JavascriptExecutor) driver;
```

This is a cast. `ChromeDriver` implements `JavascriptExecutor`, but the
variable type is `WebDriver`, so Java needs the explicit cast before
`executeScript(...)` is available.

```java
((JavascriptExecutor) driver).executeScript(
        "arguments[0].value = '2026-05-08'; arguments[0].dispatchEvent(new Event('change'));",
        dateInput
);
```

`arguments[0]` is the first Java object passed after the script string. Selenium
converts the `WebElement` into a DOM element for the browser-side script.

```java
Assert.assertEquals(names, List.of("Alice", "Bob", "Charlie"));
```

`List.of(...)` creates an immutable expected list. This is useful when the
assertion is about exact order, not just whether the values are present.

## Interview Readiness

**Question: How do you handle calendars in Selenium?**

There is no single Selenium calendar API. Native date inputs, custom widgets,
and third-party date pickers each need different strategies. Prefer user-like
clicks for custom widgets, and use JavaScript carefully when the lesson or app
requires direct stored-value control.

**Question: How do you work with web tables?**

Find rows as a list, identify the row by stable cell text, then read related
cells or click row-level actions inside that row.

**Question: When should JavaScriptExecutor be used?**

Use it selectively for browser state or properties not exposed cleanly through
WebElement APIs. Do not use it to bypass every user action.

## Revision Checklist

- Can you explain the stream pipeline used for sortable table assertions?
- Can you explain why `JavascriptExecutor` needs a cast?
- Can you explain why broken image detection uses `complete` and
  `naturalWidth`?
