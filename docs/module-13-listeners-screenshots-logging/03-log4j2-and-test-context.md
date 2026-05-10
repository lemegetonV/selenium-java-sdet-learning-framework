# Log4j2 And Test Context

## Mental Model

Logs tell the execution story over time. Screenshots show one browser moment.
Both are useful, but they answer different questions.

Module 13 adds Log4j2 so framework services can produce structured diagnostic
messages instead of using `System.out.println`.

## Code Walkthrough

Configuration file:

`src/test/resources/log4j2.xml`

Framework logging appears in:

- `src/main/java/com/learning/framework/driver/DriverFactory.java`
- `src/main/java/com/learning/framework/actions/ElementActions.java`
- `src/test/java/com/learning/tests/listeners/FrameworkTestListener.java`
- `src/test/java/com/learning/tests/listeners/FrameworkRetryAnalyzer.java`

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

## Selenium Or Framework Nuances

`ElementActions.type(...)` logs the number of characters typed, not the actual
value. This is intentional. Passwords, tokens, emails, and customer data often
flow through `sendKeys`. A framework should avoid leaking sensitive values into
console output, files, and reports.

`DriverFactory` logs creation and cleanup because driver lifecycle problems are
common Selenium framework issues. When a test fails before opening a page, the
browser lifecycle logs help confirm whether setup completed.

## Common Mistakes

- Using `System.out.println` in reusable framework classes after logging exists.
- Logging passwords or full data-provider objects.
- Forgetting to clear `ThreadContext`, which can leak one test name into the
  next test's log lines on a reused thread.
- Logging too much at `INFO`. Low-level Selenium actions are `DEBUG` here.

## Interview Readiness

Strong answer:

"A UI automation framework uses logging to record framework behavior such as
driver creation, clicks, typing, waits, listener events, and retries. Log4j2
supports levels, appenders, patterns, and thread context, so it can produce
useful console and file output without mixing diagnostic code into tests."

## How This Connects To Later Framework Design

Module 14 reporting can link log output to screenshots and report entries.
Module 15 parallel execution will make per-thread context more important
because multiple tests may write logs at the same time.

## Revision Checklist

- Can you explain why Log4j2 replaces `System.out.println` in framework code?
- Can you identify where `testName` is placed into log context?
- Can you explain why typed values are not logged?
- Can you find the file log output path?

