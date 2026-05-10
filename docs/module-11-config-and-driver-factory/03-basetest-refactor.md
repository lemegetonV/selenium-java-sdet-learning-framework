# BaseTest Refactor

## Files In This Topic

```text
src/test/java/com/learning/tests/base/BaseTest.java
src/main/java/com/learning/framework/driver/DriverFactory.java
src/main/java/com/learning/framework/config/ConfigReader.java
```

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

## What Stayed In BaseTest

`BaseTest` still owns:

- TestNG `@BeforeMethod`.
- TestNG `@AfterMethod`.
- protected test fields.
- wrapper service creation.
- per-test setup and cleanup sequence.

That is still lifecycle coordination, not browser construction.

## What Moved Out

`BaseTest` no longer owns:

- `ChromeOptions`.
- `new ChromeDriver(...)`.
- hardcoded headless setting.
- hardcoded window size.
- driver page-load timeout.
- implicit wait timeout.

Those are now `DriverFactory` and `ConfigReader` responsibilities.

## Why This Separation Matters

This separation lets future modules change browser behavior without touching
every test class.

For example:

- Module 12 can add data-driven tests without changing driver setup.
- Module 13 can add listeners and screenshots around the same driver lifecycle.
- Module 15 can add parallel execution using the existing `ThreadLocal` driver
  design.

## Interview Nuance

`BaseTest` is still useful after `DriverFactory` exists. The factory creates
and stores drivers. `BaseTest` decides when a test should create and quit a
driver in the TestNG lifecycle.
