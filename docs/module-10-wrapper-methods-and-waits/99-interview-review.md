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
- why page-specific logic stays in page objects even after wrappers exist.
- why exact wait choices matter for readable failures.

## Strong Answers

**Why do we need ElementActions if Page Objects already exist?**

Page Objects organize page behavior and locators. `ElementActions` organizes
repeated Selenium mechanics such as wait, find, click, type, text read, and
display checks.

**Why should wrappers accept `By` instead of `WebElement`?**

A `By` locator lets the wrapper find the element at action time. That works
better with explicit waits and reduces the chance of acting on an old saved
element.

In this checkpoint, [ElementActions.java](../../src/main/java/com/learning/framework/actions/ElementActions.java)
uses `By` for normal page actions and only accepts `WebElement` in
`clickInside(...)`, where [ProductsPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java)
has already selected the correct product card.

**What does WaitUtils centralize?**

It centralizes common explicit wait conditions: visible, clickable, text,
URL, and list-count waits. This keeps wait choices consistent across pages.

[WaitUtils.java](../../src/main/java/com/learning/framework/waits/WaitUtils.java)
is intentionally thin. It wraps Selenium's `ExpectedConditions` instead of
inventing a new waiting framework.

**Should wrapper methods catch and hide Selenium exceptions?**

No. They may later add context, screenshots, and logs, but they should not hide
real failures. Swallowing exceptions creates false passing tests.

**Why not add JavaScript click fallback now?**

Because JavaScript click can hide real interactability problems. It should be
introduced only with clear rules and diagnostics, not as a default first
wrapper behavior.

**Why does `LoginPage` still receive `WebDriver`?**

[LoginPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java)
owns `open()`, and opening the login URL is browser navigation. Normal element
actions in the same class still go through `ElementActions`.

**Why should product-card search stay in `ProductsPage`?**

Searching for a SauceDemo product card by visible product name is application
behavior, not a generic Selenium action. [ProductsPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java)
should own that rule; `ElementActions` should only provide generic collection
and scoped-click mechanics.

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

```java
return new CheckoutPage(actions, waits).waitForInformationStep();
```

[CartPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/CartPage.java)
models a page transition: after checkout is clicked, the method returns the
next page object after that page confirms it is ready.

```java
waits.waitForMoreThan(INVENTORY_ITEMS, 0);
```

[ProductsPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java)
does not just wait for a title. It also waits for inventory content before
product counts and product search run.

## Common Interview Traps

- Saying wrappers are just shorter method names.
- Catching every exception and returning false.
- using JavaScript click as the default click method.
- putting SauceDemo-specific behavior in `ElementActions`.
- making wrappers so generic that nobody can tell what they wait for.
- adding logging/reporting before action boundaries are stable.
- saying Page Objects and wrappers solve the same problem.
- saying a URL wait alone proves a page is ready for interaction.

## Debugging Questions

If a Module 10 test fails, ask:

- Did [BaseTest.java](../../src/test/java/com/learning/tests/base/BaseTest.java)
  create `WaitUtils` and `ElementActions` after creating the browser?
- Is the page object using the correct wrapper method for the action?
- Is the wait condition correct for the problem: visible, clickable, text, URL,
  or list count?
- Did a page method return the correct next page object after navigation?
- Did page-specific lookup logic stay in the page object instead of being
  pushed into a generic utility?

## Framework Phase Bridge

Module 11 can now introduce `ConfigReader` and `DriverFactory` because action
and wait wrappers have a place. The remaining hardcoded concern is browser
configuration: Chrome creation, headless mode, window size, and timeouts still
live in `BaseTest`.

## One-Minute Whiteboard Answer

Module 10 adds the first action and wait service layer. Tests still express
workflow through [SauceDemoPageObjectTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoPageObjectTest.java).
Page objects such as [LoginPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java)
and [ProductsPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java)
still own locators and page behavior. Common Selenium mechanics move into
[ElementActions.java](../../src/main/java/com/learning/framework/actions/ElementActions.java),
and common explicit waits move into [WaitUtils.java](../../src/main/java/com/learning/framework/waits/WaitUtils.java).
This prepares the framework for future diagnostics without hiding Selenium.
