# Log4j2 And Test Context

## Mental Model

Logs tell the execution story over time. Screenshots show one browser moment.
Both are useful, but they answer different questions.

Module 13 adds Log4j2 so framework services can produce structured diagnostic
messages instead of using `System.out.println`.

The important design idea is context. A click log is only useful if you can
tell which test produced it. Module 13 uses Log4j2 `ThreadContext` so every log
line can include the current test name.

## Code Walkthrough

Configuration file:

[src/test/resources/log4j2.xml](../../src/test/resources/log4j2.xml)

Framework logging appears in:

- [src/main/java/com/learning/framework/driver/DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java)
- [src/main/java/com/learning/framework/actions/ElementActions.java](../../src/main/java/com/learning/framework/actions/ElementActions.java)
- [src/test/java/com/learning/tests/listeners/FrameworkTestListener.java](../../src/test/java/com/learning/tests/listeners/FrameworkTestListener.java)
- [src/test/java/com/learning/tests/listeners/FrameworkRetryAnalyzer.java](../../src/test/java/com/learning/tests/listeners/FrameworkRetryAnalyzer.java)

The pattern includes:

```text
[%X{testName}]
```

That reads the Log4j2 `ThreadContext` value named `testName`.

The listener sets it in `onTestStart`:

```java
ThreadContext.put("testName", testName);
```

and clears it when the test ends:

```java
ThreadContext.clearMap();
```

## Log Output Shape

[log4j2.xml](../../src/test/resources/log4j2.xml) defines:

```xml
<Property name="logDirectory">target/logs</Property>
<Property name="logPattern">%d{HH:mm:ss.SSS} %-5level [%X{testName}] %c{1} - %msg%n%throwable</Property>
```

That means a log line includes:

- time.
- level.
- current `testName` from `ThreadContext`.
- short logger name.
- message.
- throwable stack trace when present.

The file appender writes to:

```text
target/logs/test-execution.log
```

The file appender uses `append="false"`, so each run starts a fresh log file.

## Java Syntax To Notice

Logger fields are usually `static final`:

```java
private static final Logger LOGGER = LogManager.getLogger(ElementActions.class);
```

`static` means one logger per class. `final` means the logger reference is not
reassigned. This is a common Java logging convention.

Parameterized logging:

```java
LOGGER.info("Created {} browser session", browser);
```

lets the logging library format the message only when that log level is active.

## Code Walkthrough

[FrameworkTestListener.java](../../src/test/java/com/learning/tests/listeners/FrameworkTestListener.java)
sets and clears the per-test context:

```java
ThreadContext.put("testName", testName);
...
ThreadContext.clearMap();
```

Clearing matters because TestNG may reuse the same thread for another test. If
the context were not cleared, the next test could inherit the previous test's
name in logs.

[DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java)
logs browser lifecycle:

```java
LOGGER.info("Created {} browser session with window {}x{}", ...);
LOGGER.info("Quitting browser session");
```

These are `INFO` because driver creation and cleanup are important run-level
events.

[ElementActions.java](../../src/main/java/com/learning/framework/actions/ElementActions.java)
logs wrapper actions:

```java
LOGGER.debug("Clicking element located by {}", locator);
LOGGER.debug("Typing {} characters into element located by {}", value.length(), locator);
```

These are `DEBUG` because action-level logs can be noisy but useful during
framework debugging.

## Selenium Or Framework Nuances

`ElementActions.type(...)` logs the number of characters typed, not the actual
value. This is intentional. Passwords, tokens, emails, and customer data often
flow through `sendKeys`. A framework should avoid leaking sensitive values into
console output, files, and reports.

[FrameworkTestListener.java](../../src/test/java/com/learning/tests/listeners/FrameworkTestListener.java)
also avoids dumping full data-provider records. When a parameter is a
[LoginScenario.java](../../src/test/java/com/learning/tests/models/LoginScenario.java),
it logs only the scenario name.

`DriverFactory` logs creation and cleanup because driver lifecycle problems are
common Selenium framework issues. When a test fails before opening a page, the
browser lifecycle logs help confirm whether setup completed.

## Common Mistakes

- Using `System.out.println` in reusable framework classes after logging exists.
- Logging passwords or full data-provider objects.
- Forgetting to clear `ThreadContext`, which can leak one test name into the
  next test's log lines on a reused thread.
- Logging too much at `INFO`. Low-level Selenium actions are `DEBUG` here.
- Configuring only console logs and losing diagnostics after CI finishes.
- Using logger names that are too broad to identify which framework service
  produced a line.

## Interview Readiness

Strong answer:

"A UI automation framework uses logging to record framework behavior such as
driver creation, clicks, typing, waits, listener events, and retries. Log4j2
supports levels, appenders, patterns, and thread context, so it can produce
useful console and file output without mixing diagnostic code into tests."

## Log Reading Checklist

When reading `target/logs/test-execution.log`, look for:

- `Starting TestNG context` and `Finished TestNG context`.
- `START`, `PASS`, `FAIL`, or `SKIP` from the listener.
- browser creation and cleanup from `DriverFactory`.
- click/type/select entries from `ElementActions`.
- retry warnings from `FrameworkRetryAnalyzer` when retries are enabled and a
  test fails.

For data-driven tests, confirm the log identifies the scenario name instead of
printing the full `LoginScenario` record.

## How This Connects To Later Framework Design

Module 14 reporting can link log output to screenshots and report entries.
Module 15 parallel execution will make per-thread context more important
because multiple tests may write logs at the same time.

## Revision Checklist

- Can you explain why Log4j2 replaces `System.out.println` in framework code?
- Can you identify where `testName` is placed into log context?
- Can you explain why typed values are not logged?
- Can you find the file log output path?
- Can you explain the difference between `INFO` lifecycle logs and `DEBUG`
  action logs?
- Can you explain why `append="false"` is useful for local learning runs?
