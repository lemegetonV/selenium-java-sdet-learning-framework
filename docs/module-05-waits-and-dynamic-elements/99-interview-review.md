# Module 05 Interview Review

## What You Must Be Able To Explain

Module 05 is about synchronization. You should be able to explain:

- why modern web pages need waits.
- implicit wait vs explicit wait vs fluent wait.
- polling, timeout, and ignored exceptions.
- expected conditions and how to choose one.
- `TimeoutException`.
- `StaleElementReferenceException`.
- why `Thread.sleep` is not a good framework strategy.

## Strong Answers

**What is synchronization in Selenium?**

Synchronization means making the test wait until the browser reaches the state
needed for the next command. Good synchronization waits for a meaningful
condition, not an arbitrary number of seconds.

**What is an explicit wait?**

An explicit wait waits for one specific condition, such as visibility,
clickability, text, invisibility, or staleness. It polls until the condition is
true or the timeout expires.

**What is a fluent wait?**

`FluentWait` is a configurable wait where you choose timeout, polling interval,
ignored exceptions, and custom condition logic.

**What is stale element reference?**

It means the `WebElement` object saved earlier no longer points to a live DOM
node. The page may have removed or replaced the element.

## Code Lines To Revise

```java
new WebDriverWait(driver, Duration.ofSeconds(10))
```

Create a wait tied to one driver and a maximum timeout.

```java
ExpectedConditions.visibilityOfElementLocated(By.id("finish"))
```

Find the element and wait until it is displayed.

```java
.pollingEvery(Duration.ofMillis(250))
```

Control how often a fluent wait checks the condition.

```java
wait.until(ExpectedConditions.stalenessOf(originalCheckbox))
```

Wait until the saved element reference is detached from the DOM.

## Common Interview Traps

- Saying implicit wait waits for page load. It affects element lookup, not full
  page readiness.
- Using `Thread.sleep` as the main synchronization answer.
- Waiting for presence when visibility or clickability is required.
- Retrying stale elements without locating a fresh element.
- Treating every failure as a wait problem instead of checking locator,
  context, and application behavior.

## Connection To Future Framework Modules

Module 10 will centralize wait and wrapper behavior. That should not feel like
magic: it is just a reusable home for the wait decisions learned here. Module
13 will add screenshots and logs so timeout failures are easier to diagnose.
