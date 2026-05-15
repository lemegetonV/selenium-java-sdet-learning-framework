# ElementActions

## Files In This Topic

This topic reads these files:

- [src/main/java/com/learning/framework/actions/ElementActions.java](../../src/main/java/com/learning/framework/actions/ElementActions.java)
- [src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java)
- [src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java)
- [src/main/java/com/learning/framework/pages/saucedemo/CartPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/CartPage.java)
- [src/main/java/com/learning/framework/pages/saucedemo/CheckoutPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/CheckoutPage.java)


## What ElementActions Solves

Module 09 Page Objects improved test readability, but page classes still had
repeated raw Selenium commands:

```java
driver.findElement(LOGIN_BUTTON).click();
driver.findElement(USERNAME_INPUT).sendKeys(username);
driver.findElement(PAGE_TITLE).getText();
```

`ElementActions` centralizes the first common actions:

- `click`.
- `type`.
- `getText`.
- `isDisplayed`.
- `getElementCount`.
- `findAll`.
- `selectByVisibleText`.
- `clickInside`.

The key learning point is not "shorter method names." The key learning point
is consistent behavior. If every page writes its own click/type/read logic,
then every page can make a different wait decision, a different exception
decision, and a different diagnostic decision. `ElementActions` gives those
decisions one framework home.

## Mental Model

Think of [ElementActions.java](../../src/main/java/com/learning/framework/actions/ElementActions.java)
as the first service below Page Objects:

```text
Page Object method: loginAs(...)
        |
        v
ElementActions: type username, type password, click login
        |
        v
WaitUtils: wait for visible/clickable element
        |
        v
Selenium: WebElement.clear(), sendKeys(), click()
```

Page Objects still decide *what* action the user is performing. `ElementActions`
decides *how* the Selenium action is performed safely enough for this module.

## Click

```java
public void click(By locator) {
    waits.waitForClickable(locator).click();
}
```

This combines:

1. wait for the element to be clickable.
2. click the element.

The important design choice is that the caller passes a `By`, not a saved
`WebElement`. That lets the wrapper find the element at action time.

This is used by [LoginPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java)
for the login button, [ProductsPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java)
for the cart link, and [CartPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/CartPage.java)
for the checkout button.

The wrapper does not catch the exception. If Selenium cannot click the element,
the test should fail with the real cause. Later modules can attach screenshots,
logs, and report steps around this action, but Module 10 keeps failure behavior
plain.

## Type

```java
public void type(By locator, String value) {
    WebElement element = waits.waitForVisible(locator);
    element.clear();
    element.sendKeys(value);
}
```

This combines:

1. wait for the element to be visible.
2. clear existing text.
3. type the new value.

This is the first framework method where beginner Selenium commands become a
reusable action.

This is used by the private `enterCredentials(...)` method in
[LoginPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java).
That is a useful design detail: the public page method describes the user
intent, while the private helper performs the repeated username/password entry
through the wrapper.

## Text And Display State

```java
public String getText(By locator)
public boolean isDisplayed(By locator)
```

These methods wait before reading page state. That helps avoid tests reading
from an element before the page has reached the expected state.

Current examples:

- [ProductsPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java)
  uses `getText(...)` for the products page title and cart badge.
- [LoginPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java)
  uses `getText(...)` for the locked-out-user error message.
- [CheckoutPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/CheckoutPage.java)
  uses `isDisplayed(...)` for the first checkout input.

The wrapper returns plain Java values such as `String`, `boolean`, and `int`
instead of exposing `WebElement` to tests. That keeps tests focused on
assertions, not Selenium object handling.

## Finding Lists And Counting Elements

```java
public int getElementCount(By locator) {
    return driver.findElements(locator).size();
}

public List<WebElement> findAll(By locator) {
    return driver.findElements(locator);
}
```

These methods intentionally use `findElements(...)`, not `findElement(...)`.
`findElements(...)` returns an empty list when nothing matches, which is useful
for counts and collection processing. `findElement(...)` throws immediately
when nothing matches.

[ProductsPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java)
uses `getElementCount(...)` to count inventory items and `findAll(...)` to
retrieve product cards before filtering by product name.

Important nuance: these two methods do not wait in Module 10. The page object
is responsible for calling `waitUntilLoaded()` before counting products. That
keeps the wrapper small for the first abstraction lesson. A later framework
could add list waits or separate methods such as `waitForAllVisible(...)`.

## Dropdown Support

```java
public void selectByVisibleText(By locator, String visibleText)
```

Module 06 introduced Selenium's `Select`. Module 10 gives the framework a
first wrapper for standard HTML `<select>` dropdowns. It is not used by
SauceDemo yet, but it belongs in the first wrapper layer because dropdowns were
already taught in the raw Selenium phase.

The method waits for the `<select>` element to be visible, creates Selenium's
`Select` helper, and chooses the option by visible text. It will only work for
real HTML `<select>` elements. It is not for modern custom dropdown widgets
made from `<div>`, `<button>`, and `<ul>` elements.

## Scoped Clicks

```java
public void clickInside(WebElement parent, By childLocator)
```

This keeps the Module 07 table/card lesson alive. Sometimes the safe pattern
is:

1. find the matching row or card.
2. search inside that row or card.
3. click the child action.

`ProductsPage.addProductToCart(...)` uses this to click the button inside the
matched product card.

This method accepts a `WebElement` parent because the page object already found
the correct product card in
[ProductsPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java).
Passing the parent avoids accidentally clicking the first matching
`button.btn_inventory` on the whole page.

Current limitation: `clickInside(...)` does not wait for the child element to
be clickable. That is acceptable for this checkpoint because the parent card
has already been found on the loaded products page and SauceDemo's add button
is immediately available. In a larger framework, this is a candidate for a
scoped wait method.

## What ElementActions Does Not Do Yet

Module 10 deliberately avoids:

- JavaScript click fallback.
- screenshot capture.
- automatic retries for every failure.
- logging framework integration.
- custom exception wrapping.

Those features are useful only after the learner understands the basic wrapper
shape. Overloading the first wrapper class would make it feel like magic.

It also does not know SauceDemo-specific rules. For example, finding a product
card by product name belongs in
[ProductsPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java),
not in `ElementActions`, because that rule only makes sense on the SauceDemo
inventory page.

## Java Syntax To Notice

```java
private final WaitUtils waits;
```

`ElementActions` depends on `WaitUtils`. This is composition: one framework
service uses another service to do its work.

```java
private final WebDriver driver;
```

`driver` is still needed for collection methods such as `findAll(...)` and
`getElementCount(...)`. Single-element actions can usually go through
`WaitUtils`, but collection reads still need direct driver access in this
module.

```java
Select select = new Select(waits.waitForVisible(locator));
```

This wraps Selenium's dropdown helper behind the framework action API.

## Common Mistakes

- Turning wrappers into vague aliases, such as `doClick(...)`, without defining
  the wait behavior.
- Catching every exception and returning `false`, which hides real UI failures.
- Moving page-specific search rules into the generic action service.
- Passing around saved `WebElement` instances too early and then hitting stale
  element errors after navigation or DOM refresh.
- Adding JavaScript click fallback before learning why the normal Selenium
  click failed.

## Interview Readiness

A strong answer for this module should say:

`ElementActions` centralizes repeated Selenium mechanics so page objects can
stay focused on page behavior. It still uses Selenium APIs underneath. For
example, `click(By)` waits for `elementToBeClickable` through `WaitUtils` and
then calls `WebElement.click()`. The wrapper should add consistency and future
diagnostic hooks, not hide failures or remove the need to understand Selenium.

## Revision Checklist

- Can you map every public method in
  [ElementActions.java](../../src/main/java/com/learning/framework/actions/ElementActions.java)
  to the Selenium command underneath it?
- Can you explain why `click(By)` waits but `findAll(By)` currently does not?
- Can you explain why `ProductsPage.findProductCard(...)` remains in the page
  object?
- Can you explain why JavaScript click fallback is intentionally deferred?
