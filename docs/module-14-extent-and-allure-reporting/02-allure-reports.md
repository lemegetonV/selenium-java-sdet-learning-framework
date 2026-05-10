# Allure Reports

## Mental Model

Allure works in two stages:

1. During tests, it writes structured result files under `target/allure-results`.
2. After tests, the Maven plugin converts those results into
   `target/allure-report`.

This makes Allure strong for dashboards, labels, steps, attachments, and CI
artifact workflows.

## Code Walkthrough

Dependency and plugin:

`pom.xml`

Configuration:

`src/test/resources/allure.properties`

```properties
allure.results.directory=target/allure-results
```

Allure labels and steps are demonstrated in:

- `src/test/java/com/learning/tests/saucedemo/SauceDemoPageObjectTest.java`
- `src/test/java/com/learning/tests/saucedemo/SauceDemoDataDrivenTest.java`

Example labels:

```java
@Epic("SauceDemo UI")
@Feature("Data-driven login")
@Story("JSON login data")
@Severity(SeverityLevel.NORMAL)
```

Example step:

```java
Allure.step("Open SauceDemo login page");
```

Screenshot attachment helper:

`src/test/java/com/learning/tests/reports/AllureReportUtils.java`

## Java Syntax To Notice

Allure labels are Java annotations. They do not change test logic. They attach
metadata that Allure can display in the generated report.

`Allure.step(...)` is a static API call. Module 14 uses simple no-op steps
because they are easy to read and do not require AspectJ weaving. More advanced
annotated `@Step` methods can be introduced later if the framework needs that
style.

## Framework Nuances

The `allure-testng` dependency can register its TestNG integration through
service loading. Adding the same listener explicitly in `testng.xml` can create
duplicate listener warnings, so Module 14 keeps Allure out of the XML and lets
the dependency integration handle result collection.

Allure records TestNG parameters. That is why `LoginScenario.toString()` masks
the password. Data masking is not cosmetic. Reports are often uploaded to CI,
shared with teams, or attached to tickets.

## Commands

Generate results:

```bash
mvn test -DsuiteXmlFile=testng.xml
```

Generate the HTML report:

```bash
mvn allure:report
```

Open an interactive local server when you actually want to inspect it:

```bash
mvn allure:serve
```

`allure:serve` is useful for manual review, but it is not used as an automated
quality gate because it starts a server process.

## Common Mistakes

- Expecting `target/allure-results` to be the final report.
- Forgetting to run `mvn allure:report` after generating results.
- Leaking credentials through DataProvider parameter output.
- Adding both service-loaded and XML-registered Allure listeners.
- Using Allure annotations as a substitute for clear test names.

## Interview Readiness

Strong answer:

"Allure collects structured results during the test run and then generates an
HTML report from those result files. In TestNG I use labels for hierarchy,
steps for readable workflow, and attachments for evidence such as screenshots.
I also make sure sensitive DataProvider values are masked because Allure records
parameters."

## Revision Checklist

- Can you explain the difference between Allure results and Allure report?
- Can you show where Allure results are configured?
- Can you explain what `@Epic`, `@Feature`, `@Story`, and `@Severity` add?
- Can you explain why `LoginScenario.toString()` masks the password?

