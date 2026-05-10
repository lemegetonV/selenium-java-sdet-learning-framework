# Retry And Framework Exceptions

## Mental Model

Retries and custom exceptions are both framework control tools, but they solve
different problems.

A retry analyzer decides whether TestNG should re-run a failed test. A custom
framework exception describes failures caused by framework setup or utilities.

Retries should be used carefully. They can reduce noise from rare environment
glitches, but they can also hide real bugs if enabled casually.

## Code Walkthrough

Retry files:

- `src/test/java/com/learning/tests/listeners/FrameworkRetryAnalyzer.java`
- `src/test/java/com/learning/tests/listeners/RetryAnnotationTransformer.java`

Configuration:

- `src/test/resources/config/config.properties`
- `src/main/java/com/learning/framework/config/ConfigReader.java`

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

Framework exception file:

`src/main/java/com/learning/framework/exceptions/FrameworkException.java`

Current users:

- `src/main/java/com/learning/framework/driver/DriverFactory.java`
- `src/main/java/com/learning/framework/screenshots/ScreenshotUtils.java`

## Java Syntax To Notice

`FrameworkException extends RuntimeException`.

This means callers are not forced to catch it. That is appropriate for
framework setup failures because a missing driver, unsupported browser, or
failed screenshot save usually means the test run cannot continue normally.

`Math.max(ConfigReader.getRetryCount(), 0)` prevents negative retry values from
creating confusing behavior.

## Selenium Or Framework Nuances

Retries do not fix bad locators, weak waits, broken test data, or product bugs.
Before enabling retries, inspect the failure. If the root cause is a framework
timing issue, fix the wait or action wrapper first.

Custom framework exceptions help triage. A failed assertion tells you the
application behavior did not match the expected result. A `FrameworkException`
tells you the test infrastructure had a problem.

## Common Mistakes

- Enabling retries globally without tracking why tests are flaky.
- Retrying assertion failures that are deterministic product bugs.
- Creating many custom exception types too early.
- Throwing generic `RuntimeException` everywhere and losing framework meaning.
- Catching an exception, logging it, and then swallowing it.

## Interview Readiness

Strong answer:

"A retry analyzer is a TestNG hook that decides whether a failed test should
run again. I keep retries disabled by default and make them configurable. I use
a custom framework exception for infrastructure failures so it is easier to
distinguish framework problems from product assertion failures."

## How This Connects To Later Framework Design

Module 14 can show retry and framework-exception details in reports. Module 15
will require retry behavior to remain thread-safe during parallel execution.

## Revision Checklist

- Can you explain why retry count defaults to zero?
- Can you show where retry behavior is configured?
- Can you explain the difference between assertion failure and framework
  exception?
- Can you identify where `FrameworkException` is thrown today?

