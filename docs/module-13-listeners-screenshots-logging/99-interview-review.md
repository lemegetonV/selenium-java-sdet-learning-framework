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

## Strong Answer Framing

### What is an `ITestListener`?

An `ITestListener` is a TestNG callback interface. TestNG calls it during test
lifecycle events such as start, success, failure, skip, and finish. In a
Selenium framework it is commonly used for logging, screenshots on failure,
and report attachments.

### How do you capture screenshots on failure?

Use a TestNG listener's `onTestFailure` method, get the current driver from the
driver factory, cast or check it as `TakesScreenshot`, call
`getScreenshotAs(OutputType.FILE)`, and copy the file into a framework artifact
folder with a useful name.

### Why should logs avoid full test data values?

Automation data can include passwords, tokens, emails, or customer data. Logs
are copied into CI artifacts and reports, so the framework should log useful
identifiers such as scenario names without exposing sensitive values.

### When should retries be used?

Retries should be rare and explicit. They can help with temporary environment
instability, but they should not hide broken locators, missing waits, bad test
data, or product defects.

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

