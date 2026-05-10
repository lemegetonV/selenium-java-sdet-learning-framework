# Extent Reports

## Mental Model

Extent Reports creates a human-readable HTML report from test execution events.
In this framework, Extent answers:

- which tests passed, failed, or skipped.
- which TestNG groups were assigned.
- which screenshot belongs to a failed test.
- what run metadata was recorded.

Extent is useful when you want a single HTML artifact that can be opened
directly from `target/extent-report/extent.html`.

## Code Walkthrough

Main file:

`src/test/java/com/learning/tests/reports/ExtentReportManager.java`

The setup flow:

```java
ExtentSparkReporter sparkReporter = new ExtentSparkReporter(REPORT_PATH.toString());
extentReports = new ExtentReports();
extentReports.attachReporter(sparkReporter);
```

`ExtentSparkReporter` writes the HTML file. `ExtentReports` is the report
engine. `ExtentTest` represents the current test entry.

The TestNG listener calls:

- `ExtentReportManager.initialize(...)` in `onStart`.
- `ExtentReportManager.startTest(...)` in `onTestStart`.
- `pass(...)`, `fail(...)`, or `skip(...)` when TestNG reports the result.
- `flush()` in `onFinish`.

## Java Syntax To Notice

`ThreadLocal<ExtentTest>` matters because each running test needs its own
current report entry. Module 15 will make this more important when tests run
in parallel.

`synchronized` on `initialize` and `flush` protects shared report setup and
final writing. The report object is shared; the current test entry is
thread-local.

## Framework Nuances

`flush()` is required. Without it, the report may not finish writing its HTML
content to disk.

Screenshot attachment happens only when a screenshot path exists. Passed tests
do not need screenshots by default because screenshots on every pass create
heavy artifacts without much diagnostic value.

`assignCategory(result.getMethod().getGroups())` maps TestNG groups such as
`smoke` and `regression` into Extent categories.

## Common Mistakes

- Creating a new `ExtentReports` object inside every test.
- Forgetting `flush()`.
- Attaching screenshots before the screenshot file exists.
- Writing report code inside page objects.
- Assuming Extent replaces logs. Reports summarize; logs preserve execution
  detail.

## Interview Readiness

Strong answer:

"Extent Reports is an HTML reporting library. In a TestNG Selenium framework I
usually create the report once per suite, create one `ExtentTest` per test
method, update status from a listener, attach screenshots on failure, and flush
the report at the end."

## Revision Checklist

- Can you explain the difference between `ExtentReports`, `ExtentSparkReporter`,
  and `ExtentTest`?
- Can you show where the Extent report is flushed?
- Can you explain why current test tracking uses `ThreadLocal`?
- Can you find the generated HTML report path?

