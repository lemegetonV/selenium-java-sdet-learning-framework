# POJOs and Test Data Design

## Files In This Topic

```text
src/test/java/com/learning/tests/models/LoginScenario.java
src/test/java/com/learning/tests/dataproviders/LoginDataProviders.java
src/test/java/com/learning/tests/saucedemo/SauceDemoDataDrivenTest.java
```

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

## Deterministic Data

Module 12 uses deterministic rows:

- `standard_user`.
- `locked_out_user`.
- known expected messages.

Deterministic data makes failures reproducible. If a data row fails, the
learner can rerun the same row and debug it.

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

## Interview Nuance

Data-driven testing is not only about more rows. It is about separating:

- what to test.
- how to test it.
- where data is stored.
- how data is parsed.

Strong frameworks keep those responsibilities separate.
