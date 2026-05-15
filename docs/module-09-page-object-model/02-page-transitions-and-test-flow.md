# Page Transitions and Test Flow

## Files In This Topic

This topic reads these files:

- [src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java)
- [src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java)
- [src/main/java/com/learning/framework/pages/saucedemo/CartPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/CartPage.java)
- [src/main/java/com/learning/framework/pages/saucedemo/CheckoutPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/CheckoutPage.java)
- [src/test/java/com/learning/tests/saucedemo/SauceDemoPageObjectTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoPageObjectTest.java)


## Tests Should Read Like User Workflows

Module 08 tests still mixed workflow with locators. Module 09 changes the
shape:

```java
ProductsPage productsPage = new LoginPage(driver, wait)
        .open()
        .loginAs(standardUser, password);
```

This reads as:

1. open the login page.
2. log in.
3. continue with the products page.

That is the purpose of Page Object Model: the test should be understandable
without mentally parsing selectors first.

Compare that with the Module 08 style:

```java
driver.get(loginUrl);
driver.findElement(USERNAME_INPUT).sendKeys("standard_user");
driver.findElement(PASSWORD_INPUT).sendKeys("secret_sauce");
driver.findElement(LOGIN_BUTTON).click();
```

The Module 08 code is useful for learning Selenium mechanics. The Module 09
code is better for framework tests because the test name and method calls now
describe the user workflow.

## Full Checkout Flow Trace

The longest Module 09 test is
`standardUserCanStartCheckoutForSingleProduct`.

Its page-object flow is:

```java
ProductsPage productsPage = new LoginPage(driver, wait)
        .open()
        .loginAs(standardUser, password)
        .addProductToCart("Sauce Labs Backpack");

CartPage cartPage = productsPage.openCart();
CheckoutPage checkoutPage = cartPage.checkout();
```

Read it as browser state:

```text
LoginPage
  -> open login URL
  -> login as standard user
ProductsPage
  -> add Sauce Labs Backpack
  -> open cart
CartPage
  -> start checkout
CheckoutPage
  -> verify checkout information form
```

This is the central design lesson: the Java object type should track what page
the browser is expected to be on.

## Page-To-Page Returns

Successful login leaves the login page:

```java
public ProductsPage loginAs(String username, String password)
```

The return type is `ProductsPage` because SauceDemo navigates to inventory
after a valid login.

Negative login stays on the login page:

```java
public LoginPage loginExpectingError(String username, String password)
```

The return type is `LoginPage` because the user remains on the login screen.
This makes the test flow match the real browser flow.

This is also a debugging aid. If a negative login method returned
`ProductsPage`, the test would imply a browser transition that did not happen.
The wrong return type would make the code harder to reason about even if the
Selenium commands were technically valid.

Use this rule:

```text
If the browser navigates to another page, return the next page object.
If the browser stays on the same page, return this page object.
```

## Fluent Chains

The checkout test uses a page transition:

```java
CheckoutPage checkoutPage = cartPage.checkout();
```

This works because `checkout()` returns the page object that should be used
next: `CheckoutPage`.

Use fluent chains when they stay readable. Break the chain into named
variables when assertions or debugging clarity matter.

A chain is readable when each method is a high-level user action:

```java
new LoginPage(driver, wait).open().loginAs(...).addProductToCart(...)
```

A chain becomes weak when it exposes mechanics:

```java
page.typeUsername(...).typePassword(...).clickLoginButton().waitForTitle(...)
```

The second version still knows too much about implementation steps. Module 09
prefers user-level actions.

## Row And Card Scoped Lookup

[src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java)
finds a specific product card before clicking its button:

```java
WebElement productCard = findProductCard(productName);
productCard.findElement(By.cssSelector("button.btn_inventory")).click();
```

This is safer than clicking the first inventory button on the page. The button
lookup is scoped inside the matching product card.

This pattern builds on Module 07 table lessons:

- find the row/card by stable text.
- search inside that row/card.
- perform the action inside the matched scope.

The implementation is:

```java
private WebElement findProductCard(String productName) {
    List<WebElement> products = driver.findElements(INVENTORY_ITEMS);
    return products.stream()
            .filter(product -> product.getText().contains(productName))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productName));
}
```

Important details:

- `findElements` returns a list and does not fail when no elements are found.
- `stream()` lets Java inspect each product card.
- `filter(...)` keeps only cards whose visible text contains the product name.
- `findFirst()` picks the first matching card.
- `orElseThrow(...)` gives a clear error if the product is not present.

Then the click is scoped:

```java
productCard.findElement(By.cssSelector("button.btn_inventory")).click();
```

That means Selenium searches inside the matching product card, not the whole
page. This prevents clicking the wrong product's button.

## What Page Objects Wait For

Each page object waits for the page state it needs. For example,
[src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java)
waits for the products title:

```java
wait.until(ExpectedConditions.visibilityOfElementLocated(PAGE_TITLE));
```

The wait is still raw Selenium in Module 09. Module 10 will wrap this into
centralized action and wait helpers.

Different page methods wait for different kinds of readiness:

| Page Method | Wait Condition | Why |
| --- | --- | --- |
| `LoginPage.open()` | login button visible | login page is ready for typing |
| `ProductsPage.waitUntilLoaded()` | title visible and inventory count greater than zero | inventory page has loaded useful content |
| `ProductsPage.addProductToCart(...)` | cart badge visible | cart update is visible after click |
| `CartPage.waitUntilLoaded()` | title text is `Your Cart` | navigation reached cart page |
| `CheckoutPage.waitForInformationStep()` | checkout title text and first-name input clickable | checkout information form is usable |

These waits belong in page objects because each page knows what "ready" means
for itself.

## Java Syntax To Notice

```java
return new ProductsPage(driver, wait).waitUntilLoaded();
```

This creates the next page object and immediately verifies that the next page
is ready for use.

```java
return new CheckoutPage(driver, wait).waitForInformationStep();
```

This documents that the checkout button should move the browser to the
checkout information step.

```java
products.stream()
        .filter(product -> product.getText().contains(productName))
        .findFirst()
        .orElseThrow(...)
```

This is a Java stream search. If the product is missing, the page object throws
a clear framework-level argument error instead of letting a later click fail
with a less useful message.

## Framework Bridge

Module 09 improves readability but still has repeated Selenium mechanics:

```java
driver.findElement(locator).click();
wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
```

Module 10 will introduce `ElementActions` and wait helpers so page objects can
say what they want to do without repeating the low-level find/wait/action
sequence.

## Common Mistakes

- returning `void` after a navigation and forcing the test to manually create
  the next page object.
- using a fluent chain so long that assertions and debugging become hard.
- finding a product button globally instead of inside the matched product card.
- waiting only for URL change when the page content is not ready yet.
- putting `Assert` calls into page-transition methods.

## Revision Checklist

- Can you trace the checkout test page by page?
- Can you explain the difference between `loginAs` and `loginExpectingError`
  return types?
- Can you explain why scoped lookup is safer than a global button selector?
- Can you name what each page object waits for before returning?
