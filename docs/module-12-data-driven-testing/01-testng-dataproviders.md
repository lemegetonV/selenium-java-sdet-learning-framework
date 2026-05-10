# TestNG DataProviders

## Files In This Topic

```text
src/test/java/com/learning/tests/dataproviders/LoginDataProviders.java
src/test/java/com/learning/tests/saucedemo/SauceDemoDataDrivenTest.java
src/test/java/com/learning/tests/models/LoginScenario.java
```

## What A DataProvider Does

A TestNG `@DataProvider` supplies rows of data to a test method.

Module 12 starts with:

```java
@DataProvider(name = "hardcodedLoginScenarios")
public static Object[][] hardcodedLoginScenarios()
```

The return type is `Object[][]` because TestNG treats it as:

```text
rows x parameters
```

Each row in this module has one parameter: a `LoginScenario`.

## Test Method Connection

The test method declares which provider to use:

```java
@Test(dataProvider = "jsonLoginScenarios", dataProviderClass = LoginDataProviders.class)
public void loginWorksWithJsonDataProvider(LoginScenario scenario)
```

TestNG calls the same test method once for each row returned by the provider.

## Why A Separate DataProvider Class

`LoginDataProviders` is separate from `SauceDemoDataDrivenTest` because data
loading is not test behavior.

This keeps responsibilities clear:

- DataProvider: load and convert rows.
- Test: execute behavior and assertions.
- Page Object: interact with the application.
- Reader utility: parse a file format.

## Hardcoded DataProvider

Hardcoded data is useful for the first example because there is no file parsing
yet:

```java
new LoginScenario("standard user hardcoded login", ...)
```

It is acceptable for very small smoke tests or examples. It becomes harder to
maintain when many rows are needed.

## External DataProviders

Module 12 adds:

- `jsonLoginScenarios`.
- `csvLoginScenarios`.
- `excelLoginScenarios`.

All three return the same Java shape:

```java
Object[][] rows
```

The test method does not care whether the row came from JSON, CSV, Excel, or a
hardcoded list.

## Java Syntax To Notice

```java
Object[][] rows = new Object[scenarios.size()][1];
```

This creates one TestNG row per `LoginScenario`, with one method parameter per
row.

```java
rows[index][0] = scenarios.get(index);
```

Column zero is the first argument passed to the test method.

## Common Beginner Mistakes

- returning the wrong number of parameters for the test method.
- putting WebDriver code inside the DataProvider.
- reading files inside every test step instead of once per provider call.
- duplicating the test method for each file format.
- using random data without recording what was used.
