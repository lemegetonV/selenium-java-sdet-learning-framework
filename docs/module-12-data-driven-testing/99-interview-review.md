# Module 12 Interview Review

## What You Must Be Able To Explain

Module 12 introduces data-driven testing. You should be able to explain:

- what TestNG `@DataProvider` does.
- why DataProviders return `Object[][]`.
- why this module uses `LoginScenario`.
- how JSON, CSV, and Excel rows become Java objects.
- when hardcoded data is acceptable.
- when external data files are better.
- why deterministic data is preferred here.
- why random/generated data is deferred.
- why file readers should not contain WebDriver logic.
- how `Object[][]` maps to `LoginScenario scenario`.
- why JSON, CSV, and Excel are format choices, not separate test behaviors.

## Strong Answers

**What is data-driven testing?**

Running the same test logic with multiple input and expected-result rows.

In this module, [SauceDemoDataDrivenTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoDataDrivenTest.java)
runs the same login workflow with rows from hardcoded Java, JSON, CSV, and
Excel providers.

**Why use a POJO or record for test data?**

It gives names and types to the data. The test reads `scenario.username()`
instead of relying on raw parameter order.

[LoginScenario.java](../../src/test/java/com/learning/tests/models/LoginScenario.java)
also gives the DataProvider layer one common object shape regardless of the
original file format.

**Why separate readers from DataProviders?**

Readers parse file formats. DataProviders convert rows into TestNG parameters.
Tests execute behavior. Keeping these separate makes the framework easier to
change.

[JsonDataReader.java](../../src/main/java/com/learning/framework/data/JsonDataReader.java),
[CsvDataReader.java](../../src/main/java/com/learning/framework/data/CsvDataReader.java), and
[ExcelDataReader.java](../../src/main/java/com/learning/framework/data/ExcelDataReader.java)
should not know about `LoginPage`, assertions, or TestNG groups.

**When would you use Excel?**

When business users maintain or review tabular test data in spreadsheets.

Excel is not automatically better than JSON or CSV. It adds Apache POI and
binary-file versioning tradeoffs, so use it when the ownership model justifies
it.

**What is the risk of random test data?**

Failures become harder to reproduce unless the generated values are logged and
reported clearly.

That is why generated data is deferred until later reporting and logging
modules can capture the actual row values.

**Why does the provider return `Object[][]` if the test has one parameter?**

TestNG always expects rows and columns. This module has many rows, and each row
has one column: the `LoginScenario` object passed to the test method.

## Code Lines To Revise

```java
@Test(dataProvider = "jsonLoginScenarios", dataProviderClass = LoginDataProviders.class)
```

Connects a test method to a named provider in another class.

```java
public record LoginScenario(...)
```

Defines an immutable data carrier for one scenario row.

```java
JsonDataReader.readList(JSON_LOGIN_DATA, new TypeReference<>() {})
```

Reads structured JSON into typed Java records.

```java
ExcelDataReader.readRows(EXCEL_LOGIN_DATA, EXCEL_LOGIN_SHEET)
```

Reads spreadsheet rows through Apache POI.

```java
private static Object[][] toDataProviderRows(List<LoginScenario> scenarios)
```

Converts the common Java list into the TestNG row/parameter table.

```java
private void runLoginScenario(LoginScenario scenario)
```

Keeps the actual login workflow shared across all data-provider-backed test
methods.

## Common Interview Traps

- saying DataProvider is only for login tests.
- putting browser actions inside the DataProvider.
- using Excel because it looks advanced, even when JSON would be simpler.
- using random data without recording values.
- duplicating the full test for each data source.
- storing real secrets in test data files.
- making every CSV/Excel column a separate test method parameter.
- hiding reader errors and accidentally running zero rows.
- choosing Excel because it looks advanced rather than because business users
  own the data.

## Debugging Questions

If a Module 12 data-driven test fails, ask:

- Did [LoginDataProviders.java](../../src/test/java/com/learning/tests/dataproviders/LoginDataProviders.java)
  return the correct number of columns for the test method?
- Did the resource path match the classpath location under
  [src/test/resources/testdata](../../src/test/resources/testdata)?
- Did CSV or Excel headers match the keys expected by `toLoginScenario(...)`?
- Did the failure happen during file parsing, DataProvider conversion, login
  workflow, or assertion?
- Did the assertion message include `scenario.scenarioName()` so the failing
  row is identifiable?

## Framework Phase Bridge

Module 13 can now add listeners, screenshots, and logging. That matters because
data-driven failures need context: which scenario row failed, what input was
used, what page state existed, and what screenshot was captured.

## One-Minute Whiteboard Answer

Module 12 adds data-driven testing by separating scenario rows from login test
behavior. [LoginDataProviders.java](../../src/test/java/com/learning/tests/dataproviders/LoginDataProviders.java)
returns TestNG `Object[][]` rows, where each row contains one
[LoginScenario.java](../../src/test/java/com/learning/tests/models/LoginScenario.java).
JSON is parsed directly into records, while CSV and Excel are parsed into maps
and converted to records. [SauceDemoDataDrivenTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoDataDrivenTest.java)
uses one shared `runLoginScenario(...)` workflow for every data source.
