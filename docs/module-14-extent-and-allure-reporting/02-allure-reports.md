# Allure Reports

## Mental Model

Allure works in two stages:

1. During tests, it writes structured result files under `target/allure-results`.
2. After tests, the Maven plugin converts those results into
   `target/allure-report`.

This makes Allure strong for dashboards, labels, steps, attachments, and CI
artifact workflows.

The key difference from Extent is timing. Extent writes the final HTML report
at the end of the test suite. Allure first writes machine-readable result
files, then a separate Maven goal generates the final report.

## Code Walkthrough

Dependency and plugin:

[pom.xml](../../pom.xml)

Configuration:

[src/test/resources/allure.properties](../../src/test/resources/allure.properties)

```properties
allure.results.directory=target/allure-results
```

Allure labels and steps are demonstrated in:

- [src/test/java/com/learning/tests/saucedemo/SauceDemoPageObjectTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoPageObjectTest.java)
- [src/test/java/com/learning/tests/saucedemo/SauceDemoDataDrivenTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoDataDrivenTest.java)

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

[src/test/java/com/learning/tests/reports/AllureReportUtils.java](../../src/test/java/com/learning/tests/reports/AllureReportUtils.java)

## Runtime Flow

Allure flow in Module 14:

```text
mvn test -DsuiteXmlFile=testng.xml
    -> allure-testng observes TestNG tests
    -> @Epic/@Feature/@Story/@Severity become result metadata
    -> Allure.step(...) calls become step entries
    -> FrameworkTestListener attaches failure screenshots through AllureReportUtils
    -> target/allure-results is written

mvn allure:report
    -> reads target/allure-results
    -> writes target/allure-report
```

This is why `target/allure-results` and `target/allure-report` are different
artifacts.

## Java Syntax To Notice

Allure labels are Java annotations. They do not change test logic. They attach
metadata that Allure can display in the generated report.

`Allure.step(...)` is a static API call. Module 14 uses simple no-op steps
because they are easy to read and do not require AspectJ weaving. More advanced
annotated `@Step` methods can be introduced later if the framework needs that
style.

## Code Walkthrough

```java
@Epic("SauceDemo UI")
@Feature("Data-driven login")
```

Class-level labels provide broad report grouping.

```java
@Story("JSON login data")
@Severity(SeverityLevel.NORMAL)
```

Method-level labels describe the behavior and priority of a specific test.
They are report metadata; they do not change assertion logic or execution
order.

```java
Allure.step("Run login scenario: " + scenario.scenarioName());
```

Steps describe the workflow. Module 14 deliberately uses scenario names, not
full scenario objects, because the full object includes password-bearing data.

```java
Allure.addAttachment("Failure screenshot", "image/png", screenshotStream, ".png");
```

[AllureReportUtils.java](../../src/test/java/com/learning/tests/reports/AllureReportUtils.java)
keeps attachment stream handling out of
[FrameworkTestListener.java](../../src/test/java/com/learning/tests/listeners/FrameworkTestListener.java).

## Framework Nuances

The `allure-testng` dependency can register its TestNG integration through
service loading. Adding the same listener explicitly in [testng.xml](../../testng.xml) can create
duplicate listener warnings, so Module 14 keeps Allure out of the XML and lets
the dependency integration handle result collection.

Allure records TestNG parameters. That is why `LoginScenario.toString()` masks
the password. Data masking is not cosmetic. Reports are often uploaded to CI,
shared with teams, or attached to tickets.

Allure is not listed manually in [testng.xml](../../testng.xml). The
`allure-testng` dependency can integrate through TestNG service loading. Adding
the same listener explicitly can cause duplicate listener warnings and duplicate
result behavior.

The helper attaches screenshots only when the framework listener has already
captured one. This avoids having Extent and Allure take separate screenshots
for the same failure.

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
- Treating Allure labels as test selection logic.
- Putting sensitive values in step names.
- Expecting `mvn allure:serve` to be suitable for automated verification even
  though it starts a long-running server.

## Interview Readiness

Strong answer:

"Allure collects structured results during the test run and then generates an
HTML report from those result files. In TestNG I use labels for hierarchy,
steps for readable workflow, and attachments for evidence such as screenshots.
I also make sure sensitive DataProvider values are masked because Allure records
parameters."

## Label Model

| Label | Meaning In This Project |
| --- | --- |
| `@Epic("SauceDemo UI")` | broad product/application area |
| `@Feature("Page object workflows")` | major test capability |
| `@Story("Checkout start")` | specific behavior or scenario family |
| `@Severity(SeverityLevel.CRITICAL)` | reporting priority, not assertion logic |

Labels should help report readers navigate behavior. They should not become
decorative noise.

## Revision Checklist

- Can you explain the difference between Allure results and Allure report?
- Can you show where Allure results are configured?
- Can you explain what `@Epic`, `@Feature`, `@Story`, and `@Severity` add?
- Can you explain why `LoginScenario.toString()` masks the password?
- Can you explain why Allure is not manually registered in TestNG XML?
- Can you trace where failure screenshots are attached to Allure?
