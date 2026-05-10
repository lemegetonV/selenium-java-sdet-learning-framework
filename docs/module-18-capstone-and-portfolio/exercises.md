# Module 18 Exercises

## Exercise 1 - Architecture Whiteboard

Draw the final framework flow from a Cucumber feature and from a TestNG test
down to WebDriver.

Expected answer should include:

- feature or test.
- step definition or test class.
- Page Object.
- wrapper methods and waits.
- driver factory and config.
- browser session.

## Exercise 2 - Portfolio Walkthrough

Prepare a 5-minute explanation of the project.

Required points:

- why the repo is module-based.
- why framework abstractions were introduced gradually.
- how the final framework runs locally.
- what reports are produced.
- how CI chooses smoke versus full regression.

## Exercise 3 - Failure Debugging Drill

Assume `standardUserCanStartCheckoutForSingleProduct` fails in CI.

List the artifacts and source files you would inspect first.

Expected answer should mention:

- Surefire report.
- Extent report.
- screenshot artifact.
- logs.
- `ProductsPage.java`, `CartPage.java`, and `CheckoutPage.java`.
- SauceDemo page behavior or availability.

## Exercise 4 - Future Enhancement Plan

Choose one future enhancement and outline how you would add it:

- browser matrix in CI.
- Selenium Grid service container in CI.
- Allure published to GitHub Pages.
- Dockerized execution.

Expected answer should include the files likely to change, the risk, and the
verification command.
