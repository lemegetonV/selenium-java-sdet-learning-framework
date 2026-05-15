# Module 13 Interview Review

## Core Topics

You should now be able to explain:

- what a TestNG listener is.
- why screenshot capture belongs in failure diagnostics, not every test method.
- how Selenium captures screenshots through `TakesScreenshot`.
- why framework logs are better than `System.out.println`.
- what Log4j2 appenders, patterns, levels, and `ThreadContext` do.
- why retries are dangerous when used casually.
- why custom framework exceptions improve failure triage.
- how `ITestResult` carries screenshot metadata forward to reports.
- why data-driven logs should identify rows without leaking full records.

## Strong Answer Framing

### What is an `ITestListener`?

An `ITestListener` is a TestNG callback interface. TestNG calls it during test
lifecycle events such as start, success, failure, skip, and finish. In a
Selenium framework it is commonly used for logging, screenshots on failure,
and report attachments.

In this checkpoint,
[FrameworkTestListener.java](../../src/test/java/com/learning/tests/listeners/FrameworkTestListener.java)
sets Log4j2 `ThreadContext`, logs start/pass/fail/skip, captures screenshots on
failure, and stores the screenshot path on `ITestResult`.

### How do you capture screenshots on failure?

Use a TestNG listener's `onTestFailure` method, get the current driver from the
driver factory, cast or check it as `TakesScreenshot`, call
`getScreenshotAs(OutputType.FILE)`, and copy the file into a framework artifact
folder with a useful name.

[ScreenshotUtils.java](../../src/main/java/com/learning/framework/screenshots/ScreenshotUtils.java)
handles the file naming, directory creation, Selenium temporary file copy, and
`FrameworkException` wrapping.

### Why should logs avoid full test data values?

Automation data can include passwords, tokens, emails, or customer data. Logs
are copied into CI artifacts and reports, so the framework should log useful
identifiers such as scenario names without exposing sensitive values.

That is why `FrameworkTestListener.safeParameterName(...)` logs
[LoginScenario.java](../../src/test/java/com/learning/tests/models/LoginScenario.java)
by `scenarioName()` only, and
[ElementActions.java](../../src/main/java/com/learning/framework/actions/ElementActions.java)
logs the number of characters typed rather than the typed value.

### When should retries be used?

Retries should be rare and explicit. They can help with temporary environment
instability, but they should not hide broken locators, missing waits, bad test
data, or product defects.

In this checkpoint, [config.properties](../../src/test/resources/config/config.properties)
sets `retryCount=0`, and
[RetryAnnotationTransformer.java](../../src/test/java/com/learning/tests/listeners/RetryAnnotationTransformer.java)
only attaches [FrameworkRetryAnalyzer.java](../../src/test/java/com/learning/tests/listeners/FrameworkRetryAnalyzer.java)
when retry count is greater than zero.

### What is a framework exception?

A framework exception labels failures caused by the automation infrastructure,
not the product assertion. Unsupported browser configuration, missing driver
state, or screenshot file write failure are framework problems. A wrong page
title is a test assertion failure.

## Vocabulary

- Listener
- Callback
- `ITestResult`
- Screenshot artifact
- `TakesScreenshot`
- Log level
- Appender
- Pattern layout
- Thread context
- Retry analyzer
- Annotation transformer
- Framework exception

## Red Flags In Interviews

- "I add screenshot code in every test method."
- "I retry every failed test by default."
- "I log the username and password to debug login."
- "I use print statements in framework classes."
- "Custom exceptions are only for checked exceptions."

## Practical Walkthrough

Follow this path in the code:

1. [testng.xml](../../testng.xml) registers `FrameworkTestListener`.
2. `FrameworkTestListener.onTestStart` writes `testName` to `ThreadContext`.
3. `DriverFactory.createDriver` logs browser creation.
4. `ElementActions` logs clicks and typing length.
5. `FrameworkTestListener.onTestFailure` calls `ScreenshotUtils.capture`.
6. `ScreenshotUtils` writes files under `target/screenshots`.

If you can explain that flow clearly, you understand the module.

## Debugging Questions

When a Module 13 failure happens, ask:

- Did the run use [testng.xml](../../testng.xml), so listeners were registered?
- Did `target/logs/test-execution.log` include `START` and browser lifecycle
  lines?
- Was `testName` present in the log pattern from
  [log4j2.xml](../../src/test/resources/log4j2.xml)?
- Did failure happen before or after browser creation?
- If a screenshot is missing, did `ScreenshotUtils.capture(...)` throw a
  framework exception that the listener logged as a warning?
- Is the failure a product assertion, Selenium interaction failure, or
  framework infrastructure failure?

## One-Minute Whiteboard Answer

Module 13 adds the first diagnostics layer. TestNG registers
[FrameworkTestListener.java](../../src/test/java/com/learning/tests/listeners/FrameworkTestListener.java)
from [testng.xml](../../testng.xml). The listener sets per-test log context,
logs lifecycle events, captures screenshots on failure through
[ScreenshotUtils.java](../../src/main/java/com/learning/framework/screenshots/ScreenshotUtils.java),
and stores the screenshot path on `ITestResult`. Log4j2 writes console and file
logs using `ThreadContext`. Retry support exists through a transformer and
retry analyzer but is disabled by default with `retryCount=0`.
