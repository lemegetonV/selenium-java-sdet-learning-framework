# ConfigReader

## Files In This Topic

```text
src/main/java/com/learning/framework/config/ConfigReader.java
src/test/resources/config/config.properties
src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java
```

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

## Override Precedence

`ConfigReader.get(...)` checks values in this order:

1. JVM system property, such as `-Dheadless=false`.
2. value from `config/config.properties`.

That means this command:

```bash
mvn test -Dtest=SauceDemoPageObjectTest -Dheadless=false
```

uses the file for every value except `headless`.

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

## Why The Config File Is Under Test Resources

This learning project is a test framework. The runtime config lives under:

```text
src/test/resources/config/config.properties
```

Maven copies it to the test classpath during `mvn test`, so `ConfigReader` can
load it with the class loader.

## Framework Boundary

`ConfigReader` reads values. It should not:

- create browsers.
- know SauceDemo page objects.
- start tests.
- decide assertions.

Those responsibilities belong to other layers.
