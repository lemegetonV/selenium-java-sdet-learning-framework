# TestNG Parallel Execution And ThreadLocal

## Mental Model

Selenium itself does not decide how many tests run at the same time. TestNG
does. Selenium only controls browser sessions.

When TestNG runs methods in parallel, each test thread needs its own browser
and its own framework service references. Shared mutable fields are dangerous.

Think of a parallel suite as three separate workers using the same Java
classes at the same time. Each worker must have a private browser and private
helper objects. The Java class definition is shared, but the test state must be
isolated.

The rule for this module is:

- suite-level configuration can be shared.
- immutable data can be shared or reused safely.
- browser sessions, waits, wrapper services, page objects, and active report
  nodes must belong to one test thread.

## Code Walkthrough

Parallel suite:

[testng-parallel.xml](../../testng-parallel.xml)

```xml
<suite name="Module 15 Parallel Regression" parallel="methods" thread-count="3">
```

`parallel="methods"` means TestNG can run different `@Test` methods at the
same time. `thread-count="3"` limits the thread pool to three concurrent test
methods.

This project keeps [testng.xml](../../testng.xml) sequential on purpose. That
gives you a comparison point: if the sequential suite passes and only the
parallel suite fails, the first thing to suspect is shared mutable state, not a
normal assertion problem.

Driver ownership:

[src/main/java/com/learning/framework/driver/DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java)

```java
private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
```

`DRIVER` is static because the driver factory is a utility-style framework
service. The important part is that the value inside the static `ThreadLocal`
is not one global driver. It is one value per current thread.

The creation flow is:

1. `DriverFactory.createDriver()` checks whether the current thread already has
   a driver.
2. It reads `executionMode` from [ConfigReader.java](../../src/main/java/com/learning/framework/config/ConfigReader.java).
3. It creates either a local driver or a `RemoteWebDriver`.
4. It applies timeout and window settings.
5. It stores the driver with `DRIVER.set(driver)`.

The access flow is:

```java
WebDriver driver = DRIVER.get();
```

`get()` returns the driver for the current thread only. Another TestNG worker
thread calling the same method gets its own driver.

Test-level service ownership:

[src/test/java/com/learning/tests/base/BaseTest.java](../../src/test/java/com/learning/tests/base/BaseTest.java)

```java
private final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
private final ThreadLocal<WaitUtils> waits = new ThreadLocal<>();
private final ThreadLocal<ElementActions> elementActions = new ThreadLocal<>();
```

Tests now use:

```java
new LoginPage(driver(), elementActions(), waits())
```

That small syntax change is the main design change in the test classes. In
[SauceDemoPageObjectTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoPageObjectTest.java)
and [SauceDemoDataDrivenTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoDataDrivenTest.java),
page objects are built inside the test method from the current thread's
framework services. They are not stored in static fields, and they are not
reused across tests.

The setup flow in [BaseTest.java](../../src/test/java/com/learning/tests/base/BaseTest.java)
is:

1. `@BeforeMethod` runs before each test method invocation.
2. `DriverFactory.createDriver()` creates a browser for the current worker
   thread.
3. `WebDriverWait`, `WaitUtils`, and `ElementActions` are created for that
   exact driver.
4. The references are stored in `ThreadLocal` variables.
5. The test method calls `driver()`, `waits()`, and `elementActions()` to read
   the current thread's values.

The teardown flow is equally important:

1. `@AfterMethod` runs after the test method.
2. `DriverFactory.quitDriver()` quits the current thread's browser and removes
   the factory's driver reference.
3. `BaseTest` removes its own thread-local service references.

The browser must be quit and the thread-local values must be removed because
TestNG worker threads can be reused for later method invocations.

## Java Syntax To Notice

`ThreadLocal<T>` stores one value per thread. If three TestNG worker threads are
running, each thread sees its own value when calling `get()`.

`remove()` matters. It clears the current thread's value after the test. This
prevents a reused worker thread from seeing stale browser or wrapper objects in
the next test.

Accessor methods such as `driver()` are intentional. They make the test read
almost like a field but fetch the current thread's value.

`final ThreadLocal<WebDriver> driver` means the `ThreadLocal` container
reference cannot be reassigned after construction. It does not mean the driver
inside it is a constant. Each thread can still call `set()` and `remove()` for
its own value.

The generic type in `ThreadLocal<WebDriver>` matters because it preserves type
safety. `driver.get()` returns a `WebDriver`, while `elementActions.get()`
returns an `ElementActions`.

## Selenium Or Framework Nuances

Thread-local driver factory alone is not enough if `BaseTest` stores the driver
again in a normal field. That second reference can still be overwritten by
another parallel method.

Page objects should stay per-test objects. Do not store page objects in static
fields. A page object carries framework services for one browser session.

`parallel="methods"` is a good first parallel mode for this learning framework
because each test method already opens and closes its own browser through
`@BeforeMethod` and `@AfterMethod`. If the framework used `@BeforeClass` for
browser setup, method-level parallelism would be much riskier because multiple
methods could attempt to share one browser session.

Data-driven methods in [SauceDemoDataDrivenTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoDataDrivenTest.java)
also run through the same lifecycle. Every invocation receives a
`LoginScenario` and builds a fresh [LoginPage](../../src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java)
from the current thread's services.

Class fields are not automatically forbidden. The user names in
[SauceDemoPageObjectTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoPageObjectTest.java)
are stable test data initialized once and read by methods. The unsafe pattern
is storing changing per-test objects, such as `WebDriver`, `WebDriverWait`,
`ElementActions`, or page objects, in fields that parallel methods can
overwrite.

## Debugging Parallel Failures

When a test fails only in [testng-parallel.xml](../../testng-parallel.xml):

1. Check whether the same test passes in [testng.xml](../../testng.xml).
2. Inspect `target/logs/test-execution.log` for the failing test name and
   thread ID.
3. Confirm [DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java)
   created and quit one browser on that same thread.
4. Check whether the test class stores mutable objects in fields.
5. Check whether reports or screenshots point to the correct logical test.

The most common sign of shared driver state is a failure that mentions an
unexpected page, an already-closed browser, a missing session, or actions
executing in a browser window that belongs to another test.

## Common Mistakes

- Keeping `protected WebDriver driver` as a normal field under parallel TestNG.
- Using static page objects.
- Sharing one `WebDriverWait` across threads.
- Running all tests in parallel immediately instead of starting with a small
  thread count.
- Assuming faster execution means safer execution.
- Calling `ThreadLocal.set()` during setup but forgetting `remove()` during
  teardown.
- Mixing method-level parallelism with tests that depend on execution order.
- Debugging a parallel-only failure as if it were always an application defect.

## Interview Readiness

Strong answer:

"In TestNG parallel execution, the test framework creates multiple worker
threads. A Selenium framework must keep WebDriver and related services isolated
per thread, usually with `ThreadLocal`. It is not enough for only the driver
factory to be thread-local if the base test stores that driver in shared
instance fields."

Follow-up framing:

"I would also verify that report managers, screenshots, logs, data providers,
and page objects are parallel-safe. Driver isolation solves only the browser
part of the problem."

## Revision Checklist

- Can you explain what `parallel="methods"` does?
- Can you explain why `thread-count` should start small?
- Can you show where `ThreadLocal` is used in both `DriverFactory` and
  `BaseTest`?
- Can you explain why `remove()` is called during cleanup?
- Can you explain why a page object should be created inside a test method?
- Can you explain why a sequential pass and parallel failure usually points to
  shared state?
