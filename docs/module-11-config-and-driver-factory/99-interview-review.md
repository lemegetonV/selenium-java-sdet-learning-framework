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

## Strong Answers

**Why use ConfigReader?**

To centralize configuration and avoid hardcoded browser, URL, timeout, and
window-size values in framework code.

**What is override precedence?**

Runtime system properties win over file defaults. For example,
`-Dheadless=false` overrides `headless=true` in `config.properties`.

**Why use DriverFactory?**

To keep browser creation and cleanup in one framework service instead of
spreading `new ChromeDriver(...)` across tests or `BaseTest`.

**Why introduce ThreadLocal now?**

It prepares the driver layer for parallel execution. Each thread can hold its
own driver. Actual parallel execution is still deferred to Module 15.

**Should page objects create drivers?**

No. Page objects receive framework services and model page behavior. Browser
lifecycle belongs to `BaseTest` and `DriverFactory`.

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

## Common Interview Traps

- saying `ThreadLocal` makes tests parallel by itself.
- putting test data and credentials into driver config without a strategy.
- using large implicit waits together with explicit waits.
- making `ConfigReader` create browsers.
- making `DriverFactory` know page objects.
- hardcoding URLs again inside page classes.

## Framework Phase Bridge

Module 12 can now introduce data-driven testing because the framework has a
stable browser lifecycle, page-object layer, wrapper action layer, and external
configuration layer.
