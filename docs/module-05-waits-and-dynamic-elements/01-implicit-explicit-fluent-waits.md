# Implicit, Explicit, and Fluent Waits

## Why Waits Exist

Modern web pages do not always update immediately after a click.

Common examples:

- a loader appears.
- an element is added later.
- an input becomes enabled later.
- text changes after an async request.

Without waits, tests can ask for an element before the page is ready.

## Implicit Wait

Module 05 introduces implicit wait in:

```text
src/test/java/com/learning/tests/learning/_10_ImplicitWaitAndTimeoutTest.java
```

Example:

```java
driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
```

Behavior:

- applies to future `findElement` and `findElements` calls for that driver.
- is global driver state.
- can hide where the test is waiting.

Nuance:

- implicit waits are easy to start with but harder to reason about in larger
  frameworks.
- this project will prefer explicit waits for specific conditions.
- avoid mixing long implicit waits with explicit waits because timing becomes
  less clear.

## Explicit Wait

Module 05 introduces explicit wait in:

```text
src/test/java/com/learning/tests/learning/_07_ExplicitWaitTest.java
src/test/java/com/learning/tests/learning/_08_DynamicControlsWaitTest.java
```

Example:

```java
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("finish")));
```

Behavior:

- waits for one specific condition.
- polls repeatedly until the condition is true.
- throws `TimeoutException` if the condition never becomes true.

Explicit waits are usually easier to document and debug because the condition
is close to the action that needs it.

## Fluent Wait

Module 05 introduces FluentWait in:

```text
src/test/java/com/learning/tests/learning/_09_FluentWaitTest.java
```

Example:

```java
FluentWait<WebDriver> wait = new FluentWait<>(driver)
        .withTimeout(Duration.ofSeconds(10))
        .pollingEvery(Duration.ofMillis(250))
        .ignoring(NoSuchElementException.class);
```

Behavior:

- lets the test choose timeout.
- lets the test choose polling interval.
- lets the test choose ignored exceptions.
- accepts a custom condition function.

Nuance:

- `WebDriverWait` is already a specialized FluentWait for WebDriver.
- use FluentWait when the default explicit wait shape is not expressive enough.
- do not overuse custom waits before the framework has a real need.

## Why We Do Not Add `WaitUtils` Yet

Module 05 intentionally repeats wait code.

The repetition teaches what later centralization will solve:

- consistent timeout values.
- consistent polling.
- reusable conditions.
- cleaner page objects and wrapper methods.
- better timeout messages.

The actual `WaitUtils` or wrapper layer is deferred until Module 10.

## Java Syntax To Notice

```java
Duration.ofSeconds(10)
```

`Duration` is Java's type for an amount of time. Selenium wait APIs use it so
timeouts are explicit and not just unexplained numbers.

```java
wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("finish")));
```

`until(...)` repeatedly evaluates the condition. If the condition returns a
successful value before the timeout, that value is returned to the test.

```java
FluentWait<WebDriver> wait = new FluentWait<>(driver)
```

`FluentWait<WebDriver>` is generic. It means the wait repeatedly works with a
`WebDriver` input object.

## Interview Readiness

**Question: What is synchronization in Selenium?**

Synchronization is aligning the test's next command with the browser's actual
state. The test should wait for the specific state it needs, such as visibility
or clickability, instead of guessing with fixed sleep.

**Question: Why avoid mixing implicit and explicit waits?**

Implicit wait changes global element lookup behavior. Explicit wait has its
own timeout and polling. Combining long values can make actual timing harder to
predict and debug.

**Question: When would you use FluentWait?**

Use it when you need custom polling, ignored exceptions, or a custom condition
that is more specific than the common `ExpectedConditions` helpers.

## Revision Checklist

- Can you explain which wait type is global and which is condition-specific?
- Can you explain why `Thread.sleep` is weaker than state-based waiting?
- Can you explain why this project delays `WaitUtils` until Module 10?
