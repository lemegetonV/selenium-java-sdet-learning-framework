# Module 13 Exercises

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

## Exercise 2 - Enable One Retry Locally

Run:

```bash
mvn test -DsuiteXmlFile=testng.xml -DretryCount=1
```

Expected outcome:

The suite should still pass. No retry should happen when tests pass. Be ready
to explain that a retry analyzer is only consulted after a failure.

## Exercise 3 - Trace Screenshot Flow

Read:

- [src/test/java/com/learning/tests/listeners/FrameworkTestListener.java](../../src/test/java/com/learning/tests/listeners/FrameworkTestListener.java)
- [src/main/java/com/learning/framework/screenshots/ScreenshotUtils.java](../../src/main/java/com/learning/framework/screenshots/ScreenshotUtils.java)

Write down the method call chain from `onTestFailure` to the final screenshot
file path.

Hint:

Look for `DriverFactory.getDriver()`, `ScreenshotUtils.capture(...)`, and
`result.setAttribute(...)`.

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

