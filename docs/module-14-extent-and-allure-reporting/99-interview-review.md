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

## Strong Answer Framing

### Why add Extent Reports?

Extent gives a readable HTML report that can be opened as a single file. It is
good for local review and stakeholder-friendly summaries. In a framework, I
create it once per suite, create one report node per test, update status from a
listener, attach failure screenshots, and flush at the end.

### Why add Allure if Extent already exists?

Allure stores structured result files and then generates a rich report with
labels, steps, attachments, and dashboards. It is especially useful in CI
because raw result files can be archived and converted into reports.

### Where should reporting code live?

Reporting code should live in framework support classes and listeners, not in
page objects. Tests may have lightweight labels or steps, but page objects
should stay focused on page behavior.

### How do screenshots reach reports?

On failure, the TestNG listener calls `ScreenshotUtils` to save a screenshot.
The same path is then used by Extent and Allure attachment helpers. This avoids
multiple screenshot implementations.

### What reporting security issue did Module 14 fix?

Allure records DataProvider parameters. Since `LoginScenario` contains a
password, the record overrides `toString()` to show `password=****` in reports.

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

