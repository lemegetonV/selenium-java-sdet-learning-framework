# TestNG DataProviders

## Files In This Topic

This topic reads these files:

- [src/test/java/com/learning/tests/dataproviders/LoginDataProviders.java](../../src/test/java/com/learning/tests/dataproviders/LoginDataProviders.java)
- [src/test/java/com/learning/tests/saucedemo/SauceDemoDataDrivenTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoDataDrivenTest.java)
- [src/test/java/com/learning/tests/models/LoginScenario.java](../../src/test/java/com/learning/tests/models/LoginScenario.java)


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

## Mental Model

TestNG treats a DataProvider as a table:

```text
Object[][] rows

row 0 -> arguments for invocation 1
row 1 -> arguments for invocation 2
row 2 -> arguments for invocation 3
```

In Module 12, every row has one column because every test method takes one
argument:

```java
public void loginWorksWithJsonDataProvider(LoginScenario scenario)
```

That means the provider shape is:

```text
Object[scenario count][1]
```

If the test method accepted two parameters, each row would need two columns.
That parameter-count matching is one of the most common beginner mistakes.

## Test Method Connection

The test method declares which provider to use:

```java
@Test(dataProvider = "jsonLoginScenarios", dataProviderClass = LoginDataProviders.class)
public void loginWorksWithJsonDataProvider(LoginScenario scenario)
```

TestNG calls the same test method once for each row returned by the provider.

In [SauceDemoDataDrivenTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoDataDrivenTest.java),
there are four DataProvider-backed test methods:

- `loginWorksWithHardcodedDataProvider(...)`
- `loginWorksWithJsonDataProvider(...)`
- `loginWorksWithCsvDataProvider(...)`
- `loginWorksWithExcelDataProvider(...)`

Each method receives a [LoginScenario.java](../../src/test/java/com/learning/tests/models/LoginScenario.java)
and delegates to the same private `runLoginScenario(...)` method. That private
method is the shared test behavior.

## Why A Separate DataProvider Class

`LoginDataProviders` is separate from `SauceDemoDataDrivenTest` because data
loading is not test behavior.

This keeps responsibilities clear:

- DataProvider: load and convert rows.
- Test: execute behavior and assertions.
- Page Object: interact with the application.
- Reader utility: parse a file format.

This separation also keeps browser lifecycle out of data setup. DataProviders
should not create `WebDriver`, open pages, or assert UI state. Those belong in
[BaseTest.java](../../src/test/java/com/learning/tests/base/BaseTest.java),
page objects, and test methods.

## Hardcoded DataProvider

Hardcoded data is useful for the first example because there is no file parsing
yet:

```java
new LoginScenario("standard user hardcoded login", ...)
```

It is acceptable for very small smoke tests or examples. It becomes harder to
maintain when many rows are needed.

The hardcoded provider in [LoginDataProviders.java](../../src/test/java/com/learning/tests/dataproviders/LoginDataProviders.java)
is still useful pedagogically because it shows the raw TestNG shape before file
parsing is introduced:

```java
return new Object[][] {
        { new LoginScenario(...) },
        { new LoginScenario(...) }
};
```

The extra braces matter. The outer array is the list of rows. The inner array
is the list of arguments for one test invocation.

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

## Code Walkthrough

```java
@DataProvider(name = "jsonLoginScenarios")
public static Object[][] jsonLoginScenarios()
```

The provider name is what the test method references in `@Test`. The method is
static because this module keeps providers as utility-style methods in a final
class.

```java
List<LoginScenario> scenarios = JsonDataReader.readList(
        JSON_LOGIN_DATA,
        new TypeReference<>() {
        }
);
```

JSON can be mapped directly into typed `LoginScenario` records because each
JSON object has named fields matching the record components.

```java
List<LoginScenario> scenarios = CsvDataReader.readRows(CSV_LOGIN_DATA)
        .stream()
        .map(LoginDataProviders::toLoginScenario)
        .toList();
```

CSV first becomes `Map<String, String>` rows. The provider then converts each
map into a `LoginScenario`. Excel follows the same map-to-record pattern.

```java
private static Object[][] toDataProviderRows(List<LoginScenario> scenarios)
```

This helper avoids duplicating the `Object[][]` conversion in every provider.
It is a small abstraction with a clear payoff: every data source returns the
same TestNG shape.

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

```java
.map(LoginDataProviders::toLoginScenario)
```

This is a method reference. It means "for each map row, call
`toLoginScenario(row)`." It keeps the stream conversion concise while still
using a named helper.

## Common Beginner Mistakes

- returning the wrong number of parameters for the test method.
- putting WebDriver code inside the DataProvider.
- reading files inside every test step instead of once per provider call.
- duplicating the test method for each file format.
- using random data without recording what was used.
- returning raw strings when a model object would make the row meaning clearer.
- putting assertions inside DataProviders.

## Interview Readiness

A strong answer:

A TestNG DataProvider returns a two-dimensional object array where each row is
one test invocation and each column maps to one test method parameter. In this
module, every row contains a single `LoginScenario`, so the provider returns
`Object[rows][1]`. File parsing happens in reader utilities and provider
methods; UI behavior stays in the test and page objects.

## Revision Checklist

- Can you explain why Module 12 uses `Object[][]`?
- Can you explain what `rows[index][0] = scenarios.get(index)` means?
- Can you trace one JSON row into `loginWorksWithJsonDataProvider(...)`?
- Can you explain why `LoginDataProviders` should not use `WebDriver`?
