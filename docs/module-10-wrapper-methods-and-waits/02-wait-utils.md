# WaitUtils

## Files In This Topic

This topic reads these files:

- [src/main/java/com/learning/framework/waits/WaitUtils.java](../../src/main/java/com/learning/framework/waits/WaitUtils.java)
- [src/test/java/com/learning/tests/base/BaseTest.java](../../src/test/java/com/learning/tests/base/BaseTest.java)


## Why WaitUtils Exists

Module 05 taught explicit waits. Module 09 page objects still repeated:

```java
wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
```

Module 10 centralizes common wait conditions:

```java
waits.waitForVisible(locator);
waits.waitForClickable(locator);
waits.waitForText(locator, "Products");
```

The framework is not hiding Selenium waits. It is naming the common waits so
page objects stay focused on page behavior.

## Current Wait Methods

| Method | Underlying Selenium Condition | Typical Use |
| --- | --- | --- |
| `waitForVisible(By)` | `visibilityOfElementLocated` | reading text, checking display state, typing into visible inputs |
| `waitForClickable(By)` | `elementToBeClickable` | clicking buttons, links, menu actions |
| `waitForText(By, String)` | `textToBe` | confirming page title or status text |
| `waitForUrlContains(String)` | `urlContains` | confirming navigation |
| `waitForMoreThan(By, int)` | `numberOfElementsToBeMoreThan` | waiting for inventory/table/list rows |

## Why WaitUtils Is Created In BaseTest

`BaseTest` creates:

```java
wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
waits = new WaitUtils(wait);
elementActions = new ElementActions(driver, waits);
```

This keeps one timeout policy for the test session. Module 11 will move timeout
values into configuration. Module 10 keeps the timeout value in `BaseTest`
because the framework has not introduced `ConfigReader` yet.

## Explicit Waits vs Implicit Waits

This framework continues to prefer explicit waits.

Reasons:

- the wait is tied to a specific condition.
- failure messages point to a specific expectation.
- wrapper methods can choose the correct condition per action.
- implicit waits can make debugging timing issues less obvious.

## Common Beginner Mistakes

- wrapping every wait condition before knowing whether it is used.
- mixing long implicit waits with explicit waits.
- using visibility waits before clicking when clickability is the real need.
- waiting for an element when the real issue is the wrong window or frame.
- putting waits only in tests instead of framework/page layers.

## Framework Bridge

Module 13 can later use this layer for richer diagnostics:

```text
action name + locator + timeout + current URL + screenshot
```

Module 10 does not add that yet. It creates the action/wait boundary that
diagnostics can attach to later.
