# Runbook And Portfolio Guide

This page gives the practical commands and talking points for using the final
framework as a portfolio project.

Use this page when you want to run the final checkpoint, rehearse a project
walkthrough, or prepare interview answers from the code.

## Local Runbook

Before running, verify:

- Java 21 is installed.
- Maven is available.
- Chrome is installed for the default browser path.
- the working tree is on `main` or `module-18-complete` if you want the final
  checkpoint.

Sequential framework regression:

```bash
mvn test -DsuiteXmlFile=testng.xml -Dheadless=true
```

Parallel framework regression:

```bash
mvn test -DsuiteXmlFile=testng-parallel.xml -Dheadless=true
```

Cucumber BDD suite:

```bash
mvn test -DsuiteXmlFile=testng-cucumber.xml -Dheadless=true
```

Full discovered-test regression:

```bash
mvn test -Dheadless=true
```

Allure report:

```bash
mvn allure:report
```

## Command Strategy

| Command | When To Use It | Expected Output |
| --- | --- | --- |
| `mvn test -DsuiteXmlFile=testng.xml -Dheadless=true` | main framework regression | TestNG SauceDemo suite, Extent, Surefire, Allure results |
| `mvn test -DsuiteXmlFile=testng-parallel.xml -Dheadless=true` | parallel safety proof | TestNG method-level parallel run with multiple browser threads |
| `mvn test -DsuiteXmlFile=testng-cucumber.xml -Dheadless=true` | BDD proof | Cucumber scenarios, Cucumber HTML/JSON, Allure results |
| `mvn test -Dheadless=true` | broad discovered-test run | all default-discovered tests, including raw learning tests |
| `mvn allure:report` | report generation after tests | `target/allure-report/index.html` |

For a portfolio demo, prefer the three focused suite commands first. They prove
the final framework layers directly. The full discovered-test run is useful as
a broad confidence check, but it also exercises older raw Selenium learning
tests that are less representative of the final framework architecture.

## Demo Flow

For a portfolio walkthrough, use this sequence:

1. Open [README.md](../../README.md) and explain the final framework snapshot.
2. Open [docs/README.md](../README.md) and show the module-by-module learning path.
3. Open [src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java) and explain Page Object encapsulation.
4. Open [src/main/java/com/learning/framework/actions/ElementActions.java](../../src/main/java/com/learning/framework/actions/ElementActions.java) and explain wrapper methods.
5. Open [src/main/java/com/learning/framework/driver/DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java) and explain config-driven local/Grid execution.
6. Open [src/test/java/com/learning/tests/base/BaseTest.java](../../src/test/java/com/learning/tests/base/BaseTest.java) and explain TestNG lifecycle plus ThreadLocal.
7. Open [src/test/resources/features/saucedemo_login.feature](../../src/test/resources/features/saucedemo_login.feature) and explain Cucumber as a top layer.
8. Open [.github/workflows/ui-tests.yml](../../.github/workflows/ui-tests.yml) and explain CI scopes and artifacts.

Add these short talking points:

- The repo is module-based because the project teaches why each abstraction
  appears, not only what the final abstraction looks like.
- The final framework uses dynamic `By` locators instead of PageFactory to keep
  locator ownership explicit.
- TestNG and Cucumber are both supported, but they share the same framework
  core.
- Parallel execution uses ThreadLocal driver and service ownership.
- CI uses scoped runs so pull-request feedback stays fast while broader checks
  remain available.

## Five-Minute Demo Script

1. Start with [README.md](../../README.md): explain the stack and final
   framework snapshot.
2. Open [docs/README.md](../README.md): show that this is a learning
   curriculum with historical module checkpoints.
3. Open [SauceDemoPageObjectTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoPageObjectTest.java):
   show a readable TestNG workflow.
4. Jump to [LoginPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java):
   explain encapsulation and page transitions.
5. Jump to [ElementActions.java](../../src/main/java/com/learning/framework/actions/ElementActions.java)
   and [WaitUtils.java](../../src/main/java/com/learning/framework/waits/WaitUtils.java):
   explain wrapper methods and waits.
6. Open [DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java):
   explain local/Grid, headless, and ThreadLocal.
7. Open [saucedemo_login.feature](../../src/test/resources/features/saucedemo_login.feature):
   show BDD as a top layer.
8. Open [.github/workflows/ui-tests.yml](../../.github/workflows/ui-tests.yml):
   explain CI scope selection and artifacts.

## Evidence To Show

A strong portfolio walkthrough shows both code structure and execution
evidence:

- [testng.xml](../../testng.xml) for the main TestNG suite.
- [testng-parallel.xml](../../testng-parallel.xml) for parallel execution.
- [testng-cucumber.xml](../../testng-cucumber.xml) for the BDD suite.
- [src/test/java/com/learning/tests/reports/ExtentReportManager.java](../../src/test/java/com/learning/tests/reports/ExtentReportManager.java)
  and [src/test/java/com/learning/tests/reports/AllureReportUtils.java](../../src/test/java/com/learning/tests/reports/AllureReportUtils.java)
  for report ownership.
- [src/main/java/com/learning/framework/screenshots/ScreenshotUtils.java](../../src/main/java/com/learning/framework/screenshots/ScreenshotUtils.java)
  and [src/test/java/com/learning/tests/listeners/FrameworkTestListener.java](../../src/test/java/com/learning/tests/listeners/FrameworkTestListener.java)
  for failure diagnostics.

Generated evidence paths:

| Evidence | Path |
| --- | --- |
| Surefire XML/HTML output | `target/surefire-reports/` |
| Extent report | `target/extent-report/extent.html` |
| Cucumber report | `target/cucumber-report/cucumber.html` |
| Cucumber JSON | `target/cucumber-report/cucumber.json` |
| Allure results | `target/allure-results/` |
| Allure HTML report | `target/allure-report/index.html` |
| failure screenshots | `target/screenshots/` |

## Resume Bullets

- Built a Java 21 Selenium WebDriver UI automation framework using Maven,
  TestNG, Page Object Model, wrapper actions, explicit waits, and configuration
  driven browser management.
- Added data-driven UI coverage using TestNG DataProviders with JSON, CSV, and
  Excel test data sources.
- Implemented diagnostics with Log4j2, screenshots, retry handling, Extent
  Reports, Allure reporting, and Cucumber reports.
- Added parallel TestNG execution with ThreadLocal WebDriver isolation and
  configurable Selenium Grid support.
- Integrated Cucumber BDD scenarios and GitHub Actions CI with smoke,
  regression, BDD, parallel, and full test scopes.

## Interview Story Bank

Use these examples when asked for design decisions:

- Page Object decision: [LoginPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java)
  hides locators and exposes `open()`, `loginAs(...)`, and
  `loginExpectingError(...)`.
- Wrapper decision: [ElementActions.java](../../src/main/java/com/learning/framework/actions/ElementActions.java)
  centralizes waits around clicks, typing, and element lookup.
- Parallel decision: [BaseTest.java](../../src/test/java/com/learning/tests/base/BaseTest.java)
  and [DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java)
  keep WebDriver and services thread-local.
- BDD decision: [SauceDemoSteps.java](../../src/test/java/com/learning/tests/bdd/steps/SauceDemoSteps.java)
  calls page objects instead of Selenium locators.
- CI decision: [.github/workflows/ui-tests.yml](../../.github/workflows/ui-tests.yml)
  maps smoke, regression, BDD, parallel, and full scopes to Maven commands.

## Known Limitations To State Honestly

The framework uses public demo sites, so external availability can affect
execution.

CI runs local headless Chrome only. Browser matrix and Grid-in-CI are documented
future enhancements.

Allure is uploaded as an artifact, not published as a hosted report.

The repository intentionally contains rich learning comments and docs. A
production-only framework would make comments more concise after the team has
internalized the design.

## Future Enhancements

Good future work should build on the existing design:

- Browser matrix: update [.github/workflows/ui-tests.yml](../../.github/workflows/ui-tests.yml)
  to pass `-Dbrowser=chrome/firefox/edge` through a GitHub Actions matrix.
- Grid in CI: add a Selenium Grid service/container and run with
  `-DexecutionMode=grid`.
- Hosted Allure: publish `target/allure-report/` through GitHub Pages.
- Dockerized local run: add a Dockerfile or Compose file that standardizes Java,
  Maven, browser, and optional Grid versions.

These are intentionally not included in Module 18 so the final learning
project remains readable and runnable without extra infrastructure.
