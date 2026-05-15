# DriverFactory

## Files In This Topic

This topic reads these files:

- [src/main/java/com/learning/framework/driver/DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java)
- [src/main/java/com/learning/framework/config/ConfigReader.java](../../src/main/java/com/learning/framework/config/ConfigReader.java)
- [src/test/java/com/learning/tests/base/BaseTest.java](../../src/test/java/com/learning/tests/base/BaseTest.java)


## Why DriverFactory Exists

`BaseTest` should coordinate test lifecycle. It should not grow into a large
browser-construction class.

`DriverFactory` now owns:

- browser selection.
- browser options.
- headless mode.
- window size.
- page-load timeout.
- implicit wait timeout.
- driver storage.
- driver cleanup.

## Mental Model

[DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java)
is the only class that should construct Selenium browser drivers in the
framework.

```text
BaseTest says: create a driver for this test method
        |
        v
DriverFactory reads browser settings from ConfigReader
        |
        v
DriverFactory creates Chrome/Firefox/Edge with configured options
        |
        v
DriverFactory stores the driver in ThreadLocal
        |
        v
BaseTest retrieves it and builds waits/actions
```

This keeps test lifecycle and browser construction separate. `BaseTest` knows
when a test needs a browser. `DriverFactory` knows how to create that browser.

## Browser Selection

The selected browser comes from config:

```java
switch (ConfigReader.getBrowser()) {
    case "chrome" -> createChromeDriver();
    case "firefox" -> createFirefoxDriver();
    case "edge" -> createEdgeDriver();
}
```

Default config uses Chrome because that is the known verified browser in this
project. Firefox and Edge support are added as framework options, but they are
not part of the default quality gate unless those browsers are installed.

The browser switch is intentionally explicit. If a learner runs:

```bash
mvn test -Dtest=SauceDemoPageObjectTest -Dbrowser=safari
```

the factory throws an unsupported-browser error. That is better than silently
falling back to Chrome because a false fallback makes the run configuration
look like it worked when it did not.

## Headless Mode

Headless mode also comes from config:

```java
if (ConfigReader.isHeadless()) {
    options.addArguments("--headless=new");
}
```

This lets local learners run headed when they want to watch the browser:

```bash
mvn test -Dtest=SauceDemoPageObjectTest -Dheadless=false
```

Chrome and Edge use `--headless=new` at this checkpoint. Firefox uses
`--headless`. The difference is browser-specific option syntax, which is why
browser option creation belongs in `DriverFactory` instead of `BaseTest`.

## Window Size

Chrome and Edge receive window size as a startup argument:

```java
options.addArguments(windowSizeArgument());
```

Firefox uses Selenium's window API after the driver starts:

```java
driver.manage().window().setSize(configuredWindowSize());
```

That difference is another reason to keep browser construction behind the
factory. Test classes should not need to remember which browser accepts which
option style.

## Timeouts

Module 11 separates timeout ownership:

- page-load timeout is applied to the driver.
- implicit wait is applied to the driver and defaults to `0`.
- explicit wait timeout feeds `WebDriverWait` in `BaseTest`.

The framework still prefers explicit waits. Keeping implicit wait at zero
avoids confusing timing interactions.

Module 11 uses three timeout concepts:

| Timeout | Config Key | Applied In | Meaning |
| --- | --- | --- | --- |
| page-load timeout | `pageLoadTimeoutSeconds` | [DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java) | maximum time Selenium waits for page navigation to finish |
| implicit wait | `implicitWaitSeconds` | [DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java) | fallback polling time for `findElement`; default is zero |
| explicit wait | `explicitWaitSeconds` | [BaseTest.java](../../src/test/java/com/learning/tests/base/BaseTest.java) | timeout for `WebDriverWait`, used by `WaitUtils` and page objects |

The separation matters. Page-load timeout is about navigation. Explicit waits
are about a specific UI condition. Implicit waits are broad and can make timing
failures harder to reason about, so this project keeps them disabled by
default.

## ThreadLocal

`DriverFactory` stores the driver in:

```java
private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
```

`ThreadLocal` means each execution thread can have its own driver. This does
not make the framework parallel by itself. It prepares the driver layer for
Module 15, where TestNG parallel execution and Selenium Grid are introduced.

Without `ThreadLocal`, a static driver field would be shared by all tests in
the JVM. That becomes dangerous when parallel execution arrives because one
test could overwrite or quit another test's browser. `ThreadLocal` stores a
separate value for each executing thread.

Current Module 11 runs are still effectively serial. `ThreadLocal` is added now
because the driver lifecycle is being designed, not because the suite is already
parallel.

## Lifecycle Methods

```java
createDriver()
getDriver()
quitDriver()
```

These methods give the framework one official path for driver lifecycle.

`quitDriver()` calls `DRIVER.remove()` after quitting. That cleanup matters in
long-running test processes because the thread should not keep a reference to a
closed browser.

## Code Walkthrough

```java
if (DRIVER.get() != null) {
    return;
}
```

`createDriver()` avoids creating a second browser if the current thread already
has one. In this TestNG setup, `BaseTest` creates one driver per test method
and quits it in `@AfterMethod`.

```java
WebDriver driver = switch (ConfigReader.getBrowser()) {
    case "chrome" -> createChromeDriver();
    case "firefox" -> createFirefoxDriver();
    case "edge" -> createEdgeDriver();
    default -> throw new IllegalArgumentException(...);
};
```

The Java switch expression returns the created `WebDriver`. This keeps browser
selection readable and makes unsupported values fail clearly.

```java
driver.manage().timeouts().pageLoadTimeout(...)
driver.manage().timeouts().implicitlyWait(...)
```

These are driver-level timeout settings. They belong in the factory because
they should apply to the browser session itself, not to a specific page object.

```java
DRIVER.set(driver);
```

The created browser is stored for the current thread. [BaseTest.java](../../src/test/java/com/learning/tests/base/BaseTest.java)
then calls `DriverFactory.getDriver()` to receive the same browser instance.

```java
DRIVER.remove();
```

After `quit()`, the thread-local value is cleared. This avoids keeping a stale
reference to a closed browser.

## Common Beginner Mistakes

- creating browsers directly in page objects.
- creating browsers directly in every test class after a factory exists.
- using `ThreadLocal` but forgetting to remove it.
- setting implicit waits to a large value and also using explicit waits.
- adding remote/grid logic before local driver lifecycle is stable.
- making `DriverFactory` know about SauceDemo pages or test assertions.
- using `Thread.sleep(...)` in the factory to "stabilize" browser creation.
- assuming Firefox and Edge are verified just because the factory has creation
  branches for them.

## Interview Readiness

A strong answer:

`DriverFactory` owns browser construction, configured browser options,
driver-level timeouts, thread-local storage, and cleanup. `BaseTest` still owns
when to create and quit the browser through the TestNG lifecycle. `ThreadLocal`
is introduced to prepare for future parallel execution, but it does not make
the suite parallel by itself.

## Revision Checklist

- Can you explain why `DriverFactory` uses `ConfigReader.getBrowser()`?
- Can you explain why unsupported browser values fail loudly?
- Can you explain why `DRIVER.remove()` matters after `quit()`?
- Can you distinguish page-load timeout, implicit wait, and explicit wait?
