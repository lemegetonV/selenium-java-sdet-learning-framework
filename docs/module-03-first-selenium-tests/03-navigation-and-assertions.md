# Navigation and Assertions

## `driver.get(...)`

`driver.get(url)` loads a page.

Module 03 uses it in:

```text
src/test/java/com/learning/tests/learning/_01_FirstBrowserTest.java
src/test/java/com/learning/tests/learning/_03_SauceDemoPageLoadTest.java
```

Example:

```java
driver.get("https://www.saucedemo.com/");
```

This is the first real browser automation command in the project.

Nuance:

- `get()` starts browser navigation to the target URL.
- Selenium waits for the browser's normal page-load completion signal before
  the command returns.
- normal page load completion does not guarantee every dynamic element is ready.
- later modules introduce waits for dynamic UI behavior.

Common beginner mistake:

- assuming that because `get()` returned, every element on a modern dynamic
  page is immediately safe to use. That is not always true.

## Reading Browser State

After navigation, tests read browser state:

```java
driver.getTitle();
driver.getCurrentUrl();
```

These are safe first assertions because they do not require element locators
yet. Locators begin in Module 04.

`getTitle()` reads the current document title from the browser.

`getCurrentUrl()` reads the browser's current URL after navigation, redirects,
or history actions.

Nuance:

- title and URL checks are useful smoke checks.
- they do not prove that important elements are visible or usable.
- they are intentionally enough for Module 03 because element finding starts
  in Module 04.

## Browser Navigation

`_02_NavigationTest` demonstrates:

```text
src/test/java/com/learning/tests/learning/_02_NavigationTest.java
```

Commands:

```java
driver.navigate().to("https://the-internet.herokuapp.com/login");
driver.navigate().back();
driver.navigate().forward();
driver.navigate().refresh();
```

These commands control browser history and reload behavior.

`driver.get(url)` and `driver.navigate().to(url)` both load a URL in this
module's beginner examples.

The useful learning distinction is intent:

- use `get(url)` when the test simply opens a page.
- use `navigate().to(url)` when the test is demonstrating browser navigation
  as a concept.
- use `back()`, `forward()`, and `refresh()` when browser history or reload
  behavior is part of the scenario.

Nuance:

- `back()` and `forward()` depend on browser history. If there is no previous
  or next entry, the command may not move where the test expects.
- `refresh()` reloads the current page. It does not create a new WebDriver
  session.
- after navigation, assertions should read the browser state again instead of
  assuming the command succeeded.

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

Nuance:

- assertion messages should explain the expected behavior when the default
  failure output would be unclear.
- keep early assertions simple so failures point to one browser behavior at a
  time.
- later modules will assert element text, attributes, visibility, and page
  state after interactions.

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
