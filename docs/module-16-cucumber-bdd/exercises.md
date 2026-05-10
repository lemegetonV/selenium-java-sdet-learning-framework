# Module 16 Exercises

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

## Exercise 3 - Run Only Smoke BDD Scenarios

Run only scenarios tagged as `@smoke`.

Hint:

```bash
mvn test -DsuiteXmlFile=testng-cucumber.xml -Dcucumber.filter.tags="@smoke"
```

Expected outcome:

- Only the standard-user products-page scenario runs.
- This pattern becomes important in Module 17 CI/CD.

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
