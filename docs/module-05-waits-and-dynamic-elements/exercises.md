# Module 05 Exercises

These exercises extend raw wait practice. Do not add `WaitUtils`, page
objects, wrapper methods, or retry logic yet.

## Exercise 1 - Wait For The Loading Message To Disappear

In `_07_ExplicitWaitTest`, add an assertion that the loading indicator disappears.

Hint:

```java
ExpectedConditions.invisibilityOfElementLocated(By.id("loading"))
```

Expected outcome:
- the test waits for both the loading indicator and the final message.

## Exercise 2 - Wait For The Input To Disable Again

Extend `_08_DynamicControlsWaitTest` by clicking the input button again after
typing.

Hint:
- wait for button text to become `Disable`.
- click it.
- wait for the input to become disabled.

Expected outcome:
- the test demonstrates both enable and disable timing.

## Exercise 3 - Change FluentWait Polling

Change the polling interval in `_09_FluentWaitTest` from 250 milliseconds to 500
milliseconds.

Expected outcome:
- the test should still pass.
- explain how polling interval affects responsiveness and overhead.

## Exercise 4 - Controlled Timeout Message

Add an assertion that the timeout message contains the missing locator value:

```text
does-not-exist
```

Expected outcome:
- the test proves timeout errors include useful locator context.

## Exercise 5 - Explain Why Sleep Is Weak

In your own words, compare:

```java
Thread.sleep(3000);
```

with:

```java
wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("finish")));
```

Answer:

1. Which one expresses browser state?
2. Which one can return early when the page is ready?
3. Which one is easier to debug when it fails?

## Exercise 6 - Explain Stale Element Behavior

Review `_10_ImplicitWaitAndTimeoutTest` and answer:

1. Why does the original checkbox `WebElement` become stale?
2. Why can the locator still be useful after the old object is stale?
3. Why will wrapper methods later locate a fresh element instead of reusing
   the old object?

Expected outcome:
- explain the difference between a locator and a saved `WebElement` reference.
