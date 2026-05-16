# Module 16 - Cucumber BDD

Module 16 adds Cucumber as a behavior-driven testing layer on top of the
framework built so far. The important design point is that Cucumber does not
replace Selenium, TestNG, Page Objects, wrappers, waits, driver management, or
reporting. It replaces only the top-level test expression: scenarios are now
written in Gherkin and translated into the same framework services through step
definitions.

The module should be read as an integration module, not as a rewrite. The same
SauceDemo workflows already tested through TestNG are now expressed through
business-readable scenarios in [saucedemo_login.feature](../../src/test/resources/features/saucedemo_login.feature).
The Java glue layer then routes those scenarios back into the existing page
objects and framework services.

## Why This Module Exists Now

BDD is useful only after the lower layers are stable. Modules 09 through 15
already introduced Page Objects, wrapper methods, configuration, driver
lifecycle, data, diagnostics, reporting, and parallel-safe browser ownership.
That means Module 16 can focus on the real Cucumber question: how does a plain
language scenario become a browser test without turning step definitions into a
second framework?

```mermaid
flowchart TD
    Feature[Feature file] --> Runner[CucumberTest runner]
    Runner --> Glue[Hooks and step definitions]
    Glue --> Pages[SauceDemo Page Objects]
    Pages --> Actions[ElementActions and WaitUtils]
    Actions --> Driver[DriverFactory WebDriver session]
    Glue --> Reports[Cucumber HTML JSON and Allure results]
```

## How To Study This Module

Read the files in this order:

1. [saucedemo_login.feature](../../src/test/resources/features/saucedemo_login.feature)
   to understand the behavior language first.
2. [CucumberTest.java](../../src/test/java/com/learning/tests/bdd/runners/CucumberTest.java)
   to see how Cucumber is connected to TestNG and Maven Surefire.
3. [testng-cucumber.xml](../../testng-cucumber.xml) to see the TestNG suite
   entry point for the Cucumber runner.
4. [CucumberHooks.java](../../src/test/java/com/learning/tests/bdd/hooks/CucumberHooks.java)
   to see per-scenario setup, screenshot attachment, and cleanup.
5. [CucumberScenarioContext.java](../../src/test/java/com/learning/tests/bdd/context/CucumberScenarioContext.java)
   to see how the BDD layer gets the same driver, waits, and actions as the
   TestNG layer without inheriting `BaseTest`.
6. [SauceDemoSteps.java](../../src/test/java/com/learning/tests/bdd/steps/SauceDemoSteps.java)
   to see the step-to-page-object adapter layer.
7. [pom.xml](../../pom.xml) to connect the added Cucumber and Allure Cucumber
   dependencies to the runner options.

The learning goal is to trace one scenario end to end:

```mermaid
sequenceDiagram
    participant Feature as Feature file
    participant Runner as CucumberTest
    participant Hooks as CucumberHooks
    participant Context as CucumberScenarioContext
    participant Steps as SauceDemoSteps
    participant Pages as Page Objects
    participant Driver as WebDriver

    Feature->>Runner: Scenario selected by @bdd tag
    Runner->>Hooks: Before scenario
    Hooks->>Context: openBrowser()
    Context->>Driver: DriverFactory.createDriver()
    Runner->>Steps: Execute matching step methods
    Steps->>Pages: Call page object methods
    Pages->>Driver: Browser automation through wrappers
    Runner->>Hooks: After scenario
    Hooks->>Context: closeBrowser()
```

## Files Added Or Changed

| File | Status | Ownership | Purpose |
| --- | --- | --- | --- |
| [pom.xml](../../pom.xml) | Changed | Build configuration | Adds `cucumber-java`, `cucumber-testng`, and `allure-cucumber7-jvm`. |
| [testng-cucumber.xml](../../testng-cucumber.xml) | Added | Suite configuration | Runs the Cucumber runner through TestNG. |
| [src/test/resources/features/saucedemo_login.feature](../../src/test/resources/features/saucedemo_login.feature) | Added | BDD feature | Defines login and checkout scenarios in Gherkin. |
| [src/test/java/com/learning/tests/bdd/runners/CucumberTest.java](../../src/test/java/com/learning/tests/bdd/runners/CucumberTest.java) | Added | Cucumber runner | Connects feature files, glue packages, tags, and Cucumber plugins. |
| [src/test/java/com/learning/tests/bdd/hooks/CucumberHooks.java](../../src/test/java/com/learning/tests/bdd/hooks/CucumberHooks.java) | Added | Cucumber lifecycle | Opens and closes one browser per scenario and attaches failure screenshots. |
| [src/test/java/com/learning/tests/bdd/context/CucumberScenarioContext.java](../../src/test/java/com/learning/tests/bdd/context/CucumberScenarioContext.java) | Added | Test support | Holds scenario-scoped framework services with `ThreadLocal`. |
| [src/test/java/com/learning/tests/bdd/steps/SauceDemoSteps.java](../../src/test/java/com/learning/tests/bdd/steps/SauceDemoSteps.java) | Added | Step definitions | Maps Gherkin steps to existing Page Object methods and assertions. |

## Source Ownership Model

| Source | Ownership Type | What Learners Should Notice |
| --- | --- | --- |
| [saucedemo_login.feature](../../src/test/resources/features/saucedemo_login.feature) | BDD specification | business behavior, tags, Background, Scenario, Scenario Outline, Examples, and DataTable |
| [CucumberTest.java](../../src/test/java/com/learning/tests/bdd/runners/CucumberTest.java) | runner | Cucumber options, TestNG adapter, plugins, tag filtering, sequential DataProvider |
| [testng-cucumber.xml](../../testng-cucumber.xml) | suite configuration | Maven/TestNG entry point for the Cucumber runner |
| [CucumberHooks.java](../../src/test/java/com/learning/tests/bdd/hooks/CucumberHooks.java) | scenario lifecycle | opens browser before every scenario and closes it in `finally` |
| [CucumberScenarioContext.java](../../src/test/java/com/learning/tests/bdd/context/CucumberScenarioContext.java) | scenario services | thread-local driver, wait, wrapper, and action access for Cucumber glue |
| [SauceDemoSteps.java](../../src/test/java/com/learning/tests/bdd/steps/SauceDemoSteps.java) | step adapter | maps Gherkin wording to page object calls and assertions |
| [pom.xml](../../pom.xml) | build configuration | Cucumber Java, Cucumber TestNG, and Allure Cucumber dependencies |

## Module Source Links

Use these links as the source-reading checklist for this checkpoint. They point only to files that exist at Module 16.

| File | Status | Why It Matters |
| --- | --- | --- |
| [AGENTS.md](../../AGENTS.md) | Changed | Module session metadata |
| [CLAUDE.md](../../CLAUDE.md) | Changed | Module session metadata |
| [pom.xml](../../pom.xml) | Changed | Maven build and dependency configuration |
| [src/test/java/com/learning/tests/bdd/context/CucumberScenarioContext.java](../../src/test/java/com/learning/tests/bdd/context/CucumberScenarioContext.java) | Added | Cucumber BDD test support |
| [src/test/java/com/learning/tests/bdd/hooks/CucumberHooks.java](../../src/test/java/com/learning/tests/bdd/hooks/CucumberHooks.java) | Added | Cucumber BDD test support |
| [src/test/java/com/learning/tests/bdd/runners/CucumberTest.java](../../src/test/java/com/learning/tests/bdd/runners/CucumberTest.java) | Added | Cucumber BDD test support |
| [src/test/java/com/learning/tests/bdd/steps/SauceDemoSteps.java](../../src/test/java/com/learning/tests/bdd/steps/SauceDemoSteps.java) | Added | Cucumber BDD test support |
| [src/test/resources/features/saucedemo_login.feature](../../src/test/resources/features/saucedemo_login.feature) | Added | Cucumber feature file |
| [testng-cucumber.xml](../../testng-cucumber.xml) | Added | TestNG suite configuration |

## Previous Files Reused

| File | Why It Matters In This Module |
| --- | --- |
| [src/main/java/com/learning/framework/driver/DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java) | Cucumber hooks reuse the same local/Grid driver creation rules. |
| [src/main/java/com/learning/framework/actions/ElementActions.java](../../src/main/java/com/learning/framework/actions/ElementActions.java) | Step definitions stay clean because Page Objects still use wrapper actions. |
| [src/main/java/com/learning/framework/waits/WaitUtils.java](../../src/main/java/com/learning/framework/waits/WaitUtils.java) | Cucumber does not add raw sleeps or duplicate wait logic. |
| `src/main/java/com/learning/framework/pages/saucedemo/*.java` | BDD scenarios exercise the same application model used by TestNG tests. |
| [src/main/java/com/learning/framework/screenshots/ScreenshotUtils.java](../../src/main/java/com/learning/framework/screenshots/ScreenshotUtils.java) | Failed Cucumber scenarios reuse the screenshot service from Module 13. |

## What This Module Adds

- Gherkin `Feature`, `Background`, `Scenario`, `Scenario Outline`, `Examples`,
  tags, and `DataTable`.
- A TestNG-compatible Cucumber runner using `AbstractTestNGCucumberTests`.
- Cucumber glue packages for hooks and step definitions.
- Scenario-scoped browser lifecycle separate from `BaseTest`.
- Cucumber HTML and JSON reports under `target/cucumber-report/`.
- Allure Cucumber result generation through the Cucumber plugin.

## What This Module Does Not Change

Module 16 does not replace the existing TestNG test layer. The TestNG suite in
[testng.xml](../../testng.xml) still runs [SauceDemoPageObjectTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoPageObjectTest.java)
and [SauceDemoDataDrivenTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoDataDrivenTest.java).
Cucumber adds another expression layer for behavior scenarios.

Module 16 also does not move Selenium commands into step definitions. Page
objects such as [LoginPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java),
[ProductsPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java),
[CartPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/CartPage.java),
and [CheckoutPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/CheckoutPage.java)
remain the application automation model.

The Cucumber suite does not register the TestNG `FrameworkTestListener` from
the earlier suites. Cucumber reporting is produced through the Cucumber runner
plugins in [CucumberTest.java](../../src/test/java/com/learning/tests/bdd/runners/CucumberTest.java),
while Allure receives Cucumber results through `AllureCucumber7Jvm`.

## Design Boundary

Cucumber introduces a new top-level language, but the ownership boundary stays
the same:

- Feature files describe behavior.
- Step definitions coordinate behavior.
- Page objects model pages and user flows.
- Wrapper services perform Selenium operations.
- Driver/context classes own browser lifecycle.
- Reporting plugins produce Cucumber and Allure artifacts.

If a step definition starts using locators, sleeps, JavaScript, or raw
`driver.findElement`, the BDD layer has started duplicating the framework.
This module intentionally avoids that.

## What Is Intentionally Deferred

- Large feature suites split by business domain.
- Parallel Cucumber scenario execution. The context is already ThreadLocal, but
  the runner stays sequential so learners can debug feature-to-step flow first.
- Cucumber dependency injection with PicoContainer, Spring, or Guice.
- Custom parameter types and data table transformers.
- CI tag filtering. Module 17 will decide which BDD tags run in pipelines.

## Quality Gate

Run the focused Cucumber suite:

```bash
mvn test -DsuiteXmlFile=testng-cucumber.xml
```

Expected result:

- 5 Cucumber scenarios pass.
- `target/cucumber-report/cucumber.html` is generated.
- `target/cucumber-report/cucumber.json` is generated.
- Allure result files are produced in `target/allure-results`.

Optional report check:

```bash
mvn allure:report
```

Expected result:

- `target/allure-report/index.html` is generated from the Cucumber results.

Run the existing TestNG framework suite:

```bash
mvn test -DsuiteXmlFile=testng.xml
```

Expected result:

- Existing SauceDemo TestNG tests still pass.
- Adding Cucumber did not break the non-BDD layer.

Run the full repository regression:

```bash
mvn test
```

Expected result:

- All learning tests, framework tests, and Cucumber scenarios pass.

Practical note: for documentation-only repair work, the focused Cucumber suite
and existing SauceDemo TestNG suite are the most relevant gates. A full
repository `mvn test` may exercise older raw Selenium learning tests whose
browser/session stability is outside this module's code changes.
