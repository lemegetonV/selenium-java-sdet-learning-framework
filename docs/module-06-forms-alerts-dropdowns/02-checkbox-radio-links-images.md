# Checkbox, Radio, Links, and Images

## Files In This Topic

```text
src/test/resources/module06/form-controls.html
src/test/java/com/learning/tests/learning/_12_RadioImageHyperlinkTest.java
src/test/java/com/learning/tests/learning/_13_CheckboxDropdownTest.java
```

## Checkbox Actions

`_13_CheckboxDropdownTest` uses The Internet checkboxes page:

```text
https://the-internet.herokuapp.com/checkboxes
```

Checkboxes are independent selected states. Selenium reads that state with:

```java
checkbox.isSelected()
```

Good checkbox test flow:

1. Locate the checkbox.
2. Assert its starting state if that matters.
3. Click it.
4. Assert the new selected state.

Do not assume the click worked just because no exception was thrown.

## Radio Button Actions

`_12_RadioImageHyperlinkTest` uses the local fixture radio group:

```html
<input id="contact-email" name="contact" type="radio" value="email">
<input id="contact-phone" name="contact" type="radio" value="phone">
<p id="contact-result">No contact selected</p>
```

Radio buttons with the same `name` behave as one group. Selecting one option
clears the other option:

```java
emailRadio.click();
Assert.assertTrue(emailRadio.isSelected());
Assert.assertFalse(phoneRadio.isSelected());
Assert.assertEquals(driver.findElement(By.id("contact-result")).getText(), "Preferred contact: Email");
```

Nuance:

- checkboxes support multiple independent selections.
- radio groups usually allow one selected option per `name`.
- `isSelected()` works for both checkboxes and radio buttons.
- a visible result is useful in learning fixtures because it lets a human
  confirm the same state Selenium is asserting.
- The Internet is not used here because it does not provide a simple radio
  group page for this beginner module.

## Hyperlink Actions

The fixture hyperlink is intentionally simple:

```html
<a id="details-link" href="#details">View details</a>
<p id="details-result">Details section not opened</p>
```

The test reads:

- visible text with `getText()`.
- target with `getAttribute("href")`.
- navigation result with `getCurrentUrl()`.
- fixture feedback with `getText()` from `details-result`.

Clicking a hyperlink follows its `href` just like a user click. In this
fixture, the target section also highlights and updates a visible result so the
manual page behavior is clear.

## Image Actions

The fixture image is intentionally inline and stable:

```html
<img id="sample-logo" alt="Sample inline logo" src="data:image/svg+xml,...">
```

Basic Selenium image checks:

- locate the image element.
- assert it is displayed.
- read `alt` for accessibility/meaning.
- read `src` to confirm the intended image source.

What is intentionally not handled yet:

- HTTP status checks for remote image URLs.
- visual image comparison.
- JavaScript-based natural width checks.

Those are deeper validation topics. Module 07 introduces broken image
behavior separately.
