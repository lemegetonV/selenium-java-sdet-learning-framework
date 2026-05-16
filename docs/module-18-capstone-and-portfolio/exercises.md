# Module 18 Exercises

## Reading List

Before doing the exercises, read:

1. [README.md](../../README.md)
2. [docs/README.md](../README.md)
3. [01-final-architecture-review.md](01-final-architecture-review.md)
4. [02-runbook-and-portfolio-guide.md](02-runbook-and-portfolio-guide.md)
5. [CartPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/CartPage.java)
6. [.github/workflows/ui-tests.yml](../../.github/workflows/ui-tests.yml)

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

Add these details:

- where TestNG lifecycle is owned.
- where Cucumber scenario lifecycle is owned.
- where screenshots and reports are attached.
- where CI enters the project.

## Exercise 2 - Portfolio Walkthrough

Prepare a 5-minute explanation of the project.

Required points:

- why the repo is module-based.
- why framework abstractions were introduced gradually.
- how the final framework runs locally.
- what reports are produced.
- how CI chooses smoke versus full regression.

Use these files during the walkthrough:

- [README.md](../../README.md)
- [docs/README.md](../README.md)
- [SauceDemoPageObjectTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoPageObjectTest.java)
- [LoginPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java)
- [DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java)
- [saucedemo_login.feature](../../src/test/resources/features/saucedemo_login.feature)
- [.github/workflows/ui-tests.yml](../../.github/workflows/ui-tests.yml)

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

Add:

- which CI scope was running.
- whether the failure happened before or after browser startup.
- whether failure screenshots exist.
- why [CartPage.checkout()](../../src/main/java/com/learning/framework/pages/saucedemo/CartPage.java)
  is the right place to inspect checkout transition behavior.

## Exercise 4 - Future Enhancement Plan

Choose one future enhancement and outline how you would add it:

- browser matrix in CI.
- Selenium Grid service container in CI.
- Allure published to GitHub Pages.
- Dockerized execution.

Expected answer should include the files likely to change, the risk, and the
verification command.

## Exercise 5 - README Audit

Review [README.md](../../README.md) as if you were a hiring manager or senior
SDET.

Answer:

- can you understand the project purpose in under one minute?
- are the run commands complete?
- are the reports and artifacts clear?
- are limitations honest?
- does it explain why this is a learning repo?

Expected outcome:

You can distinguish a code-complete repository from a portfolio-ready
repository.

## Exercise 6 - Module Checkpoint Drill

Use tags to inspect history:

```bash
git checkout module-09-complete
git checkout module-14-complete
git checkout module-18-complete
```

Then return:

```bash
git checkout main
```

Expected outcome:

You can explain how the project evolved from Page Objects, to reporting, to the
final packaged framework.

## Exercise 7 - Capstone Hardening Review

Read [CartPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/CartPage.java).

Answer:

- what public-site behavior is the retry protecting against?
- why does the method retry only once?
- why does it wait for [CheckoutPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/CheckoutPage.java)
  instead of assuming the click worked?
- why would a generic retry in [ElementActions.java](../../src/main/java/com/learning/framework/actions/ElementActions.java)
  be riskier?

Expected outcome:

You can explain targeted framework hardening without hiding application or
test-design bugs.

## Exercise 8 - Final Verification Run

Run the focused final gates:

```bash
mvn test -DsuiteXmlFile=testng.xml -Dheadless=true
mvn test -DsuiteXmlFile=testng-parallel.xml -Dheadless=true
mvn test -DsuiteXmlFile=testng-cucumber.xml -Dheadless=true
mvn allure:report
```

Expected outcome:

You can identify which report or artifact each command creates and explain why
the commands are useful before presenting the project.
