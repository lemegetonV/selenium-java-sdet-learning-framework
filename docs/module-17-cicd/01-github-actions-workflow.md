# GitHub Actions Workflow

The workflow file is
[.github/workflows/ui-tests.yml](../../.github/workflows/ui-tests.yml).

GitHub Actions reads workflow files from `.github/workflows/`. A workflow is
YAML, and the main building blocks are triggers, jobs, steps, actions, shell
commands, environment variables, and artifacts.

The workflow introduced in this module is:

[.github/workflows/ui-tests.yml](../../.github/workflows/ui-tests.yml)

It is intentionally one readable workflow instead of several small workflows so
learners can see the full CI decision tree in one place.

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

The workflow does not create a new test framework. It calls the same Maven
commands that you can run locally. That is a key CI design principle: local and
remote execution should share the same source of truth.

## Trigger Walkthrough

`push` and `pull_request` protect `main`. They default to the fast `smoke`
scope because UI tests are slower and more environment-sensitive than unit
tests.

`workflow_dispatch` lets a learner or maintainer run a specific scope manually.
The workflow exposes a choice input named `test_scope`.

`schedule` runs weekly in UTC and defaults to `full`. Scheduled runs are useful
for catching external-site drift, dependency changes, and browser behavior
changes even when no code has changed.

The trigger block in [.github/workflows/ui-tests.yml](../../.github/workflows/ui-tests.yml)
is:

```yaml
on:
  push:
    branches:
      - main
  pull_request:
    branches:
      - main
  workflow_dispatch:
    inputs:
      test_scope:
        default: smoke
  schedule:
    - cron: "0 2 * * 1"
```

GitHub cron schedules use UTC. The comment in the workflow explains that this
is a weekly Monday run. If you compare it with your local time zone, convert
from UTC before assuming the schedule is wrong.

## Permissions And Concurrency

The workflow uses:

```yaml
permissions:
  contents: read
```

That is enough for checkout and test execution. The job is not writing releases,
comments, pages, or packages, so it does not need broader permissions.

The workflow also uses:

```yaml
concurrency:
  group: ui-tests-${{ github.workflow }}-${{ github.ref }}
  cancel-in-progress: true
```

This prevents a queue of stale UI runs on the same branch or PR ref. If a new
commit arrives, the older in-progress run can be cancelled. That matters for UI
tests because they consume browser time and are usually slower than unit tests.

## Job Walkthrough

`runs-on: ubuntu-latest` asks GitHub for a hosted Linux runner. Linux is the
common default for Maven Selenium CI because it is fast, inexpensive, and works
well with headless Chrome.

`actions/checkout@v6` downloads this repository into the runner workspace.
Without checkout, Maven would have no [pom.xml](../../pom.xml), source code, or tests to run.

`actions/setup-java@v5` installs Java 21 and enables Maven dependency caching.
The cache is keyed from Maven dependency files, so repeated CI runs do not have
to download every dependency from scratch.

This connects directly to [pom.xml](../../pom.xml), which declares Java 21
compiler settings, dependency versions, Maven Surefire, and the Allure Maven
plugin. CI should not rely on a developer's laptop JDK.

The `Show tool versions` step prints Java, Maven, and Chrome versions. This is
diagnostic evidence. When a browser behavior changes, the version lines help
explain what changed in CI.

`timeout-minutes: 35` is also part of the design. UI suites can hang because of
browser startup, network issues, or external AUT instability. A timeout keeps a
bad run from consuming CI capacity indefinitely.

## Scope Selection

The shell step named `Resolve test scope` chooses the scope:

- manual run: use the selected `test_scope`.
- scheduled run: default to `full`.
- push or pull request: default to `smoke`.

The test step uses a `case` statement. This is intentionally simple and visible
for learners. More advanced frameworks might move this logic into a script, but
keeping it in the workflow makes the CI design easier to study in Module 17.

The scope resolution step writes to `$GITHUB_OUTPUT`:

```bash
echo "scope=$scope" >> "$GITHUB_OUTPUT"
```

That makes the resolved value available later as:

```yaml
${{ steps.scope.outputs.scope }}
```

The shell step uses `set -euo pipefail` before running commands. That makes the
script fail on command errors, unset variables, and pipeline failures. For CI,
failing loudly is better than silently skipping a test scope.

## Command Map

| Scope | Exact Command(s) In Workflow | Local Equivalent |
| --- | --- | --- |
| `smoke` | `mvn --batch-mode test -DsuiteXmlFile=testng.xml -Dgroups=smoke -Dheadless=true` and `mvn --batch-mode test -DsuiteXmlFile=testng-cucumber.xml -Dcucumber.filter.tags="@smoke" -Dheadless=true` | same commands without `--batch-mode` if preferred |
| `regression` | `mvn --batch-mode test -DsuiteXmlFile=testng.xml -Dheadless=true` and `mvn --batch-mode test -DsuiteXmlFile=testng-cucumber.xml -Dheadless=true` | main TestNG plus full BDD suite |
| `bdd` | `mvn --batch-mode test -DsuiteXmlFile=testng-cucumber.xml -Dheadless=true` | BDD-only check |
| `parallel` | `mvn --batch-mode test -DsuiteXmlFile=testng-parallel.xml -Dheadless=true` | parallel safety check |
| `full` | `mvn --batch-mode test -Dheadless=true` and `mvn --batch-mode test -DsuiteXmlFile=testng-parallel.xml -Dheadless=true` | broad default Maven run plus explicit parallel suite |

`--batch-mode` is a Maven CI convention. It reduces interactive output and
keeps logs more predictable in automation.

`-Dgroups=smoke` is consumed by TestNG/Surefire group filtering for
[testng.xml](../../testng.xml). `-Dcucumber.filter.tags="@smoke"` is consumed
by Cucumber for [testng-cucumber.xml](../../testng-cucumber.xml). They solve
similar selection problems in different test layers.

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

The `Generate Allure report` step uses:

```yaml
if: always()
continue-on-error: true
```

`if: always()` means it still runs after test failures. `continue-on-error:
true` means report generation should not hide the original test failure if the
Allure report itself has a problem.

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

## Failure Investigation Flow

When a GitHub Actions UI run fails:

1. Check which trigger and scope ran in [.github/workflows/ui-tests.yml](../../.github/workflows/ui-tests.yml).
2. Read the `Show tool versions` output for Java, Maven, and Chrome versions.
3. Open `surefire-reports` to identify the failing test or scenario.
4. Open `extent-report` for TestNG workflow failures, or `cucumber-report` for
   BDD failures.
5. Check `allure-output` for structured Allure results and generated report.
6. Check `screenshots` when the failure happened after browser startup.

This order avoids guessing. First identify the scope, then the failing test,
then the framework evidence.

## Common Mistakes

Do not run the longest possible UI suite on every pull request unless the team
accepts the feedback delay. Slow PR checks get ignored or bypassed.

Do not forget `-Dheadless=true`. GitHub-hosted runners do not provide a normal
interactive desktop for browser tests.

Do not upload only logs. UI failures need screenshots and reports.

Do not hide all command selection inside unexplained scripts. A learner should
be able to open the workflow and understand what CI is doing.

Do not treat the CI YAML as unrelated infrastructure. A bad scope filter,
missing artifact path, or incorrect system property can make a good framework
look broken.

Do not assume a passing local headed run guarantees CI success. CI uses a clean
Linux environment and headless Chrome, so browser versions, network behavior,
and filesystem paths can differ.

## Interview Readiness

Strong answer framing:

- CI is about repeatability and feedback, not only remote execution.
- Smoke tests run on PRs because they provide fast risk detection.
- Full regression can run on a schedule or manual dispatch.
- Artifacts are essential for UI tests because failures are visual and often
  environment-specific.

Follow-up framing:

"I design CI scopes intentionally. PRs run a small high-value suite, manual
dispatch lets me choose targeted suites, scheduled runs cover broader
regression, and every run uploads evidence so I can debug without rerunning
blindly."

## Revision Checklist

- Can you explain how `workflow_dispatch` selects `test_scope`?
- Can you explain why scheduled runs default to `full`?
- Can you explain why `concurrency` cancels older runs?
- Can you map each CI scope to its Maven command?
- Can you explain the difference between TestNG groups and Cucumber tags?
- Can you explain why artifact upload steps use `if: always()`?
