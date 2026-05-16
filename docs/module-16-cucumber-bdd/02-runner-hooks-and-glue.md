# Runner, Hooks, And Glue

This module adds three Cucumber Java packages:

| Package | Role |
| --- | --- |
| `com.learning.tests.bdd.runners` | TestNG entry point for Cucumber. |
| `com.learning.tests.bdd.hooks` | Scenario lifecycle setup and cleanup. |
| `com.learning.tests.bdd.steps` | Java bindings for Gherkin sentences. |

There is also a scenario context package:

| Package | Role |
| --- | --- |
| `com.learning.tests.bdd.context` | Scenario-scoped access to driver, waits, and wrapper services. |

Together these packages are called Cucumber glue. Glue is the Java code that
Cucumber searches when it needs hooks and step definitions for a feature file.

## Runner Walkthrough

The runner is:

[src/test/java/com/learning/tests/bdd/runners/CucumberTest.java](../../src/test/java/com/learning/tests/bdd/runners/CucumberTest.java)

`CucumberTest` extends `AbstractTestNGCucumberTests`. That base class adapts
Cucumber scenarios into TestNG-executable rows. This is why Maven Surefire can
run Cucumber through the same build path as the rest of the project.

Important options:

- `features` points to `src/test/resources/features`.
- `glue` lists the hook and step-definition packages.
- `plugin` creates readable Cucumber reports and Allure Cucumber results.
- `tags = "@bdd"` prevents unrelated future feature files from running unless
  they are intentionally marked for this runner.

The suite file [testng-cucumber.xml](../../testng-cucumber.xml) does not list
feature files directly. It lists the runner class:

```xml
<class name="com.learning.tests.bdd.runners.CucumberTest"/>
```

That keeps TestNG responsible for launching Java test classes and keeps
Cucumber responsible for discovering feature files and matching steps.

The runner overrides `scenarios()` because Cucumber-TestNG exposes scenarios
through a TestNG `DataProvider`. Module 16 keeps `parallel = false` so debugging
is simple. Because Module 15 already made driver ownership ThreadLocal, later
sessions can safely revisit parallel Cucumber execution.

### What The Runner Does Not Do

[CucumberTest.java](../../src/test/java/com/learning/tests/bdd/runners/CucumberTest.java)
does not create browsers, call page objects, or assert application behavior. It
is wiring:

- where features live.
- where glue lives.
- which reports to produce.
- which tags to include.
- how Cucumber scenarios are exposed to TestNG.

Keeping the runner boring is a good framework sign.

## Hooks Walkthrough

The hooks file is:

[src/test/java/com/learning/tests/bdd/hooks/CucumberHooks.java](../../src/test/java/com/learning/tests/bdd/hooks/CucumberHooks.java)

`@Before` runs before every scenario. It opens a browser through
`CucumberScenarioContext.openBrowser()`.

`@After` runs after every scenario. It captures and attaches a screenshot when a
scenario fails, then always closes the browser in a `finally` block.

The `finally` block matters. A failing assertion should not leave Chrome
sessions running. In automation interviews, this is a strong place to mention
resource cleanup and test isolation.

Hook order for one scenario:

1. Cucumber creates or prepares glue objects for the scenario.
2. `@Before` in [CucumberHooks.java](../../src/test/java/com/learning/tests/bdd/hooks/CucumberHooks.java)
   logs the scenario name and calls `CucumberScenarioContext.openBrowser()`.
3. The `Background` and scenario steps run.
4. `@After` checks `scenario.isFailed()`.
5. On failure, [ScreenshotUtils.java](../../src/main/java/com/learning/framework/screenshots/ScreenshotUtils.java)
   captures a screenshot and `scenario.attach(...)` adds it to Cucumber output.
6. The `finally` block calls `CucumberScenarioContext.closeBrowser()`.

The `finally` block runs whether the scenario passes, fails, or throws during
screenshot handling. That protects later scenarios from leaked browser
sessions.

## Context Walkthrough

The scenario context is:

[src/test/java/com/learning/tests/bdd/context/CucumberScenarioContext.java](../../src/test/java/com/learning/tests/bdd/context/CucumberScenarioContext.java)

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

The context mirrors [BaseTest.java](../../src/test/java/com/learning/tests/base/BaseTest.java)
but exists separately because Cucumber step classes do not inherit from
`BaseTest`. Cucumber creates and calls glue classes based on annotations, not
TestNG inheritance.

`openBrowser()` does four things:

1. calls [DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java)
   to create the browser.
2. creates a `WebDriverWait` using [ConfigReader.java](../../src/main/java/com/learning/framework/config/ConfigReader.java).
3. creates [WaitUtils.java](../../src/main/java/com/learning/framework/waits/WaitUtils.java)
   and [ElementActions.java](../../src/main/java/com/learning/framework/actions/ElementActions.java).
4. stores those services in thread-local variables for the current scenario.

`closeBrowser()` quits the driver through `DriverFactory.quitDriver()` and then
removes the Cucumber context values. The removal matters because a future
parallel Cucumber run may reuse worker threads.

## Glue Matching Model

Cucumber matches a Gherkin sentence to a Java method by annotation text.
For example, this feature step:

```gherkin
When I login as "standard_user" with password "secret_sauce"
```

matches this method in [SauceDemoSteps.java](../../src/test/java/com/learning/tests/bdd/steps/SauceDemoSteps.java):

```java
@When("I login as {string} with password {string}")
public void iLoginAsWithPassword(String username, String password)
```

`{string}` is a Cucumber expression placeholder. Cucumber extracts quoted
values from the step and passes them as Java method arguments.

## Java Syntax To Notice

`@CucumberOptions` is configuration metadata on the runner class. It is read by
Cucumber when the runner executes.

`@Before` and `@After` in [CucumberHooks.java](../../src/test/java/com/learning/tests/bdd/hooks/CucumberHooks.java)
come from `io.cucumber.java`, not TestNG. They wrap Cucumber scenarios, not
TestNG methods.

`Scenario` is a Cucumber runtime object. The hook uses it to read the scenario
name, check whether it failed, and attach screenshot bytes.

## Common Mistakes

Do not put `driver.findElement` calls directly in step definitions. That makes
Gherkin steps tightly coupled to HTML.

Do not use one static `WebDriver` for all Cucumber scenarios. It causes state
leakage and breaks parallel execution.

Do not make feature files depend on exact UI implementation language such as
button IDs or CSS classes.

Do not skip teardown when a scenario fails. Browser leaks make later failures
hard to diagnose.

Do not point `glue` at the entire `com.learning` tree. Keep glue packages
specific so Cucumber does not scan unrelated Java classes or accidentally pick
up future step definitions.

Do not assume a Cucumber `@Before` hook is the same annotation as TestNG
`@BeforeMethod`. They solve similar lifecycle problems, but they are different
framework APIs.

## Interview Readiness

Strong answer:

"In a Cucumber-TestNG project, the runner extends `AbstractTestNGCucumberTests`.
The runner points to feature files and glue packages. Hooks create and clean up
scenario resources, and step definitions map Gherkin sentences to existing
page-object workflows. Browser lifecycle is not in the runner; it belongs in
hooks or a scenario context."

## Revision Checklist

- Can you explain why [testng-cucumber.xml](../../testng-cucumber.xml) points
  to a runner class instead of a feature file?
- Can you explain why Cucumber needs [CucumberScenarioContext.java](../../src/test/java/com/learning/tests/bdd/context/CucumberScenarioContext.java)
  instead of inheriting [BaseTest.java](../../src/test/java/com/learning/tests/base/BaseTest.java)?
- Can you explain what `scenario.attach(...)` does?
- Can you explain why `@DataProvider(parallel = false)` was chosen for Module
  16?
