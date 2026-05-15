# Module 09 Exercises

## Exercise 1 - Add A Logout Page Action

Add a logout workflow to `ProductsPage`.

Hint:

- inspect SauceDemo's menu button and logout link.
- keep locators private in `ProductsPage`.
- decide what page object should be returned after logout.

Expected outcome:

- a test can call a readable method such as `productsPage.logout()`.
- the returned page object matches the browser state after logout.

## Exercise 2 - Add Product Price Reading

Add a method that returns the price for a product by name.

Hint:

- reuse the product-card lookup idea.
- search inside the matching product card.
- return the visible price text.

Expected outcome:

- the test can assert that `"Sauce Labs Backpack"` has a price like `"$29.99"`.
- the test does not know the CSS selector for product prices.

## Exercise 3 - Expand Checkout Later

Write down when you would expand or split `CheckoutPage`.

Hint:

- think about checkout information, overview, and complete screens.
- think about how many methods belong to one class before it becomes hard to
  scan.

Expected outcome:

- a good answer mentions that Module 09 verifies the transition into checkout
  information only, and a larger checkout workflow could become
  `CheckoutInformationPage`, `CheckoutOverviewPage`, and
  `CheckoutCompletePage` when the framework is ready.

## Exercise 4 - Identify Remaining Duplication

Find three repeated Selenium mechanics inside the page objects.

Hint:

- look for `driver.findElement`.
- look for `wait.until`.
- look for `getText`, `click`, and `sendKeys`.

Expected outcome:

- the learner can explain why Module 10 introduces wrapper methods and waits.

## Exercise 5 - Trace The Checkout Flow

Trace the `standardUserCanStartCheckoutForSingleProduct` test from the test
method into every page object it uses.

Your answer should mention:

- [LoginPage.open()](../../src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java)
- [LoginPage.loginAs(...)](../../src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java)
- [ProductsPage.addProductToCart(...)](../../src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java)
- [ProductsPage.openCart()](../../src/main/java/com/learning/framework/pages/saucedemo/ProductsPage.java)
- [CartPage.checkout()](../../src/main/java/com/learning/framework/pages/saucedemo/CartPage.java)
- [CheckoutPage.waitForInformationStep()](../../src/main/java/com/learning/framework/pages/saucedemo/CheckoutPage.java)

Expected outcome:

- you can explain which page object is active after each browser transition.
- you can explain why each navigation method returns a specific page class.

## Exercise 6 - Separate Page State From Assertions

Pick one assertion from
[SauceDemoPageObjectTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoPageObjectTest.java)
and identify:

- the page-object method that reads state.
- the test assertion that decides the expected value.

Hint:

Use `getInventoryItemCount()`, `containsProduct(...)`, or `getTitle()`.

Expected outcome:

You can explain why Page Objects expose state but tests own expectations.

## Exercise 7 - Explain The `By` Decision

Write a short interview answer explaining why this project uses `By` locators
instead of PageFactory.

Your answer should mention:

- explicit locators.
- action-time element lookup.
- future wrapper methods.
- dynamic locator flexibility.
- stale element risk.

Expected outcome:

You can discuss PageFactory as a tradeoff instead of saying it is simply wrong.
