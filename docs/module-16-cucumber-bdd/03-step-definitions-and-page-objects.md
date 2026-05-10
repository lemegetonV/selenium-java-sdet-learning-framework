# Step Definitions And Page Objects

The step definitions are:

```text
src/test/java/com/learning/tests/bdd/steps/SauceDemoSteps.java
```

This class is intentionally thin. It stores page objects for the current
scenario, calls page methods, and performs assertions. It does not own locators,
wait strategies, driver creation, screenshots, or report configuration.

## Code Walkthrough

`theSauceDemoLoginPageIsOpen()` creates a `LoginPage` using services from
`CucumberScenarioContext`:

- `driver()`
- `elementActions()`
- `waits()`

This mirrors the existing TestNG tests, but the services come from Cucumber
hooks instead of `BaseTest`.

`iLoginAsWithPassword()` calls `loginPage.loginAs(...)` and stores the returned
`ProductsPage`. The return type documents a successful page transition.

`iSubmitLoginForWithPassword()` calls `loginPage.loginExpectingError(...)` and
keeps the `LoginPage`. The browser stays on the login page after a failed
login, and the Java object model reflects that fact.

`iAddTheFollowingProductsToTheCart(DataTable productTable)` receives the
Gherkin table from the feature file and converts it to `List<Map<String,
String>>`. The header name `product` becomes the map key.

## Java Syntax To Notice

`DataTable` is a Cucumber type, not a Selenium or TestNG type. Cucumber injects
it into the step method when the matching Gherkin step has a table.

`List<Map<String, String>>` means:

- the outer `List` represents table rows.
- each `Map` represents one row.
- the key is the column header.
- the value is the cell value for that row.

This is a common Java collections shape in automation frameworks. You will see
similar ideas later with JSON, Excel, API response maps, and Cucumber custom
data table types.

## Selenium And Framework Nuances

The step definition never calls Selenium directly. For example, cart behavior
is expressed through:

```text
ProductsPage.addProductToCart(...)
ProductsPage.openCart()
CartPage.containsProduct(...)
CartPage.checkout()
```

That keeps BDD stable when locator details change. If SauceDemo changes a cart
selector, `CartPage` should change, not every Gherkin step.

Assertions remain in the step layer because the step states the expected
business outcome. Page Objects answer questions such as `getTitle()` or
`containsProduct(...)`; steps decide what the scenario expects.

## How This Connects To Later Framework Design

Module 17 can use Cucumber tags in CI, for example running `@smoke` on every
push and `@regression` on scheduled workflows.

Future framework hardening can add:

- custom parameter types for known user roles.
- data table transformers for product objects.
- dependency injection instead of a static ThreadLocal context.
- shared report attachments for Cucumber, TestNG, Extent, and Allure.

Those are intentionally deferred until the learner has seen the basic BDD path
working end to end.
