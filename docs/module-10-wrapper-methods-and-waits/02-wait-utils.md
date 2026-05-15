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

## Mental Model

[WaitUtils.java](../../src/main/java/com/learning/framework/waits/WaitUtils.java)
is a thin wrapper around Selenium's `WebDriverWait` and `ExpectedConditions`.
It does not create the browser, choose the timeout, or decide which page is
loaded. It receives an already-created `WebDriverWait` from
[BaseTest.java](../../src/test/java/com/learning/tests/base/BaseTest.java)
and exposes named wait methods that page objects and action wrappers can reuse.

```text
BaseTest creates WebDriverWait with 10 seconds
        |
        v
WaitUtils receives that wait
        |
        v
ElementActions and Page Objects call named wait methods
        |
        v
Selenium polls the browser until the condition passes or times out
```

## Current Wait Methods

| Method | Underlying Selenium Condition | Typical Use |
| --- | --- | --- |
| `waitForVisible(By)` | `visibilityOfElementLocated` | reading text, checking display state, typing into visible inputs |
| `waitForClickable(By)` | `elementToBeClickable` | clicking buttons, links, menu actions |
| `waitForText(By, String)` | `textToBe` | confirming page title or status text |
| `waitForUrlContains(String)` | `urlContains` | confirming navigation |
| `waitForMoreThan(By, int)` | `numberOfElementsToBeMoreThan` | waiting for inventory/table/list rows |

## Code Walkthrough

```java
private final WebDriverWait wait;
```

`WaitUtils` stores the Selenium wait object. The timeout and polling behavior
come from that object, not from every individual wait method. In this module,
the timeout is still hardcoded in
[BaseTest.java](../../src/test/java/com/learning/tests/base/BaseTest.java)
as `DEFAULT_TIMEOUT_SECONDS`.

```java
public WebElement waitForVisible(By locator) {
    return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
}
```

This waits until Selenium can locate the element and the element is visible.
The method returns the resulting `WebElement`, which lets
[ElementActions.java](../../src/main/java/com/learning/framework/actions/ElementActions.java)
immediately call methods such as `clear()`, `sendKeys(...)`, or `getText()`.

```java
public WebElement waitForClickable(By locator) {
    return wait.until(ExpectedConditions.elementToBeClickable(locator));
}
```

This waits for Selenium's clickable condition, which means the element is
visible and enabled. It is used before normal click actions.

```java
public boolean waitForText(By locator, String expectedText) {
    return wait.until(ExpectedConditions.textToBe(locator, expectedText));
}
```

This waits for exact text. [CartPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/CartPage.java)
uses it for `"Your Cart"` and
[CheckoutPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/CheckoutPage.java)
uses it for `"Checkout: Your Information"`. Exact text is useful for stable
page titles, but it is stricter than `containsText` style checks.

```java
public List<WebElement> waitForMoreThan(By locator, int minimumCount) {
    return wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(locator, minimumCount));
}
```

[ProductsPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java)
uses this in `waitUntilLoaded()` to confirm the inventory list is populated
before the test counts products or searches for a product card.

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

The creation order matters:

1. `driver = new ChromeDriver(options)` creates the browser session.
2. `wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS))`
   creates a Selenium wait tied to that browser.
3. `waits = new WaitUtils(wait)` wraps that Selenium wait.
4. `elementActions = new ElementActions(driver, waits)` gives actions access
   to both direct driver collection reads and named waits.

This is simple dependency injection. `BaseTest` builds the services and passes
them to the classes that need them.

## Explicit Waits vs Implicit Waits

This framework continues to prefer explicit waits.

Reasons:

- the wait is tied to a specific condition.
- failure messages point to a specific expectation.
- wrapper methods can choose the correct condition per action.
- implicit waits can make debugging timing issues less obvious.

Module 10 does not set an implicit wait. That is intentional. The learner
should be able to look at a page method and identify the exact wait condition
being used for that page state.

## Selenium Nuances

- A visibility wait is not the same as a clickability wait. Inputs usually need
  visibility before typing; buttons usually need clickability before clicking.
- A URL wait proves navigation changed, but it does not prove the page is ready
  for interaction. This is why the current page objects mainly wait for visible
  titles or elements.
- `waitForText(...)` waits for exact text. Extra whitespace or changed copy can
  make it fail.
- `waitForMoreThan(...)` is useful for lists, but it only proves the count. It
  does not prove every row/card has all expected child elements.
- Central waits do not automatically solve stale elements. A stale element
  problem still requires understanding when the DOM was refreshed and whether
  the element should be re-located.

## Common Beginner Mistakes

- wrapping every wait condition before knowing whether it is used.
- mixing long implicit waits with explicit waits.
- using visibility waits before clicking when clickability is the real need.
- waiting for an element when the real issue is the wrong window or frame.
- putting waits only in tests instead of framework/page layers.
- assuming every wait should live in `WaitUtils`; page-specific readiness still
  belongs in page methods such as `ProductsPage.waitUntilLoaded()`.

## Framework Bridge

Module 13 can later use this layer for richer diagnostics:

```text
action name + locator + timeout + current URL + screenshot
```

Module 10 does not add that yet. It creates the action/wait boundary that
diagnostics can attach to later.

## Interview Readiness

A strong answer:

`WaitUtils` centralizes common explicit wait conditions so page objects and
action wrappers do not repeat `wait.until(...)` everywhere. It should stay thin
and readable. The framework still needs the learner to choose the correct wait
for the situation: visible for reading/typing, clickable for clicking, exact
text for stable labels, URL checks for navigation, and list-count waits for
collection loading.

## Revision Checklist

- Can you explain why `WaitUtils` receives `WebDriverWait` instead of creating
  its own driver?
- Can you explain the difference between `waitForVisible(...)` and
  `waitForClickable(...)`?
- Can you find where `waitForMoreThan(...)` is used in
  [ProductsPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java)?
- Can you explain why Module 10 does not introduce implicit waits?
