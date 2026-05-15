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

## Fluent Chains

The checkout test uses a page transition:

```java
CheckoutPage checkoutPage = cartPage.checkout();
```

This works because `checkout()` returns the page object that should be used
next: `CheckoutPage`.

Use fluent chains when they stay readable. Break the chain into named
variables when assertions or debugging clarity matter.

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

## What Page Objects Wait For

Each page object waits for the page state it needs. For example,
[src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java)
waits for the products title:

```java
wait.until(ExpectedConditions.visibilityOfElementLocated(PAGE_TITLE));
```

The wait is still raw Selenium in Module 09. Module 10 will wrap this into
centralized action and wait helpers.

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
