# Module 17 Exercises

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

## Exercise 2 - Run BDD Locally Like CI

Run the BDD workflow path locally:

```bash
mvn test -DsuiteXmlFile=testng-cucumber.xml -Dheadless=true
```

Expected outcome:

- 5 Cucumber scenarios pass.
- `target/cucumber-report/` is generated.

## Exercise 3 - Explain A Failure Investigation

Imagine the CI smoke scope fails only in GitHub Actions, not locally.

Write down the first five facts you would inspect.

Hint:

- browser version.
- Surefire report.
- screenshot artifact.
- Extent or Cucumber report.
- test logs and failing step.

## Exercise 4 - Add A Future Browser Matrix Design

Do not implement it yet. Sketch how Module 18 or a future module could extend
the workflow to run against Chrome, Firefox, and Edge.

Expected answer should mention:

- a GitHub Actions matrix.
- passing `-Dbrowser=...`.
- unique artifact names per browser.
- why full matrix execution may be too slow for every pull request.
