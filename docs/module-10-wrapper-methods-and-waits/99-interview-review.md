# Module 10 Interview Review

## What You Must Be Able To Explain

Module 10 introduces wrapper methods and centralized waits. You should be able
to explain:

- why `ElementActions` exists.
- why `WaitUtils` exists.
- why wrappers accept `By` locators.
- how wrappers improve Page Objects.
- why wrapper methods should not hide every failure.
- why JavaScript click fallback is deferred.
- how this prepares for logging, screenshots, and reporting.
- why `DriverFactory` is still a separate future concern.

## Strong Answers

**Why do we need ElementActions if Page Objects already exist?**

Page Objects organize page behavior and locators. `ElementActions` organizes
repeated Selenium mechanics such as wait, find, click, type, text read, and
display checks.

**Why should wrappers accept `By` instead of `WebElement`?**

A `By` locator lets the wrapper find the element at action time. That works
better with explicit waits and reduces the chance of acting on an old saved
element.

**What does WaitUtils centralize?**

It centralizes common explicit wait conditions: visible, clickable, text,
URL, and list-count waits. This keeps wait choices consistent across pages.

**Should wrapper methods catch and hide Selenium exceptions?**

No. They may later add context, screenshots, and logs, but they should not hide
real failures. Swallowing exceptions creates false passing tests.

**Why not add JavaScript click fallback now?**

Because JavaScript click can hide real interactability problems. It should be
introduced only with clear rules and diagnostics, not as a default first
wrapper behavior.

## Code Lines To Revise

```java
public void click(By locator) {
    waits.waitForClickable(locator).click();
}
```

Waits for clickability, then clicks.

```java
public void type(By locator, String value) {
    WebElement element = waits.waitForVisible(locator);
    element.clear();
    element.sendKeys(value);
}
```

Waits for visibility, clears, then types.

```java
waits = new WaitUtils(wait);
elementActions = new ElementActions(driver, waits);
```

`BaseTest` creates wrapper services after creating the browser and wait.

```java
actions.type(USERNAME_INPUT, username);
```

Page Object calls a framework action instead of repeating raw Selenium.

## Common Interview Traps

- Saying wrappers are just shorter method names.
- Catching every exception and returning false.
- using JavaScript click as the default click method.
- putting SauceDemo-specific behavior in `ElementActions`.
- making wrappers so generic that nobody can tell what they wait for.
- adding logging/reporting before action boundaries are stable.

## Framework Phase Bridge

Module 11 can now introduce `ConfigReader` and `DriverFactory` because action
and wait wrappers have a place. The remaining hardcoded concern is browser
configuration: Chrome creation, headless mode, window size, and timeouts still
live in `BaseTest`.
