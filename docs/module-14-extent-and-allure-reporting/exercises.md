# Module 14 Exercises

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

## Exercise 2 - Compare Report Styles

Inspect the Extent HTML report and generated Allure report.

Answer:

- Which one is faster to open directly?
- Which one shows labels and steps more naturally?
- Which one feels better for a CI dashboard?

Expected outcome:

You can compare tools instead of saying one is always better.

## Exercise 3 - Trace Screenshot Attachment Flow

Read:

- `src/test/java/com/learning/tests/listeners/FrameworkTestListener.java`
- `src/test/java/com/learning/tests/reports/ExtentReportManager.java`
- `src/test/java/com/learning/tests/reports/AllureReportUtils.java`

Write the flow from `onTestFailure` to Extent and Allure attachment.

Hint:

The screenshot is captured once and reused.

## Exercise 4 - Verify Safe Data

Run:

```bash
rg -n "secret_sauce" target/extent-report target/allure-results target/logs/test-execution.log
```

Expected outcome:

There should be no matches. If a future change leaks the password, inspect
`LoginScenario.toString()` first.

## Exercise 5 - Explain Allure Labels

Open:

- `src/test/java/com/learning/tests/saucedemo/SauceDemoPageObjectTest.java`
- `src/test/java/com/learning/tests/saucedemo/SauceDemoDataDrivenTest.java`

Explain:

- why `@Epic` is broader than `@Feature`.
- why `@Story` is close to a scenario or behavior.
- why severity is reporting metadata, not assertion logic.

Expected outcome:

You can use Allure labels intentionally instead of decorating tests randomly.

