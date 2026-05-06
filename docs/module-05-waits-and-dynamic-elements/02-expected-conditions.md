# Expected Conditions

## What `ExpectedConditions` Provides

`ExpectedConditions` is a Selenium helper class with common wait conditions.

Module 05 uses:

- `visibilityOfElementLocated`.
- `invisibilityOfElementLocated`.
- `textToBePresentInElementLocated`.
- `elementToBeClickable`.

These conditions are used with:

```java
wait.until(...)
```

## `visibilityOfElementLocated`

Used in:

```text
src/test/java/com/learning/tests/learning/_07_ExplicitWaitTest.java
src/test/java/com/learning/tests/learning/_08_DynamicControlsWaitTest.java
```

Behavior:

- finds the element.
- waits until it is displayed.
- returns the `WebElement`.

Good use:

- waiting for text that appears after a loader.
- waiting for an element that is added back to the page.

## `invisibilityOfElementLocated`

Used in:

```text
src/test/java/com/learning/tests/learning/_08_DynamicControlsWaitTest.java
```

Behavior:

- returns true when the element is hidden or gone.

Good use:

- waiting for loaders to disappear.
- waiting for removed elements.

Nuance:

- disappearing from the DOM and becoming hidden are different UI behaviors.
- this condition can handle both for many beginner cases.

## `textToBePresentInElementLocated`

Used in:

```text
src/test/java/com/learning/tests/learning/_08_DynamicControlsWaitTest.java
```

Behavior:

- finds the element.
- waits until its visible text contains the expected text.

Good use:

- waiting until a reused button changes from `Remove` to `Add`.
- waiting until a status message reports a completed action.

## `elementToBeClickable`

Used in:

```text
src/test/java/com/learning/tests/learning/_08_DynamicControlsWaitTest.java
```

Behavior:

- waits until the element is visible and enabled.
- returns the `WebElement`.

Good use:

- waiting before clicking or typing into an input that is initially disabled.

Nuance:

- clickable does not guarantee every real-world click will succeed.
- overlays, animations, scrolling, or intercepted clicks can still cause
  failures.
- later wrapper methods will centralize safer click behavior.

## `stalenessOf`

Module 05 introduces `stalenessOf` in:

```text
src/test/java/com/learning/tests/learning/_10_ImplicitWaitAndTimeoutTest.java
```

The condition receives an already-located `WebElement` and waits until that
specific object reference is detached from the DOM:

```java
wait.until(ExpectedConditions.stalenessOf(originalCheckbox));
```

Use it when:

- an element is removed or replaced after an action.
- the old `WebElement` should no longer be reused.
- the test needs to prove the page moved from one DOM state to another.

Nuance:

- the locator may still be useful after the change.
- the saved `WebElement` object may be stale even if a new matching element
  appears later.
- future wrapper methods can retry by locating a fresh element, but Module 05
  keeps the behavior raw and visible.

## Common Beginner Mistakes

- waiting for presence when the test needs visibility.
- waiting for clickability when the test only needs to assert presence.
- using one generic wait everywhere without thinking about the condition.
- using `Thread.sleep(...)` instead of waiting for browser state.
