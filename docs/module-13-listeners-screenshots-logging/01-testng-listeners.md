# TestNG Listeners

## Mental Model

Test methods should verify product behavior. They should not repeat diagnostic
plumbing such as "log when the test starts" or "take a screenshot on failure".

TestNG listeners solve that by letting framework code react to test lifecycle
events. TestNG calls listener methods automatically:

- `onStart` when a TestNG context begins.
- `onTestStart` before a test method runs.
- `onTestSuccess` after a passing test.
- `onTestFailure` after a failing test.
- `onTestSkipped` when TestNG skips a test.
- `onFinish` when the context ends.

In this project, the listener is
[FrameworkTestListener.java](../../src/test/java/com/learning/tests/listeners/FrameworkTestListener.java).
It is diagnostic infrastructure. It does not decide whether SauceDemo behavior
is correct; it records what happened around the test.

## Code Walkthrough

Main file:

[src/test/java/com/learning/tests/listeners/FrameworkTestListener.java](../../src/test/java/com/learning/tests/listeners/FrameworkTestListener.java)

The class implements:

```java
public class FrameworkTestListener implements ITestListener
```

`ITestListener` is a TestNG interface. Implementing it is an OOP contract:
the class promises that it can receive TestNG lifecycle callbacks.

The listener is registered in:

[testng.xml](../../testng.xml)

```xml
<listeners>
    <listener class-name="com.learning.tests.listeners.FrameworkTestListener"/>
    <listener class-name="com.learning.tests.listeners.RetryAnnotationTransformer"/>
</listeners>
```

This keeps the tests clean. `SauceDemoPageObjectTest` and
`SauceDemoDataDrivenTest` do not need listener code inside each test method.

## Runtime Callback Flow

For each test method, TestNG and the framework interact like this:

```text
TestNG context starts
    -> onStart(context)
Test method is about to run
    -> onTestStart(result)
    -> ThreadContext.put("testName", displayName)
BaseTest creates browser
Test method executes
    -> page objects and wrappers log activity
Test passes/fails/skips
    -> onTestSuccess/onTestFailure/onTestSkipped
    -> listener logs status
    -> on failure, listener captures screenshot
    -> ThreadContext.clearMap()
BaseTest quits browser
TestNG context finishes
    -> onFinish(context)
```

The ordering matters. `onTestFailure(...)` runs while the browser from
[DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java)
is still available. After `BaseTest` teardown, screenshot capture would be too
late.

## Java Syntax To Notice

`@Override` means the method is implementing a method declared by the TestNG
interface. If the method name or parameter type is wrong, Java compilation
fails. That is valuable because listener method signatures must be exact.

`ITestResult` gives the listener runtime information about the current test:

- method name.
- test parameters.
- thrown exception.
- attributes that can be passed to reports later.

`result.setAttribute("screenshotPath", path)` stores extra framework metadata
on the test result. Module 14 can read this same value when adding report
attachments.

## Display Names And Safe Parameters

The listener builds log names with:

```java
private String displayName(ITestResult result)
```

For tests without parameters, the display name is just the method name. For
data-driven tests, the listener reads `result.getParameters()` and converts
each parameter through `safeParameterName(...)`.

[LoginScenario.java](../../src/test/java/com/learning/tests/models/LoginScenario.java)
contains a password, so the listener logs only `scenario.scenarioName()`.
That gives useful row identity without dumping credentials into logs, reports,
or CI artifacts.

## Selenium Or Framework Nuances

The listener captures screenshots in `onTestFailure`, before `BaseTest` quits
the browser in `@AfterMethod`. That timing matters. After `quit()`, the browser
session no longer exists and Selenium cannot capture the page state.

The listener does not own driver creation. It asks `DriverFactory.getDriver()`
for the current test thread's browser. This keeps one owner for browser
lifecycle and avoids hidden driver creation inside diagnostics.

The listener catches screenshot failures inside `captureFailureScreenshot(...)`.
That prevents a secondary diagnostics problem, such as disk write failure, from
masking the original test failure. It logs a warning instead.

## What The Listener Stores For Later

```java
public static final String SCREENSHOT_PATH_ATTRIBUTE = "screenshotPath";
```

This constant is the contract between Module 13 and Module 14. Module 13 stores
the screenshot path on `ITestResult`; Module 14 reporting code can read the
same attribute and attach the image to Extent or Allure.

## Common Mistakes

- Registering a listener but running a command that does not use the suite XML.
  The module listener registration is in [testng.xml](../../testng.xml), so use
  `mvn test -DsuiteXmlFile=testng.xml` when verifying listener behavior.
- Taking screenshots in `@AfterMethod` after the driver has already quit.
- Logging full data-provider objects. Module 13 avoids this for
  `LoginScenario` because the record contains a password field.
- Adding assertion logic inside the listener. Assertions belong in tests.
- Forgetting to clear `ThreadContext` after pass, fail, or skip.
- Creating a new driver inside the listener when `DriverFactory.getDriver()`
  fails.

## Interview Readiness

Strong answer:

"A TestNG listener is a callback class that reacts to test lifecycle events.
In a Selenium framework I use it for cross-cutting diagnostics such as logging
test start/pass/fail and taking screenshots on failure. This keeps tests
focused on business assertions while framework behavior stays centralized."

Be ready to explain why listener code is framework code, not page-object code.

## Revision Checklist

- Can you trace `onTestFailure(...)` from log entry to screenshot attribute?
- Can you explain why `safeParameterName(...)` treats `LoginScenario`
  specially?
- Can you explain why listener registration is in [testng.xml](../../testng.xml)?
- Can you explain what would happen if `ThreadContext.clearMap()` were omitted?

## How This Connects To Later Framework Design

Module 14 will use the same listener position to attach screenshots and status
details to Extent and Allure reports. Module 15 will make the ThreadLocal driver
and per-test log context more important when tests run in parallel.
