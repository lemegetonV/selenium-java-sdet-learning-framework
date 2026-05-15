# TestNG Lifecycle and BaseTest

## Files In This Topic

This topic reads these files:

- [src/test/java/com/learning/tests/base/BaseTest.java](../../src/test/java/com/learning/tests/base/BaseTest.java)
- [src/test/java/com/learning/tests/saucedemo/LoginFoundationTest.java](../../src/test/java/com/learning/tests/saucedemo/LoginFoundationTest.java)


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

The important learning shift is this:

```text
Before Module 08:
each test method knows how to create Chrome, create a wait, run the scenario,
and quit the browser.

After Module 08:
BaseTest owns browser lifecycle; LoginFoundationTest owns SauceDemo behavior.
```

This is the first time the project separates framework setup from application
test logic. That separation is small, but every later framework layer depends
on it.

## Source Reading Order

Read the files in this order:

1. [src/test/java/com/learning/tests/base/BaseTest.java](../../src/test/java/com/learning/tests/base/BaseTest.java)
2. [src/test/java/com/learning/tests/saucedemo/LoginFoundationTest.java](../../src/test/java/com/learning/tests/saucedemo/LoginFoundationTest.java)
3. [testng.xml](../../testng.xml)
4. [pom.xml](../../pom.xml)

Start with `BaseTest` because it explains where `driver` and `wait` come from.
Then read `LoginFoundationTest` and notice that it uses those inherited fields
without creating the browser itself.

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

For the `standardUserCanReachProductsPage` test, the real execution story is:

```text
TestNG creates LoginFoundationTest object
        |
        v
@BeforeClass setUpClassData()
        |
        v
@BeforeMethod BaseTest.setUpBrowser()
        |
        v
@Test standardUserCanReachProductsPage()
        |
        v
@AfterMethod BaseTest.tearDownBrowser()
```

For the second test method, TestNG runs another `@BeforeMethod` and another
`@AfterMethod`. The same Java test class instance can continue, but the browser
session is new for each test method.

## Where `driver` And `wait` Come From

`LoginFoundationTest` never declares its own `driver` field. It receives the
field through inheritance:

```java
public class LoginFoundationTest extends BaseTest
```

`BaseTest` declares:

```java
protected WebDriver driver;
protected WebDriverWait wait;
```

The child class can then call:

```java
driver.get(loginUrl);
wait.until(ExpectedConditions.urlContains("/inventory.html"));
```

That works because `protected` fields are visible to subclasses. The test class
is not using magic global state; it is using inherited state prepared by
`BaseTest.setUpBrowser()`.

If `LoginFoundationTest` did not extend `BaseTest`, this code would not
compile because `driver` and `wait` would not exist in the child class.

## Why Browser Setup Uses `@BeforeMethod`

`BaseTest` uses:

```java
@BeforeMethod(alwaysRun = true)
public void setUpBrowser() {
    ChromeOptions options = new ChromeOptions();
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

Module 08 deliberately keeps browser creation visible:

```java
ChromeOptions options = new ChromeOptions();
driver = new ChromeDriver(options);
```

This is not the final driver architecture. It is the first refactor from
"every test creates Chrome" to "one base class creates Chrome." If the module
jumped straight to a driver factory, configuration files, and parallel
execution, it would hide the simple problem being solved here.

## Headless System Property

`BaseTest` reads the `headless` JVM system property:

```java
if (Boolean.parseBoolean(System.getProperty("headless", "true"))) {
    options.addArguments("--headless=new");
}
```

This means the default behavior is headless Chrome. A learner can run a visible
browser with:

```bash
mvn test -Dtest=LoginFoundationTest -Dheadless=false
```

Important details:

- `System.getProperty("headless", "true")` reads a JVM property.
- the second argument, `"true"`, is the fallback if no property is supplied.
- `Boolean.parseBoolean(...)` converts the text into a boolean.
- `--headless=new` is a Chrome option passed before `ChromeDriver` starts.

This is not full configuration management yet. It is only a first command-line
override so learners can debug with a visible browser.

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

Module 08 also sets the fields back to `null`:

```java
driver = null;
wait = null;
```

That does not quit the browser by itself. `quit()` already did that. The `null`
assignment prevents the Java object from keeping references to a closed driver
and wait after cleanup.

## Why `wait` Lives Beside `driver`

Module 05 taught explicit waits. Module 08 makes one `WebDriverWait` available
to child tests:

```java
protected WebDriverWait wait;
```

That avoids recreating a wait object in each test method, while keeping the raw
wait API visible. Module 10 will later centralize waits inside wrapper actions
so page and test classes do less low-level waiting.

In this module, the wait is still intentionally raw Selenium:

```java
wait.until(ExpectedConditions.urlContains("/inventory.html"));
wait.until(ExpectedConditions.visibilityOfElementLocated(LOGIN_ERROR));
```

That is the right learning stage. Module 05 already taught explicit waits, so
Module 08 reuses that knowledge inside a more framework-like test class. The
project should not hide waits behind helpers until learners have seen why
repeated wait code becomes noisy.

## LoginFoundationTest Walkthrough

`LoginFoundationTest` owns SauceDemo-specific behavior at this checkpoint.

It stores locators:

```java
private static final By USERNAME_INPUT = By.id("user-name");
private static final By LOGIN_BUTTON = By.id("login-button");
```

It stores class-level data:

```java
private String loginUrl;
```

It prepares that data once:

```java
@BeforeClass(alwaysRun = true)
public void setUpClassData() {
    loginUrl = "https://www.saucedemo.com/";
}
```

Then every test method follows the same pattern:

```text
open SauceDemo login page
submit credentials
wait for expected browser/page state
assert the outcome
```

The standard-user test waits for URL navigation and then checks the product
page:

```java
wait.until(ExpectedConditions.urlContains("/inventory.html"));
Assert.assertEquals(driver.findElement(PRODUCTS_TITLE).getText(), "Products");
Assert.assertEquals(driver.findElements(INVENTORY_ITEMS).size(), 6);
```

The locked-out-user test stays on the login page and waits for the error:

```java
String errorMessage = wait
        .until(ExpectedConditions.visibilityOfElementLocated(LOGIN_ERROR))
        .getText();
```

The two tests prove different outcomes:

- valid login navigates to inventory.
- locked-out login stays on login and displays an error.

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

## Responsibility Boundary In This Module

| Code Area | Owns | Does Not Own |
| --- | --- | --- |
| `BaseTest` | browser startup, `WebDriverWait`, cleanup | SauceDemo locators, usernames, assertions |
| `LoginFoundationTest` | SauceDemo flow, locators, assertions | creating or quitting Chrome |
| `testng.xml` | suite membership and group filtering | browser creation, assertions |
| `pom.xml` | Maven/Surefire execution config | test scenario behavior |

This boundary is the main design lesson. `BaseTest` is allowed to know how to
start a browser. It is not allowed to know what SauceDemo login means.

## Common Beginner Mistakes

- using `@BeforeClass` for browser setup, then accidentally sharing browser
  state across test methods.
- forgetting `alwaysRun = true` on cleanup methods.
- calling `driver.quit()` inside a child test even though `BaseTest` already
  owns cleanup.
- making `driver` public.
- putting SauceDemo locators into `BaseTest`.
- assuming inheritance means every shared method belongs in the parent class.
- using `Thread.sleep` instead of the explicit waits already available.
- deleting raw learning tests after adding `BaseTest`; they still document the
  learning path that created the need for the abstraction.

## Debugging Checklist

If a Module 08 test fails, debug in this order:

1. Did `BaseTest.setUpBrowser()` run before the test method?
2. Did Chrome start in the expected headed/headless mode?
3. Did the test call `driver.get(loginUrl)` before finding elements?
4. Is the wait checking the right browser state, such as URL or visible error?
5. Did `tearDownBrowser()` run and quit Chrome after failure?

This checklist trains framework thinking. Do not start by changing selectors
or adding sleeps until the lifecycle path is confirmed.

## Framework Bridge

`BaseTest` should stay application-neutral. It can know how to create and
clean a browser. It should not know:

- SauceDemo usernames.
- SauceDemo locators.
- checkout steps.
- product names.
- page assertions.

That separation keeps the framework layer reusable.
