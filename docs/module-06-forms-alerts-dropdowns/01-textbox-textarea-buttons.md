# Textbox, Textarea, and Buttons

## Files In This Topic

```text
src/test/resources/module06/form-controls.html
src/test/java/com/learning/tests/learning/_11_TextboxTextareaButtonTest.java
```

This local fixture is used because The Internet does not provide a compact
textarea/radio/image/button page for this module. The module still uses The
Internet for checkboxes, dropdowns, alerts, and login.

The HTML fixture includes clearly labeled controls:

```html
<input id="display-name" name="displayName" type="text">
<textarea id="notes" name="notes"></textarea>
<button id="save-profile" type="button">Save profile</button>
<div id="save-result">...</div>
```

## Textbox Actions

The test locates the textbox and types into it:

```java
WebElement displayName = driver.findElement(By.id("display-name"));
displayName.clear();
displayName.sendKeys("Selenium learner");
```

Important points:

- `clear()` removes existing editable text.
- `sendKeys(...)` types into the focused control.
- text inputs store typed text in the `value` attribute/property.
- reading `getText()` from an input usually returns an empty string because
  the text is not child text between opening and closing tags.

Use this assertion shape for text inputs:

```java
Assert.assertEquals(displayName.getAttribute("value"), "Selenium learner");
```

## Textarea Actions

A textarea is also a `WebElement`:

```java
WebElement notes = driver.findElement(By.id("notes"));
notes.sendKeys("Practicing text input controls.");
```

Nuance:

- Selenium uses the same `sendKeys(...)` API for textboxes and textareas.
- textarea text is still best verified through `getAttribute("value")`.
- multi-line text can be sent with newline characters when needed.

## Button Actions

The fixture has a button with `type="button"`:

```html
<button id="save-profile" type="button">Save profile</button>
```

The test clicks it:

```java
saveButton.click();
```

Selenium does not know the business meaning of the button. It only asks the
browser to perform the normal click action. The page's JavaScript decides what
happens next.

This fixture updates a visible saved profile summary:

```java
Assert.assertEquals(driver.findElement(By.id("save-status")).getText(), "Saved profile");
Assert.assertEquals(driver.findElement(By.id("saved-name")).getText(), "Module 06");
Assert.assertEquals(driver.findElement(By.id("saved-contact")).getText(), "Email");
```

That gives the learner a direct connection between:

- the text typed into the textbox.
- the text typed into the textarea.
- the selected radio option.
- the visible result produced by clicking the button.

## Common Beginner Mistakes

- using `getText()` to read textbox value.
- typing without clearing when the field already has content.
- assuming every button submits a form; `type="button"` and `type="submit"`
  behave differently.
- clicking and asserting immediately when the page updates asynchronously. If
  the update is delayed, reuse the explicit wait pattern from Module 05.
