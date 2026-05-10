# Selenium Java UI Automation Learning Framework

![Java 21](https://img.shields.io/badge/Java-21-blue)
![Selenium](https://img.shields.io/badge/Selenium-WebDriver-43B02A)
![TestNG](https://img.shields.io/badge/TestNG-framework-orange)
![Cucumber](https://img.shields.io/badge/Cucumber-BDD-23D96C)
![Maven](https://img.shields.io/badge/Maven-build-C71A36)

This repository is a progressive Selenium Java UI automation learning
framework. It starts with Java and OOP foundations, then evolves module by
module into a production-style Selenium framework using TestNG, Page Object
Model, wrapper methods, waits, configuration, data-driven testing, diagnostics,
Extent Reports, Allure, parallel execution, Selenium Grid support, Cucumber
BDD, and GitHub Actions CI.

The project is designed for SDET learning and interview readiness. It does not
jump directly to the final architecture. Each module teaches the problem first,
then introduces the next framework layer that solves it.

## Final Framework Snapshot

| Area | Implementation |
| --- | --- |
| Language | Java 21 |
| Build | Maven |
| Browser automation | Selenium WebDriver |
| Test runner | TestNG |
| BDD layer | Cucumber with TestNG runner |
| Application under test | SauceDemo |
| Selenium playground | The Internet plus limited local fixtures |
| Page model | Dynamic `By` locators, no PageFactory |
| Driver lifecycle | `DriverFactory` with local/Grid execution modes |
| Actions and waits | `ElementActions` and `WaitUtils` wrappers |
| Data | TestNG DataProvider, JSON, CSV, Excel |
| Diagnostics | Log4j2, screenshots, retry analyzer, TestNG listener |
| Reports | Surefire, Extent, Allure, Cucumber HTML/JSON |
| Parallel execution | TestNG method-level parallel suite with ThreadLocal drivers |
| CI | GitHub Actions workflow with selectable UI test scopes |

## Architecture

```text
Gherkin feature or TestNG test
        |
        v
Step definition or test class
        |
        v
Page Object
        |
        v
ElementActions and WaitUtils
        |
        v
DriverFactory and ConfigReader
        |
        v
Selenium WebDriver browser session
```

Core source paths:

| Path | Purpose |
| --- | --- |
| `src/main/java/com/learning/framework/` | reusable framework code |
| `src/main/java/com/learning/framework/pages/saucedemo/` | SauceDemo Page Objects |
| `src/test/java/com/learning/tests/base/` | TestNG browser lifecycle |
| `src/test/java/com/learning/tests/saucedemo/` | framework-style TestNG tests |
| `src/test/java/com/learning/tests/bdd/` | Cucumber runner, hooks, context, and steps |
| `src/test/java/com/learning/tests/learning/` | raw Selenium learning tests |
| `src/test/resources/features/` | Cucumber feature files |
| `src/test/resources/testdata/` | JSON, CSV, and Excel data |
| `docs/` | module-by-module learning documentation |

## Run Locally

Prerequisites:

- Java 21.
- Maven.
- Chrome, Firefox, or Edge installed locally.

Install dependencies and run all discovered tests:

```bash
mvn test
```

Run the main framework regression:

```bash
mvn test -DsuiteXmlFile=testng.xml
```

Run the parallel regression:

```bash
mvn test -DsuiteXmlFile=testng-parallel.xml
```

Run the Cucumber BDD suite:

```bash
mvn test -DsuiteXmlFile=testng-cucumber.xml
```

Run smoke checks:

```bash
mvn test -DsuiteXmlFile=testng.xml -Dgroups=smoke
mvn test -DsuiteXmlFile=testng-cucumber.xml -Dcucumber.filter.tags="@smoke"
```

Run with a visible browser:

```bash
mvn test -DsuiteXmlFile=testng.xml -Dheadless=false
```

Generate Allure after tests:

```bash
mvn allure:report
```

## Configuration

Default configuration lives in:

```text
src/test/resources/config/config.properties
```

Common overrides:

```bash
mvn test -Dbrowser=chrome
mvn test -Dbrowser=firefox
mvn test -Dheadless=false
mvn test -DexecutionMode=grid -DgridUrl=http://localhost:4444
```

Supported browser values:

- `chrome`
- `firefox`
- `edge`

Supported execution modes:

- `local`
- `grid`

## Reports And Artifacts

Generated files are intentionally ignored by Git.

| Output | Path |
| --- | --- |
| Surefire/TestNG reports | `target/surefire-reports/` |
| Extent report | `target/extent-report/extent.html` |
| Cucumber report | `target/cucumber-report/cucumber.html` |
| Cucumber JSON | `target/cucumber-report/cucumber.json` |
| Allure results | `target/allure-results/` |
| Allure report | `target/allure-report/` |
| Screenshots | `target/screenshots/` |
| Logs | `logs/` |

## CI

GitHub Actions workflow:

```text
.github/workflows/ui-tests.yml
```

Workflow scopes:

| Scope | Purpose |
| --- | --- |
| `smoke` | default push and pull request confidence check |
| `regression` | TestNG framework regression plus Cucumber BDD suite |
| `bdd` | Cucumber-only validation |
| `parallel` | TestNG parallel regression |
| `full` | all discovered tests plus parallel suite |

The workflow runs headless Chrome on Java 21, uploads reports and screenshots
as artifacts, and supports manual scope selection through `workflow_dispatch`.

## Learning Path

The curriculum is documented under [docs/README.md](docs/README.md).

Completed checkpoints are tagged from `module-01-complete` through
`module-18-complete`. A learner can check out any module tag and study the
framework exactly as it existed at that point.

## Interview Talking Points

This project can be used to discuss:

- why Page Objects reduce locator duplication.
- how wrapper methods centralize Selenium commands and waits.
- why `ThreadLocal` matters for parallel WebDriver execution.
- how TestNG DataProviders separate test data from test flow.
- how listeners, screenshots, logs, Extent, and Allure improve diagnostics.
- how Cucumber fits above Page Objects without replacing the framework.
- how CI selects fast smoke checks versus broader regression checks.

## Known Limitations

- SauceDemo and The Internet are public training sites, so network or site
  availability can affect local and CI runs.
- Selenium Grid support is implemented, but the CI workflow does not start a
  Grid service container.
- Cross-browser matrix execution is documented as a future enhancement, not
  enabled by default.
- Allure reports are generated as artifacts, not published to GitHub Pages.
- This is a learning framework; comments and docs are intentionally richer than
  normal production code.
