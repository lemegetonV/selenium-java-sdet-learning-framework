# Data Readers: JSON, CSV, and Excel

## Files In This Topic

This topic reads these files:

- [src/main/java/com/learning/framework/data/JsonDataReader.java](../../src/main/java/com/learning/framework/data/JsonDataReader.java)
- [src/main/java/com/learning/framework/data/CsvDataReader.java](../../src/main/java/com/learning/framework/data/CsvDataReader.java)
- [src/main/java/com/learning/framework/data/ExcelDataReader.java](../../src/main/java/com/learning/framework/data/ExcelDataReader.java)
- [src/test/resources/testdata/login-data.json](../../src/test/resources/testdata/login-data.json)
- [src/test/resources/testdata/login-data.csv](../../src/test/resources/testdata/login-data.csv)
- [src/test/resources/testdata/login-data.xlsx](../../src/test/resources/testdata/login-data.xlsx)


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

## Mental Model

The reader utilities are format adapters:

```text
JSON file  -> JsonDataReader  -> List<LoginScenario>
CSV file   -> CsvDataReader   -> List<Map<String, String>>
Excel file -> ExcelDataReader -> List<Map<String, String>>
```

They do not know TestNG, SauceDemo, or Selenium. Their job is to turn a
classpath resource into Java data. [LoginDataProviders.java](../../src/test/java/com/learning/tests/dataproviders/LoginDataProviders.java)
then adapts that Java data into TestNG's `Object[][]` shape.

## Classpath Resource Loading

All three readers load files from the test classpath. The data files live under
[src/test/resources/testdata](../../src/test/resources/testdata/), and Maven
copies them into `target/test-classes/testdata` during test execution.

This is why the provider constants look like:

```java
private static final String JSON_LOGIN_DATA = "testdata/login-data.json";
```

The path is not an absolute path on your machine. It is a classpath resource
path, which makes the tests portable across local and CI runs.

## JsonDataReader Walkthrough

[JsonDataReader.java](../../src/main/java/com/learning/framework/data/JsonDataReader.java)
uses one shared Jackson `ObjectMapper`:

```java
private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
```

The main API is generic:

```java
public static <T> List<T> readList(String classpathResource, TypeReference<List<T>> typeReference)
```

`<T>` means the same reader can read a list of different model types later.
Module 12 uses it for `List<LoginScenario>`.

```java
try (InputStream inputStream = ClassLoader.getSystemResourceAsStream(classpathResource))
```

The stream is automatically closed after parsing. If the resource is missing,
the reader throws a clear `IllegalArgumentException`. If Jackson cannot parse
the file, the reader wraps the `IOException` in `UncheckedIOException`.

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

## CsvDataReader Walkthrough

[CsvDataReader.java](../../src/main/java/com/learning/framework/data/CsvDataReader.java)
reads the resource URL, converts it to a `Path`, and uses `Files.readAllLines`.
That is acceptable for the small deterministic data file in this module.

The first row is treated as headers:

```java
String[] headers = splitLine(lines.get(0));
```

Each later row is converted into a `LinkedHashMap`:

```java
row.put(headers[column], values[column]);
```

`LinkedHashMap` preserves insertion order, which can make debugging output more
predictable. The provider then reads keys such as `scenarioName`, `username`,
and `successfulLogin`.

Important limitation:

```java
return line.split(",", -1);
```

This keeps empty trailing columns, but it does not handle quoted commas,
escaped quotes, or multiline fields. That is fine for
[login-data.csv](../../src/test/resources/testdata/login-data.csv). A
production framework should use a CSV library when data becomes more complex.

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

Module 12 reads the `login` sheet from
[src/test/resources/testdata/login-data.xlsx](../../src/test/resources/testdata/login-data.xlsx).

The workbook at this checkpoint has one sheet named `login` with the same five
columns as JSON and CSV:

- `scenarioName`
- `username`
- `password`
- `successfulLogin`
- `expectedMessage`

## ExcelDataReader Walkthrough

[ExcelDataReader.java](../../src/main/java/com/learning/framework/data/ExcelDataReader.java)
uses Apache POI because `.xlsx` is a structured workbook format, not a text
file.

```java
try (Workbook workbook = new XSSFWorkbook(inputStream))
```

The workbook is closed automatically. The reader then selects a named sheet:

```java
Sheet sheet = workbook.getSheet(sheetName);
```

If the sheet does not exist, the reader fails clearly. That is important
because a typo in `EXCEL_LOGIN_SHEET = "login"` should not silently read the
wrong data.

```java
DataFormatter formatter = new DataFormatter();
```

`DataFormatter` converts cell values into the displayed string form. That helps
when spreadsheet cells contain booleans, numbers, or formatted values.

The first row becomes headers. Later rows become maps from header name to cell
value. This mirrors the CSV reader so the DataProvider can use the same
`toLoginScenario(...)` conversion for both formats.

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

The dependency changes are in [pom.xml](../../pom.xml).

This does not introduce framework logging yet. Real logging design remains in
Module 13.

## Format Tradeoffs

| Format | Strength | Cost |
| --- | --- | --- |
| JSON | typed structure, nested fields, developer-friendly diffs | less friendly for non-technical reviewers |
| CSV | simple tabular text, easy diffs, easy exports | weak typing, quoting rules become tricky |
| Excel | familiar for business users, supports sheets and manual review | binary diffs, merge conflicts, heavier dependency |

The framework should not choose Excel just because it feels enterprise. Choose
the simplest format that fits the data ownership and review workflow.

## Common Beginner Mistakes

- parsing JSON with string operations.
- assuming CSV is always safe to split on comma in production.
- reading Excel as plain text.
- letting file column order leak directly into test methods.
- storing passwords or secrets in plain data files for real projects.
- making readers call page objects or TestNG assertions.
- hiding parse errors and returning empty data, which can create false
  confidence when no rows actually ran.

## Interview Readiness

A strong answer:

Data readers are responsible for parsing file formats, not running tests.
Module 12 uses Jackson for JSON, a deliberately simple header-based CSV reader,
and Apache POI for `.xlsx`. JSON maps directly to `LoginScenario`; CSV and
Excel become maps first, then are converted into `LoginScenario` by the
DataProvider layer. The test method receives the same Java model regardless of
the original file format.

## Revision Checklist

- Can you explain why JSON maps directly into `LoginScenario`?
- Can you explain why CSV and Excel use `Map<String, String>` first?
- Can you state the limitation of the current CSV reader?
- Can you explain why Excel requires Apache POI?
