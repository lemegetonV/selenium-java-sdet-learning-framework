# Module 15 Interview Review

## Core Topics

You should now be able to explain:

- TestNG parallel modes.
- `parallel="methods"` and `thread-count`.
- `ThreadLocal<WebDriver>`.
- why `BaseTest` also needs thread-local service references.
- local parallel execution vs Selenium Grid.
- `RemoteWebDriver`.
- thread-safe report and screenshot handling.
- isolated test data.

Use these files as your interview source map:

| Topic | Source |
| --- | --- |
| TestNG method-level parallelism | [testng-parallel.xml](../../testng-parallel.xml) |
| Sequential comparison suite | [testng.xml](../../testng.xml) |
| Thread-local browser ownership | [DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java) |
| Thread-local test services | [BaseTest.java](../../src/test/java/com/learning/tests/base/BaseTest.java) |
| Local vs Grid switch | [config.properties](../../src/test/resources/config/config.properties) and [ConfigReader.java](../../src/main/java/com/learning/framework/config/ConfigReader.java) |
| Parallel-safe reporting | [ExtentReportManager.java](../../src/test/java/com/learning/tests/reports/ExtentReportManager.java) |
| Parallel-safe screenshots | [ScreenshotUtils.java](../../src/main/java/com/learning/framework/screenshots/ScreenshotUtils.java) |
| Test method usage pattern | [SauceDemoPageObjectTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoPageObjectTest.java) and [SauceDemoDataDrivenTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoDataDrivenTest.java) |

## Strong Answer Framing

### Does Selenium support parallel execution by itself?

Selenium controls browser sessions. TestNG, JUnit, Maven, or another runner
controls parallel test scheduling. Selenium Grid can provide remote browser
capacity, but TestNG still decides which tests run concurrently.

### Why use `ThreadLocal<WebDriver>`?

In parallel execution, each test thread needs its own browser session.
`ThreadLocal` stores one driver per thread so one test does not click, type, or
quit another test's browser.

### Why was `BaseTest` changed?

Because a thread-local driver factory is not enough if `BaseTest` copies that
driver into a normal instance field. TestNG can execute methods from the same
test class instance on different threads. The base class now exposes
`driver()`, `waits()`, and `elementActions()` accessors that read from
thread-local values.

### What is Selenium Grid?

Selenium Grid routes WebDriver commands from the test code to remote browser
nodes. The framework creates a `RemoteWebDriver` with the Grid URL and browser
options. Grid helps scale browser capacity across machines or containers.

### What should not be parallelized blindly?

Tests that share external state, depend on execution order, mutate shared data,
or reuse global objects should not be made parallel until the design is fixed.

## Vocabulary

- Thread pool
- `parallel="methods"`
- `thread-count`
- `ThreadLocal`
- worker thread
- cross-test contamination
- RemoteWebDriver
- Grid hub or router
- browser node
- capabilities
- immutable test data

## Red Flags In Interviews

- "I keep one static WebDriver for all tests."
- "Grid makes tests parallel automatically."
- "Retries are enough to solve parallel failures."
- "I use one ExtentTest static variable."
- "I do not need cleanup because the JVM ends anyway."

## Practical Walkthrough

1. [testng-parallel.xml](../../testng-parallel.xml) starts three method threads.
2. Each thread runs `BaseTest.setUpBrowser`.
3. `DriverFactory` creates one WebDriver per thread.
4. Tests call `driver()`, `elementActions()`, and `waits()`.
5. Log lines show different thread IDs.
6. `BaseTest.tearDownBrowser` quits and removes the current thread's driver.

If you can explain that flow, you understand the module.

## Scenario-Based Questions

### The test passes sequentially but fails in the parallel suite. What do you check first?

Start with shared state. Compare [testng.xml](../../testng.xml) and
[testng-parallel.xml](../../testng-parallel.xml) to confirm the only major
change is scheduling. Then inspect [BaseTest.java](../../src/test/java/com/learning/tests/base/BaseTest.java),
[DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java),
and the test class for mutable fields, static page objects, shared waits, or
stateful report variables.

### Why is `ThreadLocal<WebDriver>` in `DriverFactory` not enough by itself?

Because a later class can accidentally copy the current driver into a normal
field. Module 15 fixes this in [BaseTest.java](../../src/test/java/com/learning/tests/base/BaseTest.java)
by making the driver, raw wait, `WaitUtils`, and `ElementActions` thread-local
too. Tests must read those values through accessors.

### What is the difference between a shared report object and shared test state?

[ExtentReportManager.java](../../src/test/java/com/learning/tests/reports/ExtentReportManager.java)
has one shared `ExtentReports` object because the suite writes one HTML report.
But each active test needs its own `ExtentTest`, so the current test node is
stored in `ThreadLocal<ExtentTest>`.

### What makes Grid different from local parallel execution?

Local parallel execution creates multiple browsers on the same machine.
Grid execution creates browser sessions through `RemoteWebDriver` at the
configured Grid URL. TestNG still decides concurrency; Grid provides remote
browser capacity.

### Why does screenshot naming include a thread ID?

[ScreenshotUtils.java](../../src/main/java/com/learning/framework/screenshots/ScreenshotUtils.java)
already includes a timestamp and test name. Module 15 adds the thread ID so a
failure artifact can be traced back to a specific parallel worker in logs.

## One-Minute Whiteboard Answer

"Module 15 makes the framework parallel-safe. TestNG controls concurrency with
`parallel="methods"` in `testng-parallel.xml`. `DriverFactory` stores one
`WebDriver` per worker thread using `ThreadLocal`, and `BaseTest` also stores
the related wait and action services in thread-local variables. Tests create
page objects inside the method from those accessors, so page objects stay tied
to the correct browser. Reports and screenshots need the same care: Extent has
a shared report engine but a thread-local current test node, screenshots include
thread IDs, and logs use thread context. Grid is separate: it changes where the
browser is created through `RemoteWebDriver`, while TestNG still controls how
many tests run at once."
