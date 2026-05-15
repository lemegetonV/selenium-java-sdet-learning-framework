# Module 14 Exercises

Use these exercises after reading:

- [ExtentReportManager.java](../../src/test/java/com/learning/tests/reports/ExtentReportManager.java)
- [AllureReportUtils.java](../../src/test/java/com/learning/tests/reports/AllureReportUtils.java)
- [FrameworkTestListener.java](../../src/test/java/com/learning/tests/listeners/FrameworkTestListener.java)
- [LoginScenario.java](../../src/test/java/com/learning/tests/models/LoginScenario.java)
- [SauceDemoPageObjectTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoPageObjectTest.java)
- [SauceDemoDataDrivenTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoDataDrivenTest.java)
- [allure.properties](../../src/test/resources/allure.properties)
- [testng.xml](../../testng.xml)

## Exercise 1 - Generate Both Reports

Run:

```bash
mvn test -DsuiteXmlFile=testng.xml
mvn allure:report
```

Find:

- `target/extent-report/extent.html`
- `target/allure-results/`
- `target/allure-report/`

Expected outcome:

You can explain which command creates each artifact.

Revision question:

- why does `mvn test` create `target/allure-results`, while
  `mvn allure:report` creates `target/allure-report`?

## Exercise 2 - Compare Report Styles

Inspect the Extent HTML report and generated Allure report.

Answer:

- Which one is faster to open directly?
- Which one shows labels and steps more naturally?
- Which one feels better for a CI dashboard?

Expected outcome:

You can compare tools instead of saying one is always better.

Revision question:

- which report would you attach to a quick local bug note, and which would you
  prefer for a CI dashboard?

## Exercise 3 - Trace Screenshot Attachment Flow

Read:

- [src/test/java/com/learning/tests/listeners/FrameworkTestListener.java](../../src/test/java/com/learning/tests/listeners/FrameworkTestListener.java)
- [src/test/java/com/learning/tests/reports/ExtentReportManager.java](../../src/test/java/com/learning/tests/reports/ExtentReportManager.java)
- [src/test/java/com/learning/tests/reports/AllureReportUtils.java](../../src/test/java/com/learning/tests/reports/AllureReportUtils.java)

Write the flow from `onTestFailure` to Extent and Allure attachment.

Hint:

The screenshot is captured once and reused.

Expected outcome:

You can explain that the listener captures the screenshot through
`ScreenshotUtils`, Extent attaches the screenshot path, and Allure attaches the
same file through an input stream.

## Exercise 4 - Verify Safe Data

Run:

```bash
rg -n "secret_sauce" target/extent-report target/allure-results target/logs/test-execution.log
```

Expected outcome:

There should be no matches. If a future change leaks the password, inspect
`LoginScenario.toString()` first.

Revision question:

- why can source test data contain the demo password while generated reports
  should not expose it?

## Exercise 5 - Explain Allure Labels

Open:

- [src/test/java/com/learning/tests/saucedemo/SauceDemoPageObjectTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoPageObjectTest.java)
- [src/test/java/com/learning/tests/saucedemo/SauceDemoDataDrivenTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoDataDrivenTest.java)

Explain:

- why `@Epic` is broader than `@Feature`.
- why `@Story` is close to a scenario or behavior.
- why severity is reporting metadata, not assertion logic.

Expected outcome:

You can use Allure labels intentionally instead of decorating tests randomly.

## Exercise 6 - Trace Extent Flush

Read:

- [src/test/java/com/learning/tests/listeners/FrameworkTestListener.java](../../src/test/java/com/learning/tests/listeners/FrameworkTestListener.java)
- [src/test/java/com/learning/tests/reports/ExtentReportManager.java](../../src/test/java/com/learning/tests/reports/ExtentReportManager.java)

Answer:

- where is Extent initialized?
- where is the current `ExtentTest` created?
- where is the report flushed?
- what could happen if `flush()` is removed?

Expected outcome:

You can explain the full Extent lifecycle from suite start to HTML file write.

## Exercise 7 - Explain Safe Parameter Rendering

Read:

[src/test/java/com/learning/tests/models/LoginScenario.java](../../src/test/java/com/learning/tests/models/LoginScenario.java)

Answer:

- why does the record still store the real password?
- why does `toString()` mask it?
- which tools might call `toString()` implicitly?

Expected outcome:

You can distinguish execution data from report display data.

## Exercise 8 - Find The Allure Integration Boundary

Read:

- [pom.xml](../../pom.xml)
- [testng.xml](../../testng.xml)
- [src/test/resources/allure.properties](../../src/test/resources/allure.properties)

Answer:

- where is `allure-testng` added?
- why is the Allure listener not listed in XML?
- where is the Allure results directory configured?

Expected outcome:

You can explain service-loaded Allure integration and avoid duplicate listener
registration.
