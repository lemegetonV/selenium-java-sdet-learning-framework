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

1. `testng-parallel.xml` starts three method threads.
2. Each thread runs `BaseTest.setUpBrowser`.
3. `DriverFactory` creates one WebDriver per thread.
4. Tests call `driver()`, `elementActions()`, and `waits()`.
5. Log lines show different thread IDs.
6. `BaseTest.tearDownBrowser` quits and removes the current thread's driver.

If you can explain that flow, you understand the module.

