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

Add this command-level trace to your explanation:

```text
Maven/Surefire -> TestNG suite -> @BeforeClass -> @BeforeMethod -> @Test -> @AfterMethod -> @AfterClass
```

Then answer:

- which steps happen once per class?
- which steps happen once per test method?
- which class owns browser setup?
- which class owns SauceDemo assertions?

## Exercise 4 - Identify What Should Not Go In BaseTest

List five things that should not be added to `BaseTest`.

Hint:

- think about SauceDemo-specific behavior.
- think about future modules that deserve separate classes.

Expected outcome:

Examples include locators, usernames, product names, checkout actions,
Page Object methods, screenshot code before Module 13, and browser selection
before `DriverFactory` is introduced.

## Exercise 5 - Trace The Suite Command

Explain what happens when this command runs:

```bash
mvn test -DsuiteXmlFile=testng.xml
```

Your answer should mention:

- Maven `test` phase.
- Maven Surefire.
- the `testng-suite` profile.
- [testng.xml](../../testng.xml).
- TestNG `regression` group filtering.
- [LoginFoundationTest.java](../../src/test/java/com/learning/tests/saucedemo/LoginFoundationTest.java).

Hint:

Start from [pom.xml](../../pom.xml), then open [testng.xml](../../testng.xml),
then open the test class.

Expected outcome:

You can explain why the suite command is different from plain `mvn test`.

## Exercise 6 - Defend The Missing Page Object

In one paragraph, explain why Module 08 does not introduce a `LoginPage` class
yet.

Your answer should include:

- what `BaseTest` solves.
- what problem Page Objects will solve later.
- why teaching both at the same time would make the boundary harder to see.

Expected outcome:

You can explain the learning sequence instead of treating the missing Page
Object as a mistake.
