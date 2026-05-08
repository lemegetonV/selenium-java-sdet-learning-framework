# Module 07 Interview Review

## What You Must Be Able To Explain

Module 07 completes the raw Selenium phase. You should be able to explain:

- window handles and switching.
- frame and nested-frame context.
- file upload and download validation.
- mouse actions, keyboard actions, and `Keys`.
- Shadow DOM access through open shadow roots.
- calendar/date-picker strategy differences.
- web table row extraction and row actions.
- JavaScriptExecutor and when not to use it.
- broken image detection.
- advanced Selenium exceptions and future framework diagnostics.

You should also be able to point to the exact test class that demonstrates
each topic. Interview preparation is stronger when you can connect the answer
to code you have written:

| Topic | File |
| --- | --- |
| Windows and nested frames | `src/test/java/com/learning/tests/learning/_15_WindowsAndFramesTest.java` |
| Upload and download | `src/test/java/com/learning/tests/learning/_16_FileUploadDownloadTest.java` |
| Mouse, keyboard, drag/drop, Shadow DOM | `src/test/java/com/learning/tests/learning/_17_MouseKeyboardShadowDomTest.java` |
| Calendars and tables | `src/test/java/com/learning/tests/learning/_18_CalendarAndWebTableTest.java` |
| JavaScript and exception mapping | `src/test/java/com/learning/tests/learning/_19_JavaScriptAndExceptionsTest.java` |

## Strong Answers

**How do you handle a new browser window?**

Save the original handle, capture handles before the action, trigger the new
window, wait for handle count to increase, identify the new handle, switch to
it, assert behavior, close it if needed, and switch back.

The key interview nuance: `getWindowHandles()` tells you what exists, but
`switchTo().window(...)` decides where the next Selenium command runs.

**How do you handle frames?**

Switch into the frame before locating elements inside it. For nested frames,
switch step by step through parent frames. Return with `parentFrame()` or
`defaultContent()` depending on the target.

The key interview nuance: a frame is not a new browser window. It is a nested
browsing context inside the current page.

**How do you upload files with Selenium?**

Send the absolute file path to an `<input type="file">` element. Selenium does
not automate the OS file picker.

The key interview nuance: upload is DOM automation, not desktop automation.

**How do you handle web tables?**

Find table rows, filter to the row with the required cell value, then read
related cells or click row-level actions inside that same row.

The key interview nuance: after matching a row, scope the next locator to that
row so you do not click a similar button from another record.

**When should JavaScriptExecutor be used?**

Use it when normal WebDriver APIs do not expose the needed browser state or
when a controlled fixture needs direct state setup. Avoid using it to hide
real user-interaction problems.

The key interview nuance: JavaScriptExecutor is not a replacement for Selenium
user actions. It is a controlled escape hatch.

**How do you approach Selenium exceptions in a framework?**

First classify the failure: context, locator, timing, interactability, or stale
element. Then add diagnostics such as action name, locator, screenshot, page
URL, and browser logs. Do not hide the original exception unless you replace it
with a clearer framework-level exception that preserves the cause.

## Code Lines To Revise

```java
driver.switchTo().window(newWindow);
```

Change Selenium's active browser window.

```java
wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("frame-top"));
```

Wait for a frame and switch into it in one condition.

```java
fileInput.sendKeys(uploadFile.toString());
```

Upload by sending an absolute path to a file input.

```java
SearchContext shadowRoot = shadowHost.getShadowRoot();
```

Enter an open shadow root and search within that context.

```java
Assert.expectThrows(NoSuchFrameException.class, () -> driver.switchTo().frame("missing-frame"));
```

Assert a known exception in a teaching test.

```java
.filter(row -> row.getText().contains("Doe"))
```

Use a stream predicate to find a row by cell text.

## Common Interview Traps

- Forgetting Selenium does not automatically switch to new windows.
- Trying to locate frame elements from the top page.
- Automating the OS file picker instead of sending a file path to the input.
- Assuming a download is complete as soon as the click returns.
- Treating custom calendars as if Selenium has one calendar command.
- Using JavaScript clicks everywhere instead of fixing waits or locators.
- Retrying every exception blindly.
- Adding longer waits when the real issue is the wrong frame, window, or
  shadow root.

## Framework Phase Bridge

Module 08 can now introduce TestNG framework structure because the raw pain is
visible: repeated driver creation, repeated cleanup, repeated waits, repeated
context handling, repeated fixture path helpers, and repeated diagnostics. The
framework phase should centralize that repetition gradually without hiding the
concepts learned here.
