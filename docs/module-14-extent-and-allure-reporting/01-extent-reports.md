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

In this module, Extent is driven by the existing TestNG framework listener, not
by test methods and not by page objects. That keeps report lifecycle code in
one support layer.

## Code Walkthrough

Main file:

[src/test/java/com/learning/tests/reports/ExtentReportManager.java](../../src/test/java/com/learning/tests/reports/ExtentReportManager.java)

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

## Runtime Flow

Extent flow in Module 14:

```text
FrameworkTestListener.onStart
    -> ExtentReportManager.initialize(suiteName)
FrameworkTestListener.onTestStart
    -> ExtentReportManager.startTest(result, displayName)
FrameworkTestListener.onTestSuccess
    -> ExtentReportManager.pass("Test passed")
FrameworkTestListener.onTestFailure
    -> ScreenshotUtils.capture(...)
    -> ExtentReportManager.fail(throwable, screenshotPath)
FrameworkTestListener.onTestSkipped
    -> ExtentReportManager.skip(throwable)
FrameworkTestListener.onFinish
    -> ExtentReportManager.flush()
```

This means Extent sees status through TestNG lifecycle callbacks. The test
methods do not call Extent APIs.

## Java Syntax To Notice

`ThreadLocal<ExtentTest>` matters because each running test needs its own
current report entry. Module 15 will make this more important when tests run
in parallel.

`synchronized` on `initialize` and `flush` protects shared report setup and
final writing. The report object is shared; the current test entry is
thread-local.

## Code Walkthrough

```java
private static final Path REPORT_PATH = Paths.get("target", "extent-report", "extent.html");
```

The Extent HTML report is a generated artifact. It belongs under `target/`, not
in source control.

```java
Files.createDirectories(REPORT_PATH.getParent());
```

The manager creates `target/extent-report` before Extent writes the HTML file.

```java
sparkReporter.config().setDocumentTitle("Selenium Framework Report");
sparkReporter.config().setReportName(suiteName);
sparkReporter.config().setTheme(Theme.STANDARD);
```

These settings control report presentation. They do not affect test behavior.

```java
extentReports.setSystemInfo("Project", "Selenium Java UI Automation Learning Framework");
extentReports.setSystemInfo("Module", "Module 14 - Extent and Allure Reporting");
```

System info gives the report reader context about the run and module.

```java
ExtentTest extentTest = extentReports.createTest(displayName)
        .assignCategory(result.getMethod().getGroups());
```

The display name comes from the listener. TestNG groups become Extent
categories, so `smoke` and `regression` are visible in the report.

## Framework Nuances

`flush()` is required. Without it, the report may not finish writing its HTML
content to disk.

Screenshot attachment happens only when a screenshot path exists. Passed tests
do not need screenshots by default because screenshots on every pass create
heavy artifacts without much diagnostic value.

`assignCategory(result.getMethod().getGroups())` maps TestNG groups such as
`smoke` and `regression` into Extent categories.

`CURRENT_TEST.remove()` is called after pass, fail, and skip. That clears the
thread-local Extent node after the test status is recorded, which matters for
parallel execution in Module 15.

The manager intentionally does not know SauceDemo page objects. It only knows
TestNG result metadata, report status, and optional screenshot paths.

## Common Mistakes

- Creating a new `ExtentReports` object inside every test.
- Forgetting `flush()`.
- Attaching screenshots before the screenshot file exists.
- Writing report code inside page objects.
- Assuming Extent replaces logs. Reports summarize; logs preserve execution
  detail.
- Forgetting to remove the current `ExtentTest` from `ThreadLocal`.
- Attaching screenshots on every passing test and creating noisy, heavy
  reports.
- Creating report entries with unsafe DataProvider parameter text.

## Interview Readiness

Strong answer:

"Extent Reports is an HTML reporting library. In a TestNG Selenium framework I
usually create the report once per suite, create one `ExtentTest` per test
method, update status from a listener, attach screenshots on failure, and flush
the report at the end."

## Debugging Checklist

If `target/extent-report/extent.html` is missing:

- confirm the run used [testng.xml](../../testng.xml), so
  [FrameworkTestListener.java](../../src/test/java/com/learning/tests/listeners/FrameworkTestListener.java)
  was registered.
- confirm `ExtentReportManager.initialize(...)` ran in `onStart`.
- confirm `ExtentReportManager.flush()` ran in `onFinish`.
- inspect `target/logs/test-execution.log` for Extent initialization or flush
  messages.

## Revision Checklist

- Can you explain the difference between `ExtentReports`, `ExtentSparkReporter`,
  and `ExtentTest`?
- Can you show where the Extent report is flushed?
- Can you explain why current test tracking uses `ThreadLocal`?
- Can you find the generated HTML report path?
- Can you explain why Extent categories come from TestNG groups?
- Can you explain why Extent integration belongs in the listener layer?
