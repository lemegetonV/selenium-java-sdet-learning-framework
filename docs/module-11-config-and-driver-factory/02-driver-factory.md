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

## Timeouts

Module 11 separates timeout ownership:

- page-load timeout is applied to the driver.
- implicit wait is applied to the driver and defaults to `0`.
- explicit wait timeout feeds `WebDriverWait` in `BaseTest`.

The framework still prefers explicit waits. Keeping implicit wait at zero
avoids confusing timing interactions.

## ThreadLocal

`DriverFactory` stores the driver in:

```java
private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
```

`ThreadLocal` means each execution thread can have its own driver. This does
not make the framework parallel by itself. It prepares the driver layer for
Module 15, where TestNG parallel execution and Selenium Grid are introduced.

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

## Common Beginner Mistakes

- creating browsers directly in page objects.
- creating browsers directly in every test class after a factory exists.
- using `ThreadLocal` but forgetting to remove it.
- setting implicit waits to a large value and also using explicit waits.
- adding remote/grid logic before local driver lifecycle is stable.
