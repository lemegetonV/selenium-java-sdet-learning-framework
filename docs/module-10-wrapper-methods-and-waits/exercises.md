# Module 10 Exercises

Use these exercises after reading:

- [ElementActions.java](../../src/main/java/com/learning/framework/actions/ElementActions.java)
- [WaitUtils.java](../../src/main/java/com/learning/framework/waits/WaitUtils.java)
- [BaseTest.java](../../src/test/java/com/learning/tests/base/BaseTest.java)
- [LoginPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java)
- [ProductsPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java)
- [CartPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/CartPage.java)
- [CheckoutPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/CheckoutPage.java)

## Exercise 1 - Add Placeholder Reading

Add a method to `ElementActions` that reads an attribute:

```java
getAttribute(By locator, String attributeName)
```

Hint:

- wait for the element to be visible.
- call `getAttribute(attributeName)`.

Expected outcome:

- `LoginPage` could read placeholder text without using `driver.findElement`
  directly.
- the method belongs in [ElementActions.java](../../src/main/java/com/learning/framework/actions/ElementActions.java)
  because attribute reading is a generic element action.

Revision question:

- should this method wait for visibility, presence, or clickability, and why?

## Exercise 2 - Add A URL Wait To A Page Object

Use `WaitUtils.waitForUrlContains(...)` in a page transition.

Hint:

- successful login reaches `/inventory.html`.
- keep the title wait too, because URL alone does not prove the page is ready.

Expected outcome:

- the page transition waits for both URL and visible page state.
- the change would likely live in [LoginPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java)
  after clicking the login button and before returning `ProductsPage`.

Revision question:

- why is URL alone weaker than waiting for the products title or inventory list?

## Exercise 3 - Explain Why Not To Catch Everything

Write a short answer explaining why `ElementActions.click(...)` should not
catch all Selenium exceptions and silently continue.

Expected outcome:

- the answer mentions false positives, hidden bugs, and loss of diagnostic
  detail.
- the answer explains that later modules can add screenshots and logs without
  swallowing the Selenium exception.

## Exercise 4 - Identify What Belongs In Module 13

List three things deliberately deferred from Module 10.

Hint:

- think about screenshots, logs, reports, and retries.

Expected outcome:

- examples include screenshot on failure, Log4j2 integration, Extent/Allure
  report steps, retry analyzer, custom framework exceptions, and rich failure
  attachments.

## Exercise 5 - Trace One Test End To End

Trace `standardUserCanStartCheckoutForSingleProduct()` in
[SauceDemoPageObjectTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoPageObjectTest.java).

Hint:

- list each page object method called.
- list each wrapper method used inside those page object methods.
- note where assertions happen.

Expected outcome:

- you can explain the path from test -> page object -> `ElementActions` ->
  `WaitUtils` -> Selenium.
- you can identify that assertions stay in the test class.

## Exercise 6 - Decide Where Logic Belongs

For each item, decide whether it belongs in a Page Object, `ElementActions`, or
`WaitUtils`:

- find a SauceDemo product card by product name.
- wait until a locator is clickable.
- type into an input after clearing it.
- return a `CheckoutPage` after clicking checkout.
- select a visible option from a standard HTML `<select>`.

Expected outcome:

- product-card rules and page transitions belong in page objects.
- clickable waits belong in [WaitUtils.java](../../src/main/java/com/learning/framework/waits/WaitUtils.java).
- generic type/select actions belong in [ElementActions.java](../../src/main/java/com/learning/framework/actions/ElementActions.java).

## Exercise 7 - Explain A Current Limitation

Write a short note about one limitation of the Module 10 wrapper layer.

Examples:

- `clickInside(...)` does not wait for the child element to be clickable.
- `findAll(...)` does not wait for a minimum count.
- no screenshots, logs, or report steps are attached to wrapper actions yet.
- browser options and timeout values still live in [BaseTest.java](../../src/test/java/com/learning/tests/base/BaseTest.java).

Expected outcome:

- the note explains why the limitation is acceptable in Module 10 and which
  later module is likely to improve it.
