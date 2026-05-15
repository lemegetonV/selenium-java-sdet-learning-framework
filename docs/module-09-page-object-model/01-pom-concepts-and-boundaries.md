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

## What Stays In Tests

Tests still own assertions:

```java
Assert.assertEquals(productsPage.getTitle(), "Products");
```

That keeps the page object reusable. The page object can tell the test what
the page says; the test decides whether that result is acceptable for the
scenario.

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
