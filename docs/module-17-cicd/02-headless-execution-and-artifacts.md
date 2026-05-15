# Headless Execution And Artifacts

CI runners are not the same as a developer laptop. This module keeps the
browser in headless mode and uploads artifacts so failures can be investigated
after the runner disappears.

## Headless Browser Model

Headless Chrome runs without a visible browser window. Selenium commands still
drive a real browser engine, but there is no desktop UI for a human to watch.

The workflow passes:

```text
-Dheadless=true
```

That system property is read by the framework through
[src/main/java/com/learning/framework/config/ConfigReader.java](../../src/main/java/com/learning/framework/config/ConfigReader.java),
which eventually affects
[src/main/java/com/learning/framework/driver/DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java)
and browser options. The CI workflow should not create a separate driver path.
It should reuse the same framework configuration path that local execution
uses.

## Why Artifacts Matter

GitHub-hosted runners are temporary. After a job finishes, files in `target/`
are gone unless the workflow uploads them.

The workflow uploads:

| Artifact | Source Path | Reason |
| --- | --- | --- |
| `surefire-reports` | `target/surefire-reports/` | Maven/TestNG pass-fail details and XML output. |
| `extent-report` | `target/extent-report/` | Framework HTML report from Module 14. |
| `cucumber-report` | `target/cucumber-report/` | Cucumber HTML and JSON report from Module 16. |
| `allure-output` | `target/allure-results/`, `target/allure-report/` | Raw Allure results and generated Allure report. |
| `screenshots` | `target/screenshots/` | Failure screenshots from listeners and Cucumber hooks. |

Every artifact step uses `if: always()`. That means reports are uploaded even
when tests fail. This is critical for UI automation because the most valuable
evidence is produced during failure.

## Source Walkthrough

Open [.github/workflows/ui-tests.yml](../../.github/workflows/ui-tests.yml)
and read the artifact upload steps after the Maven execution step. Each upload
maps back to a framework responsibility introduced earlier:

- [src/test/java/com/learning/tests/listeners/FrameworkTestListener.java](../../src/test/java/com/learning/tests/listeners/FrameworkTestListener.java)
  attaches TestNG failure evidence.
- [src/main/java/com/learning/framework/screenshots/ScreenshotUtils.java](../../src/main/java/com/learning/framework/screenshots/ScreenshotUtils.java)
  writes screenshots into the target directory.
- [src/test/java/com/learning/tests/reports/ExtentReportManager.java](../../src/test/java/com/learning/tests/reports/ExtentReportManager.java)
  owns the Extent report lifecycle.
- [src/test/java/com/learning/tests/reports/AllureReportUtils.java](../../src/test/java/com/learning/tests/reports/AllureReportUtils.java)
  attaches Allure-friendly evidence.
- [src/test/java/com/learning/tests/bdd/hooks/CucumberHooks.java](../../src/test/java/com/learning/tests/bdd/hooks/CucumberHooks.java)
  captures BDD scenario context when Cucumber runs in CI.

The important design point is ownership. The workflow uploads files, but it
does not decide what a screenshot means, how a report is structured, or when a
browser should be closed. Those decisions stay inside the framework classes.

## Nuances

`if-no-files-found: ignore` is intentional. For example, a passing run may not
create screenshots. The workflow should not fail just because there was no
failure screenshot to upload.

`continue-on-error: true` is used only for `mvn allure:report`. If tests fail,
the job should still try to generate a report, but a report-generation issue
should not hide the original test failure.

Chrome and Selenium versions on GitHub-hosted runners can change over time.
That is why the workflow prints `google-chrome --version`. If a test starts
failing only in CI, browser version is one of the first facts to check.

## How This Connects To Module 18

Module 18 can package this project as a portfolio-ready framework. CI evidence
will matter there because a portfolio repo is stronger when a reviewer can see:

- tests run automatically.
- reports are generated.
- failures retain useful artifacts.
- the README explains how to run local and CI scopes.
