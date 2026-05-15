# ConfigReader

## Files In This Topic

This topic reads these files:

- [src/main/java/com/learning/framework/config/ConfigReader.java](../../src/main/java/com/learning/framework/config/ConfigReader.java)
- [src/test/resources/config/config.properties](../../src/test/resources/config/config.properties)
- [src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java)


## Why ConfigReader Exists

Hardcoded framework values make tests harder to run in different environments.

Module 11 moves these values into `config.properties`:

```properties
browser=chrome
headless=true
baseUrl=https://www.saucedemo.com/
explicitWaitSeconds=10
pageLoadTimeoutSeconds=30
implicitWaitSeconds=0
windowWidth=1440
windowHeight=900
```

Now the framework can use defaults while still allowing command-line overrides.

## Mental Model

[ConfigReader.java](../../src/main/java/com/learning/framework/config/ConfigReader.java)
is the framework's single read path for runtime settings.

```text
Java code asks: ConfigReader.getBrowser()
        |
        v
ConfigReader checks: System.getProperty("browser")
        |
        v
If no override exists: read browser from config.properties
        |
        v
Return a trimmed typed value to framework code
```

This keeps configuration decisions out of page objects, tests, and driver
creation code. Those classes should ask for a named value instead of knowing
where the value came from.

## Override Precedence

`ConfigReader.get(...)` checks values in this order:

1. JVM system property, such as `-Dheadless=false`.
2. value from `config/config.properties`.

That means this command:

```bash
mvn test -Dtest=SauceDemoPageObjectTest -Dheadless=false
```

uses the file for every value except `headless`.

Another example:

```bash
mvn test -Dtest=SauceDemoPageObjectTest -Dbrowser=chrome -Dheadless=true
```

Here `browser` and `headless` come from the Maven command, while `baseUrl`,
timeouts, and window size still come from
[config.properties](../../src/test/resources/config/config.properties).

This is useful because the repository can keep stable defaults while CI or a
local learner changes only the values needed for that run.

## Typed Getters

`ConfigReader` exposes typed methods:

```java
ConfigReader.getBrowser()
ConfigReader.isHeadless()
ConfigReader.getBaseUrl()
ConfigReader.getExplicitWaitSeconds()
ConfigReader.getPageLoadTimeoutSeconds()
```

Typed getters matter because framework code should not parse booleans and
integers all over the project.

## Code Walkthrough

```java
private static final String CONFIG_FILE = "config/config.properties";
```

The path is a classpath path, not a file-system path from the project root.
Maven copies [config.properties](../../src/test/resources/config/config.properties)
from `src/test/resources` into the test classpath before tests run.

```java
private static final Properties PROPERTIES = loadProperties();
```

This loads the properties file once when `ConfigReader` is first used. That is
simple and efficient for this module. It also means changing the file while a
test JVM is already running will not reload values.

```java
String overrideValue = System.getProperty(key);
if (overrideValue != null && !overrideValue.isBlank()) {
    return overrideValue.trim();
}
```

This is the override gate. Maven `-D` values become JVM system properties, so
the framework can change behavior without editing committed files.

```java
String fileValue = PROPERTIES.getProperty(key);
if (fileValue == null || fileValue.isBlank()) {
    throw new IllegalArgumentException("Missing configuration value for key: " + key);
}
```

Missing config fails clearly. That is better than silently using a hidden
default because a learner can immediately see which key is missing.

```java
return get("browser").toLowerCase();
```

`getBrowser()` normalizes browser names so `Chrome`, `CHROME`, and `chrome`
can all resolve to `chrome` after the value is read.

```java
return Boolean.parseBoolean(get("headless"));
```

Boolean parsing is intentionally centralized. One nuance: Java returns `false`
for any value other than case-insensitive `"true"`. In a stricter future
framework, you might validate boolean values explicitly.

```java
return Integer.parseInt(value);
```

`getInt(...)` turns string values into integers and wraps bad numeric values in
an `IllegalArgumentException` that names the bad key.

## Java Syntax To Notice

```java
private static final Properties PROPERTIES = loadProperties();
```

This loads the file once when `ConfigReader` is first used.

```java
try (InputStream inputStream = ...)
```

This is try-with-resources. Java automatically closes the stream after the
block finishes.

```java
throw new IllegalArgumentException(...)
```

Invalid or missing configuration should fail clearly. Silent defaults can make
framework behavior confusing.

## Values Used In This Module

| Key | Current Default | Used By | Purpose |
| --- | --- | --- | --- |
| `browser` | `chrome` | [DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java) | selects Chrome, Firefox, or Edge creation path |
| `headless` | `true` | [DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java) | chooses headed or headless browser options |
| `baseUrl` | `https://www.saucedemo.com/` | [LoginPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java) | opens the AUT without hardcoding URL in the page object |
| `explicitWaitSeconds` | `10` | [BaseTest.java](../../src/test/java/com/learning/tests/base/BaseTest.java) | creates `WebDriverWait` duration |
| `pageLoadTimeoutSeconds` | `30` | [DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java) | caps full page-load waits at driver level |
| `implicitWaitSeconds` | `0` | [DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java) | keeps implicit wait disabled by default |
| `windowWidth` | `1440` | [DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java) | browser viewport width |
| `windowHeight` | `900` | [DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java) | browser viewport height |

## Why The Config File Is Under Test Resources

This learning project is a test framework. The runtime config lives at
[src/test/resources/config/config.properties](../../src/test/resources/config/config.properties).

Maven copies it to the test classpath during `mvn test`, so `ConfigReader` can
load it with the class loader.

## Framework Boundary

`ConfigReader` reads values. It should not:

- create browsers.
- know SauceDemo page objects.
- start tests.
- decide assertions.

Those responsibilities belong to other layers.

## Common Mistakes

- Reading `System.getProperty(...)` directly in page objects or tests instead
  of going through `ConfigReader`.
- Hiding missing keys with undocumented fallback values.
- Treating `src/test/resources/config/config.properties` as a production
  secret store. This file is committed and should not contain real secrets.
- Parsing integers and booleans throughout the framework instead of using
  typed getters.
- Putting test data, such as usernames and product names, into driver config
  without a clear data strategy. Module 12 handles test data separately.

## Interview Readiness

A strong answer:

`ConfigReader` centralizes runtime settings. It first checks JVM system
properties, which Maven passes through `-D`, then falls back to
`config.properties` on the test classpath. It exposes typed getters so the rest
of the framework asks for `getBrowser()`, `isHeadless()`, or
`getExplicitWaitSeconds()` instead of parsing raw strings everywhere.

## Revision Checklist

- Can you explain why `CONFIG_FILE` is loaded from the classpath?
- Can you explain what happens when `-Dheadless=false` is supplied?
- Can you identify every Module 11 config key and where it is used?
- Can you explain why missing config should fail loudly?
