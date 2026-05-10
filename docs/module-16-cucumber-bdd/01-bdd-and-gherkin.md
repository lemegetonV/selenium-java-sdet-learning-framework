# BDD And Gherkin

BDD means behavior-driven development. In automation framework terms, it means
the test intent is written as business-readable behavior first, then connected
to executable automation through step definitions.

The feature file for this module is:

```text
src/test/resources/features/saucedemo_login.feature
```

## Mental Model

A Cucumber test has three layers:

1. Gherkin describes the behavior.
2. Step definitions bind each sentence to Java code.
3. Page Objects and framework services perform browser automation.

The feature file should not talk about Selenium details such as locators,
clicks, waits, or `WebDriver`. It should describe user-visible behavior:
opening a login page, logging in, seeing a products page, adding a product to a
cart, and starting checkout.

## Syntax To Notice

`Feature` names the business capability under test. In this module it is
`SauceDemo login and checkout`.

`Background` runs before every scenario in the feature. Here it opens the login
page so individual scenarios do not repeat setup text.

`Scenario` describes one concrete behavior. A good scenario should be small
enough that a failure points to one business outcome.

`Scenario Outline` lets one behavior run with multiple example rows. The
placeholders such as `<username>` and `<message>` are replaced by values from
the `Examples` table.

`DataTable` is used when a step needs structured input that is more readable as
a table than as a long sentence. The checkout scenario uses a product table:

```gherkin
And I add the following products to the cart:
  | product             |
  | Sauce Labs Backpack |
```

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
