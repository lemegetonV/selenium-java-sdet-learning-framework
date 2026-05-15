# BaseTest Refactor

## Files In This Topic

This topic reads these files:

- [src/test/java/com/learning/tests/base/BaseTest.java](../../src/test/java/com/learning/tests/base/BaseTest.java)
- [src/main/java/com/learning/framework/driver/DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java)
- [src/main/java/com/learning/framework/config/ConfigReader.java](../../src/main/java/com/learning/framework/config/ConfigReader.java)


## What Changed

Before Module 11, `BaseTest` created Chrome directly.

After Module 11, `BaseTest` asks `DriverFactory` for a driver:

```java
DriverFactory.createDriver();
driver = DriverFactory.getDriver();
```

Then it creates explicit wait and wrapper services:

```java
wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWaitSeconds()));
waits = new WaitUtils(wait);
elementActions = new ElementActions(driver, waits);
```

## Mental Model

[BaseTest.java](../../src/test/java/com/learning/tests/base/BaseTest.java)
is still the TestNG lifecycle coordinator.

It does not know how to build Chrome options anymore. It does know this
sequence:

```text
Before each test method:
    create browser through DriverFactory
    retrieve the driver
    create WebDriverWait from configured explicit timeout
    create WaitUtils and ElementActions

After each test method:
    quit the browser through DriverFactory
    clear protected fields
```

That is the correct responsibility boundary. `BaseTest` coordinates the test
method lifecycle; [DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java)
handles browser construction and cleanup details.

## What Stayed In BaseTest

`BaseTest` still owns:

- TestNG `@BeforeMethod`.
- TestNG `@AfterMethod`.
- protected test fields.
- wrapper service creation.
- per-test setup and cleanup sequence.

That is still lifecycle coordination, not browser construction.

`BaseTest` also remains the place where framework services are assembled for
tests:

- `driver` comes from `DriverFactory`.
- `wait` is built using `ConfigReader.getExplicitWaitSeconds()`.
- `waits` wraps `WebDriverWait`.
- `elementActions` wraps `driver` and `WaitUtils`.

This gives child tests one consistent set of protected services without making
each test class repeat setup code.

## What Moved Out

`BaseTest` no longer owns:

- `ChromeOptions`.
- `new ChromeDriver(...)`.
- hardcoded headless setting.
- hardcoded window size.
- driver page-load timeout.
- implicit wait timeout.

Those are now `DriverFactory` and `ConfigReader` responsibilities.

## Code Walkthrough

```java
DriverFactory.createDriver();
driver = DriverFactory.getDriver();
```

The first line asks the factory to create a browser for the current thread. The
second line retrieves that browser so the rest of the test framework can use
it.

```java
wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWaitSeconds()));
```

The explicit wait timeout is no longer hardcoded in `BaseTest`. The value comes
from [ConfigReader.java](../../src/main/java/com/learning/framework/config/ConfigReader.java),
which can read either [config.properties](../../src/test/resources/config/config.properties)
or a Maven `-DexplicitWaitSeconds=...` override.

```java
waits = new WaitUtils(wait);
elementActions = new ElementActions(driver, waits);
```

Module 10's wrapper services still exist. Module 11 changes how the driver and
timeout are obtained before those services are created.

```java
DriverFactory.quitDriver();
driver = null;
wait = null;
waits = null;
elementActions = null;
```

`quitDriver()` closes and removes the browser from `ThreadLocal`. Clearing the
protected fields makes it obvious that the old test-method state should not be
used after teardown.

## Why This Separation Matters

This separation lets future modules change browser behavior without touching
every test class.

For example:

- Module 12 can add data-driven tests without changing driver setup.
- Module 13 can add listeners and screenshots around the same driver lifecycle.
- Module 15 can add parallel execution using the existing `ThreadLocal` driver
  design.

It also reduces the blast radius of browser changes. If the project needs a new
Chrome option for CI, the change belongs in
[DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java),
not in every page object or every test class.

## Interview Nuance

`BaseTest` is still useful after `DriverFactory` exists. The factory creates
and stores drivers. `BaseTest` decides when a test should create and quit a
driver in the TestNG lifecycle.

## Common Mistakes

- Deleting `BaseTest` because `DriverFactory` exists. They solve different
  problems.
- Creating `WebDriverWait` inside every page object instead of once during test
  setup.
- Letting tests call `DriverFactory.createDriver()` directly in every test
  method.
- Forgetting that `@AfterMethod(alwaysRun = true)` protects cleanup even when a
  test fails.
- Keeping old `ChromeOptions` setup in `BaseTest` after the factory exists.

## Revision Checklist

- Can you explain what remains in `BaseTest` after Module 11?
- Can you explain why `BaseTest` reads explicit wait seconds but not browser
  name directly?
- Can you trace `setUpBrowser()` from `DriverFactory.createDriver()` to
  `ElementActions` creation?
- Can you explain why teardown clears both the driver factory state and local
  protected fields?
