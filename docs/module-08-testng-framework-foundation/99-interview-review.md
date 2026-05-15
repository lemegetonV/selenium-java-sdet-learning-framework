# Module 08 Interview Review

## What You Must Be Able To Explain

Module 08 is about TestNG framework foundation, not Page Objects yet. You
should be able to explain:

- why `BaseTest` exists.
- the difference between `@BeforeMethod` and `@BeforeClass`.
- why `@AfterMethod` calls `driver.quit()`.
- what `alwaysRun = true` protects.
- why `driver` and `wait` are `protected`.
- how a child test class inherits setup from `BaseTest`.
- what [testng.xml](../../testng.xml) controls.
- how TestNG groups support smoke and regression suites.
- why the locators still live in `LoginFoundationTest`.

## Strong Answers

**Why did we introduce `BaseTest`?**

Earlier modules repeated browser setup, wait setup, and cleanup in every test
method. `BaseTest` centralizes that lifecycle so framework-style tests start
from a consistent browser state.

**Why use `@BeforeMethod` for browser creation?**

It creates a fresh browser before every test method. That gives isolation:
cookies, local storage, current URL, windows, and login state do not leak from
one test to another.

**Why not use `@BeforeClass` for browser creation?**

`@BeforeClass` would share one browser across all tests in the class. That can
make tests faster, but it also makes tests easier to couple accidentally.
Module 08 prioritizes isolation and clarity.

**What does `protected WebDriver driver` mean?**

The parent class and child classes can access the field, but unrelated classes
cannot use it as public global state.

**What does [testng.xml](../../testng.xml) add?**

It lets the project define a named suite, choose test classes, include groups,
and prepare for future suite-level settings such as browser, environment, and
parallel execution.

**How does `mvn test -DsuiteXmlFile=testng.xml` reach the test method?**

Maven runs the `test` phase. Surefire is the plugin responsible for executing
tests in that phase. Because the `suiteXmlFile` property is present, the
`testng-suite` Maven profile activates and passes `testng.xml` into Surefire.
Surefire starts TestNG with that suite file. TestNG reads the class name,
filters methods by the included `regression` group, then runs the matching
methods with the normal TestNG lifecycle.

**What is the exact responsibility split in Module 08?**

`BaseTest` owns browser lifecycle: Chrome options, Chrome startup,
`WebDriverWait`, and cleanup. `LoginFoundationTest` owns SauceDemo behavior:
locators, credentials used by the scenario, navigation, waits for page state,
and assertions. `testng.xml` owns suite selection. `pom.xml` owns Maven
execution configuration.

**Why is the local `loginAs(...)` helper not a Page Object?**

It is private to one test class, uses raw Selenium directly, and depends on
locators stored inside the test class. It removes duplication inside the class,
but it does not model a reusable page. Module 09 will move that behavior into
a `LoginPage`.

## Code Lines To Revise

```java
public class LoginFoundationTest extends BaseTest
```

The child test class inherits browser setup and cleanup.

```java
@BeforeMethod(alwaysRun = true)
public void setUpBrowser()
```

Runs before each test method and should run even when groups or configuration
change.

```java
@AfterMethod(alwaysRun = true)
public void tearDownBrowser()
```

Runs after each test method and owns browser cleanup.

```java
@Test(groups = {"smoke", "regression"})
```

Assigns the test to multiple execution groups.

```xml
<include name="regression"/>
```

Runs only tests that belong to the `regression` group inside the selected
classes.

```xml
<suiteXmlFile>${suiteXmlFile}</suiteXmlFile>
```

Passes the command-line suite file value into Maven Surefire.

```java
System.getProperty("headless", "true")
```

Reads the optional JVM property that controls whether Chrome starts headless.

```java
private static final By LOGIN_BUTTON = By.id("login-button");
```

Keeps the locator as a named constant in the test class until Page Objects are
introduced.

## Common Interview Traps

- Saying `close()` and `quit()` are the same.
- Putting application locators into `BaseTest`.
- Sharing one browser across tests without understanding state leakage.
- Making `driver` public.
- Claiming Module 08 is already a full framework.
- Adding Page Objects before explaining what problem `BaseTest` solved.
- Saying TestNG groups and Maven profiles are the same thing.
- Saying `@BeforeClass` is always better because it is faster.
- Forgetting that the current design prioritizes test isolation over speed.

## Whiteboard Flow

Be able to draw this from memory:

```text
mvn test -DsuiteXmlFile=testng.xml
        |
        v
Surefire profile loads testng.xml
        |
        v
TestNG selects LoginFoundationTest regression methods
        |
        v
BaseTest @BeforeMethod creates driver and wait
        |
        v
LoginFoundationTest uses driver/wait for SauceDemo scenario
        |
        v
BaseTest @AfterMethod quits driver
```

This is the shortest complete explanation of Module 08.

## Debugging Questions

If a Module 08 suite fails, ask:

1. Did the browser start in `BaseTest.setUpBrowser()`?
2. Did the test open `loginUrl` before locating elements?
3. Did the expected wait match the browser outcome?
4. Was the test method included by the selected TestNG group?
5. Did `tearDownBrowser()` quit the browser even after failure?

## Framework Phase Bridge

Module 09 can now introduce Page Object Model because browser lifecycle has a
home. The next duplication is locator and page-action duplication. That should
be solved in page classes, not by adding SauceDemo details to `BaseTest`.
