# Screenshots On Failure

## Mental Model

A UI test failure message often tells you what assertion failed, but not what
the browser looked like. A screenshot gives visual evidence of the browser
state at the moment the failure was observed.

In a framework, screenshot code should be centralized. Tests should not decide
where screenshots are saved or how filenames are built.

## Code Walkthrough

Main file:

[src/main/java/com/learning/framework/screenshots/ScreenshotUtils.java](../../src/main/java/com/learning/framework/screenshots/ScreenshotUtils.java)

Important Selenium API:

```java
driver instanceof TakesScreenshot screenshotDriver
```

`TakesScreenshot` is a Selenium interface. Not every theoretical `WebDriver`
implementation has to support screenshots, so the utility checks the interface
before calling:

```java
screenshotDriver.getScreenshotAs(OutputType.FILE)
```

Selenium returns a temporary file. The framework copies it to:

`target/screenshots/`

The listener calls the utility from:

[src/test/java/com/learning/tests/listeners/FrameworkTestListener.java](../../src/test/java/com/learning/tests/listeners/FrameworkTestListener.java)

## Java Syntax To Notice

Pattern matching in:

```java
if (!(driver instanceof TakesScreenshot screenshotDriver)) {
```

does two things:

- checks whether `driver` implements the interface.
- creates a typed variable named `screenshotDriver` when the check succeeds.

`Files.createDirectories(...)` is defensive. The test run should not fail just
because `target/screenshots` does not exist yet.

`StandardCopyOption.REPLACE_EXISTING` makes the utility deterministic if two
captures produce the same filename, though the timestamp makes collisions
unlikely.

## Selenium Or Framework Nuances

Screenshots are browser viewport evidence. They do not prove the DOM state by
themselves. If a test fails because hidden HTML changed, the screenshot may not
show enough. Later reporting can combine screenshots with logs and exception
stack traces.

Headless screenshots can differ slightly from headed screenshots because the
browser window size and rendering mode affect layout. Module 11 already
controls window size through `config.properties`.

## Common Mistakes

- Calling screenshot code after `driver.quit()`.
- Saving all screenshots as `screenshot.png` and overwriting evidence.
- Putting screenshot code inside each test instead of a listener.
- Treating screenshots as a replacement for clear assertions.
- Logging screenshot paths without storing them on `ITestResult`.

## Interview Readiness

Strong answer:

"In Selenium, screenshots are captured through the `TakesScreenshot` interface.
In a TestNG framework I usually capture them in `onTestFailure` because the
browser is still alive and the failure result is available. The screenshot path
can then be stored in the test result and attached to a report."

## How This Connects To Later Framework Design

Module 14 will attach screenshot paths to Extent and Allure reports. Module 15
will require screenshot filenames to remain unique when multiple tests run at
the same time.

## Revision Checklist

- Can you explain what `TakesScreenshot` is?
- Can you explain why Selenium first returns a temporary screenshot file?
- Can you find where screenshots are saved?
- Can you explain why the utility throws `FrameworkException`?

