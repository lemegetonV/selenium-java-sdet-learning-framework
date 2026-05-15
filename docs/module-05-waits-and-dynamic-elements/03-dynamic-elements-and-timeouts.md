# Dynamic Elements and Timeouts

## Dynamic Loading

The Internet dynamic loading pages delay content after a button click.

Module 05 uses:

```text
https://the-internet.herokuapp.com/dynamic_loading/1
https://the-internet.herokuapp.com/dynamic_loading/2
```

The first page hides an existing element until loading finishes.

The second page renders the element after loading finishes.

Both are timing problems. The test should wait for browser state, not sleep for
an arbitrary number of seconds.

## Dynamic Controls

The dynamic controls page changes the DOM after a delay:

```text
https://the-internet.herokuapp.com/dynamic_controls
```

Module 05 uses:

[src/test/java/com/learning/tests/learning/_08_DynamicControlsWaitTest.java](../../src/test/java/com/learning/tests/learning/_08_DynamicControlsWaitTest.java)

Important nuance:

- the checkbox is first inside a `<div id="checkbox">`.
- after the Add action, the new checkbox is an `<input id="checkbox">`.
- the id stays useful, but the DOM shape changes.

That is why the test uses:

```java
By checkbox = By.id("checkbox");
```

instead of a more shape-dependent selector such as:

```java
By.cssSelector("#checkbox input")
```

This is a real locator-stability lesson: dynamic pages can change structure
even when the visible feature looks the same.

## Timeout Failures

When a wait condition never becomes true, Selenium throws
`TimeoutException`.

Module 05 demonstrates this without leaving the test failing:

[src/test/java/com/learning/tests/learning/_10_ImplicitWaitAndTimeoutTest.java](../../src/test/java/com/learning/tests/learning/_10_ImplicitWaitAndTimeoutTest.java)

The test intentionally waits for an element that does not exist and asserts
that `TimeoutException` is thrown.

This teaches the failure mode while keeping the module quality gate green.

## Why Not `Thread.sleep`

`Thread.sleep(3000)` pauses for exactly three seconds whether the page is ready
or not.

Problems:

- too short creates flaky tests.
- too long slows every run.
- it does not explain what browser state the test needs.
- it hides the real condition.

Waits are better because they express what the test is waiting for.

## Stale Element Basics

A stale element happens when a `WebElement` reference points to an element that
is no longer attached to the current DOM.

Module 05 demonstrates the stale reference in:

[src/test/java/com/learning/tests/learning/_10_ImplicitWaitAndTimeoutTest.java](../../src/test/java/com/learning/tests/learning/_10_ImplicitWaitAndTimeoutTest.java)

The dynamic controls page prepares the concept:

- the checkbox is removed.
- the old `WebElement` reference is no longer safe.
- `ExpectedConditions.stalenessOf(...)` waits for that old reference to detach.
- the test intentionally verifies `StaleElementReferenceException`.

Later modules will handle stale elements more directly when wrapper methods
and retry behavior are introduced.

## Java Syntax To Notice

```java
TimeoutException timeout = Assert.expectThrows(
        TimeoutException.class,
        () -> wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("does-not-exist")))
);
```

The `() -> ...` part is a lambda. It delays running the wait until TestNG is
ready to observe the expected exception. Without the lambda, the exception
would be thrown before `expectThrows` could assert it.

```java
originalCheckbox::isSelected
```

This is a method reference. It points to the `isSelected` method on the saved
`WebElement` object. TestNG calls it and expects the stale element exception.

## Interview Readiness

**Question: What causes stale element reference?**

The page changed after Selenium found an element. The saved `WebElement`
reference points to a DOM node that no longer exists in the current page
state. The solution is usually to locate the element again after the DOM
change, not reuse the old object.

**Question: What information should a wait timeout failure provide?**

It should say what condition was being waited for, which locator or element was
involved, and how long the wait lasted. Later framework utilities will improve
these diagnostics.

**Question: Why is `Thread.sleep` considered a bad synchronization strategy?**

It waits for time, not state. It can be too short on slow runs and too long on
fast runs, causing both flakiness and wasted time.

## Revision Checklist

- Can you explain why dynamic pages can change DOM shape?
- Can you explain what the lambda in `expectThrows` is doing?
- Can you explain how stale elements connect to future retry and wrapper logic?
