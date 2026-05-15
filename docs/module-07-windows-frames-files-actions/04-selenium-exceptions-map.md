# Selenium Exceptions Map

## Files In This Topic

This topic reads these files:

- [src/test/java/com/learning/tests/learning/_19_JavaScriptAndExceptionsTest.java](../../src/test/java/com/learning/tests/learning/_19_JavaScriptAndExceptionsTest.java)


Module 07 completes the raw Selenium exception map before framework
abstractions begin.

## Exceptions Already Seen

| Exception | First Module | Meaning |
| --- | --- | --- |
| `NoSuchElementException` | Module 04 | valid locator, but no matching element in current context |
| `InvalidSelectorException` | Module 04 | selector syntax is invalid |
| `TimeoutException` | Module 05 | wait condition did not become true in time |
| `StaleElementReferenceException` | Module 05 | saved `WebElement` no longer points to a live DOM node |

## Exceptions Added In Module 07

| Exception | Common Cause | Example In This Module |
| --- | --- | --- |
| `NoSuchFrameException` | switching to a frame that does not exist in the current context | `driver.switchTo().frame("missing-frame")` |
| `NoSuchWindowException` | switching to a missing/closed window handle | `driver.switchTo().window("missing-window-handle")` |
| `ElementNotInteractableException` | element exists but cannot be interacted with, often hidden or disabled | clicking hidden button fixture |

## Why Exceptions Matter For Framework Design

The final framework should not swallow Selenium exceptions blindly. It should
make failures easier to diagnose.

Later modules can add:

- meaningful wrapper method messages.
- screenshots on failure.
- logging with locator/action context.
- retry only where retry is justified.
- custom framework exceptions for framework-level problems.

Module 07 keeps the raw exceptions visible so the learner understands the
failure category before wrappers are introduced.

## Beginner Mistakes

- treating every exception as a wait problem.
- retrying stale elements without locating a fresh element.
- switching frames/windows without proving the target exists.
- using JavaScript click to hide `ElementNotInteractableException` before
  understanding why the element cannot be used.

## Diagnostic Decision Guide

When a Selenium exception appears, diagnose in this order:

1. **Context:** Am I in the correct window, frame, or shadow root?
2. **Locator:** Is the selector valid, stable, and searching the right scope?
3. **Timing:** Does the element/page state appear after a delay?
4. **Interactability:** Is the element visible, enabled, unobstructed, and the
   right target for the action?
5. **Freshness:** Did I save a `WebElement` before the DOM was rebuilt?

This order prevents a common beginner habit: adding longer waits before
checking whether Selenium is searching the wrong context.

## Java Syntax To Notice

```java
Assert.expectThrows(
        NoSuchFrameException.class,
        () -> driver.switchTo().frame("missing-frame")
);
```

`Assert.expectThrows` verifies that a specific exception type is thrown. The
`() -> ...` part is a lambda expression: it delays the action so TestNG can run
it inside the assertion and inspect the thrown exception.

This pattern is only used here as a learning exercise. Normal test code should
usually assert application behavior, not intentionally trigger Selenium
failures.

## Interview Readiness

**Question: Should framework code catch and hide Selenium exceptions?**

No. Framework code should add useful context and diagnostics, then either let
the failure surface or throw a clearer framework-level exception. Swallowing
exceptions creates false confidence.

**Question: What should you check when you see `NoSuchFrameException`?**

Check whether the frame exists in the current context, whether the page has
loaded it yet, and whether Selenium is already inside the correct parent frame.

**Question: What should you check when you see `ElementNotInteractableException`?**

Check visibility, enabled state, overlays, scrolling, timing, and whether the
test is trying to interact with the wrong element.

## Revision Checklist

- Can you map every exception in this file to a likely root cause?
- Can you explain which exceptions are timing-related and which are context or
  syntax problems?
- Can you explain how screenshots and logs will improve diagnosis later?
