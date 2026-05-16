# Module 16 Exercises

## Reading List

Before doing the exercises, read:

1. [saucedemo_login.feature](../../src/test/resources/features/saucedemo_login.feature)
2. [CucumberTest.java](../../src/test/java/com/learning/tests/bdd/runners/CucumberTest.java)
3. [CucumberHooks.java](../../src/test/java/com/learning/tests/bdd/hooks/CucumberHooks.java)
4. [CucumberScenarioContext.java](../../src/test/java/com/learning/tests/bdd/context/CucumberScenarioContext.java)
5. [SauceDemoSteps.java](../../src/test/java/com/learning/tests/bdd/steps/SauceDemoSteps.java)
6. [testng-cucumber.xml](../../testng-cucumber.xml)

## Exercise 1 - Add A New Negative Login Example

Add one more row to the `Scenario Outline` in:

```text
src/test/resources/features/saucedemo_login.feature
```

Use a username/password combination that should keep the user on the login
page.

Hint:

- Keep the same step wording.
- Only add a row to the `Examples` table.
- Run `mvn test -DsuiteXmlFile=testng-cucumber.xml`.

Expected outcome:

- Cucumber runs one additional scenario example.
- No Java step definition change is required.

Questions to answer:

- Why does adding one `Examples` row create another executable scenario?
- Which Java method receives the `<username>`, `<password>`, and `<message>`
  values?
- Why is this better than copying the entire scenario?

## Exercise 2 - Add A Product Count Step

Add a new Gherkin step after successful login that verifies at least one
product exists.

Hint:

- Add the sentence to the feature file.
- Add a matching `@Then` method in `SauceDemoSteps.java`.
- Reuse `productsPage.getInventoryItemCount()`.

Expected outcome:

- The new step expresses a business-readable catalog check.
- Selenium locator logic remains inside `ProductsPage`.

Questions to answer:

- Should this assertion live in [SauceDemoSteps.java](../../src/test/java/com/learning/tests/bdd/steps/SauceDemoSteps.java)
  or [ProductsPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java)?
- Why should the step not call `driver.findElements(...)` directly?

## Exercise 3 - Run Only Smoke BDD Scenarios

Run only scenarios tagged as `@smoke`.

Hint:

```bash
mvn test -DsuiteXmlFile=testng-cucumber.xml -Dcucumber.filter.tags="@smoke"
```

Expected outcome:

- Only the standard-user products-page scenario runs.
- This pattern becomes important in Module 17 CI/CD.

Follow-up:

Try:

```bash
mvn test -DsuiteXmlFile=testng-cucumber.xml -Dcucumber.filter.tags="@regression and not @checkout"
```

Expected outcome:

- Regression login scenarios run.
- The checkout scenario is excluded.

## Exercise 4 - Explain The Layering

In your own words, explain this flow:

```text
Feature file -> Step definition -> Page Object -> ElementActions -> WebDriver
```

Expected answer should mention:

- feature files describe behavior.
- step definitions bind Gherkin to Java.
- Page Objects know page-specific locators and flows.
- wrappers centralize Selenium operations and waits.
- WebDriver performs the browser automation.

## Exercise 5 - Trace One Scenario End To End

Trace the scenario:

```text
Standard user can start checkout for a selected product
```

Use:

- [saucedemo_login.feature](../../src/test/resources/features/saucedemo_login.feature)
- [SauceDemoSteps.java](../../src/test/java/com/learning/tests/bdd/steps/SauceDemoSteps.java)
- [ProductsPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java)
- [CartPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/CartPage.java)
- [CheckoutPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/CheckoutPage.java)

Expected outcome:

You can explain which step method creates or updates each page object field:
`loginPage`, `productsPage`, `cartPage`, and `checkoutPage`.

## Exercise 6 - Explain Cucumber Lifecycle

Read:

- [CucumberHooks.java](../../src/test/java/com/learning/tests/bdd/hooks/CucumberHooks.java)
- [CucumberScenarioContext.java](../../src/test/java/com/learning/tests/bdd/context/CucumberScenarioContext.java)

Answer:

- what opens the browser before a scenario?
- what closes the browser after a scenario?
- when is a screenshot attached?
- why is cleanup inside a `finally` block?
- why does Cucumber use this context instead of [BaseTest.java](../../src/test/java/com/learning/tests/base/BaseTest.java)?

## Exercise 7 - Inspect BDD Reports

Run:

```bash
mvn clean test -DsuiteXmlFile=testng-cucumber.xml
mvn allure:report
```

Inspect:

- `target/cucumber-report/cucumber.html`
- `target/cucumber-report/cucumber.json`
- `target/allure-results`
- `target/allure-report/index.html`

Expected outcome:

You can explain which reports are generated directly by Cucumber plugins and
which report is generated later by the Allure Maven plugin.
