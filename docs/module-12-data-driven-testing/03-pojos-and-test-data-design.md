# POJOs and Test Data Design

## Files In This Topic

This topic reads these files:

- [src/test/java/com/learning/tests/models/LoginScenario.java](../../src/test/java/com/learning/tests/models/LoginScenario.java)
- [src/test/java/com/learning/tests/dataproviders/LoginDataProviders.java](../../src/test/java/com/learning/tests/dataproviders/LoginDataProviders.java)
- [src/test/java/com/learning/tests/saucedemo/SauceDemoDataDrivenTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoDataDrivenTest.java)


## Why Use LoginScenario

Without a model object, a test might receive several strings:

```java
public void testLogin(String username, String password, String expectedMessage)
```

That works, but the meaning depends on parameter order. Module 12 uses:

```java
public record LoginScenario(...)
```

The test reads clearly:

```java
scenario.username()
scenario.password()
scenario.expectedMessage()
```

This is safer and more expressive.

## Mental Model

[LoginScenario.java](../../src/test/java/com/learning/tests/models/LoginScenario.java)
is the contract between data files and tests.

```text
External row -> LoginScenario -> SauceDemoDataDrivenTest
```

Once a row becomes a `LoginScenario`, the test no longer cares whether the
source was hardcoded Java, JSON, CSV, or Excel. That is the main design value
of using a model object.

## Java Record Syntax

`LoginScenario` is a Java record:

```java
public record LoginScenario(
        String scenarioName,
        String username,
        String password,
        boolean successfulLogin,
        String expectedMessage
) {
}
```

A record automatically provides:

- constructor.
- field accessors such as `username()`.
- `equals`.
- `hashCode`.
- `toString`.

Records are useful for immutable data carrier objects.

## Field Meaning

| Field | Meaning | Used In |
| --- | --- | --- |
| `scenarioName` | readable row label for assertion messages | [SauceDemoDataDrivenTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoDataDrivenTest.java) |
| `username` | SauceDemo username to enter | `LoginPage.loginAs(...)` or `loginExpectingError(...)` |
| `password` | SauceDemo password to enter | `LoginPage.loginAs(...)` or `loginExpectingError(...)` |
| `successfulLogin` | controls positive vs negative flow | `runLoginScenario(...)` branch |
| `expectedMessage` | expected title or error-text fragment | TestNG assertion |

This is more readable than positional parameters because the test can say
`scenario.successfulLogin()` instead of remembering that column 3 means the
expected outcome.

## Deterministic Data

Module 12 uses deterministic rows:

- `standard_user`.
- `locked_out_user`.
- known expected messages.

Deterministic data makes failures reproducible. If a data row fails, the
learner can rerun the same row and debug it.

The current files use stable SauceDemo users:

- successful rows use `standard_user` and expect the products page title.
- negative rows use `locked_out_user` and expect a locked-out error fragment.

The negative rows intentionally use either the full message or a stable
fragment. The test uses `contains(...)` for errors so minor prefix/suffix text
changes are less brittle than exact equality.

## Generated Data Is Deferred

Generated or random data can be useful, but it creates new responsibilities:

- record the generated value.
- make failed rows reproducible.
- avoid collisions.
- clean up created data.
- keep reports readable.

That belongs later, after reporting and logging are available.

## Test Logic Stays Shared

`SauceDemoDataDrivenTest` has one private workflow method:

```java
private void runLoginScenario(LoginScenario scenario)
```

Every DataProvider uses this same method. That is the point of data-driven
testing: data changes, behavior stays centralized.

## Code Walkthrough

```java
private void runLoginScenario(LoginScenario scenario)
```

[SauceDemoDataDrivenTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoDataDrivenTest.java)
keeps the workflow in one helper so each DataProvider-backed test method
delegates to the same behavior.

```java
if (scenario.successfulLogin()) {
    ProductsPage productsPage = loginPage.loginAs(...);
    Assert.assertEquals(productsPage.getTitle(), scenario.expectedMessage(), scenario.scenarioName());
}
```

Positive rows assert the products page title. The scenario name is passed as
the assertion message so a failure points back to the data row.

```java
loginPage.loginExpectingError(...);
Assert.assertTrue(loginPage.getErrorMessage().contains(scenario.expectedMessage()), scenario.scenarioName());
```

Negative rows stay on the login page and assert the error text contains the
expected fragment.

## Data Shape Decisions

This module keeps one model for all login rows. That is appropriate because the
positive and negative cases share the same fields. If future tests need very
different data shapes, the framework should add a new model instead of forcing
unrelated fields into `LoginScenario`.

The model lives under
[src/test/java/com/learning/tests/models/LoginScenario.java](../../src/test/java/com/learning/tests/models/LoginScenario.java)
because it is test data shape, not production framework behavior.

## Interview Nuance

Data-driven testing is not only about more rows. It is about separating:

- what to test.
- how to test it.
- where data is stored.
- how data is parsed.

Strong frameworks keep those responsibilities separate.

## Common Mistakes

- Passing five raw strings into every test method instead of a named model.
- Adding optional fields to one shared model until it becomes unclear.
- Using random/generated values before reports can show exactly what data was
  used.
- Putting browser behavior into the model object.
- Treating test data files as secure storage for real credentials.

## Interview Readiness

A strong answer:

`LoginScenario` is an immutable record that represents one login test row. It
gives names and types to the row fields, removes fragile parameter ordering from
test methods, and lets every data source feed the same test workflow. Data
models should describe test inputs and expected outcomes; they should not know
about WebDriver or page object behavior.

## Revision Checklist

- Can you explain why `LoginScenario` is a record?
- Can you explain how `successfulLogin` changes the test branch?
- Can you explain why `scenarioName` is useful in assertion messages?
- Can you explain why generated data is deferred until diagnostics improve?
