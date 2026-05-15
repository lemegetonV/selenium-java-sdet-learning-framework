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

Conceptually, PageFactory tends to make the page object look like this:

```java
@FindBy(id = "login-button")
private WebElement loginButton;
```

Then the page method clicks the field:

```java
loginButton.click();
```

That can be readable, but the learner must understand that Selenium still has
to locate the element at runtime. The annotation does not remove browser
timing, stale element, or wait concerns.

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

In Module 09, the design stays explicit:

```java
private static final By LOGIN_BUTTON = By.id("login-button");
driver.findElement(LOGIN_BUTTON).click();
```

This is slightly more verbose, but it is easier to teach:

1. the locator is a `By`.
2. Selenium resolves the `By` into a `WebElement`.
3. Selenium performs an action on that element.

Module 10 will wrap step 2 and step 3, but the same `By` locator can be passed
into the wrapper.

## `By` Locators As Framework Inputs

The important future-facing idea is that `By` is data the framework can pass
around.

For example, Module 09 has:

```java
private static final By USERNAME_INPUT = By.id("user-name");
```

Module 10 can later express:

```java
actions.type(USERNAME_INPUT, username);
```

That is clean because `USERNAME_INPUT` is already a locator object. If the page
stored only initialized `WebElement` fields, wrapper methods would have less
control over when the element is found and which wait condition is applied.

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

The current Module 09 implementation uses a product-card search instead of a
dynamic XPath:

```java
findProductCard(productName)
```

That is intentional. It keeps the first POM lesson connected to the Module 07
row/card-scoped lookup pattern. Dynamic locator factory methods can be taught
later when wrapper methods exist and the need is clearer.

## What Is Deferred

Module 09 does not yet add:

- `ElementActions.click(By locator)`.
- dynamic locator helper factories.
- custom wait utilities.
- stale retry logic.
- JavaScript click fallback.

Those belong in Module 10 or later because learners should first understand
what Page Objects changed.

## Common Mistakes

- saying PageFactory is bad without explaining the tradeoff.
- thinking `@FindBy` removes the need for waits.
- storing public `WebElement` fields and letting tests click them directly.
- using PageFactory examples from older tutorials without checking whether the
  framework needs dynamic locators and wrapper actions.
- mixing PageFactory fields and `By` wrapper design in the same small page
  object without a clear reason.

## Revision Checklist

- Can you recognize PageFactory syntax in interview questions?
- Can you explain why this framework chooses `By` locators?
- Can you explain why locating elements at action time helps with stale
  element risk?
- Can you describe how Module 09 `By` locators prepare for Module 10 wrappers?
