# Selenium Manager and ChromeDriver

## What Selenium Manager Does

Selenium needs a browser driver executable to control a browser.

For Chrome, that executable is ChromeDriver.

Modern Selenium includes Selenium Manager. When a test creates a
`ChromeDriver`, Selenium Manager can locate or download the matching driver so
the learner does not manually manage driver binaries in this module.

Module 03 uses this directly:

[src/test/java/com/learning/tests/learning/_01_FirstBrowserTest.java](../../src/test/java/com/learning/tests/learning/_01_FirstBrowserTest.java)

Nuance:

- Selenium Manager is part of modern Selenium, not a separate dependency in
  this project.
- It helps find or download the correct driver executable.
- It does not install Chrome itself. The browser still needs to exist on the
  machine.
- It reduces beginner setup friction, but later framework modules will still
  teach browser selection and driver lifecycle clearly.

## Selenium Manager vs WebDriverManager

Many Selenium courses and interview discussions mention WebDriverManager. In
most Java Selenium contexts that means the Bonigarcia WebDriverManager
library, which became popular because older Selenium projects had to manage
browser driver executables manually.

This project uses Selenium Manager as the primary path because Selenium 4
already includes it. There is no extra dependency to configure for Module 03:

```java
return new ChromeDriver(options);
```

When this line runs, Selenium can invoke Selenium Manager behind the scenes to
resolve the matching ChromeDriver.

The practical comparison is:

| Topic | Selenium Manager | Bonigarcia WebDriverManager |
| --- | --- | --- |
| Where it comes from | built into Selenium 4 | separate Java dependency |
| Beginner setup | no extra Maven dependency | add dependency and call setup API |
| Typical modern use | default choice for simple Selenium 4 projects | useful when a project needs its richer configuration options |
| This project | primary approach | explained for recognition, not installed now |

Interview nuance:

- WebDriverManager is still worth recognizing because many existing
  frameworks and tutorials use it.
- Selenium Manager is the modern default for this learning repo.
- Driver lifecycle is still a framework responsibility. Selenium Manager helps
  resolve the executable; it does not decide when to create, reuse, or quit
  browser sessions.

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
| `_01_BrowserDriver` interface | `WebDriver` interface |
| `_02_ChromeBrowserDriver` class | `ChromeDriver` class |
| `_01_BrowserDriver browser = ...` | `WebDriver driver = ...` |
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

Read that repeated helper in:

- [src/test/java/com/learning/tests/learning/_01_FirstBrowserTest.java](../../src/test/java/com/learning/tests/learning/_01_FirstBrowserTest.java)
- [src/test/java/com/learning/tests/learning/_02_NavigationTest.java](../../src/test/java/com/learning/tests/learning/_02_NavigationTest.java)
- [src/test/java/com/learning/tests/learning/_03_SauceDemoPageLoadTest.java](../../src/test/java/com/learning/tests/learning/_03_SauceDemoPageLoadTest.java)

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

## Java Syntax To Notice

```java
WebDriver driver = createChromeDriver();
```

The variable type is `WebDriver`, the Selenium interface. The object returned
from the helper is a `ChromeDriver`. This is the real Selenium version of the
polymorphism learned in Module 02.

```java
ChromeOptions options = new ChromeOptions();
```

`ChromeOptions` is a configuration object. It must be configured before the
driver is created because browser startup settings cannot all be changed after
Chrome is already running.

```java
Boolean.parseBoolean(System.getProperty("headless", "true"))
```

This reads a Maven/system property. If the property is missing, `"true"` is
used as the default. That is why `mvn test` runs headless, while
`mvn test -Dheadless=false` shows the browser.

## Interview Readiness

**Question: What is Selenium WebDriver?**

WebDriver is Selenium's browser automation API and also the main Java
interface used by tests. It lets test code send commands to a browser session,
such as opening URLs, finding elements, reading browser state, and quitting the
session.

**Question: What does Selenium Manager do?**

Selenium Manager helps Selenium locate or obtain the correct browser driver
binary. It does not design the framework, manage test lifecycle, choose
browsers for parallel execution, or replace `quit()`.

**Question: Why is `quit()` important?**

`quit()` ends the whole WebDriver session and releases browser/driver
resources. Without it, local machines and CI agents can accumulate orphaned
browser processes.

## Revision Checklist

- Can you explain the difference between Selenium Manager, ChromeDriver, and
  WebDriver?
- Can you explain why options are created before the driver?
- Can you explain why Module 03 repeats `createChromeDriver()` instead of
  hiding it in a base class?
