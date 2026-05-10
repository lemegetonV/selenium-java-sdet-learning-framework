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

## Code Walkthrough

Main file:

`src/test/java/com/learning/tests/listeners/FrameworkTestListener.java`

The class implements:

```java
public class FrameworkTestListener implements ITestListener
```

`ITestListener` is a TestNG interface. Implementing it is an OOP contract:
the class promises that it can receive TestNG lifecycle callbacks.

The listener is registered in:

`testng.xml`

```xml
<listeners>
    <listener class-name="com.learning.tests.listeners.FrameworkTestListener"/>
    <listener class-name="com.learning.tests.listeners.RetryAnnotationTransformer"/>
</listeners>
```

This keeps the tests clean. `SauceDemoPageObjectTest` and
`SauceDemoDataDrivenTest` do not need listener code inside each test method.

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

## Selenium Or Framework Nuances

The listener captures screenshots in `onTestFailure`, before `BaseTest` quits
the browser in `@AfterMethod`. That timing matters. After `quit()`, the browser
session no longer exists and Selenium cannot capture the page state.

The listener does not own driver creation. It asks `DriverFactory.getDriver()`
for the current test thread's browser. This keeps one owner for browser
lifecycle and avoids hidden driver creation inside diagnostics.

## Common Mistakes

- Registering a listener but running a command that does not use the suite XML.
  The module listener registration is in `testng.xml`, so use
  `mvn test -DsuiteXmlFile=testng.xml` when verifying listener behavior.
- Taking screenshots in `@AfterMethod` after the driver has already quit.
- Logging full data-provider objects. Module 13 avoids this for
  `LoginScenario` because the record contains a password field.
- Adding assertion logic inside the listener. Assertions belong in tests.

## Interview Readiness

Strong answer:

"A TestNG listener is a callback class that reacts to test lifecycle events.
In a Selenium framework I use it for cross-cutting diagnostics such as logging
test start/pass/fail and taking screenshots on failure. This keeps tests
focused on business assertions while framework behavior stays centralized."

Be ready to explain why listener code is framework code, not page-object code.

## How This Connects To Later Framework Design

Module 14 will use the same listener position to attach screenshots and status
details to Extent and Allure reports. Module 15 will make the ThreadLocal driver
and per-test log context more important when tests run in parallel.

## Revision Checklist

- Can you explain why a listener is better than adding screenshot code to every
  test method?
- Can you identify which listener method captures screenshots?
- Can you explain why screenshot capture must happen before driver cleanup?
- Can you show where the listener is registered?

