# Parallel-Safe Reports And Data

## Mental Model

Parallel execution makes cross-test contamination easier to expose. The browser
is not the only state that can leak. Reports, logs, screenshots, test data, and
page objects all need isolation rules.

## Code Walkthrough

Report current test:

`src/test/java/com/learning/tests/reports/ExtentReportManager.java`

```java
private static final ThreadLocal<ExtentTest> CURRENT_TEST = new ThreadLocal<>();
```

This keeps each test thread connected to its own Extent test node.

Screenshot filenames:

`src/main/java/com/learning/framework/screenshots/ScreenshotUtils.java`

```java
"-thread-" + Thread.currentThread().threadId()
```

The thread ID makes screenshot filenames easier to trace when failures happen
in parallel.

DataProvider model:

`src/test/java/com/learning/tests/models/LoginScenario.java`

`LoginScenario` is immutable because it is a Java record. Immutable data rows
are safer in parallel tests because one test cannot modify the row being used
by another test.

## Framework Nuances

Synchronized report writes are used around Extent operations because the report
object is shared. The current test entry is thread-local, but the report engine
still writes one output file.

Log4j2 `ThreadContext` was introduced in Module 13. Under parallel execution it
becomes more valuable because interleaved log lines still carry the current
test name.

Allure handles parallel result files well because each test result is written
as separate structured artifacts under `target/allure-results`.

## Common Mistakes

- Sharing mutable test data between threads.
- Generating all screenshots with the same filename.
- Making report managers store the current test in a normal static field.
- Forgetting logs are interleaved in parallel runs.
- Running dependent workflows in parallel when they share external state.

## Interview Readiness

Strong answer:

"Parallel-safe reporting means each test thread must write to its own report
node while shared report flushing is controlled. I use `ThreadLocal` for the
current Extent test, unique screenshot names, Log4j ThreadContext for test
names, and immutable test data rows."

## Revision Checklist

- Can you explain why Extent uses `ThreadLocal<ExtentTest>`?
- Can you show why screenshot filenames now include thread ID?
- Can you explain why records are useful for DataProvider rows?
- Can you describe which artifacts are shared and which are per-test?

