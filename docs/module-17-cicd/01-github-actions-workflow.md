# GitHub Actions Workflow

The workflow file is
[.github/workflows/ui-tests.yml](../../.github/workflows/ui-tests.yml).

GitHub Actions reads workflow files from `.github/workflows/`. A workflow is
YAML, and the main building blocks are triggers, jobs, steps, actions, shell
commands, environment variables, and artifacts.

## Mental Model

CI answers four questions:

1. When should automation run?
2. What machine should run it?
3. What commands should execute?
4. What evidence should remain after the run?

This project answers those questions as:

- run on `push`, `pull_request`, manual `workflow_dispatch`, and weekly
  `schedule`.
- use GitHub-hosted `ubuntu-latest`.
- set up Java 21 and Maven cache.
- run selected Maven test scopes in headless Chrome.
- upload Surefire, Extent, Cucumber, Allure, and screenshot artifacts.

## Trigger Walkthrough

`push` and `pull_request` protect `main`. They default to the fast `smoke`
scope because UI tests are slower and more environment-sensitive than unit
tests.

`workflow_dispatch` lets a learner or maintainer run a specific scope manually.
The workflow exposes a choice input named `test_scope`.

`schedule` runs weekly in UTC and defaults to `full`. Scheduled runs are useful
for catching external-site drift, dependency changes, and browser behavior
changes even when no code has changed.

## Job Walkthrough

`runs-on: ubuntu-latest` asks GitHub for a hosted Linux runner. Linux is the
common default for Maven Selenium CI because it is fast, inexpensive, and works
well with headless Chrome.

`actions/checkout@v6` downloads this repository into the runner workspace.
Without checkout, Maven would have no [pom.xml](../../pom.xml), source code, or tests to run.

`actions/setup-java@v5` installs Java 21 and enables Maven dependency caching.
The cache is keyed from Maven dependency files, so repeated CI runs do not have
to download every dependency from scratch.

The `Show tool versions` step prints Java, Maven, and Chrome versions. This is
diagnostic evidence. When a browser behavior changes, the version lines help
explain what changed in CI.

## Scope Selection

The shell step named `Resolve test scope` chooses the scope:

- manual run: use the selected `test_scope`.
- scheduled run: default to `full`.
- push or pull request: default to `smoke`.

The test step uses a `case` statement. This is intentionally simple and visible
for learners. More advanced frameworks might move this logic into a script, but
keeping it in the workflow makes the CI design easier to study in Module 17.

## Code Walkthrough

Start at the top of [.github/workflows/ui-tests.yml](../../.github/workflows/ui-tests.yml).
The `on` block is the contract for when automation runs. `push` and
`pull_request` are feedback triggers, `workflow_dispatch` is the manual
learning and debugging trigger, and `schedule` is the maintenance trigger.

Next read the setup steps. `actions/checkout` makes the repository files
available, `actions/setup-java` installs Java 21, and the version-printing
step creates evidence about the runtime environment. In UI automation this is
not noise; browser, Java, and Maven versions often explain why local and CI
behavior differ.

Then read the scope-selection shell. The workflow intentionally keeps the
scope names close to the commands they run:

- `smoke` runs the fastest framework confidence path.
- `regression` runs the main TestNG suite and the full Cucumber suite.
- `bdd` isolates [testng-cucumber.xml](../../testng-cucumber.xml) for feature
  and step-definition work.
- `parallel` isolates [testng-parallel.xml](../../testng-parallel.xml) for
  thread-safety checks.
- `full` adds the broadest Maven run and the parallel suite.

Finally read the upload steps. They are part of the test design, not an
afterthought. A CI failure without reports, logs, screenshots, or Allure data
forces the engineer to rerun the failure blindly.

## Framework Files To Connect

CI does not create a separate automation framework. It calls the same files
the local framework uses:

- [pom.xml](../../pom.xml) resolves Selenium, TestNG, Cucumber, reporting, and
  Maven execution.
- [src/main/java/com/learning/framework/config/ConfigReader.java](../../src/main/java/com/learning/framework/config/ConfigReader.java)
  reads system properties such as `headless`.
- [src/main/java/com/learning/framework/driver/DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java)
  converts those properties into local or remote browser sessions.
- [src/test/java/com/learning/tests/listeners/FrameworkTestListener.java](../../src/test/java/com/learning/tests/listeners/FrameworkTestListener.java)
  creates listener-driven diagnostics for TestNG runs.
- [src/test/java/com/learning/tests/bdd/hooks/CucumberHooks.java](../../src/test/java/com/learning/tests/bdd/hooks/CucumberHooks.java)
  owns scenario-level browser setup and screenshots for Cucumber runs.

## Common Mistakes

Do not run the longest possible UI suite on every pull request unless the team
accepts the feedback delay. Slow PR checks get ignored or bypassed.

Do not forget `-Dheadless=true`. GitHub-hosted runners do not provide a normal
interactive desktop for browser tests.

Do not upload only logs. UI failures need screenshots and reports.

Do not hide all command selection inside unexplained scripts. A learner should
be able to open the workflow and understand what CI is doing.

## Interview Readiness

Strong answer framing:

- CI is about repeatability and feedback, not only remote execution.
- Smoke tests run on PRs because they provide fast risk detection.
- Full regression can run on a schedule or manual dispatch.
- Artifacts are essential for UI tests because failures are visual and often
  environment-specific.
