# Retry And Framework Exceptions

## Mental Model

Retries and custom exceptions are both framework control tools, but they solve
different problems.

A retry analyzer decides whether TestNG should re-run a failed test. A custom
framework exception describes failures caused by framework setup or utilities.

Retries should be used carefully. They can reduce noise from rare environment
glitches, but they can also hide real bugs if enabled casually.

Module 13 makes retry support available but inactive:

```properties
retryCount=0
```

That default is intentional. A learning framework should teach how retries
work without normalizing "rerun until green" as a testing strategy.

## Code Walkthrough

Retry files:

- [src/test/java/com/learning/tests/listeners/FrameworkRetryAnalyzer.java](../../src/test/java/com/learning/tests/listeners/FrameworkRetryAnalyzer.java)
- [src/test/java/com/learning/tests/listeners/RetryAnnotationTransformer.java](../../src/test/java/com/learning/tests/listeners/RetryAnnotationTransformer.java)

Configuration:

- [src/test/resources/config/config.properties](../../src/test/resources/config/config.properties)
- [src/main/java/com/learning/framework/config/ConfigReader.java](../../src/main/java/com/learning/framework/config/ConfigReader.java)

Default:

```properties
retryCount=0
```

The transformer applies the analyzer only when retries are enabled:

```java
if (ConfigReader.getRetryCount() > 0 && annotation.getRetryAnalyzerClass() == null) {
    annotation.setRetryAnalyzer(FrameworkRetryAnalyzer.class);
}
```

## Retry Flow

When `retryCount` is greater than zero:

1. TestNG reads listener/transformer registration from [testng.xml](../../testng.xml).
2. [RetryAnnotationTransformer.java](../../src/test/java/com/learning/tests/listeners/RetryAnnotationTransformer.java)
   receives each `@Test` annotation before execution.
3. If the test does not already have a retry analyzer, the transformer attaches
   [FrameworkRetryAnalyzer.java](../../src/test/java/com/learning/tests/listeners/FrameworkRetryAnalyzer.java).
4. If a test fails, TestNG calls `retry(...)`.
5. `FrameworkRetryAnalyzer` compares its current `attempts` count with
   `ConfigReader.getRetryCount()`.
6. If another attempt is allowed, it logs a warning and returns `true`.
7. If no attempt is left, it returns `false` and the failure remains final.

When `retryCount=0`, the transformer does not attach the retry analyzer.

Framework exception file:

[src/main/java/com/learning/framework/exceptions/FrameworkException.java](../../src/main/java/com/learning/framework/exceptions/FrameworkException.java)

Current users:

- [src/main/java/com/learning/framework/driver/DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java)
- [src/main/java/com/learning/framework/screenshots/ScreenshotUtils.java](../../src/main/java/com/learning/framework/screenshots/ScreenshotUtils.java)

## FrameworkException Flow

[FrameworkException.java](../../src/main/java/com/learning/framework/exceptions/FrameworkException.java)
is currently thrown for framework infrastructure problems:

- unsupported browser or missing driver state in
  [DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java).
- unsupported screenshot driver or failed screenshot save in
  [ScreenshotUtils.java](../../src/main/java/com/learning/framework/screenshots/ScreenshotUtils.java).

This distinction helps triage:

- `AssertionError` usually means the product did not match the expected
  behavior.
- `FrameworkException` means framework setup, browser lifecycle, or utility
  behavior failed.

## Java Syntax To Notice

`FrameworkException extends RuntimeException`.

This means callers are not forced to catch it. That is appropriate for
framework setup failures because a missing driver, unsupported browser, or
failed screenshot save usually means the test run cannot continue normally.

`Math.max(ConfigReader.getRetryCount(), 0)` prevents negative retry values from
creating confusing behavior.

`annotation.getRetryAnalyzerClass() == null` protects tests that already define
their own retry analyzer. The transformer does not overwrite an explicit
method-level decision.

## Selenium Or Framework Nuances

Retries do not fix bad locators, weak waits, broken test data, or product bugs.
Before enabling retries, inspect the failure. If the root cause is a framework
timing issue, fix the wait or action wrapper first.

Custom framework exceptions help triage. A failed assertion tells you the
application behavior did not match the expected result. A `FrameworkException`
tells you the test infrastructure had a problem.

Retries should be treated as a diagnostic tool, not a permanent fix. If the
same scenario needs retries often, the next engineering step is to inspect
waits, locators, browser state, test data, and application behavior.

## Common Mistakes

- Enabling retries globally without tracking why tests are flaky.
- Retrying assertion failures that are deterministic product bugs.
- Creating many custom exception types too early.
- Throwing generic `RuntimeException` everywhere and losing framework meaning.
- Catching an exception, logging it, and then swallowing it.
- Assuming a retry analyzer runs before every test; it is consulted after
  failure.
- Retrying a failure without preserving logs/screenshots from the first
  attempt.

## Interview Readiness

Strong answer:

"A retry analyzer is a TestNG hook that decides whether a failed test should
run again. I keep retries disabled by default and make them configurable. I use
a custom framework exception for infrastructure failures so it is easier to
distinguish framework problems from product assertion failures."

## Decision Guide

| Situation | Retry? | Better Action |
| --- | --- | --- |
| transient browser startup hiccup | maybe, if rare and visible in logs | keep retry low and investigate environment |
| wrong expected title | no | fix product expectation or bug |
| stale element from weak wait | no | improve wait/action design |
| unsupported browser config | no | fail fast with `FrameworkException` |
| screenshot write failure | no | fix artifact path/permissions |

## How This Connects To Later Framework Design

Module 14 can show retry and framework-exception details in reports. Module 15
will require retry behavior to remain thread-safe during parallel execution.

## Revision Checklist

- Can you explain why retry count defaults to zero?
- Can you show where retry behavior is configured?
- Can you explain the difference between assertion failure and framework
  exception?
- Can you identify where `FrameworkException` is thrown today?
- Can you explain how the annotation transformer applies retries centrally?
- Can you explain why negative retry counts are clamped to zero?
