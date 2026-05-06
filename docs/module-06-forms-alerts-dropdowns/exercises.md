# Module 06 Exercises

These exercises extend raw control interaction practice. Do not add page
objects, wrapper methods, data providers, screenshots, or logging yet.

## Exercise 1 - Clear And Replace Text

In `_11_TextboxTextareaButtonTest`, type one value into the display name,
clear it, and type a second value.

Expected outcome:
- assert the final `value` is the second value.
- explain why `getAttribute("value")` is used instead of `getText()`.

## Exercise 2 - Add A Textarea Newline

In `_11_TextboxTextareaButtonTest`, type two lines into the textarea.

Hint:

```java
notes.sendKeys("Line one\nLine two");
```

Expected outcome:
- assert the textarea value contains both lines.

## Exercise 3 - Radio Group Explanation

In `_12_RadioImageHyperlinkTest`, select email, then phone, then email again.

Expected outcome:
- assert only one radio button is selected at a time.
- explain why the shared `name` attribute matters.

## Exercise 4 - Dropdown Option Count

In `_13_CheckboxDropdownTest`, call:

```java
dropdown.getOptions()
```

Expected outcome:
- assert the dropdown has three options including the disabled placeholder.
- print nothing from the framework code; use assertions only.

## Exercise 5 - Prompt Dismissal

In `_14_AlertsAndAuthenticationTest`, add a prompt test that types text but
dismisses the prompt.

Expected outcome:
- assert the result says `You entered: null`.

## Exercise 6 - Invalid Login

Add a second form authentication test using an invalid password.

Expected outcome:
- assert the URL remains on `/login`.
- assert the flash message contains `Your password is invalid!`.
- use an explicit wait if the message is not immediately ready.
