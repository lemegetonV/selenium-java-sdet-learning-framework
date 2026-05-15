# Module 12 Exercises

Use these exercises after reading:

- [LoginScenario.java](../../src/test/java/com/learning/tests/models/LoginScenario.java)
- [LoginDataProviders.java](../../src/test/java/com/learning/tests/dataproviders/LoginDataProviders.java)
- [SauceDemoDataDrivenTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoDataDrivenTest.java)
- [JsonDataReader.java](../../src/main/java/com/learning/framework/data/JsonDataReader.java)
- [CsvDataReader.java](../../src/main/java/com/learning/framework/data/CsvDataReader.java)
- [ExcelDataReader.java](../../src/main/java/com/learning/framework/data/ExcelDataReader.java)
- [login-data.json](../../src/test/resources/testdata/login-data.json)
- [login-data.csv](../../src/test/resources/testdata/login-data.csv)
- [login-data.xlsx](../../src/test/resources/testdata/login-data.xlsx)

## Exercise 1 - Add A Missing Password Row

Add a missing-password row to the JSON file and make sure the JSON DataProvider
test handles it.

Hint:

- use username `standard_user`.
- use an empty password.
- expected message can be a stable fragment such as `Password is required`.

Expected outcome:

- the JSON DataProvider runs one additional row.
- the test method does not need to be duplicated.
- the new row still maps into [LoginScenario.java](../../src/test/java/com/learning/tests/models/LoginScenario.java).

Revision question:

- should the expected message be an exact full string or a stable fragment?
  Explain using the negative assertion in
  [SauceDemoDataDrivenTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoDataDrivenTest.java).

## Exercise 2 - Add A CSV Column

Add a `category` column to the CSV file.

Hint:

- update the header.
- update each row.
- decide whether `LoginScenario` should include it or whether it is only
  metadata for a future module.

Expected outcome:

- the learner can explain that changing data shape may require model changes.
- the learner can explain whether [LoginDataProviders.java](../../src/test/java/com/learning/tests/dataproviders/LoginDataProviders.java)
  should pass the new value into `LoginScenario`.

## Exercise 3 - Explain Excel Tradeoffs

Write a short answer explaining when Excel is useful and when it is not.

Expected outcome:

- a good answer mentions business readability.
- a good answer also mentions merge conflicts, binary diffs, and versioning
  challenges.
- a good answer compares Excel against [login-data.json](../../src/test/resources/testdata/login-data.json)
  and [login-data.csv](../../src/test/resources/testdata/login-data.csv), not
  against a vague idea of "external data."

## Exercise 4 - Keep DataProviders Browser-Free

Explain why `LoginDataProviders` should not call `new LoginPage(...)`.

Expected outcome:

- the answer separates data preparation from browser behavior.
- the answer mentions that WebDriver lifecycle belongs to `BaseTest`.

## Exercise 5 - Trace One CSV Row

Trace the `locked out csv login` row from
[login-data.csv](../../src/test/resources/testdata/login-data.csv) into the
test method.

Hint:

- start in `CsvDataReader.readRows(...)`.
- follow the map into `LoginDataProviders.toLoginScenario(...)`.
- follow the resulting record into `loginWorksWithCsvDataProvider(...)`.

Expected outcome:

- you can explain how header names become map keys.
- you can explain why the test receives one `LoginScenario` instead of five
  raw strings.

## Exercise 6 - Break A Header Intentionally

Rename `successfulLogin` to `success` in a temporary CSV copy and predict what
will happen.

Hint:

- inspect `toLoginScenario(...)` in
  [LoginDataProviders.java](../../src/test/java/com/learning/tests/dataproviders/LoginDataProviders.java).
- think about `Boolean.parseBoolean(row.get("successfulLogin"))`.

Expected outcome:

- the learner can explain why data shape validation matters.
- the learner can propose adding clearer validation before converting maps into
  `LoginScenario`.

## Exercise 7 - Explain `Object[][]`

Write a short explanation of this line:

```java
Object[][] rows = new Object[scenarios.size()][1];
```

Expected outcome:

- the first dimension is test invocations.
- the second dimension is method parameters.
- `[1]` is used because each test method receives one `LoginScenario`.

## Exercise 8 - Choose A Data Format

For each case, choose JSON, CSV, or Excel and explain why:

- developers maintain nested checkout data in Git.
- business users maintain simple login rows in a spreadsheet.
- the team needs small tabular data with clean text diffs.

Expected outcome:

- the answer chooses based on ownership, data shape, review needs, and
  maintenance cost.
