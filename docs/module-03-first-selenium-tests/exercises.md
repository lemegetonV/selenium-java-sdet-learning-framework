# Module 03 Exercises

These exercises extend the first Selenium tests. Keep the code raw and
explicit. Do not add `BaseTest`, page objects, waits, or wrapper methods yet.

## Exercise 1 - Add Another Page Title Test

Create a new test method that opens:

```text
https://the-internet.herokuapp.com/login
```

Assert the page title is:

```text
The Internet
```

Hint:
- use the same setup/try/finally pattern as `FirstBrowserTest`.

Expected outcome:
- `mvn test` runs one additional Selenium test.

## Exercise 2 - Run With Browser Visible

Run:

```bash
mvn test -Dheadless=false
```

Observe the browser opening and closing.

Expected outcome:
- the same tests pass, but Chrome is visible.

## Exercise 3 - Add A Current URL Assertion

Extend `NavigationTest` with one more assertion after `refresh()`.

Hint:
- read the current URL with `driver.getCurrentUrl()`.

Expected outcome:
- the test proves the browser is still on the login page after refresh.

## Exercise 4 - Explain The OOP Mapping

In your own words, explain this code:

```java
WebDriver driver = new ChromeDriver(options);
```

Answer:

1. What is the interface?
2. What is the concrete class?
3. What object does the variable point to at runtime?
4. How did Module 02 prepare for this?

## Exercise 5 - Identify The Duplication

Compare:

```text
src/test/java/com/learning/tests/learning/FirstBrowserTest.java
src/test/java/com/learning/tests/learning/NavigationTest.java
src/test/java/com/learning/tests/learning/SauceDemoPageLoadTest.java
```

List the duplicated setup and cleanup code.

Expected outcome:
- you can explain why a later module will introduce shared setup.
