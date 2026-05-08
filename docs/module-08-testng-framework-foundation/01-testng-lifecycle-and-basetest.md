# TestNG Lifecycle and BaseTest

## Files In This Topic

```text
src/test/java/com/learning/tests/base/BaseTest.java
src/test/java/com/learning/tests/saucedemo/LoginFoundationTest.java
```

## The Problem From Earlier Modules

Raw Selenium tests created and closed their own browser:

```java
WebDriver driver = new ChromeDriver(options);
try {
    driver.get("https://www.saucedemo.com/");
} finally {
    driver.quit();
}
```

That was useful while learning Selenium commands. It becomes expensive once
many tests need the same setup. Module 08 moves that lifecycle into
`BaseTest`.

## TestNG Lifecycle Order

For each test method in `LoginFoundationTest`, TestNG now runs:

```text
@BeforeClass      once before the class
@BeforeMethod     before each @Test
@Test             the actual test method
@AfterMethod      after each @Test
@AfterClass       once after the class
```

In this module:

- `@BeforeClass` prepares class-level data such as the SauceDemo URL.
- `@BeforeMethod` creates a fresh browser in `BaseTest`.
- `@Test` performs one test scenario.
- `@AfterMethod` quits the browser in `BaseTest`.
- `@AfterClass` clears class-level data.

## Why Browser Setup Uses `@BeforeMethod`

`BaseTest` uses:

```java
@BeforeMethod(alwaysRun = true)
public void setUpBrowser() {
    driver = new ChromeDriver(options);
    wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
}
```

This means every test method gets a new browser session.

Benefits:

- a failed test does not leave login state for the next test.
- cookies and local storage are isolated.
- one test cannot accidentally depend on a previous test's page.
- failures are easier to diagnose because the starting state is predictable.

Tradeoff:

- test execution is slower because each test starts a browser.

That tradeoff is acceptable for the first framework layer. Later modules can
discuss performance and parallel execution after the lifecycle is reliable.

## Why Cleanup Uses `@AfterMethod`

`BaseTest` uses:

```java
@AfterMethod(alwaysRun = true)
public void tearDownBrowser() {
    if (driver != null) {
        driver.quit();
    }
}
```

The `alwaysRun = true` setting matters because cleanup should still happen
when a test fails or a group configuration changes.

`quit()` is also intentional:

- `close()` closes the current tab or window.
- `quit()` ends the full WebDriver session and closes all browser windows that
  belong to it.

Framework code should avoid leaving browser processes behind.

## Why `wait` Lives Beside `driver`

Module 05 taught explicit waits. Module 08 makes one `WebDriverWait` available
to child tests:

```java
protected WebDriverWait wait;
```

That avoids recreating a wait object in each test method, while keeping the raw
wait API visible. Module 10 will later centralize waits inside wrapper actions
so page and test classes do less low-level waiting.

## Java and OOP Concepts

`LoginFoundationTest` extends `BaseTest`:

```java
public class LoginFoundationTest extends BaseTest
```

This is inheritance. The child class receives the setup and cleanup behavior
defined in the parent class.

`driver` and `wait` are marked `protected`:

```java
protected WebDriver driver;
protected WebDriverWait wait;
```

`protected` means:

- `BaseTest` can use the fields.
- child classes such as `LoginFoundationTest` can use the fields.
- unrelated classes in other packages cannot treat them as public global state.

This is a learning-stage compromise. Later framework modules may reduce direct
driver usage in page classes by routing actions through wrapper methods.

## Common Beginner Mistakes

- using `@BeforeClass` for browser setup, then accidentally sharing browser
  state across test methods.
- forgetting `alwaysRun = true` on cleanup methods.
- calling `driver.quit()` inside a child test even though `BaseTest` already
  owns cleanup.
- making `driver` public.
- putting SauceDemo locators into `BaseTest`.

## Framework Bridge

`BaseTest` should stay application-neutral. It can know how to create and
clean a browser. It should not know:

- SauceDemo usernames.
- SauceDemo locators.
- checkout steps.
- product names.
- page assertions.

That separation keeps the framework layer reusable.
