# Parallel-Safe Reports And Data

## Mental Model

Parallel execution makes cross-test contamination easier to expose. The browser
is not the only state that can leak. Reports, logs, screenshots, test data, and
page objects all need isolation rules.

The easiest way to reason about Module 15 is to ask this question for every
object:

"If two tests use this at the same time, do they need separate instances, or is
there one shared object with controlled access?"

The answer depends on the object:

- `WebDriver`, `WebDriverWait`, `WaitUtils`, `ElementActions`, and page objects
  are per-test/per-thread objects.
- `ExtentReports` is a shared report engine, but the active `ExtentTest` must
  be per-thread.
- Allure writes separate result files, so it naturally handles parallel result
  artifacts well.
- `LoginScenario` rows are immutable records, so they are safe to pass into
  parallel test invocations.

## Code Walkthrough

Report current test:

[src/test/java/com/learning/tests/reports/ExtentReportManager.java](../../src/test/java/com/learning/tests/reports/ExtentReportManager.java)

```java
private static final ThreadLocal<ExtentTest> CURRENT_TEST = new ThreadLocal<>();
```

This keeps each test thread connected to its own Extent test node.

[ExtentReportManager.java](../../src/test/java/com/learning/tests/reports/ExtentReportManager.java)
has two different kinds of state:

- `extentReports`: one shared report object for the suite.
- `CURRENT_TEST`: one current test node per worker thread.

That distinction matters. If `CURRENT_TEST` were a normal static field, one
parallel test could overwrite the report node used by another test. A passed
test might log into the wrong report entry, or a failure screenshot might
attach to the wrong test.

The public report methods are synchronized because the shared Extent report
object writes to one output file:

```java
public static synchronized void startTest(...)
public static synchronized void pass(...)
public static synchronized void fail(...)
public static synchronized void skip(...)
public static synchronized void flush()
```

Synchronization protects shared report writes. `ThreadLocal` protects the
identity of the current test node.

Screenshot filenames:

[src/main/java/com/learning/framework/screenshots/ScreenshotUtils.java](../../src/main/java/com/learning/framework/screenshots/ScreenshotUtils.java)

```java
"-thread-" + Thread.currentThread().threadId()
```

The thread ID makes screenshot filenames easier to trace when failures happen
in parallel.

The full screenshot name also includes a timestamp and sanitized logical test
name. The combination reduces filename collisions and makes the artifact
readable:

```text
yyyyMMdd-HHmmss-SSS-thread-<id>-<test-name>.png
```

The screenshot utility is still passed a `WebDriver` from the listener path,
so the browser session being captured must come from the same current thread.
That is why [BaseTest.java](../../src/test/java/com/learning/tests/base/BaseTest.java)
and [DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java)
must remain aligned.

DataProvider model:

[src/test/java/com/learning/tests/models/LoginScenario.java](../../src/test/java/com/learning/tests/models/LoginScenario.java)

`LoginScenario` is immutable because it is a Java record. Immutable data rows
are safer in parallel tests because one test cannot modify the row being used
by another test.

[SauceDemoDataDrivenTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoDataDrivenTest.java)
receives a `LoginScenario` and passes its values into the page object flow.
The test does not mutate the scenario. That makes the data row safe even when
multiple invocations are active.

[LoginScenario.java](../../src/test/java/com/learning/tests/models/LoginScenario.java)
also masks the password in `toString()`, which matters because TestNG and
reporting tools often render parameter objects. Module 14 introduced the safe
display behavior; Module 15 proves why this matters more when many report
events are produced at the same time.

## Framework Nuances

Synchronized report writes are used around Extent operations because the report
object is shared. The current test entry is thread-local, but the report engine
still writes one output file.

Log4j2 `ThreadContext` was introduced in Module 13. Under parallel execution it
becomes more valuable because interleaved log lines still carry the current
test name.

Allure handles parallel result files well because each test result is written
as separate structured artifacts under `target/allure-results`.

The listener flow from [FrameworkTestListener.java](../../src/test/java/com/learning/tests/listeners/FrameworkTestListener.java)
does not change conceptually in this module, but its assumptions are tested
under concurrency:

1. `onTestStart` sets Log4j `ThreadContext` and starts the current Extent test.
2. The test method runs with a thread-local driver and services.
3. `onTestSuccess`, `onTestFailure`, or `onTestSkipped` logs the result to the
   current thread's report node.
4. On failure, screenshot capture uses the current thread's browser session.
5. Thread-local logging and report context are cleared after the result is
   recorded.

The cleanup steps are part of the correctness story. Parallel tests can reuse
worker threads, so stale `ThreadContext` or `ThreadLocal` values can leak into
the next invocation if they are not removed.

## Artifact Ownership

| Artifact | Produced By | Parallel Safety Rule |
| --- | --- | --- |
| `target/logs/test-execution.log` | Log4j2 and `FrameworkTestListener` | use `ThreadContext` so interleaved lines still show test identity |
| `target/screenshots/*.png` | [ScreenshotUtils.java](../../src/main/java/com/learning/framework/screenshots/ScreenshotUtils.java) | include timestamp, thread ID, and sanitized test name |
| `target/extent-report/extent.html` | [ExtentReportManager.java](../../src/test/java/com/learning/tests/reports/ExtentReportManager.java) | synchronize shared report writes and keep current test node thread-local |
| `target/allure-results/*` | Allure TestNG integration and [AllureReportUtils.java](../../src/test/java/com/learning/tests/reports/AllureReportUtils.java) | write separate result artifacts per test |
| `target/allure-report/index.html` | Allure Maven plugin | generated after results exist |

## What To Inspect After A Parallel Run

After running:

```bash
mvn clean test -DsuiteXmlFile=testng-parallel.xml
```

check:

- `target/logs/test-execution.log` contains multiple thread IDs.
- `target/extent-report/extent.html` has one entry per logical test result.
- `target/allure-results` contains fresh result files from the clean run.
- failure screenshots, if any, include the relevant test name and thread ID.
- no report entry shows a test name mixed with another test's steps or error.

## Common Mistakes

- Sharing mutable test data between threads.
- Generating all screenshots with the same filename.
- Making report managers store the current test in a normal static field.
- Forgetting logs are interleaved in parallel runs.
- Running dependent workflows in parallel when they share external state.
- Making `ExtentTest currentTest` a normal static field.
- Clearing browser state but forgetting to clear reporting/logging context.
- Judging artifact safety from one passing run instead of inspecting logs and
  reports after a clean parallel run.

## Interview Readiness

Strong answer:

"Parallel-safe reporting means each test thread must write to its own report
node while shared report flushing is controlled. I use `ThreadLocal` for the
current Extent test, unique screenshot names, Log4j ThreadContext for test
names, and immutable test data rows."

Follow-up framing:

"Parallel safety is not only WebDriver. I also verify data providers,
screenshots, reports, logging context, and page object lifecycle. A framework
can have isolated browsers and still produce misleading artifacts if report
state is shared incorrectly."

## Revision Checklist

- Can you explain why Extent uses `ThreadLocal<ExtentTest>`?
- Can you show why screenshot filenames now include thread ID?
- Can you explain why records are useful for DataProvider rows?
- Can you describe which artifacts are shared and which are per-test?
- Can you explain why report writes are synchronized even though
  `CURRENT_TEST` is thread-local?
- Can you explain how Log4j `ThreadContext` helps with interleaved logs?
