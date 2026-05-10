# Runbook And Portfolio Guide

This page gives the practical commands and talking points for using the final
framework as a portfolio project.

## Local Runbook

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

## Demo Flow

For a portfolio walkthrough, use this sequence:

1. Open `README.md` and explain the final framework snapshot.
2. Open `docs/README.md` and show the module-by-module learning path.
3. Open `LoginPage.java` and explain Page Object encapsulation.
4. Open `ElementActions.java` and explain wrapper methods.
5. Open `DriverFactory.java` and explain config-driven local/Grid execution.
6. Open `BaseTest.java` and explain TestNG lifecycle plus ThreadLocal.
7. Open `saucedemo_login.feature` and explain Cucumber as a top layer.
8. Open `.github/workflows/ui-tests.yml` and explain CI scopes and artifacts.

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

## Known Limitations To State Honestly

The framework uses public demo sites, so external availability can affect
execution.

CI runs local headless Chrome only. Browser matrix and Grid-in-CI are documented
future enhancements.

Allure is uploaded as an artifact, not published as a hosted report.

The repository intentionally contains rich learning comments and docs. A
production-only framework would make comments more concise after the team has
internalized the design.
