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
