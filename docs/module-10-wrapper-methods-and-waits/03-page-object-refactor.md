# Page Object Refactor

## Files In This Topic

This topic reads these files:

- [src/test/java/com/learning/tests/base/BaseTest.java](../../src/test/java/com/learning/tests/base/BaseTest.java)
- [src/test/java/com/learning/tests/saucedemo/SauceDemoPageObjectTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoPageObjectTest.java)
- [src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java)
- [src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java)
- [src/main/java/com/learning/framework/pages/saucedemo/CartPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/CartPage.java)
- [src/main/java/com/learning/framework/pages/saucedemo/CheckoutPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/CheckoutPage.java)


## What Changed From Module 09

Module 09 page objects received `WebDriver` and `WebDriverWait`.

Module 10 page objects receive wrapper services:

```java
new LoginPage(driver, elementActions, waits)
```

`driver` remains in `LoginPage` only because opening a URL is browser-level
navigation. Normal element work goes through `ElementActions` and `WaitUtils`.

## Mental Model

Module 09 answered: "Where should locators and page behavior live?"

Module 10 answers: "Where should repeated Selenium mechanics live?"

The page objects are still the public API for the application under test. The
new wrapper layer changes the implementation behind that API:

```text
Test says: login as a standard user
LoginPage says: type username, type password, click login
ElementActions says: wait, clear, sendKeys, click
WaitUtils says: use Selenium ExpectedConditions
```

This keeps the learner from confusing Page Object Model with a complete
framework. POM organizes pages; wrappers organize repeated browser mechanics.

## Constructor Changes

In [SauceDemoPageObjectTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoPageObjectTest.java),
the first page object is now created like this:

```java
new LoginPage(driver, elementActions, waits)
```

Those objects come from [BaseTest.java](../../src/test/java/com/learning/tests/base/BaseTest.java).
This is the module's first clear dependency-passing pattern:

- `driver` is passed where browser navigation is still needed.
- `elementActions` is passed where element interactions are needed.
- `waits` is passed where a page-specific readiness check is needed.

Only [LoginPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java)
receives `WebDriver` in Module 10 because it owns `open()` and calls
`driver.get(LOGIN_URL)`. [ProductsPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java),
[CartPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/CartPage.java),
and [CheckoutPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/CheckoutPage.java)
only need wrapper services.

## LoginPage Refactor

Before:

```java
driver.findElement(USERNAME_INPUT).sendKeys(username);
driver.findElement(LOGIN_BUTTON).click();
```

After:

```java
actions.type(USERNAME_INPUT, username);
actions.click(LOGIN_BUTTON);
```

The page still owns the login locators. The wrapper owns the repeated Selenium
mechanics.

The full login flow in [LoginPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java)
now separates three responsibilities:

- `open()` performs browser navigation and waits for the login button.
- `loginAs(...)` describes a successful login and returns `ProductsPage`.
- `loginExpectingError(...)` describes a failed login and returns `LoginPage`.

The private `enterCredentials(...)` helper is intentionally not moved into
`ElementActions`. It is not a generic Selenium operation; it is a login-page
operation made of two generic `type(...)` calls.

## ProductsPage Refactor

`ProductsPage` still finds a product card by name:

```java
List<WebElement> products = actions.findAll(INVENTORY_ITEMS);
```

This keeps the page-specific product search in the page object. It does not
belong in `ElementActions` because `ElementActions` should not know SauceDemo
product-card rules.

[ProductsPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java)
also shows the difference between generic collection mechanics and
application-specific logic:

- `actions.findAll(INVENTORY_ITEMS)` is generic Selenium collection access.
- `filter(product -> product.getText().contains(productName))` is SauceDemo
  product-card logic.
- `actions.clickInside(productCard, PRODUCT_ADD_BUTTON)` is a reusable scoped
  child lookup once the page object has selected the right parent.

This is a good interview example because it prevents a common over-abstraction:
do not put product-name search rules inside a generic framework utility.

## CartPage Refactor

`CartPage.checkout()` now reads:

```java
actions.click(CHECKOUT_BUTTON);
return new CheckoutPage(actions, waits).waitForInformationStep();
```

This line shows two framework ideas at once:

- use wrapper actions for the click.
- return the next page object after navigation.

In [CartPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/CartPage.java),
`containsProduct(...)` and `getCartItemCount()` still ask cart-level questions.
They call a private `findCartItems()` helper, which uses `actions.findAll(...)`.
That keeps raw Selenium access behind the wrapper while preserving readable
cart behavior.

## CheckoutPage Refactor

`CheckoutPage` now uses:

```java
waits.waitForText(PAGE_TITLE, "Checkout: Your Information");
actions.isDisplayed(FIRST_NAME_INPUT);
```

The page object still describes checkout state. The wrapper services perform
the wait and element read.

[CheckoutPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/CheckoutPage.java)
waits for two signals in `waitForInformationStep()`:

- exact title text: `"Checkout: Your Information"`.
- first name input is clickable.

This is stronger than only checking the URL because the test needs the form to
be interactable. Module 10 stops at the first checkout step; it does not model
the full checkout overview and completion flow yet.

## What Did Not Change

Tests still own assertions:

```java
Assert.assertEquals(productsPage.getTitle(), "Products");
```

Page objects still own locators:

```java
private static final By PAGE_TITLE = By.cssSelector("[data-test='title']");
```

`BaseTest` still owns browser lifecycle.

The TestNG suite in [testng.xml](../../testng.xml) still runs the framework
tests by group. Module 10 changes the internals of the page objects, but the
suite-level execution model remains the same.

## Before And After Dependency Shape

Module 09:

```text
SauceDemoPageObjectTest -> LoginPage(driver, wait)
LoginPage -> driver.findElement + wait.until
```

Module 10:

```text
SauceDemoPageObjectTest -> LoginPage(driver, elementActions, waits)
LoginPage -> ElementActions + WaitUtils
ElementActions -> WaitUtils + Selenium WebDriver
WaitUtils -> WebDriverWait + ExpectedConditions
```

This is why Module 10 is a framework-design module, not just a code cleanup
module. It introduces a reusable action/wait boundary that later logging,
screenshots, reports, and retries can attach to.

## Common Mistakes

- Moving assertions into page objects while refactoring Selenium calls.
- Passing `WebDriver` everywhere even when a page only needs wrapper services.
- Moving SauceDemo-specific product lookup rules into `ElementActions`.
- Thinking wrappers make Selenium knowledge unnecessary.
- Adding every possible wrapper method before the current tests need them.
- Using URL waits alone when page readiness depends on visible or clickable
  elements.

## Interview Nuance

Wrapper methods do not remove the need to understand Selenium. They make the
framework consistent. A strong explanation should still say which Selenium
command is underneath each wrapper.

## Revision Checklist

- Can you explain why `LoginPage.open()` still uses `driver.get(...)`?
- Can you explain why successful login returns `ProductsPage` but failed login
  returns `LoginPage`?
- Can you trace `standardUserCanStartCheckoutForSingleProduct()` from
  [SauceDemoPageObjectTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoPageObjectTest.java)
  through all page objects and wrapper calls?
- Can you explain why tests still own assertions after the refactor?
