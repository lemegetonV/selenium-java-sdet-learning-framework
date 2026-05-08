# Selenium Exceptions Map

## Files In This Topic

```text
src/test/java/com/learning/tests/learning/_19_JavaScriptAndExceptionsTest.java
```

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
