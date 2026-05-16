# Step Definitions And Page Objects

The step definitions are:

[src/test/java/com/learning/tests/bdd/steps/SauceDemoSteps.java](../../src/test/java/com/learning/tests/bdd/steps/SauceDemoSteps.java)

This class is intentionally thin. It stores page objects for the current
scenario, calls page methods, and performs assertions. It does not own locators,
wait strategies, driver creation, screenshots, or report configuration.

The step class is the adapter between business wording and framework code. It
should be easy to read because the hard Selenium work has already been pushed
down into page objects and wrapper services.

## Code Walkthrough

`theSauceDemoLoginPageIsOpen()` creates a `LoginPage` using services from
`CucumberScenarioContext`:

- `driver()`
- `elementActions()`
- `waits()`

This mirrors the existing TestNG tests, but the services come from Cucumber
hooks instead of `BaseTest`.

The step method creates [LoginPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java)
inside the scenario flow. It should not be static and should not be reused
across scenarios, because a page object is tied to one browser session and one
set of wrapper services.

`iLoginAsWithPassword()` calls `loginPage.loginAs(...)` and stores the returned
`ProductsPage`. The return type documents a successful page transition.

`iSubmitLoginForWithPassword()` calls `loginPage.loginExpectingError(...)` and
keeps the `LoginPage`. The browser stays on the login page after a failed
login, and the Java object model reflects that fact.

`iAddTheFollowingProductsToTheCart(DataTable productTable)` receives the
Gherkin table from the feature file and converts it to `List<Map<String,
String>>`. The header name `product` becomes the map key.

The checkout flow then moves through page objects:

```text
ProductsPage -> CartPage -> CheckoutPage
```

Those fields in [SauceDemoSteps.java](../../src/test/java/com/learning/tests/bdd/steps/SauceDemoSteps.java)
represent scenario state. They are normal instance fields because Cucumber
creates glue object state for a scenario. They are not framework-wide static
fields.

## Step-To-Source Map

| Gherkin Step | Java Method | Framework Source Used |
| --- | --- | --- |
| `Given the SauceDemo login page is open` | `theSauceDemoLoginPageIsOpen()` | [LoginPage.open()](../../src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java) |
| `When I login as ...` | `iLoginAsWithPassword(...)` | [LoginPage.loginAs(...)](../../src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java) |
| `When I submit login for ...` | `iSubmitLoginForWithPassword(...)` | [LoginPage.loginExpectingError(...)](../../src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java) |
| `When I add the following products...` | `iAddTheFollowingProductsToTheCart(...)` | [ProductsPage.addProductToCart(...)](../../src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java) |
| `When I open the cart` | `iOpenTheCart()` | [ProductsPage.openCart()](../../src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java) |
| `When I start checkout` | `iStartCheckout()` | [CartPage.checkout()](../../src/main/java/com/learning/framework/pages/saucedemo/CartPage.java) |
| `Then the products page should show title ...` | `theProductsPageShouldShowTitle(...)` | [ProductsPage.getTitle()](../../src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java) |
| `Then the product catalog should contain ...` | `theProductCatalogShouldContainItems(...)` | [ProductsPage.getInventoryItemCount()](../../src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java) |
| `Then the login error should contain ...` | `theLoginErrorShouldContain(...)` | [LoginPage.getErrorMessage()](../../src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java) |
| `Then the cart badge should show ...` | `theCartBadgeShouldShow(...)` | [ProductsPage.getCartBadgeCount()](../../src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java) |
| `Then the cart should contain ...` | `theCartShouldContain(...)` | [CartPage.containsProduct(...)](../../src/main/java/com/learning/framework/pages/saucedemo/CartPage.java) |
| `Then the checkout title should be ...` | `theCheckoutTitleShouldBe(...)` | [CheckoutPage.getTitle()](../../src/main/java/com/learning/framework/pages/saucedemo/CheckoutPage.java) |
| `Then the customer information form should be displayed` | `theCustomerInformationFormShouldBeDisplayed()` | [CheckoutPage.isCustomerInformationFormDisplayed()](../../src/main/java/com/learning/framework/pages/saucedemo/CheckoutPage.java) |

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

The assertion methods use TestNG `Assert` because this project is still
running Cucumber through TestNG. Cucumber supplies the scenario language;
TestNG assertions still define pass/fail conditions in Java.

The step annotations use Cucumber expressions:

- `{string}` captures quoted text.
- `{int}` captures an integer and passes it as `int`.
- a trailing colon in a step such as `I add the following products to the cart:`
  allows the following Gherkin table to be passed as a `DataTable`.

## Selenium And Framework Nuances

The step definition never calls Selenium directly. For example, cart behavior
is expressed through:

- [ProductsPage.addProductToCart(...)](../../src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java)
- [ProductsPage.openCart()](../../src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java)
- [CartPage.containsProduct(...)](../../src/main/java/com/learning/framework/pages/saucedemo/CartPage.java)
- [CartPage.checkout()](../../src/main/java/com/learning/framework/pages/saucedemo/CartPage.java)

That keeps BDD stable when locator details change. If SauceDemo changes a cart
selector, `CartPage` should change, not every Gherkin step.

Assertions remain in the step layer because the step states the expected
business outcome. Page Objects answer questions such as `getTitle()` or
`containsProduct(...)`; steps decide what the scenario expects.

This distinction is important:

- Page object: "What is visible on this page?"
- Step definition: "What does this scenario expect?"

For example, [ProductsPage.getInventoryItemCount()](../../src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java)
returns a count. The step `theProductCatalogShouldContainItems(int
expectedCount)` decides that the count should equal the Gherkin value.

## Common Mistakes

- Writing raw Selenium locators in [SauceDemoSteps.java](../../src/test/java/com/learning/tests/bdd/steps/SauceDemoSteps.java).
- Creating a new browser inside a step instead of using [CucumberHooks.java](../../src/test/java/com/learning/tests/bdd/hooks/CucumberHooks.java).
- Making each Gherkin sentence too low-level, such as one step for every click.
- Putting assertions inside page objects so they cannot be reused by different
  scenarios.
- Using static page object fields that can leak between scenarios.

## Debugging A Step Failure

When a Cucumber scenario fails:

1. Read the failing step text in the console or Cucumber report.
2. Find the matching annotation in [SauceDemoSteps.java](../../src/test/java/com/learning/tests/bdd/steps/SauceDemoSteps.java).
3. Identify which page object method was called.
4. Inspect the page object or wrapper if the issue is Selenium behavior.
5. Inspect the feature file if the issue is an incorrect business expectation.

Do not start by changing the feature text unless the behavior statement itself
is wrong. Most browser failures belong in page objects, wrappers, waits, or
test data.

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

## Revision Checklist

- Can you trace every Gherkin step in [saucedemo_login.feature](../../src/test/resources/features/saucedemo_login.feature)
  to a method in [SauceDemoSteps.java](../../src/test/java/com/learning/tests/bdd/steps/SauceDemoSteps.java)?
- Can you explain why step definitions call page objects instead of Selenium?
- Can you explain why assertions live in steps rather than page objects?
- Can you explain the Java type `List<Map<String, String>>` from the DataTable?
- Can you explain why scenario page fields should not be static?
