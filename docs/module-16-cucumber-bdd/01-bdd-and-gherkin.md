# BDD And Gherkin

BDD means behavior-driven development. In automation framework terms, it means
the test intent is written as business-readable behavior first, then connected
to executable automation through step definitions.

The feature file for this module is:

[src/test/resources/features/saucedemo_login.feature](../../src/test/resources/features/saucedemo_login.feature)

BDD is useful when the scenario wording expresses behavior that a tester,
developer, product owner, or interviewer can discuss without reading Java code.
It is not useful when Gherkin becomes a thin English translation of Selenium
commands.

## Mental Model

A Cucumber test has three layers:

1. Gherkin describes the behavior.
2. Step definitions bind each sentence to Java code.
3. Page Objects and framework services perform browser automation.

The feature file should not talk about Selenium details such as locators,
clicks, waits, or `WebDriver`. It should describe user-visible behavior:
opening a login page, logging in, seeing a products page, adding a product to a
cart, and starting checkout.

In this project, the layers map to exact files:

| Layer | Module 16 Source |
| --- | --- |
| Behavior language | [saucedemo_login.feature](../../src/test/resources/features/saucedemo_login.feature) |
| Step bindings | [SauceDemoSteps.java](../../src/test/java/com/learning/tests/bdd/steps/SauceDemoSteps.java) |
| Browser lifecycle | [CucumberHooks.java](../../src/test/java/com/learning/tests/bdd/hooks/CucumberHooks.java) and [CucumberScenarioContext.java](../../src/test/java/com/learning/tests/bdd/context/CucumberScenarioContext.java) |
| Page automation | [LoginPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java), [ProductsPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java), [CartPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/CartPage.java), [CheckoutPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/CheckoutPage.java) |
| Selenium services | [ElementActions.java](../../src/main/java/com/learning/framework/actions/ElementActions.java), [WaitUtils.java](../../src/main/java/com/learning/framework/waits/WaitUtils.java), [DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java) |

## Syntax To Notice

`Feature` names the business capability under test. In this module it is
`SauceDemo login and checkout`.

`Background` runs before every scenario in the feature. Here it opens the login
page so individual scenarios do not repeat setup text.

The `Background` step:

```gherkin
Given the SauceDemo login page is open
```

maps to `theSauceDemoLoginPageIsOpen()` in [SauceDemoSteps.java](../../src/test/java/com/learning/tests/bdd/steps/SauceDemoSteps.java).
The hook opens the browser before the scenario; the background then opens the
application page inside that browser.

`Scenario` describes one concrete behavior. A good scenario should be small
enough that a failure points to one business outcome.

`Scenario Outline` lets one behavior run with multiple example rows. The
placeholders such as `<username>` and `<message>` are replaced by values from
the `Examples` table.

This feature file has one scenario outline with two example rows. Cucumber
treats that as two executable scenario examples, so the suite has five
executed scenarios even though the file visually contains four scenario
blocks.

`DataTable` is used when a step needs structured input that is more readable as
a table than as a long sentence. The checkout scenario uses a product table:

```gherkin
And I add the following products to the cart:
  | product             |
  | Sauce Labs Backpack |
```

The matching Java method receives a `DataTable` in [SauceDemoSteps.java](../../src/test/java/com/learning/tests/bdd/steps/SauceDemoSteps.java)
and converts it with `productTable.asMaps()`. The `product` header becomes the
map key used by the step.

## Feature File Walkthrough

The tags at the top of [saucedemo_login.feature](../../src/test/resources/features/saucedemo_login.feature)
apply to the full feature:

```gherkin
@bdd @saucedemo
Feature: SauceDemo login and checkout
```

The runner in [CucumberTest.java](../../src/test/java/com/learning/tests/bdd/runners/CucumberTest.java)
uses `tags = "@bdd"`, so only scenarios under that feature tag are selected by
default.

The smoke login scenario proves the happy path:

```gherkin
@smoke @login
Scenario: Standard user reaches the products page
```

The locked-out scenario and invalid-login outline prove negative login
behavior. The checkout scenario demonstrates that Cucumber can still express a
multi-page workflow without mentioning Selenium classes or locators.

## Good Gherkin vs Selenium Script Wording

Prefer:

```gherkin
When I login as "standard_user" with password "secret_sauce"
Then the products page should show title "Products"
```

Avoid:

```gherkin
When I type "standard_user" into the username field
And I type "secret_sauce" into the password field
And I click the login button
```

The avoided version is not always wrong, but it usually describes UI mechanics
rather than business behavior. In this module, UI mechanics belong inside page
objects such as [LoginPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java).

## Nuances

Gherkin is not a programming language. Avoid writing scenarios that mirror
implementation steps like "click the login button" or "find the cart badge."
Those details belong in Page Objects and wrappers.

Tags are execution metadata. `@smoke`, `@regression`, `@login`, and `@checkout`
let the runner or CI select subsets later. They should describe scenario
purpose, not implementation packages.

Duplicate step wording is a maintenance smell. If two teams write similar
sentences with tiny differences, Cucumber glue becomes harder to search and
reuse. Prefer a small vocabulary of clear domain steps.

Do not put secrets or real credentials into feature files. SauceDemo uses a
public demo password, but in real projects Gherkin should use role names or
test-user aliases, and step definitions should resolve credentials from secure
configuration.

Feature files should be stable even when locators change. If a CSS selector
changes, [CartPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/CartPage.java)
or [ProductsPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java)
should change. The feature wording should usually remain the same.

## Interview Readiness

Strong answer framing:

- Cucumber does not automate the browser by itself. Selenium still drives the
  browser.
- Gherkin improves communication when non-technical stakeholders actually read
  or review scenarios.
- Step definitions should be thin adapters, not a place to hide raw Selenium
  logic.
- Page Objects remain valuable in BDD frameworks because they protect steps
  from HTML and locator changes.

Common interview question:

> What is the difference between TestNG and Cucumber?

Answer:

TestNG is a testing framework that executes Java test methods and provides
annotations, groups, suites, assertions, and DataProviders. Cucumber is a BDD
tool that executes Gherkin scenarios by mapping steps to Java methods. In this
project, Cucumber uses TestNG as the runner through `cucumber-testng`.

Follow-up framing:

"I use Cucumber only as the behavior expression layer. Selenium automation
still lives in page objects and framework services. That keeps Gherkin readable
and prevents step definitions from becoming a second Selenium framework."

## Revision Checklist

- Can you explain why Cucumber does not replace Selenium?
- Can you trace a line from [saucedemo_login.feature](../../src/test/resources/features/saucedemo_login.feature)
  to its Java method in [SauceDemoSteps.java](../../src/test/java/com/learning/tests/bdd/steps/SauceDemoSteps.java)?
- Can you explain why the scenario outline produces two executable examples?
- Can you explain when a `DataTable` is better than a long sentence?
- Can you identify feature wording that is too close to Selenium mechanics?
