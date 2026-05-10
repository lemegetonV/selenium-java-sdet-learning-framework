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

## Revision Checklist

- I can explain the difference between a workflow, job, and step.
- I can explain why Java 21 setup belongs in CI.
- I can explain why Maven cache improves CI runtime.
- I can explain the smoke, regression, BDD, parallel, and full scopes.
- I can explain why artifacts use `if: always()`.
- I can explain why headless execution must reuse the same framework config.
- I can explain what evidence I would inspect after a CI failure.
