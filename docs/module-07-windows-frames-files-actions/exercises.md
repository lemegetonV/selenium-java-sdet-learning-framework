# Module 07 Exercises

These exercises extend advanced raw Selenium mechanics. Do not add `BaseTest`,
page objects, wrapper methods, screenshots, logging, or reusable utility
classes yet.

## Exercise 1 - Return From A Frame

In `_15_WindowsAndFramesTest`, add an assertion that fails unless Selenium has
returned to the top page after leaving the nested frame.

Expected outcome:
- use `driver.switchTo().defaultContent()`.
- assert the top-page heading.

## Exercise 2 - Validate Download Content

In `_16_FileUploadDownloadTest`, add an assertion that the downloaded file name
is exactly:

```text
module07-download.txt
```

Expected outcome:
- validate both file existence and file content.
- keep the temporary directory cleanup.

## Exercise 3 - Keyboard Shortcut

In `_17_MouseKeyboardShadowDomTest`, type text into the keyboard input, select
all text, and replace it.

Hint:

```java
new Actions(driver)
        .keyDown(Keys.COMMAND)
        .sendKeys("a")
        .keyUp(Keys.COMMAND)
        .sendKeys("replacement")
        .perform();
```

Expected outcome:
- explain that keyboard shortcuts can be OS-specific.
- use `Keys.CONTROL` instead of `Keys.COMMAND` if your environment requires it.

## Exercise 4 - Add A Table Email Assertion

In `_18_CalendarAndWebTableTest`, find the row where the last name is `Smith`
and assert the email is `jsmith@example.com`.

Expected outcome:
- search within the row, not the whole page.
- explain why row-scoped lookup is safer.

## Exercise 5 - Explain Broken Image Detection

Review `_19_JavaScriptAndExceptionsTest`.

Answer:

1. Why can Selenium locate a broken image element?
2. Why does the test need browser-side image properties?
3. What does `naturalWidth === 0` tell us?

## Exercise 6 - Exception Classification

For each exception below, write the likely cause:

- `NoSuchFrameException`.
- `NoSuchWindowException`.
- `ElementNotInteractableException`.
- `StaleElementReferenceException`.

Expected outcome:
- do not say "Selenium failed" generically.
- connect each exception to a specific browser state problem.
