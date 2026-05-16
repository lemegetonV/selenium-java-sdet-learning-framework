# Module 17 Interview Review

## Core Vocabulary

- CI: continuous integration, automatically validating changes.
- CD: continuous delivery or deployment, automatically preparing or releasing
  validated changes.
- workflow: a GitHub Actions automation file.
- trigger: event that starts a workflow.
- runner: machine that executes workflow jobs.
- job: group of steps that run on one runner.
- step: one action or shell command inside a job.
- artifact: file or directory saved from a workflow run.
- headless browser: browser execution without a visible GUI.

## Source Map

| Topic | Source |
| --- | --- |
| CI workflow | [.github/workflows/ui-tests.yml](../../.github/workflows/ui-tests.yml) |
| Maven build and Surefire | [pom.xml](../../pom.xml) |
| Main TestNG suite | [testng.xml](../../testng.xml) |
| Parallel suite | [testng-parallel.xml](../../testng-parallel.xml) |
| Cucumber suite | [testng-cucumber.xml](../../testng-cucumber.xml) |
| Headless config read | [ConfigReader.java](../../src/main/java/com/learning/framework/config/ConfigReader.java) |
| Browser creation | [DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java) |
| TestNG failure evidence | [FrameworkTestListener.java](../../src/test/java/com/learning/tests/listeners/FrameworkTestListener.java) |
| BDD failure evidence | [CucumberHooks.java](../../src/test/java/com/learning/tests/bdd/hooks/CucumberHooks.java) |

## Strong Answers

Why run Selenium tests in CI?

To make UI regression checks repeatable and visible to the team. Local passing
tests are useful, but CI proves the framework can run from a clean machine with
declared dependencies.

Why not run every UI test on every PR?

UI tests are slower and more sensitive to external systems. A common strategy
is smoke tests on PRs, broader regression on schedule or manual dispatch, and
full regression before releases.

Why upload artifacts?

A CI runner is temporary. Without artifacts, screenshots, HTML reports, JSON
reports, and Surefire XML disappear after the job. UI failures need this
evidence for debugging.

Why use headless mode?

Hosted CI runners do not provide an interactive desktop. Headless mode lets the
browser engine run in automation-friendly environments while still executing
real browser behavior.

What can make UI tests flaky in CI?

Common causes include timing issues, network latency, external AUT instability,
browser version changes, insufficient waits, shared test data, and tests that
depend on execution order.

How does this workflow choose what to run?

[.github/workflows/ui-tests.yml](../../.github/workflows/ui-tests.yml) resolves
a `test_scope`. Manual runs use the selected `workflow_dispatch` input,
scheduled runs default to `full`, and push/pull-request runs default to
`smoke`. A shell `case` statement maps the scope to Maven commands.

Why does the smoke scope run both TestNG and Cucumber?

The framework now has two top-level expression layers: TestNG tests and
Cucumber BDD scenarios. A fast PR signal should cover both critical Java test
methods and the BDD glue path.

What is the difference between TestNG groups and Cucumber tags in CI?

TestNG groups filter Java test methods, such as `-Dgroups=smoke` with
[testng.xml](../../testng.xml). Cucumber tags filter Gherkin scenarios, such as
`-Dcucumber.filter.tags="@smoke"` with [testng-cucumber.xml](../../testng-cucumber.xml).

Why use `if: always()` for artifact uploads?

Because failed UI tests produce the most valuable evidence. The workflow should
upload reports and screenshots even when a Maven test command fails.

Why is `mvn allure:report` `continue-on-error`?

The job should still try to create an Allure report after failures, but a
report-generation issue should not hide the original test failure. Test failure
is the primary signal.

## Scenario Walkthrough

For a pull request to `main`:

1. The `pull_request` trigger starts [.github/workflows/ui-tests.yml](../../.github/workflows/ui-tests.yml).
2. GitHub creates an `ubuntu-latest` runner.
3. The workflow checks out the repository.
4. `actions/setup-java` installs Temurin Java 21 and enables Maven cache.
5. Tool versions are printed for diagnostics.
6. Scope resolution defaults to `smoke`.
7. The workflow runs TestNG smoke tests with [testng.xml](../../testng.xml).
8. The workflow runs Cucumber smoke scenarios with [testng-cucumber.xml](../../testng-cucumber.xml).
9. The workflow attempts Allure report generation.
10. The workflow uploads all available artifacts.

## One-Minute Whiteboard Answer

"Module 17 adds GitHub Actions CI for the Selenium framework. The workflow runs
on pushes, pull requests, manual dispatch, and a weekly schedule. It sets up
Java 21, caches Maven dependencies, prints tool versions, resolves a test
scope, and runs the matching Maven commands in headless Chrome. PRs and pushes
default to smoke, manual runs can choose a scope, and schedules default to full.
After tests, it generates Allure when possible and uploads Surefire, Extent,
Cucumber, Allure, and screenshot artifacts with `if: always()` so failures can
be debugged after the runner is gone."

## Red Flags In Interviews

- "CI just runs the same full suite on every PR no matter how long it takes."
- "We do not upload screenshots because the logs are enough."
- "Headless mode means Selenium is not using a real browser."
- "Cucumber tags and TestNG groups are identical."
- "If Allure report generation fails, that should replace the test failure."
- "The workflow can use any Java version installed on the runner."

## Revision Checklist

- I can explain the difference between a workflow, job, and step.
- I can explain why Java 21 setup belongs in CI.
- I can explain why Maven cache improves CI runtime.
- I can explain the smoke, regression, BDD, parallel, and full scopes.
- I can explain why artifacts use `if: always()`.
- I can explain why headless execution must reuse the same framework config.
- I can explain what evidence I would inspect after a CI failure.
- I can map every workflow scope to its exact Maven command.
- I can explain why the workflow uses `permissions: contents: read`.
- I can explain why stale runs are cancelled with `concurrency`.
