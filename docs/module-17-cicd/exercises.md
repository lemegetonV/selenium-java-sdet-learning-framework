# Module 17 Exercises

## Reading List

Before doing the exercises, read:

1. [.github/workflows/ui-tests.yml](../../.github/workflows/ui-tests.yml)
2. [pom.xml](../../pom.xml)
3. [testng.xml](../../testng.xml)
4. [testng-cucumber.xml](../../testng-cucumber.xml)
5. [testng-parallel.xml](../../testng-parallel.xml)
6. [ConfigReader.java](../../src/main/java/com/learning/framework/config/ConfigReader.java)
7. [DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java)

## Exercise 1 - Trace A Smoke Run

Open:

```text
.github/workflows/ui-tests.yml
```

Trace what happens on a pull request to `main`.

Expected answer should mention:

- the `pull_request` trigger.
- the default `smoke` scope.
- Java 21 setup.
- TestNG smoke command.
- Cucumber `@smoke` command.
- artifact upload with `if: always()`.

Questions to answer:

- Why does this event default to `smoke` instead of `full`?
- Which command uses TestNG groups?
- Which command uses Cucumber tags?
- Which artifacts might not exist after a passing smoke run?

## Exercise 2 - Run BDD Locally Like CI

Run the BDD workflow path locally:

```bash
mvn test -DsuiteXmlFile=testng-cucumber.xml -Dheadless=true
```

Expected outcome:

- 5 Cucumber scenarios pass.
- `target/cucumber-report/` is generated.

Follow-up:

Run only BDD smoke like the workflow's smoke scope:

```bash
mvn test -DsuiteXmlFile=testng-cucumber.xml -Dcucumber.filter.tags="@smoke" -Dheadless=true
```

Expected outcome:

- only the Cucumber smoke scenario runs.

## Exercise 3 - Explain A Failure Investigation

Imagine the CI smoke scope fails only in GitHub Actions, not locally.

Write down the first five facts you would inspect.

Hint:

- browser version.
- Surefire report.
- screenshot artifact.
- Extent or Cucumber report.
- test logs and failing step.

Add:

- which workflow scope ran.
- whether the failing path was TestNG or Cucumber.
- whether the failure happened before browser startup or during browser
  interaction.

## Exercise 4 - Add A Future Browser Matrix Design

Do not implement it yet. Sketch how Module 18 or a future module could extend
the workflow to run against Chrome, Firefox, and Edge.

Expected answer should mention:

- a GitHub Actions matrix.
- passing `-Dbrowser=...`.
- unique artifact names per browser.
- why full matrix execution may be too slow for every pull request.

## Exercise 5 - Map Every Scope To Commands

Read the `case "$scope"` block in [.github/workflows/ui-tests.yml](../../.github/workflows/ui-tests.yml).

Create a table with:

- scope name.
- Maven command or commands.
- suite file used.
- expected report directories.
- when you would run that scope.

Expected outcome:

You can explain why `smoke`, `regression`, `bdd`, `parallel`, and `full` are
separate choices.

## Exercise 6 - Trace Headless Configuration

Trace this argument:

```text
-Dheadless=true
```

through:

- [.github/workflows/ui-tests.yml](../../.github/workflows/ui-tests.yml)
- [pom.xml](../../pom.xml)
- [ConfigReader.java](../../src/main/java/com/learning/framework/config/ConfigReader.java)
- [DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java)

Expected outcome:

You can explain how a Maven system property changes browser options without
changing test code.

## Exercise 7 - Artifact Ownership Review

For each artifact path, identify which framework component creates it:

- `target/surefire-reports/`
- `target/extent-report/`
- `target/cucumber-report/`
- `target/allure-results/`
- `target/allure-report/`
- `target/screenshots/`

Expected outcome:

You can explain that the workflow uploads artifacts, but framework/test tools
produce them.

## Exercise 8 - Design A CI Policy

Write a short policy for when to run each scope:

- pull request.
- push to `main`.
- manual debugging.
- scheduled regression.
- release candidate.

Expected outcome:

Your policy should balance feedback speed with confidence. It should not run
the slowest possible UI suite for every tiny change unless you can justify the
cost.
