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
- what `testng.xml` controls.
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

**What does `testng.xml` add?**

It lets the project define a named suite, choose test classes, include groups,
and prepare for future suite-level settings such as browser, environment, and
parallel execution.

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

## Common Interview Traps

- Saying `close()` and `quit()` are the same.
- Putting application locators into `BaseTest`.
- Sharing one browser across tests without understanding state leakage.
- Making `driver` public.
- Claiming Module 08 is already a full framework.
- Adding Page Objects before explaining what problem `BaseTest` solved.

## Framework Phase Bridge

Module 09 can now introduce Page Object Model because browser lifecycle has a
home. The next duplication is locator and page-action duplication. That should
be solved in page classes, not by adding SauceDemo details to `BaseTest`.
