# Module 04 Exercises

These exercises extend raw locator and WebElement practice. Do not add waits,
page objects, wrapper methods, or `BaseTest` yet.

## Exercise 1 - Add A CSS Locator Assertion

In `_04_LocatorStrategyTest`, add a CSS locator for the password field.

Hint:

```java
By.cssSelector("input[data-test='password']")
```

Expected outcome:
- assert its placeholder is `Password`.

## Exercise 2 - Count Links On The Internet Home Page

Create a test that uses:

```java
driver.findElements(By.tagName("a"))
```

Expected outcome:
- assert the page has more than 20 links.

## Exercise 3 - Try A Bad Locator Safely

Use `findElements` with a locator that matches nothing.

Expected outcome:
- assert the returned list is empty.
- do not use `findElement` for this exercise.

## Exercise 4 - Add Another WebElement Command Check

Extend `_06_WebElementCommandTest` by typing into the password input and reading:

```java
passwordInput.getAttribute("value")
```

Expected outcome:
- assert it equals `secret_sauce`.

## Exercise 5 - Explain Locator Choice

Pick one locator from Module 04 and answer:

1. What locator strategy does it use?
2. Why is it stable or unstable?
3. What alternative locator could work?
4. Which one would you keep and why?

## Exercise 6 - Practice A Scoped Locator

In `_04_LocatorStrategyTest`, first locate the SauceDemo login container and
then find the login button from inside that container.

Hint:

```java
WebElement loginContainer = driver.findElement(By.id("login_button_container"));
loginContainer.findElement(By.cssSelector("input[data-test='login-button']"));
```

Expected outcome:
- assert the button value is `Login`.
- explain why the child lookup is scoped.

## Exercise 7 - Practice An XPath Axis

On The Internet tables page, write an XPath that starts from a known last name
cell and moves to another cell in the same row.

Expected outcome:
- use an axis such as `following-sibling::` or `ancestor::`.
- assert the related cell text.
- do not introduce table helper methods yet.
