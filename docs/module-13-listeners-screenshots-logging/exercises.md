# Module 13 Exercises

Use these exercises after reading:

- [FrameworkTestListener.java](../../src/test/java/com/learning/tests/listeners/FrameworkTestListener.java)
- [ScreenshotUtils.java](../../src/main/java/com/learning/framework/screenshots/ScreenshotUtils.java)
- [log4j2.xml](../../src/test/resources/log4j2.xml)
- [ElementActions.java](../../src/main/java/com/learning/framework/actions/ElementActions.java)
- [DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java)
- [FrameworkRetryAnalyzer.java](../../src/test/java/com/learning/tests/listeners/FrameworkRetryAnalyzer.java)
- [RetryAnnotationTransformer.java](../../src/test/java/com/learning/tests/listeners/RetryAnnotationTransformer.java)
- [FrameworkException.java](../../src/main/java/com/learning/framework/exceptions/FrameworkException.java)
- [testng.xml](../../testng.xml)

## Exercise 1 - Read The Log Story

Run:

```bash
mvn test -DsuiteXmlFile=testng.xml
```

Open:

`target/logs/test-execution.log`

Identify:

- one `START` line.
- one `PASS` line.
- one browser creation line.
- one click or type action line.

Expected outcome:

You can explain how listener logs and framework service logs combine into one
execution story.

Revision question:

- which class wrote each line: listener, driver factory, action wrapper, or
  retry analyzer?

## Exercise 2 - Enable One Retry Locally

Run:

```bash
mvn test -DsuiteXmlFile=testng.xml -DretryCount=1
```

Expected outcome:

The suite should still pass. No retry should happen when tests pass. Be ready
to explain that a retry analyzer is only consulted after a failure.

Revision question:

- where does [RetryAnnotationTransformer.java](../../src/test/java/com/learning/tests/listeners/RetryAnnotationTransformer.java)
  attach the retry analyzer, and why is that better than editing every
  `@Test` method?

## Exercise 3 - Trace Screenshot Flow

Read:

- [src/test/java/com/learning/tests/listeners/FrameworkTestListener.java](../../src/test/java/com/learning/tests/listeners/FrameworkTestListener.java)
- [src/main/java/com/learning/framework/screenshots/ScreenshotUtils.java](../../src/main/java/com/learning/framework/screenshots/ScreenshotUtils.java)

Write down the method call chain from `onTestFailure` to the final screenshot
file path.

Hint:

Look for `DriverFactory.getDriver()`, `ScreenshotUtils.capture(...)`, and
`result.setAttribute(...)`.

Expected outcome:

You can explain how `onTestFailure(...)` gets a live driver, captures a PNG,
stores the absolute path as `screenshotPath`, and leaves that attribute
available for Module 14 reports.

## Exercise 4 - Explain Safe Logging

Read:

- [src/test/java/com/learning/tests/listeners/FrameworkTestListener.java](../../src/test/java/com/learning/tests/listeners/FrameworkTestListener.java)
- [src/main/java/com/learning/framework/actions/ElementActions.java](../../src/main/java/com/learning/framework/actions/ElementActions.java)

Answer:

- Why does the listener log a `LoginScenario` by scenario name only?
- Why does `ElementActions.type(...)` log character count instead of typed
  text?

Expected outcome:

You can explain diagnostic value and data-safety tradeoffs.

Revision question:

- what sensitive values might leak if the framework logged full objects or raw
  `sendKeys` values?

## Exercise 5 - Custom Exception Classification

Read:

[src/main/java/com/learning/framework/exceptions/FrameworkException.java](../../src/main/java/com/learning/framework/exceptions/FrameworkException.java)

Classify each failure as either test assertion failure or framework exception:

- product title is `Products` but expected `Cart`.
- browser value is configured as `safari` when only Chrome, Firefox, and Edge
  are supported.
- screenshot cannot be written to disk.
- locked-out user error message is missing.

Expected outcome:

You can distinguish product behavior failures from framework infrastructure
failures.

## Exercise 6 - Explain ThreadContext Cleanup

Read:

- [src/test/java/com/learning/tests/listeners/FrameworkTestListener.java](../../src/test/java/com/learning/tests/listeners/FrameworkTestListener.java)
- [src/test/resources/log4j2.xml](../../src/test/resources/log4j2.xml)

Answer:

- where is `ThreadContext.put("testName", ...)` called?
- where is `ThreadContext.clearMap()` called?
- what could happen if the context were not cleared?

Expected outcome:

You can explain how per-thread log context prepares the framework for later
parallel execution.

## Exercise 7 - Force A FrameworkException Mentally

Read:

- [src/main/java/com/learning/framework/driver/DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java)
- [src/main/java/com/learning/framework/screenshots/ScreenshotUtils.java](../../src/main/java/com/learning/framework/screenshots/ScreenshotUtils.java)

Predict which message would be thrown for:

- calling `DriverFactory.getDriver()` before `createDriver()`.
- setting `browser=safari`.
- using a driver that does not implement `TakesScreenshot`.

Expected outcome:

You can explain why framework failures should have framework vocabulary.

## Exercise 8 - Find The Module 14 Handoff

Find the constant:

```java
SCREENSHOT_PATH_ATTRIBUTE
```

Expected outcome:

You can explain why storing the screenshot path on `ITestResult` is useful for
Extent and Allure reporting in the next module.
