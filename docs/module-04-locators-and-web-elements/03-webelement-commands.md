# WebElement Commands

## What A `WebElement` Represents

A `WebElement` is Selenium's object for an element found on the page.

The basic flow is:

```java
WebElement usernameInput = driver.findElement(By.id("user-name"));
usernameInput.sendKeys("locked_out_user");
```

Module 04 demonstrates this in:

```text
src/test/java/com/learning/tests/learning/WebElementCommandTest.java
```

## `sendKeys`

`sendKeys` types text into an editable element.

```java
usernameInput.sendKeys("locked_out_user");
```

Nuance:

- it appends text to the current value.
- use `clear()` first when the field may already contain text.
- later wrapper methods will centralize safer typing behavior.

## `clear`

`clear` removes the current value from an editable field.

```java
usernameInput.clear();
```

Nuance:

- it is meant for input-like elements.
- it does not submit a form.
- if the app uses complex JavaScript-controlled inputs, later modules may need
  more robust handling.

## `click`

`click` performs the element's default click action.

```java
loginButton.click();
```

Nuance:

- the element must be interactable.
- overlays, disabled state, timing, or scrolling can cause click failures.
- Module 05 introduces waits before the framework wraps clicking.

## `getText`

`getText` reads visible text from an element.

```java
errorMessage.getText();
```

Nuance:

- it reads visible rendered text, not every hidden DOM text node.
- exact text assertions can be brittle if copy changes.
- use meaningful contains/assertion messages when full text is not the focus.

## `getAttribute`

`getAttribute` reads an attribute value from the DOM.

```java
usernameInput.getAttribute("value");
```

Nuance:

- for input values, `getText()` usually returns an empty string.
- use `getAttribute("value")` to read what is typed into an input.
- later modules will compare attributes, properties, and CSS values more
  carefully when needed.

## Common Beginner Mistakes

- using `getText()` to read an input field value.
- forgetting that `sendKeys()` appends.
- clicking before the element is ready.
- writing one long test that mixes locator strategy, form behavior, and waits.

Module 04 keeps each example narrow so the Selenium command behavior is easy
to see.
