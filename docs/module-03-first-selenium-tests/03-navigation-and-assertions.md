# Navigation and Assertions

## `driver.get(...)`

`driver.get(url)` loads a page.

Module 03 uses it in:

```text
src/test/java/com/learning/tests/learning/FirstBrowserTest.java
src/test/java/com/learning/tests/learning/SauceDemoPageLoadTest.java
```

Example:

```java
driver.get("https://www.saucedemo.com/");
```

This is the first real browser automation command in the project.

## Reading Browser State

After navigation, tests read browser state:

```java
driver.getTitle();
driver.getCurrentUrl();
```

These are safe first assertions because they do not require element locators
yet. Locators begin in Module 04.

## Browser Navigation

`NavigationTest` demonstrates:

```text
src/test/java/com/learning/tests/learning/NavigationTest.java
```

Commands:

```java
driver.navigate().to("https://the-internet.herokuapp.com/login");
driver.navigate().back();
driver.navigate().forward();
driver.navigate().refresh();
```

These commands control browser history and reload behavior.

## Assertions

Assertions explain what the test expects.

Good beginner assertions are simple and close to the browser action:

```java
Assert.assertEquals(driver.getTitle(), "Swag Labs");
```

That means:

- actual value: the title read from the browser.
- expected value: the title the test requires.
- failure result: TestNG marks the test failed if they differ.

## Why We Avoid Locators Here

It would be tempting to locate the username and password fields on SauceDemo
immediately.

That is intentionally deferred.

Module 03 focuses on:

- browser launch.
- navigation.
- title.
- URL.
- raw setup and cleanup.

Module 04 teaches element locators and interactions in a dedicated module.

## Key Takeaways

- `driver.get(...)` loads a URL.
- `driver.navigate()` controls browser history.
- `getTitle()` and `getCurrentUrl()` are simple first browser observations.
- assertions turn observations into pass/fail test results.
- keeping locators out of Module 03 preserves the learning progression.
