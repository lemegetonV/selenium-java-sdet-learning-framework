# Module 11 Interview Review

## What You Must Be Able To Explain

Module 11 introduces configuration and driver factory design. You should be
able to explain:

- why config values moved out of source code.
- how `System.getProperty` overrides `config.properties`.
- what `ConfigReader` owns.
- what `DriverFactory` owns.
- why `BaseTest` still exists.
- why `ThreadLocal<WebDriver>` is introduced before parallel execution.
- why implicit wait defaults to zero.
- how headless mode is controlled.
- how this prepares for CI and Selenium Grid later.
- how `baseUrl` reaches `LoginPage.open()`.
- why `BaseTest`, `DriverFactory`, and `ConfigReader` should stay separate.

## Strong Answers

**Why use ConfigReader?**

To centralize configuration and avoid hardcoded browser, URL, timeout, and
window-size values in framework code.

[ConfigReader.java](../../src/main/java/com/learning/framework/config/ConfigReader.java)
also gives the framework typed accessors, so code calls `getBrowser()`,
`isHeadless()`, and `getExplicitWaitSeconds()` instead of parsing raw strings
in multiple places.

**What is override precedence?**

Runtime system properties win over file defaults. For example,
`-Dheadless=false` overrides `headless=true` in `config.properties`.

The default file is [config.properties](../../src/test/resources/config/config.properties).
It is loaded from the test classpath, not from a hardcoded absolute path.

**Why use DriverFactory?**

To keep browser creation and cleanup in one framework service instead of
spreading `new ChromeDriver(...)` across tests or `BaseTest`.

[DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java)
also owns browser-specific options, driver-level timeouts, `ThreadLocal`
storage, and `quitDriver()` cleanup.

**Why introduce ThreadLocal now?**

It prepares the driver layer for parallel execution. Each thread can hold its
own driver. Actual parallel execution is still deferred to Module 15.

**Should page objects create drivers?**

No. Page objects receive framework services and model page behavior. Browser
lifecycle belongs to `BaseTest` and `DriverFactory`.

**Why does `BaseTest` still exist?**

[BaseTest.java](../../src/test/java/com/learning/tests/base/BaseTest.java)
coordinates the TestNG lifecycle. It decides when each test method creates and
quits a browser, then assembles `WebDriverWait`, `WaitUtils`, and
`ElementActions`.

**How does `LoginPage` avoid a hardcoded URL now?**

[LoginPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java)
calls `ConfigReader.getBaseUrl()` inside `open()`. The URL comes from either
the Maven/JVM override or the default config file.

## Code Lines To Revise

```java
String overrideValue = System.getProperty(key);
```

Reads runtime overrides from Maven/JVM properties.

```java
private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
```

Stores one driver per execution thread.

```java
DriverFactory.createDriver();
driver = DriverFactory.getDriver();
```

`BaseTest` coordinates driver lifecycle without constructing the browser
directly.

```java
driver.get(ConfigReader.getBaseUrl());
```

Page navigation uses externalized environment configuration.

```java
driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getImplicitWaitSeconds()));
```

The factory applies implicit wait at driver level. The default is zero because
this framework prefers explicit waits through `WaitUtils`.

```java
DRIVER.remove();
```

After quitting the browser, the factory clears the thread-local reference so a
closed driver is not reused by the same thread later.

## Common Interview Traps

- saying `ThreadLocal` makes tests parallel by itself.
- putting test data and credentials into driver config without a strategy.
- using large implicit waits together with explicit waits.
- making `ConfigReader` create browsers.
- making `DriverFactory` know page objects.
- hardcoding URLs again inside page classes.
- treating `ThreadLocal` as a substitute for TestNG parallel configuration.
- putting passwords or secrets into committed config files.
- changing Java source just to switch headless mode for one run.

## Debugging Questions

If Module 11 setup fails, ask:

- Is [config.properties](../../src/test/resources/config/config.properties)
  present under `src/test/resources/config/`?
- Did Maven copy test resources before the test started?
- Is the config key missing, blank, or non-numeric where an integer is
  expected?
- Did a `-D` override accidentally supply an unsupported browser value?
- Did [DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java)
  call `DRIVER.remove()` during cleanup?
- Is [BaseTest.java](../../src/test/java/com/learning/tests/base/BaseTest.java)
  still creating wrapper services after retrieving the driver?

## Framework Phase Bridge

Module 12 can now introduce data-driven testing because the framework has a
stable browser lifecycle, page-object layer, wrapper action layer, and external
configuration layer.

## One-Minute Whiteboard Answer

Module 11 separates configuration, browser construction, and test lifecycle.
[ConfigReader.java](../../src/main/java/com/learning/framework/config/ConfigReader.java)
reads runtime settings with Maven/JVM overrides taking priority over
[config.properties](../../src/test/resources/config/config.properties).
[DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java)
uses those values to create the selected browser, apply browser options and
driver-level timeouts, store the driver in `ThreadLocal`, and quit it cleanly.
[BaseTest.java](../../src/test/java/com/learning/tests/base/BaseTest.java)
still coordinates TestNG setup/teardown and creates wait/action services for
tests.
