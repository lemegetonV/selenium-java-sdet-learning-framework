# Selenium Manager and ChromeDriver

## What Selenium Manager Does

Selenium needs a browser driver executable to control a browser.

For Chrome, that executable is ChromeDriver.

Modern Selenium includes Selenium Manager. When a test creates a
`ChromeDriver`, Selenium Manager can locate or download the matching driver so
the learner does not manually manage driver binaries in this module.

Module 03 uses this directly:

```text
src/test/java/com/learning/tests/learning/FirstBrowserTest.java
```

Nuance:

- Selenium Manager is part of modern Selenium, not a separate dependency in
  this project.
- It helps find or download the correct driver executable.
- It does not install Chrome itself. The browser still needs to exist on the
  machine.
- It reduces beginner setup friction, but later framework modules will still
  teach browser selection and driver lifecycle clearly.

## `WebDriver driver = new ChromeDriver(options)`

The first test creates a browser like this:

```java
WebDriver driver = createChromeDriver();
```

Inside `createChromeDriver()`:

```java
return new ChromeDriver(options);
```

This connects directly to Module 02:

| Module 02 Example | Module 03 Selenium Code |
| --- | --- |
| `BrowserDriver` interface | `WebDriver` interface |
| `ChromeBrowserDriver` class | `ChromeDriver` class |
| `BrowserDriver browser = ...` | `WebDriver driver = ...` |
| simulated `open(...)` method | real `driver.get(...)` method |

Important nuance:

- the runtime object is a `ChromeDriver`.
- the variable type is `WebDriver`.
- using the interface type keeps the test closer to cross-browser design.
- the test should call common WebDriver behavior, not Chrome-only behavior,
  unless the module explicitly needs a Chrome-specific feature.

## Chrome Options

Each test creates `ChromeOptions`:

```java
ChromeOptions options = new ChromeOptions();
```

The option used most often in this module is headless mode.

Headless mode runs Chrome without showing the browser window. That is useful
for CI and for quick local test runs:

```bash
mvn test
```

To see the browser:

```bash
mvn test -Dheadless=false
```

Nuance:

- headless mode still runs a real browser engine.
- visual behavior can occasionally differ from headed mode, especially around
  window size, focus, downloads, and native dialogs.
- the module sets a stable window size so page layout is less likely to change
  between local and headless runs.
- visible mode is useful when debugging the first tests because learners can
  watch browser navigation happen.

## Why Setup Is Repeated

Each Module 03 test class has its own `createChromeDriver()` method.

That repetition is intentional.

The learner should first see the raw cost of repeated setup:

- every class creates options.
- every class creates a driver.
- every class must remember to call `quit()`.
- every class chooses the same window size.

Module 08 introduces `BaseTest` only after this duplication is visible.

## Cleanup With `quit()`

Every test uses `finally`:

```java
try {
    driver.get("https://the-internet.herokuapp.com/");
} finally {
    driver.quit();
}
```

The `finally` block matters because browser cleanup should happen even when an
assertion fails. Later this cleanup moves into TestNG lifecycle methods such as
`@AfterMethod`.

Common beginner mistakes:

- forgetting `quit()` and leaving browser processes open.
- using `close()` when the intent is to end the whole browser session.
- creating a driver before each test but only cleaning it up when assertions
  pass.

`quit()` ends the entire WebDriver session. `close()` closes the current
browser window. If more windows exist, the session may still be alive. For
beginner tests with one driver per test, `quit()` is the safer cleanup habit.
