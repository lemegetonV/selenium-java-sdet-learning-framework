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

## Strong Answers

**What is data-driven testing?**

Running the same test logic with multiple input and expected-result rows.

**Why use a POJO or record for test data?**

It gives names and types to the data. The test reads `scenario.username()`
instead of relying on raw parameter order.

**Why separate readers from DataProviders?**

Readers parse file formats. DataProviders convert rows into TestNG parameters.
Tests execute behavior. Keeping these separate makes the framework easier to
change.

**When would you use Excel?**

When business users maintain or review tabular test data in spreadsheets.

**What is the risk of random test data?**

Failures become harder to reproduce unless the generated values are logged and
reported clearly.

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

## Common Interview Traps

- saying DataProvider is only for login tests.
- putting browser actions inside the DataProvider.
- using Excel because it looks advanced, even when JSON would be simpler.
- using random data without recording values.
- duplicating the full test for each data source.
- storing real secrets in test data files.

## Framework Phase Bridge

Module 13 can now add listeners, screenshots, and logging. That matters because
data-driven failures need context: which scenario row failed, what input was
used, what page state existed, and what screenshot was captured.
