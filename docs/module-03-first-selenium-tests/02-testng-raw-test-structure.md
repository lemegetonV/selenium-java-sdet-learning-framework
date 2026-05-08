# TestNG Raw Test Structure

## Why TestNG Appears Here

Module 03 introduces TestNG only enough to run first Selenium tests.

The framework features of TestNG are deferred:

- no `@BeforeMethod`.
- no `@AfterMethod`.
- no groups.
- no `testng.xml`.
- no listeners.
- no DataProvider.
- no retry analyzer.

Those appear later when the module scope teaches them.

## Minimal Test Method

The first test uses:

```java
@Test
public void opensTheInternetHomePage() {
    WebDriver driver = createChromeDriver();
    try {
        driver.get("https://the-internet.herokuapp.com/");
        Assert.assertEquals(driver.getTitle(), "The Internet");
    } finally {
        driver.quit();
    }
}
```

This structure is intentionally direct:

1. create browser.
2. navigate.
3. assert.
4. quit browser.

## Where To Study

| File | Focus |
| --- | --- |
| `src/test/java/com/learning/tests/learning/_01_FirstBrowserTest.java` | first browser launch, title, current URL |
| `src/test/java/com/learning/tests/learning/_02_NavigationTest.java` | navigation commands |
| `src/test/java/com/learning/tests/learning/_03_SauceDemoPageLoadTest.java` | first SauceDemo page load |

## Why There Is No Base Class

The repeated `createChromeDriver()` method is not a mistake.

It creates the learning problem that Module 08 solves with `BaseTest`.

If the project hides setup too early, learners do not see:

- where browser objects come from.
- why cleanup matters.
- why repeated setup becomes painful.
- what inheritance later removes.

## TestNG Assertions

Module 03 uses `org.testng.Assert`.

Examples:

```java
Assert.assertEquals(driver.getTitle(), "The Internet");
Assert.assertTrue(driver.getCurrentUrl().contains("saucedemo.com"));
```

Assertions turn browser observations into test results. If the actual browser
state does not match the expected state, TestNG fails the test.

## Console Output

The tests do not add framework logging.

Maven and TestNG print the test run summary. Log4j2 is intentionally deferred
until the logging module.

## Java And TestNG Syntax To Notice

```java
@Test
public void opensTheInternetHomePage()
```

`@Test` is a TestNG annotation. It tells TestNG that this method is a test
method. The method is `public` so the framework can discover and invoke it.

```java
try {
    driver.get("https://the-internet.herokuapp.com/");
} finally {
    driver.quit();
}
```

`finally` runs whether the assertion passes or fails. This is the first cleanup
pattern in the project. Later modules move the cleanup into TestNG lifecycle
methods, but the reason stays the same: browser sessions must be closed.

```java
Assert.assertTrue(condition, "message");
```

The message should explain the behavior expected by the test. It should help
the learner diagnose a failure without rereading the whole method.

## Interview Readiness

**Question: Why introduce TestNG before building a framework?**

TestNG gives the project a test runner, assertions, and later lifecycle hooks.
Module 03 uses only the smallest part first so WebDriver behavior stays visible.

**Question: Why is setup duplicated in Module 03?**

The duplication is intentional. Learners should see the repeated driver
creation and cleanup before Module 08 extracts it into `BaseTest`.

**Question: What is the difference between a failed assertion and a Selenium
exception?**

A failed assertion means Selenium gathered browser state but the state did not
match the expected result. A Selenium exception usually means the browser
command itself could not complete, such as a missing element or invalid
selector in later modules.

## Revision Checklist

- Can you explain what `@Test` does?
- Can you explain why `finally` is used before `@AfterMethod` is introduced?
- Can you explain what TestNG assertions add beyond printing values?
