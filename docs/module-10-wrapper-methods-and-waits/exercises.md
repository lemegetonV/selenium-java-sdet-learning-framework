# Module 10 Exercises

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

## Exercise 2 - Add A URL Wait To A Page Object

Use `WaitUtils.waitForUrlContains(...)` in a page transition.

Hint:

- successful login reaches `/inventory.html`.
- keep the title wait too, because URL alone does not prove the page is ready.

Expected outcome:

- the page transition waits for both URL and visible page state.

## Exercise 3 - Explain Why Not To Catch Everything

Write a short answer explaining why `ElementActions.click(...)` should not
catch all Selenium exceptions and silently continue.

Expected outcome:

- the answer mentions false positives, hidden bugs, and loss of diagnostic
  detail.

## Exercise 4 - Identify What Belongs In Module 13

List three things deliberately deferred from Module 10.

Hint:

- think about screenshots, logs, reports, and retries.

Expected outcome:

- examples include screenshot on failure, Log4j2 integration, Extent/Allure
  report steps, retry analyzer, custom framework exceptions, and rich failure
  attachments.
