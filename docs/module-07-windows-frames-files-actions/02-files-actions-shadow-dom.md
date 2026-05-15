# Files, Actions, and Shadow DOM

## Files In This Topic

This topic reads these files:

- [src/test/java/com/learning/tests/learning/_16_FileUploadDownloadTest.java](../../src/test/java/com/learning/tests/learning/_16_FileUploadDownloadTest.java)
- [src/test/java/com/learning/tests/learning/_17_MouseKeyboardShadowDomTest.java](../../src/test/java/com/learning/tests/learning/_17_MouseKeyboardShadowDomTest.java)
- [src/test/resources/module07/advanced-interactions.html](../../src/test/resources/module07/advanced-interactions.html)
- [src/test/resources/module07/upload-sample.txt](../../src/test/resources/module07/upload-sample.txt)


This topic uses The Internet for upload, hover, and key presses:

```text
https://the-internet.herokuapp.com/upload
https://the-internet.herokuapp.com/hovers
https://the-internet.herokuapp.com/key_presses
```

The local fixture remains for exact download content, visible drag/drop, and
open Shadow DOM click behavior.

## File Upload

Selenium uploads a file by sending an absolute path to an
`<input type="file">` element:

```java
fileInput.sendKeys(uploadFile.toString());
```

It does not automate the operating system file picker. That is important
because OS dialogs are outside normal WebDriver control.

The key distinction is that Selenium controls the browser DOM, not the native
operating-system UI. The file chooser belongs to the OS. In automation, the
stable path is to locate the file input and send the path directly.

Good upload test flow:

1. Locate the file input.
2. Send an absolute file path.
3. Assert the page displays or processes the uploaded file.

Module 07 uses The Internet for this because its upload page is stable and
clear.

## File Download

Module 07 uses a local fixture for download because The Internet has download
links, but this repo does not own their exact content. The local link lets the
test validate a known file name and known file content.

The test configures Chrome with a temporary download directory:

```java
preferences.put("download.default_directory", downloadDirectory.toString());
preferences.put("download.prompt_for_download", false);
```

These preferences must be set before `new ChromeDriver(options)` because they
change how the browser profile behaves. Once Chrome has started, changing the
Java `Map` would not update that already-running browser session.

After clicking the download link, the test waits for the file to exist:

```java
wait.until(currentDriver -> Files.exists(downloadedFile));
```

This is a file-system wait, not a DOM wait. The browser may need time to write
the file after the click has returned.

In production frameworks, downloads often need extra checks:

- wait for temporary download extensions to disappear.
- validate file size or content.
- clean up download directories between tests.
- keep download paths isolated for parallel execution.

Module 07 keeps the first version small: one temporary directory, one known
file name, one content assertion.

## Mouse Actions

The hover example uses The Internet. Selenium's `Actions` class builds
user-like gestures:

```java
new Actions(driver).moveToElement(hoverCard).perform();
```

`Actions` is a builder. Calls like `moveToElement(...)`, `clickAndHold(...)`,
and `release()` queue a gesture. `perform()` sends the built gesture to the
browser. Without `perform()`, the action sequence has not executed.

Use Actions for:

- hover.
- drag/drop.
- key combinations.
- composite gestures.

Do not use Actions when a normal `click()` or `sendKeys()` is enough.

## Keyboard Actions

The keyboard example uses The Internet key presses page. Module 07 sends
normal text and a special key:

```java
keyboardInput.sendKeys("abc");
keyboardInput.sendKeys(Keys.ESCAPE);
```

Selenium exposes special keys through the `Keys` enum. Future wrapper methods
may centralize keyboard actions, but this module keeps the raw API visible.

The `Keys` enum is Java's type-safe way to represent non-text keyboard input.
For example, `Keys.ESCAPE` is clearer and less error-prone than trying to type
an escape character manually.

## Drag And Drop

The local fixture uses both HTML drag events and a mouse-event fallback so the
learner can see the gesture and the beginner Selenium `Actions` sequence stays
deterministic:

```java
new Actions(driver)
        .clickAndHold(source)
        .moveToElement(target)
        .release()
        .perform();
```

The Internet has a drag/drop page, but HTML5 drag/drop is often inconsistent
with beginner Selenium `Actions` examples. Module 07 uses a deterministic
fixture for the first raw Actions lesson and defers JavaScript fallbacks.

The fixture reports the gesture through visible state:

```java
Assert.assertEquals(driver.findElement(By.id("drag-result")).getText(), "Dropped: Drag source");
Assert.assertTrue(driver.findElement(By.id("drop-target")).getAttribute("class").contains("drop-ready"));
```

This is intentionally more visible than a hidden assertion-only fixture. A
learner opening the page manually can see the source enter dragging state and
the drop target turn ready after the drop.

## Shadow DOM

The local fixture uses an open shadow root with a clickable button. Shadow DOM
hides internal elements behind a shadow host:

```java
WebElement shadowHost = driver.findElement(By.id("shadow-host"));
SearchContext shadowRoot = shadowHost.getShadowRoot();
```

Normal page-level `findElement` does not search inside the shadow root. For an
open shadow root, Selenium can enter it and search from there.

Closed shadow roots are intentionally not handled in this module.

The returned type is `SearchContext`, not `WebDriver`, because a shadow root is
a searchable area, not a full browser session. It can find elements inside
itself, but it does not navigate, manage windows, or own the driver lifecycle.

## Java Syntax To Notice

```java
Path uploadFile = Path.of("src/test/resources/module07/upload-sample.txt").toAbsolutePath();
```

`Path` represents a file-system path in a platform-aware way. Calling
`toAbsolutePath()` makes the path unambiguous for the browser process.

```java
Map<String, Object> preferences = new HashMap<>();
preferences.put("download.default_directory", downloadDirectory.toString());
```

Chrome preferences are key-value settings. The value type is `Object` because
different preferences can use strings, booleans, numbers, or other structured
values.

```java
wait.until(currentDriver -> Files.exists(downloadedFile));
```

This wait uses a lambda instead of an `ExpectedConditions` helper because the
condition is not about the DOM. It polls the file system until the downloaded
file appears.

## Interview Readiness

**Question: How does Selenium upload a file?**

It sends the absolute file path to an `<input type="file">` element with
`sendKeys`. It does not automate the native operating-system file chooser.

**Question: How do you validate downloads?**

Configure the browser download directory, click the download link, then wait
for and inspect the file-system result. The DOM may not tell you when a file is
fully written.

**Question: What is Selenium `Actions` used for?**

`Actions` builds composite user-like gestures such as hover, drag/drop, and key
combinations. Use simple `click` or `sendKeys` when those are enough.

**Question: What is Shadow DOM?**

Shadow DOM hides internal elements behind a shadow host. Selenium can enter an
open shadow root with `getShadowRoot()`, but closed shadow roots are not
available through normal WebDriver APIs.

## Revision Checklist

- Can you explain why upload uses an absolute path?
- Can you explain why download validation waits on the file system?
- Can you explain why open Shadow DOM is different from normal DOM lookup?
