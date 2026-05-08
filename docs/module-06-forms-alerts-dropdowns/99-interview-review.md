# Module 06 Interview Review

## What You Must Be Able To Explain

Module 06 covers common web controls. You should be able to explain:

- textbox and textarea typing.
- why input values are read with `getAttribute("value")`.
- button click behavior and result assertions.
- checkbox vs radio selected state.
- hyperlink and image attribute validation.
- real `<select>` dropdowns and Selenium `Select`.
- JavaScript alert, confirm, and prompt handling.
- login form flow and waiting for navigation.

## Strong Answers

**How do you handle a textbox in Selenium?**

Find the input element, clear it when needed, type with `sendKeys`, and assert
the field value with `getAttribute("value")`.

**How do you handle checkboxes and radio buttons?**

Click the control and verify selected state with `isSelected()`. Checkboxes are
independent; radio buttons are grouped by shared `name`.

**When do you use Selenium `Select`?**

Only when the element is a real HTML `<select>`. Custom dropdowns must be
handled with normal locators, clicks, and waits.

**How do you handle JavaScript alerts?**

Switch to the alert using `driver.switchTo().alert()`, read text if needed, and
then `accept`, `dismiss`, or `sendKeys` depending on dialog type.

## Code Lines To Revise

```java
Assert.assertEquals(displayName.getAttribute("value"), "Selenium learner");
```

Input text is stored as value, not visible child text.

```java
Assert.assertTrue(firstCheckbox.isSelected());
```

The test asserts state after click instead of assuming interaction succeeded.

```java
Select dropdown = new Select(dropdownElement);
```

This wraps a real `<select>` element. It is not for custom dropdowns.

```java
Alert alert = driver.switchTo().alert();
```

Selenium changes context from page DOM to browser dialog.

## Common Interview Traps

- Using `getText()` for input values.
- Using `Select` on custom dropdowns.
- Clicking a checkbox without asserting `isSelected()`.
- Treating alerts like HTML modals.
- Submitting login and asserting immediately without waiting for navigation.

## Connection To Future Framework Modules

Module 09 will move control interactions into page objects. Module 10 will wrap
common actions such as typing, clicking, selecting, and waiting. Module 13 will
add screenshots and logging, where credential masking becomes more important.
Module 06 keeps the raw Selenium behavior visible before those abstractions
arrive.
