# Screenshots On Failure

## Mental Model

A UI test failure message often tells you what assertion failed, but not what
the browser looked like. A screenshot gives visual evidence of the browser
state at the moment the failure was observed.

In a framework, screenshot code should be centralized. Tests should not decide
where screenshots are saved or how filenames are built.

Module 13 uses this ownership:

```text
FrameworkTestListener detects failure
        |
        v
DriverFactory supplies current WebDriver
        |
        v
ScreenshotUtils captures and saves PNG
        |
        v
ITestResult stores screenshotPath
```

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

## Code Walkthrough

```java
private static final Path SCREENSHOT_DIRECTORY = Paths.get("target", "screenshots");
```

Screenshots are build artifacts, so they live under `target/`. They are not
source files and should not be committed.

```java
private static final DateTimeFormatter FILE_TIMESTAMP =
        DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS");
```

The timestamp makes filenames easier to sort and reduces the chance of one
failure overwriting another.

```java
public static Path capture(WebDriver driver, String logicalName)
```

The utility receives a driver and a logical name. The listener passes the safe
test display name, so data-driven screenshots can include scenario names
without including full records or passwords.

```java
Path targetFile = SCREENSHOT_DIRECTORY.resolve(fileNameFor(logicalName));
Files.copy(sourceFile.toPath(), targetFile, StandardCopyOption.REPLACE_EXISTING);
return targetFile.toAbsolutePath();
```

The returned absolute path is stored on `ITestResult` by
[FrameworkTestListener.java](../../src/test/java/com/learning/tests/listeners/FrameworkTestListener.java).
That path is the bridge to Module 14 reports.

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

`sanitize(logicalName)` replaces unsafe filename characters with `_`. This is
important because data-driven display names can contain spaces, brackets, or
characters that are awkward in file paths.

## Selenium Or Framework Nuances

Screenshots are browser viewport evidence. They do not prove the DOM state by
themselves. If a test fails because hidden HTML changed, the screenshot may not
show enough. Later reporting can combine screenshots with logs and exception
stack traces.

Headless screenshots can differ slightly from headed screenshots because the
browser window size and rendering mode affect layout. Module 11 already
controls window size through `config.properties`.

Screenshots are captured before browser cleanup. In this TestNG flow,
`onTestFailure(...)` runs before `@AfterMethod`, so
[DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java)
still has a live driver.

If screenshot capture itself fails, the listener logs a warning and keeps the
original test failure as the main failure. Diagnostic failure should not hide
the actual assertion or Selenium error.

## Common Mistakes

- Calling screenshot code after `driver.quit()`.
- Saving all screenshots as `screenshot.png` and overwriting evidence.
- Putting screenshot code inside each test instead of a listener.
- Treating screenshots as a replacement for clear assertions.
- Logging screenshot paths without storing them on `ITestResult`.
- Assuming screenshots include the whole page; Selenium captures the current
  viewport unless browser/tool behavior says otherwise.
- Committing generated screenshots from `target/screenshots`.

## Interview Readiness

Strong answer:

"In Selenium, screenshots are captured through the `TakesScreenshot` interface.
In a TestNG framework I usually capture them in `onTestFailure` because the
browser is still alive and the failure result is available. The screenshot path
can then be stored in the test result and attached to a report."

## Failure Triage Model

Use screenshots with logs and stack traces:

- stack trace tells where the failure happened.
- logs tell what the framework was doing before the failure.
- screenshot shows visible browser state at failure time.
- `scenarioName` tells which data row was running.

None of these is enough alone. Together they reduce time spent reproducing UI
failures.

## How This Connects To Later Framework Design

Module 14 will attach screenshot paths to Extent and Allure reports. Module 15
will require screenshot filenames to remain unique when multiple tests run at
the same time.

## Revision Checklist

- Can you explain what `TakesScreenshot` is?
- Can you explain why Selenium first returns a temporary screenshot file?
- Can you find where screenshots are saved?
- Can you explain why the utility throws `FrameworkException`?
- Can you explain why the listener catches screenshot exceptions?
- Can you explain how Module 14 will find the screenshot path?
