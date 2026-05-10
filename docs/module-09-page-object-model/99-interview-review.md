# Module 09 Interview Review

## What You Must Be Able To Explain

Module 09 introduces Page Object Model. You should be able to explain:

- what a Page Object is.
- why locators are private.
- why page methods should describe user actions.
- why test methods keep assertions.
- why page actions return the next page object after navigation.
- why this project uses `By` locators instead of PageFactory.
- why page objects receive `WebDriver` instead of creating it.
- what duplication still remains for Module 10.

## Strong Answers

**Why do we use Page Object Model?**

To separate page structure from test intent. Tests should describe workflows
and assertions. Page objects should know locators, page actions, and page-level
state.

**Why are locators private in page classes?**

Because locators are implementation details. If the page HTML changes, the page
object should absorb that change without forcing every test class to update.

**Should assertions be inside page objects?**

Usually no. Page objects can return page state, such as title text or item
count. Test classes should decide what is expected for the scenario.

**Why does `loginAs` return `ProductsPage`?**

A successful SauceDemo login navigates to the products page. Returning
`ProductsPage` makes that browser transition visible in Java.

**Why not PageFactory?**

This framework uses `By` locators because later wrapper methods can resolve
elements at action time, apply waits consistently, support dynamic locators,
and reduce stale element risk.

## Code Lines To Revise

```java
private static final By USERNAME_INPUT = By.id("user-name");
```

Private locator owned by the page object.

```java
public ProductsPage loginAs(String username, String password)
```

Page action that returns the next page after successful navigation.

```java
return new ProductsPage(driver, wait).waitUntilLoaded();
```

Creates the next page object and verifies it is ready.

```java
productCard.findElement(By.cssSelector("button.btn_inventory")).click();
```

Scoped lookup: find the button inside the matched product card.

```java
ProductsPage productsPage = new LoginPage(driver, wait)
        .open()
        .loginAs(standardUser, password);
```

Test flow reads as user workflow instead of raw Selenium mechanics.

```java
CheckoutPage checkoutPage = cartPage.checkout();
```

Navigation action returns the next page object.

## Common Interview Traps

- Saying Page Object Model means putting all Selenium code in one utility.
- Making page locators public.
- Creating a browser inside a page object.
- Putting every assertion inside page objects.
- Returning `void` from navigation actions and forcing tests to guess the next
  page.
- Claiming PageFactory is always bad instead of explaining the tradeoff.

## Framework Phase Bridge

Module 10 can now introduce wrapper methods because the remaining duplication
is visible inside page objects:

```java
driver.findElement(...)
wait.until(...)
click()
sendKeys()
getText()
```

The next goal is not to change test readability again. The next goal is to
make page-object internals more consistent and diagnostic.
