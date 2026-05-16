# Module 16 Interview Review

## Core Vocabulary

- BDD: behavior-driven development, focused on shared examples of behavior.
- Gherkin: plain-language syntax used by Cucumber feature files.
- Feature: a business capability under test.
- Scenario: one concrete example of behavior.
- Scenario Outline: one behavior executed with multiple example rows.
- Step definition: Java method bound to a Gherkin sentence.
- Glue: packages where Cucumber searches for hooks and step definitions.
- Hook: setup or teardown method around Cucumber scenarios.
- Tag: scenario metadata used for filtering and organization.
- DataTable: structured step input passed from Gherkin to Java.

## Source Map

| Topic | Source |
| --- | --- |
| Gherkin scenarios | [saucedemo_login.feature](../../src/test/resources/features/saucedemo_login.feature) |
| TestNG runner bridge | [CucumberTest.java](../../src/test/java/com/learning/tests/bdd/runners/CucumberTest.java) |
| Suite entry point | [testng-cucumber.xml](../../testng-cucumber.xml) |
| Scenario setup/teardown | [CucumberHooks.java](../../src/test/java/com/learning/tests/bdd/hooks/CucumberHooks.java) |
| Scenario service access | [CucumberScenarioContext.java](../../src/test/java/com/learning/tests/bdd/context/CucumberScenarioContext.java) |
| Step bindings | [SauceDemoSteps.java](../../src/test/java/com/learning/tests/bdd/steps/SauceDemoSteps.java) |
| Cucumber dependencies | [pom.xml](../../pom.xml) |

## Strong Answers

What is Cucumber?

Cucumber is a BDD tool that runs Gherkin scenarios by matching each step to a
step definition. It is not a browser automation tool. In UI automation,
Cucumber usually sits above Selenium and a test framework such as TestNG or
JUnit.

What does `AbstractTestNGCucumberTests` do?

It adapts Cucumber scenarios into TestNG tests. This lets Maven Surefire,
TestNG suites, TestNG reporting, and TestNG-compatible CI execution run
Cucumber scenarios.

Where should Selenium code live in a Cucumber framework?

Selenium code should stay in Page Objects, wrapper methods, waits, and driver
services. Step definitions should call those abstractions instead of locating
elements directly.

Why do we use hooks?

Hooks run before or after scenarios. In UI automation, they are commonly used
to create a browser, reset state, capture failure evidence, and quit the
browser.

What is the risk of overusing Cucumber?

If non-technical stakeholders do not read feature files, Cucumber can become an
extra maintenance layer. It is valuable when scenarios express business rules
clearly and step definitions remain reusable.

How does a feature file reach Selenium code?

[CucumberTest.java](../../src/test/java/com/learning/tests/bdd/runners/CucumberTest.java)
selects the feature and glue. Cucumber matches each Gherkin step to a method in
[SauceDemoSteps.java](../../src/test/java/com/learning/tests/bdd/steps/SauceDemoSteps.java).
The step calls existing page objects, which use wrapper services and WebDriver.

Why does this module have [CucumberScenarioContext.java](../../src/test/java/com/learning/tests/bdd/context/CucumberScenarioContext.java)?

TestNG tests inherit [BaseTest.java](../../src/test/java/com/learning/tests/base/BaseTest.java),
but Cucumber step classes are called by Cucumber glue, not by TestNG class
inheritance. The scenario context gives hooks and steps access to the same
framework services while keeping state scenario/thread scoped.

Why are step definitions thin?

Thin steps keep Gherkin stable and readable. The step should coordinate page
objects and assertions. Locators, waits, and Selenium details belong in page
objects, wrappers, and driver services.

How do Cucumber tags differ from TestNG groups?

Both can select tests, but they live at different layers. Cucumber tags select
feature scenarios such as `@smoke` or `@checkout`. TestNG groups select Java
test methods. In this module, Cucumber scenarios are selected with
`-Dcucumber.filter.tags`.

## Scenario Walkthrough

For `Standard user can start checkout for a selected product`:

1. [saucedemo_login.feature](../../src/test/resources/features/saucedemo_login.feature)
   defines the scenario and product table.
2. [CucumberHooks.java](../../src/test/java/com/learning/tests/bdd/hooks/CucumberHooks.java)
   opens a browser before the scenario.
3. The background step creates [LoginPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java).
4. The login step stores [ProductsPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java).
5. The DataTable step calls `ProductsPage.addProductToCart(...)`.
6. Cart and checkout steps move through [CartPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/CartPage.java)
   and [CheckoutPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/CheckoutPage.java).
7. Assertions in [SauceDemoSteps.java](../../src/test/java/com/learning/tests/bdd/steps/SauceDemoSteps.java)
   verify business outcomes.
8. The after hook closes the browser and attaches a screenshot only if needed.

## One-Minute Whiteboard Answer

"Cucumber adds a BDD layer above the existing Selenium framework. The feature
file describes behavior in Gherkin. The runner extends
`AbstractTestNGCucumberTests`, so TestNG and Maven can execute Cucumber
scenarios. Hooks open and close a browser per scenario through a scenario
context. Step definitions are thin adapters: they match Gherkin sentences,
call existing page objects, and assert outcomes. Selenium remains in page
objects, wrapper actions, waits, and DriverFactory. Tags let us select scenario
subsets, and Cucumber/Allure plugins produce BDD reports."

## Red Flags In Interviews

- "Cucumber replaces Selenium."
- "I put locators directly in every step definition."
- "One static driver is enough for all scenarios."
- "Feature files should describe every click and field interaction."
- "Cucumber tags and TestNG groups are the same thing."
- "Hooks are optional, so browser cleanup can happen later."

## Revision Checklist

- I can explain how a feature file reaches Selenium code.
- I can explain why Cucumber does not replace Page Objects.
- I can explain `Scenario` vs `Scenario Outline`.
- I can explain how `Examples` values replace placeholders.
- I can explain why hooks are needed for browser lifecycle.
- I can explain why scenario state must not be stored in one static driver.
- I can explain how tags help CI/CD select test subsets.
- I can explain why this module keeps Cucumber execution sequential.
- I can explain which reports are produced by Cucumber plugins.
- I can explain why Cucumber needs a context class instead of `BaseTest`.
