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

## ProductsPage Refactor

`ProductsPage` still finds a product card by name:

```java
List<WebElement> products = actions.findAll(INVENTORY_ITEMS);
```

This keeps the page-specific product search in the page object. It does not
belong in `ElementActions` because `ElementActions` should not know SauceDemo
product-card rules.

## CartPage Refactor

`CartPage.checkout()` now reads:

```java
actions.click(CHECKOUT_BUTTON);
return new CheckoutPage(actions, waits).waitForInformationStep();
```

This line shows two framework ideas at once:

- use wrapper actions for the click.
- return the next page object after navigation.

## CheckoutPage Refactor

`CheckoutPage` now uses:

```java
waits.waitForText(PAGE_TITLE, "Checkout: Your Information");
actions.isDisplayed(FIRST_NAME_INPUT);
```

The page object still describes checkout state. The wrapper services perform
the wait and element read.

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

## Interview Nuance

Wrapper methods do not remove the need to understand Selenium. They make the
framework consistent. A strong explanation should still say which Selenium
command is underneath each wrapper.
