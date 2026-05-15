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

## Text And Display State

```java
public String getText(By locator)
public boolean isDisplayed(By locator)
```

These methods wait before reading page state. That helps avoid tests reading
from an element before the page has reached the expected state.

## Dropdown Support

```java
public void selectByVisibleText(By locator, String visibleText)
```

Module 06 introduced Selenium's `Select`. Module 10 gives the framework a
first wrapper for standard HTML `<select>` dropdowns. It is not used by
SauceDemo yet, but it belongs in the first wrapper layer because dropdowns were
already taught in the raw Selenium phase.

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

## What ElementActions Does Not Do Yet

Module 10 deliberately avoids:

- JavaScript click fallback.
- screenshot capture.
- automatic retries for every failure.
- logging framework integration.
- custom exception wrapping.

Those features are useful only after the learner understands the basic wrapper
shape. Overloading the first wrapper class would make it feel like magic.

## Java Syntax To Notice

```java
private final WaitUtils waits;
```

`ElementActions` depends on `WaitUtils`. This is composition: one framework
service uses another service to do its work.

```java
Select select = new Select(waits.waitForVisible(locator));
```

This wraps Selenium's dropdown helper behind the framework action API.
