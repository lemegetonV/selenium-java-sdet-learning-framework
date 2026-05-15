# Module 14 Interview Review

## Core Topics

You should now be able to explain:

- TestNG default reports and their limits.
- Extent report setup and flush lifecycle.
- Allure result generation and HTML report generation.
- Allure labels such as epic, feature, story, and severity.
- Allure steps.
- screenshot attachments.
- report artifacts and safe data handling.
- why Allure raw results are different from an Allure HTML report.
- why report integration belongs in listeners/support classes.
- how DataProvider parameter masking protects reports.

## Strong Answer Framing

### Why add Extent Reports?

Extent gives a readable HTML report that can be opened as a single file. It is
good for local review and stakeholder-friendly summaries. In a framework, I
create it once per suite, create one report node per test, update status from a
listener, attach failure screenshots, and flush at the end.

In this checkpoint, [ExtentReportManager.java](../../src/test/java/com/learning/tests/reports/ExtentReportManager.java)
owns that lifecycle and [FrameworkTestListener.java](../../src/test/java/com/learning/tests/listeners/FrameworkTestListener.java)
calls it from TestNG callbacks.

### Why add Allure if Extent already exists?

Allure stores structured result files and then generates a rich report with
labels, steps, attachments, and dashboards. It is especially useful in CI
because raw result files can be archived and converted into reports.

The raw files live under `target/allure-results`; `mvn allure:report` converts
them into `target/allure-report`.

### Where should reporting code live?

Reporting code should live in framework support classes and listeners, not in
page objects. Tests may have lightweight labels or steps, but page objects
should stay focused on page behavior.

Page objects should not know whether the project uses Extent, Allure, both, or
neither.

### How do screenshots reach reports?

On failure, the TestNG listener calls `ScreenshotUtils` to save a screenshot.
The same path is then used by Extent and Allure attachment helpers. This avoids
multiple screenshot implementations.

Extent receives the path through `ExtentReportManager.fail(...)`; Allure
receives it through [AllureReportUtils.java](../../src/test/java/com/learning/tests/reports/AllureReportUtils.java).

### What reporting security issue did Module 14 fix?

Allure records DataProvider parameters. Since `LoginScenario` contains a
password, the record overrides `toString()` to show `password=****` in reports.

This protects implicit report output. It is not enough to avoid logging the
password manually; report frameworks can render parameter objects themselves.

## Vocabulary

- Artifact
- ExtentReports
- ExtentSparkReporter
- ExtentTest
- Flush
- Allure results
- Allure report
- Label
- Step
- Attachment
- Service-loaded listener
- Data masking

## Red Flags In Interviews

- "I put report code inside page objects."
- "I commit reports to source control."
- "Allure results are the same as the HTML report."
- "I log all test data for debugging."
- "I do not need to flush Extent."

## Practical Walkthrough

1. `mvn test -DsuiteXmlFile=testng.xml` runs TestNG.
2. `FrameworkTestListener` starts Extent test entries.
3. SauceDemo tests add Allure labels and steps.
4. Allure writes files to `target/allure-results`.
5. Extent writes `target/extent-report/extent.html` on flush.
6. `mvn allure:report` converts Allure results into `target/allure-report`.

If you can explain this flow, you understand the module.

## Debugging Questions

If Module 14 reporting does not look right, ask:

- Did the run use [testng.xml](../../testng.xml) so the framework listener was
  active?
- Did [ExtentReportManager.flush()](../../src/test/java/com/learning/tests/reports/ExtentReportManager.java)
  run in `onFinish`?
- Does `target/allure-results` exist before running `mvn allure:report`?
- Is Allure accidentally registered twice, once through service loading and
  once manually in XML?
- Are DataProvider parameters safe when rendered through
  [LoginScenario.toString()](../../src/test/java/com/learning/tests/models/LoginScenario.java)?
- Did a screenshot file exist before Extent/Allure tried to attach it?

## One-Minute Whiteboard Answer

Module 14 turns Module 13 diagnostics into reports. Extent is managed by
[ExtentReportManager.java](../../src/test/java/com/learning/tests/reports/ExtentReportManager.java):
initialize once, create one `ExtentTest` per test, record pass/fail/skip,
attach failure screenshots, and flush the HTML report. Allure collects raw
results during TestNG execution through `allure-testng`; tests add labels and
steps, and the listener attaches failure screenshots through
[AllureReportUtils.java](../../src/test/java/com/learning/tests/reports/AllureReportUtils.java).
`mvn allure:report` generates the final Allure HTML report from raw results.
