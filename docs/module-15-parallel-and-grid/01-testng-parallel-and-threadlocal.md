# TestNG Parallel Execution And ThreadLocal

## Mental Model

Selenium itself does not decide how many tests run at the same time. TestNG
does. Selenium only controls browser sessions.

When TestNG runs methods in parallel, each test thread needs its own browser
and its own framework service references. Shared mutable fields are dangerous.

## Code Walkthrough

Parallel suite:

`testng-parallel.xml`

```xml
<suite name="Module 15 Parallel Regression" parallel="methods" thread-count="3">
```

`parallel="methods"` means TestNG can run different `@Test` methods at the
same time. `thread-count="3"` limits the thread pool to three concurrent test
methods.

Driver ownership:

`src/main/java/com/learning/framework/driver/DriverFactory.java`

```java
private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
```

Test-level service ownership:

`src/test/java/com/learning/tests/base/BaseTest.java`

```java
private final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
private final ThreadLocal<WaitUtils> waits = new ThreadLocal<>();
private final ThreadLocal<ElementActions> elementActions = new ThreadLocal<>();
```

Tests now use:

```java
new LoginPage(driver(), elementActions(), waits())
```

## Java Syntax To Notice

`ThreadLocal<T>` stores one value per thread. If three TestNG worker threads are
running, each thread sees its own value when calling `get()`.

`remove()` matters. It clears the current thread's value after the test. This
prevents a reused worker thread from seeing stale browser or wrapper objects in
the next test.

Accessor methods such as `driver()` are intentional. They make the test read
almost like a field but fetch the current thread's value.

## Selenium Or Framework Nuances

Thread-local driver factory alone is not enough if `BaseTest` stores the driver
again in a normal field. That second reference can still be overwritten by
another parallel method.

Page objects should stay per-test objects. Do not store page objects in static
fields. A page object carries framework services for one browser session.

## Common Mistakes

- Keeping `protected WebDriver driver` as a normal field under parallel TestNG.
- Using static page objects.
- Sharing one `WebDriverWait` across threads.
- Running all tests in parallel immediately instead of starting with a small
  thread count.
- Assuming faster execution means safer execution.

## Interview Readiness

Strong answer:

"In TestNG parallel execution, the test framework creates multiple worker
threads. A Selenium framework must keep WebDriver and related services isolated
per thread, usually with `ThreadLocal`. It is not enough for only the driver
factory to be thread-local if the base test stores that driver in shared
instance fields."

## Revision Checklist

- Can you explain what `parallel="methods"` does?
- Can you explain why `thread-count` should start small?
- Can you show where `ThreadLocal` is used in both `DriverFactory` and
  `BaseTest`?
- Can you explain why `remove()` is called during cleanup?

