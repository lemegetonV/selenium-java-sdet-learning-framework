# PageFactory vs By Locators

## Files In This Topic

This topic reads these files:

- [src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java)
- [src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java)
- [src/main/java/com/learning/framework/pages/saucedemo/CartPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/CartPage.java)
- [src/main/java/com/learning/framework/pages/saucedemo/CheckoutPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/CheckoutPage.java)


## What PageFactory Is

You may see Selenium examples that use PageFactory:

```java
@FindBy(id = "user-name")
private WebElement usernameInput;
```

PageFactory initializes annotated `WebElement` fields and can make simple page
classes look compact.

It is common in older Selenium training material and interview discussions, so
you should recognize it.

## Why This Framework Does Not Use PageFactory

This project uses `By` locators:

```java
private static final By USERNAME_INPUT = By.id("user-name");
```

Reasons:

- `By` locators are explicit and easy to pass into wrapper methods later.
- elements are found at action time, which reduces stale element risk.
- dynamic locators are easier to build from method parameters.
- wait utilities work naturally with `By`.
- the final framework direction is wrapper-based, not field-initialization
  based.

## Interview Nuance

PageFactory is not automatically wrong. It can be acceptable in simple
frameworks. The important interview answer is to explain the tradeoff.

Strong answer:

> I know PageFactory and `@FindBy`, but for this framework I prefer `By`
> locators because wrapper actions and explicit waits can resolve elements at
> the moment of interaction. That keeps the framework more flexible and reduces
> stale element issues.

## Dynamic Locator Direction

`ProductsPage` accepts a product name:

```java
addProductToCart("Sauce Labs Backpack")
```

In Module 09, it finds the product by filtering product cards. Later modules
may introduce dynamic `By` factories for patterns such as:

```java
private By productCardByName(String productName) {
    return By.xpath("...");
}
```

That direction works naturally with `By` locators and wrapper methods.

## What Is Deferred

Module 09 does not yet add:

- `ElementActions.click(By locator)`.
- dynamic locator helper factories.
- custom wait utilities.
- stale retry logic.
- JavaScript click fallback.

Those belong in Module 10 or later because learners should first understand
what Page Objects changed.
