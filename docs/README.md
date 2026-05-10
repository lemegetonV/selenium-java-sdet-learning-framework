# Selenium Framework Learning Documentation

This documentation is organized by module. Each module teaches the concept,
shows the exact files introduced or changed, explains Java/Selenium/framework
nuances, and includes exercises for revision.

## Module Index

| Module | Topic | Documentation |
| --- | --- | --- |
| 01 | Java OOP Foundation | [docs/module-01-java-oops-foundation](module-01-java-oops-foundation/00-module-overview.md) |
| 02 | OOP for Selenium | [docs/module-02-oops-for-selenium](module-02-oops-for-selenium/00-module-overview.md) |
| 03 | First Selenium Tests | [docs/module-03-first-selenium-tests](module-03-first-selenium-tests/00-module-overview.md) |
| 04 | Locators and Web Elements | [docs/module-04-locators-and-web-elements](module-04-locators-and-web-elements/00-module-overview.md) |
| 05 | Waits and Dynamic Elements | [docs/module-05-waits-and-dynamic-elements](module-05-waits-and-dynamic-elements/00-module-overview.md) |
| 06 | Forms, Alerts, Dropdowns | [docs/module-06-forms-alerts-dropdowns](module-06-forms-alerts-dropdowns/00-module-overview.md) |
| 07 | Windows, Frames, Files, Actions | [docs/module-07-windows-frames-files-actions](module-07-windows-frames-files-actions/00-module-overview.md) |
| 08 | TestNG Framework Foundation | [docs/module-08-testng-framework-foundation](module-08-testng-framework-foundation/00-module-overview.md) |
| 09 | Page Object Model | [docs/module-09-page-object-model](module-09-page-object-model/00-module-overview.md) |
| 10 | Wrapper Methods and Waits | [docs/module-10-wrapper-methods-and-waits](module-10-wrapper-methods-and-waits/00-module-overview.md) |
| 11 | Config and Driver Factory | [docs/module-11-config-and-driver-factory](module-11-config-and-driver-factory/00-module-overview.md) |
| 12 | Data Driven Testing | [docs/module-12-data-driven-testing](module-12-data-driven-testing/00-module-overview.md) |
| 13 | Listeners, Screenshots, Logging | [docs/module-13-listeners-screenshots-logging](module-13-listeners-screenshots-logging/00-module-overview.md) |
| 14 | Extent and Allure Reporting | [docs/module-14-extent-and-allure-reporting](module-14-extent-and-allure-reporting/00-module-overview.md) |
| 15 | Parallel Execution and Selenium Grid | [docs/module-15-parallel-and-grid](module-15-parallel-and-grid/00-module-overview.md) |
| 16 | Cucumber BDD | [docs/module-16-cucumber-bdd](module-16-cucumber-bdd/00-module-overview.md) |
| 17 | CI/CD | [docs/module-17-cicd](module-17-cicd/00-module-overview.md) |
| 18 | Capstone and Portfolio Packaging | [docs/module-18-capstone-and-portfolio](module-18-capstone-and-portfolio/00-module-overview.md) |

## Source Map

| Source Area | Meaning |
| --- | --- |
| `src/main/java/com/learning/examples/moduleXX/` | early learning-only Java examples |
| `src/test/java/com/learning/tests/learning/` | raw Selenium concept tests |
| `src/main/java/com/learning/framework/` | reusable framework code |
| `src/main/java/com/learning/framework/pages/` | Page Objects |
| `src/test/java/com/learning/tests/base/` | TestNG lifecycle base classes |
| `src/test/java/com/learning/tests/saucedemo/` | framework TestNG tests |
| `src/test/java/com/learning/tests/bdd/` | Cucumber BDD layer |
| `src/test/resources/features/` | Gherkin feature files |
| `src/test/resources/testdata/` | external test data |

## How To Study The Repo

Start with Module 01 if Java/OOP fundamentals need revision. Start at Module
08 if the goal is to focus only on framework construction. Start at Module 09
if the goal is Page Object Model interview preparation.

Use tags for historical checkpoints:

```bash
git checkout module-09-complete
git checkout module-14-complete
git checkout module-18-complete
```

Return to latest main when finished:

```bash
git checkout main
```

## Final Verification Commands

```bash
mvn test -DsuiteXmlFile=testng.xml -Dheadless=true
mvn test -DsuiteXmlFile=testng-parallel.xml -Dheadless=true
mvn test -DsuiteXmlFile=testng-cucumber.xml -Dheadless=true
mvn test -Dheadless=true
mvn allure:report
```
