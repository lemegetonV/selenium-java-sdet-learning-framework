# Module 03 Interview Review

## What You Must Be Able To Explain

Module 03 is the first real Selenium module. You should be able to explain:

- what Selenium WebDriver is.
- why `WebDriver` is the variable type and `ChromeDriver` is the concrete
  object.
- what Selenium Manager does.
- why `ChromeOptions` is configured before browser creation.
- why headless mode is useful and where it can differ from visible mode.
- why `try/finally` and `quit()` are required.
- why TestNG assertions are used instead of console output.

## Strong Answers

**What is Selenium WebDriver?**

Selenium WebDriver is the browser automation API used to send commands to a
real browser session. In Java, `WebDriver` is also the interface that tests use
to interact with browser implementations such as `ChromeDriver`.

**What is ChromeDriver?**

ChromeDriver is the concrete browser driver implementation for Chrome. It
receives WebDriver commands and controls a Chrome browser session.

**What is Selenium Manager?**

Selenium Manager is built into modern Selenium. It helps resolve the correct
browser driver executable. It does not remove the need for clean driver
lifecycle code.

**Why use `try/finally`?**

The test must quit the browser even if navigation or assertion fails. The
`finally` block guarantees cleanup code runs after the `try` block.

**Why not create `BaseTest` immediately?**

The learner first needs to see repeated driver setup and cleanup. Module 08
will introduce `BaseTest` after the duplication is visible and meaningful.

## Code Lines To Revise

```java
WebDriver driver = createChromeDriver();
```

The test depends on the `WebDriver` interface, while the helper returns a
Chrome-specific implementation.

```java
options.addArguments("--headless=new");
```

This starts Chrome without a visible UI. It is useful for CI and quick local
runs, but visible mode remains useful for debugging.

```java
driver.get("https://the-internet.herokuapp.com/");
```

This opens a full page and waits for normal page-load completion, not every
dynamic UI condition.

```java
Assert.assertEquals(driver.getTitle(), "The Internet");
```

This turns a browser observation into a test result.

## Common Interview Traps

- Saying Selenium Manager manages browser lifecycle. It only helps with driver
  executable resolution.
- Saying `close()` and `quit()` are the same. `close()` closes the current
  window; `quit()` ends the entire session.
- Assuming headless mode is a fake browser. It is still a real browser engine.
- Treating title/URL checks as complete functional validation.
- Hiding all setup too early and losing understanding of driver ownership.

## Connection To Future Framework Modules

Module 03 deliberately repeats driver setup. Module 08 will centralize test
lifecycle. Module 11 will improve browser/config ownership. Module 13 will add
failure diagnostics. Those framework features make sense only because Module 03
made the raw WebDriver lifecycle explicit first.
