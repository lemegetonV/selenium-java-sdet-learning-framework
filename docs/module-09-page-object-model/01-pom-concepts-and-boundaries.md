# POM Concepts and Boundaries

## Files In This Topic

This topic reads these files:

- [src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java)
- [src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java)
- [src/main/java/com/learning/framework/pages/saucedemo/CartPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/CartPage.java)
- [src/main/java/com/learning/framework/pages/saucedemo/CheckoutPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/CheckoutPage.java)
- [src/test/java/com/learning/tests/saucedemo/SauceDemoPageObjectTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoPageObjectTest.java)


## What A Page Object Is

A Page Object is a Java class that represents a page, screen, or meaningful
part of an application UI.

It usually owns:

- locators for that page.
- user actions available on that page.
- page-level state reads.
- page-level waits.
- navigation from one page to the next.

It should not own:

- TestNG assertions for business expectations.
- browser setup or cleanup.
- test data selection strategy.
- reporting listeners.
- cross-browser driver creation.

In Module 09, a Page Object is not an abstract theory. It is visible in these
new classes:

| Page Object | Page Or Screen Modeled | Main Responsibility |
| --- | --- | --- |
| [LoginPage](../../src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java) | SauceDemo login screen | opening login, submitting credentials, reading login errors |
| [ProductsPage](../../src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java) | product inventory screen | reading catalog state, adding products, opening cart |
| [CartPage](../../src/main/java/com/learning/framework/pages/saucedemo/CartPage.java) | cart screen | reading cart items and starting checkout |
| [CheckoutPage](../../src/main/java/com/learning/framework/pages/saucedemo/CheckoutPage.java) | checkout information screen | confirming the first checkout step is ready |

Each class represents a browser state the user can recognize.

## Encapsulation Of Locators

Module 09 moves SauceDemo locators out of the test class and into page classes
such as
[src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java):

```java
private static final By USERNAME_INPUT = By.id("user-name");
```

The important keyword is `private`. Tests should not reach into a page object
and ask for its locator. They should ask the page object to perform a user
action:

```java
loginPage.loginAs("standard_user", "secret_sauce");
```

This is encapsulation. The page hides implementation details and exposes a
small public API.

Encapsulation has a concrete maintenance benefit. If SauceDemo changes the
username field from:

```java
By.id("user-name")
```

to another stable selector, only [LoginPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java)
should change. The test should still say:

```java
new LoginPage(driver, wait).open().loginAs(standardUser, password);
```

That is the difference between page-level implementation detail and test-level
business intent.

## Public Page Actions

Good page methods describe user intent:

```java
loginAs(...)
addProductToCart(...)
openCart()
checkout()
enterCustomerInformation(...)
finishOrder()
```

Weak page methods expose mechanics:

```java
clickLoginButton()
typeUsername()
findProductDiv()
```

Mechanical methods are sometimes useful inside a page class, but test methods
should usually call workflow-level actions.

Module 09 deliberately uses workflow-level methods:

```java
new LoginPage(driver, wait).open().loginAs(standardUser, password);
productsPage.addProductToCart("Sauce Labs Backpack");
CartPage cartPage = productsPage.openCart();
CheckoutPage checkoutPage = cartPage.checkout();
```

These methods are readable because they use the user's language, not Selenium's
language. The test does not mention `By.id`, CSS selectors, `sendKeys`, or the
cart link selector.

## What Stays In Tests

Tests still own assertions:

```java
Assert.assertEquals(productsPage.getTitle(), "Products");
```

That keeps the page object reusable. The page object can tell the test what
the page says; the test decides whether that result is acceptable for the
scenario.

For example, [ProductsPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java)
can expose:

```java
public int getInventoryItemCount()
```

But [SauceDemoPageObjectTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoPageObjectTest.java)
owns the expectation:

```java
Assert.assertEquals(productsPage.getInventoryItemCount(), 6);
```

This distinction matters. The same page object could be reused by another test
that expects a different product count after filtering, sorting, or using a
different test user.

## Why Page Objects Are In `src/main`

The page objects live under
[src/main/java/com/learning/framework/pages/saucedemo/](../../src/main/java/com/learning/framework/pages/saucedemo/).

They are reusable framework classes, not raw learning tests. This matches the
source-organization rule that real framework code belongs under
`com.learning.framework`.

## Java Syntax To Notice

```java
private final WebDriver driver;
private final WebDriverWait wait;
```

`final` means the page object receives these dependencies once through the
constructor and does not reassign them. The browser session is still owned by
`BaseTest`.

```java
public LoginPage(WebDriver driver, WebDriverWait wait) {
    this.driver = driver;
    this.wait = wait;
}
```

This constructor is dependency passing. The page object does not create a new
browser. It uses the browser the test already owns.

The keyword `this` matters:

```java
this.driver = driver;
this.wait = wait;
```

The left side is the object's field. The right side is the constructor
parameter. After construction, the page object has access to the same browser
session and explicit wait that `BaseTest` created for the current test method.

Do not read this as driver ownership. The page object uses the driver; it does
not own the driver's lifecycle.

## Object Creation Flow

The standard login test starts with:

```java
ProductsPage productsPage = new LoginPage(driver, wait)
        .open()
        .loginAs(standardUser, password);
```

That line does several things:

1. `new LoginPage(driver, wait)` creates a Java object for the login page.
2. `.open()` navigates the browser to SauceDemo and returns the same
   `LoginPage`.
3. `.loginAs(...)` submits credentials.
4. successful login changes the browser page.
5. `loginAs(...)` returns a `ProductsPage`.

The variable type is `ProductsPage` because after successful login, that is the
page the browser should be showing.

This is why Page Objects are more than locator containers. They can model page
transitions in Java return types.

## Framework Boundary

In Module 09:

```text
BaseTest -> lifecycle
Page Object -> page behavior
Test class -> scenario and assertion
```

If a class starts owning all three responsibilities, the framework becomes hard
to maintain.

## Common Beginner Mistakes

- putting assertions for every scenario inside page objects.
- making locators public.
- creating `new ChromeDriver()` inside a page object.
- returning `void` from every page action even when the action navigates.
- adding wrapper utilities before understanding what Page Objects solve.
- turning one page object into a giant class for the whole application.

## Debugging Checklist

If a Page Object test fails, ask:

1. Did `BaseTest` create the browser and wait?
2. Did the test create the correct starting page object?
3. Did the page method wait for the page state it needs?
4. Did a navigation method return the page object that matches the browser?
5. Is the assertion in the test, or did a page object start making scenario
   decisions?

This checklist helps separate lifecycle failures, page modeling failures, and
assertion failures.

## Revision Checklist

- Can you explain why page objects receive `driver` and `wait`?
- Can you identify the private locators in each page class?
- Can you explain why `loginAs` returns `ProductsPage`?
- Can you explain why `loginExpectingError` returns `LoginPage`?
- Can you show one assertion that correctly remains in the test?
