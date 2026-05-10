# Data Readers: JSON, CSV, and Excel

## Files In This Topic

```text
src/main/java/com/learning/framework/data/JsonDataReader.java
src/main/java/com/learning/framework/data/CsvDataReader.java
src/main/java/com/learning/framework/data/ExcelDataReader.java
src/test/resources/testdata/login-data.json
src/test/resources/testdata/login-data.csv
src/test/resources/testdata/login-data.xlsx
```

## JSON Reader

`JsonDataReader` uses Jackson:

```java
OBJECT_MAPPER.readValue(inputStream, typeReference)
```

JSON is good when:

- data has named fields.
- nested data may be needed later.
- developers maintain the data.
- the project wants strong mapping to POJOs.

Module 12 maps JSON rows directly into `LoginScenario` records.

## CSV Reader

`CsvDataReader` reads simple header-based CSV:

```text
scenarioName,username,password,successfulLogin,expectedMessage
```

The reader returns:

```java
List<Map<String, String>>
```

CSV is good when:

- data is tabular.
- rows are simple.
- business users may export data from spreadsheets.

This module intentionally supports a simple CSV shape without embedded commas.
That limitation is documented in code because production CSV parsing can become
surprisingly complex.

## Excel Reader

`ExcelDataReader` uses Apache POI:

```java
Workbook workbook = new XSSFWorkbook(inputStream)
```

Excel is useful when:

- business users own the rows.
- multiple sheets are needed.
- data is reviewed manually.
- teams already maintain spreadsheets.

Module 12 reads the `login` sheet from:

```text
src/test/resources/testdata/login-data.xlsx
```

## Why Different Readers Return A Common Shape

External formats differ, but DataProviders need a stable Java object.

The flow is:

```text
JSON -> LoginScenario
CSV -> Map -> LoginScenario
Excel -> Map -> LoginScenario
```

The test method receives only:

```java
LoginScenario scenario
```

That keeps test logic independent from file format.

## Dependency Notes

Module 12 adds:

- Jackson for JSON parsing.
- Apache POI for Excel parsing.
- Log4j-to-SLF4J bridge because Apache POI uses Log4j APIs internally.

This does not introduce framework logging yet. Real logging design remains in
Module 13.

## Common Beginner Mistakes

- parsing JSON with string operations.
- assuming CSV is always safe to split on comma in production.
- reading Excel as plain text.
- letting file column order leak directly into test methods.
- storing passwords or secrets in plain data files for real projects.
