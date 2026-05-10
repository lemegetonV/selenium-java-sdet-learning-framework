# Runner, Hooks, And Glue

This module adds three Cucumber Java packages:

| Package | Role |
| --- | --- |
| `com.learning.tests.bdd.runners` | TestNG entry point for Cucumber. |
| `com.learning.tests.bdd.hooks` | Scenario lifecycle setup and cleanup. |
| `com.learning.tests.bdd.steps` | Java bindings for Gherkin sentences. |

## Runner Walkthrough

The runner is:

```text
src/test/java/com/learning/tests/bdd/runners/CucumberTest.java
```

`CucumberTest` extends `AbstractTestNGCucumberTests`. That base class adapts
Cucumber scenarios into TestNG-executable rows. This is why Maven Surefire can
run Cucumber through the same build path as the rest of the project.

Important options:

- `features` points to `src/test/resources/features`.
- `glue` lists the hook and step-definition packages.
- `plugin` creates readable Cucumber reports and Allure Cucumber results.
- `tags = "@bdd"` prevents unrelated future feature files from running unless
  they are intentionally marked for this runner.

The runner overrides `scenarios()` because Cucumber-TestNG exposes scenarios
through a TestNG `DataProvider`. Module 16 keeps `parallel = false` so debugging
is simple. Because Module 15 already made driver ownership ThreadLocal, later
sessions can safely revisit parallel Cucumber execution.

## Hooks Walkthrough

The hooks file is:

```text
src/test/java/com/learning/tests/bdd/hooks/CucumberHooks.java
```

`@Before` runs before every scenario. It opens a browser through
`CucumberScenarioContext.openBrowser()`.

`@After` runs after every scenario. It captures and attaches a screenshot when a
scenario fails, then always closes the browser in a `finally` block.

The `finally` block matters. A failing assertion should not leave Chrome
sessions running. In automation interviews, this is a strong place to mention
resource cleanup and test isolation.

## Context Walkthrough

The scenario context is:

```text
src/test/java/com/learning/tests/bdd/context/CucumberScenarioContext.java
```

TestNG classes use `BaseTest`, but Cucumber step classes are created by
Cucumber. They do not inherit TestNG `@BeforeMethod` setup. This is why the BDD
layer has its own context class.

The context stores:

- `WebDriver`
- `WebDriverWait`
- `WaitUtils`
- `ElementActions`

Each value is held in a `ThreadLocal`. That repeats the Module 15 rule: browser
state belongs to the executing thread, not to shared static fields.

## Common Mistakes

Do not put `driver.findElement` calls directly in step definitions. That makes
Gherkin steps tightly coupled to HTML.

Do not use one static `WebDriver` for all Cucumber scenarios. It causes state
leakage and breaks parallel execution.

Do not make feature files depend on exact UI implementation language such as
button IDs or CSS classes.

Do not skip teardown when a scenario fails. Browser leaks make later failures
hard to diagnose.
