# Module 11 Exercises

Use these exercises after reading:

- [config.properties](../../src/test/resources/config/config.properties)
- [ConfigReader.java](../../src/main/java/com/learning/framework/config/ConfigReader.java)
- [DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java)
- [BaseTest.java](../../src/test/java/com/learning/tests/base/BaseTest.java)
- [LoginPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java)

## Exercise 1 - Run Headed Locally

Run the SauceDemo framework tests with a visible browser.

Command:

```bash
mvn test -Dtest=SauceDemoPageObjectTest -Dheadless=false
```

Expected outcome:

- the same tests pass.
- Chrome opens visibly.
- no source file is edited.

Revision question:

- why is changing `-Dheadless=false` better than editing
  [config.properties](../../src/test/resources/config/config.properties) for a
  one-off local run?

## Exercise 2 - Add A Config Getter

Add a typed getter for a new config value named `retryCount`.

Hint:

- follow the style of `getBaseUrl()` and `isHeadless()`.
- internally call `getInt(...)`.

Expected outcome:

- framework code can call a named method instead of passing a raw string key.
- the parsing logic stays inside [ConfigReader.java](../../src/main/java/com/learning/framework/config/ConfigReader.java).

Revision question:

- should `retryCount` belong in framework config, test data, or a later
  reporting/listener module? Explain the ownership.

## Exercise 3 - Explain ThreadLocal

Write a short explanation of why `ThreadLocal<WebDriver>` is useful for future
parallel execution.

Expected outcome:

- the explanation mentions one driver per thread.
- the explanation also says ThreadLocal does not automatically make tests
  parallel.
- the explanation mentions cleanup with `DRIVER.remove()` in
  [DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java).

## Exercise 4 - Try An Invalid Browser

Run:

```bash
mvn test -Dtest=SauceDemoPageObjectTest -Dbrowser=safari
```

Expected outcome:

- the framework fails clearly with an unsupported-browser message.
- this is better than silently falling back to Chrome.

## Exercise 5 - Trace The Base URL

Trace how `baseUrl` gets from configuration into browser navigation.

Hint:

- start in [config.properties](../../src/test/resources/config/config.properties).
- find the typed getter in [ConfigReader.java](../../src/main/java/com/learning/framework/config/ConfigReader.java).
- find where [LoginPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java)
  calls the getter.

Expected outcome:

- you can explain why `LoginPage.open()` no longer hardcodes
  `https://www.saucedemo.com/`.

## Exercise 6 - Compare Timeout Types

Write a short explanation of these three values:

- `explicitWaitSeconds`.
- `pageLoadTimeoutSeconds`.
- `implicitWaitSeconds`.

Hint:

- one is used in [BaseTest.java](../../src/test/java/com/learning/tests/base/BaseTest.java).
- two are applied in [DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java).

Expected outcome:

- the answer distinguishes page navigation, explicit UI conditions, and broad
  implicit element lookup waits.
- the answer explains why this framework keeps implicit wait at zero.

## Exercise 7 - Explain The Responsibility Boundary

For each item, decide whether it belongs in `ConfigReader`, `DriverFactory`,
`BaseTest`, or a Page Object:

- read `browser=chrome`.
- create `ChromeOptions`.
- call `@BeforeMethod`.
- open the SauceDemo login URL.
- quit and remove the current thread's driver.
- create `ElementActions`.

Expected outcome:

- config reads belong in `ConfigReader`.
- browser construction and cleanup belong in `DriverFactory`.
- TestNG lifecycle and service assembly belong in `BaseTest`.
- page navigation belongs in the relevant Page Object.
