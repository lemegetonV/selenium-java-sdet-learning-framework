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

```text
src/test/java/com/learning/tests/learning/DynamicControlsWaitTest.java
```

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

```text
src/test/java/com/learning/tests/learning/ImplicitWaitAndTimeoutTest.java
```

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

Module 05 avoids deep stale-element recovery, but dynamic controls prepare the
concept:

- the checkbox is removed.
- the old element reference would no longer be safe.
- the test waits and finds the element again after it is added.

Later modules will handle stale elements more directly when wrapper methods
and retry behavior are introduced.
