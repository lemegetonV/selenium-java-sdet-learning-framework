# Module 08 Exercises

## Exercise 1 - Add A Smoke-Only Suite

Create a second TestNG XML file named `testng-smoke.xml` that runs only the
`smoke` group from `LoginFoundationTest`.

Hint:

- copy the structure of [testng.xml](../../testng.xml).
- change the included group from `regression` to `smoke`.
- keep the same fully qualified class name.

Expected outcome:

- only `standardUserCanReachProductsPage` runs.

## Exercise 2 - Add A New Negative Login Test

Add a test for a blank password.

Hint:

- use `driver.get(loginUrl)`.
- type only the username.
- click the login button.
- wait for the login error.
- assert that the message mentions password being required.

Expected outcome:

- the test belongs to the `regression` group.
- browser setup and cleanup still happen through `BaseTest`.

## Exercise 3 - Explain The Lifecycle

Write the execution order for one test method in `LoginFoundationTest`.

Hint:

- include `@BeforeClass`.
- include `@BeforeMethod`.
- include the `@Test` method.
- include `@AfterMethod`.
- include `@AfterClass`.

Expected outcome:

```text
setUpClassData -> setUpBrowser -> test method -> tearDownBrowser -> clearClassData
```

For multiple test methods, `setUpBrowser` and `tearDownBrowser` repeat for
each test method.

## Exercise 4 - Identify What Should Not Go In BaseTest

List five things that should not be added to `BaseTest`.

Hint:

- think about SauceDemo-specific behavior.
- think about future modules that deserve separate classes.

Expected outcome:

Examples include locators, usernames, product names, checkout actions,
Page Object methods, screenshot code before Module 13, and browser selection
before `DriverFactory` is introduced.
