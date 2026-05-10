# Reporting, Tags, And Execution

Module 16 adds a dedicated suite file:

```text
testng-cucumber.xml
```

Run it with:

```bash
mvn test -DsuiteXmlFile=testng-cucumber.xml
```

## Reports

`CucumberTest.java` configures these Cucumber plugins:

| Plugin | Output |
| --- | --- |
| `pretty` | readable console scenario output |
| `html:target/cucumber-report/cucumber.html` | local Cucumber HTML report |
| `json:target/cucumber-report/cucumber.json` | machine-readable Cucumber report data |
| `io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm` | Allure results for Cucumber scenarios |

The Cucumber report is useful for quickly reading scenarios and steps. Allure
is useful when the whole framework report needs a richer dashboard.

## Tags

Feature-level tags:

- `@bdd`
- `@saucedemo`

Scenario-level tags:

- `@smoke`
- `@regression`
- `@login`
- `@checkout`

Tags are not comments. They are executable filters. Cucumber can filter
scenarios using tag expressions such as:

```bash
mvn test -DsuiteXmlFile=testng-cucumber.xml -Dcucumber.filter.tags="@smoke"
```

The runner also has `tags = "@bdd"`. Command-line tag filters combine with
runner filters, so a scenario must satisfy both.

## Nuances

Do not create too many tags. Tags should support execution decisions and test
ownership, not decorate every scenario with redundant labels.

Do not make one giant feature file. This module starts with one file because
the project is teaching the Cucumber connection. Real projects usually split
features by business capability.

Do not assume Cucumber reports replace Allure or Extent. Cucumber reports are
excellent for scenario readability. Allure and Extent are broader automation
reports with screenshots, metadata, and historical trends when connected to CI.

## Interview Readiness

Likely question:

> How do you run selected Cucumber scenarios?

Strong answer:

Use tags and tag expressions. For example, mark fast critical scenarios with
`@smoke`, then run them with `-Dcucumber.filter.tags="@smoke"`. In a TestNG
Cucumber setup, the runner class still goes through Maven Surefire or a TestNG
suite, but Cucumber decides which scenarios match the tag expression.

Likely question:

> Why do Cucumber frameworks still need Page Objects?

Strong answer:

Gherkin describes business behavior, and step definitions connect that behavior
to code. Page Objects still protect the automation from HTML details and keep
Selenium commands centralized. Without Page Objects, step definitions become
hard to reuse and expensive to maintain.
