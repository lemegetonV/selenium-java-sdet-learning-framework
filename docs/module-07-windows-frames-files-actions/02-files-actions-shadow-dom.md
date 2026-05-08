# Files, Actions, and Shadow DOM

## Files In This Topic

```text
src/test/java/com/learning/tests/learning/_16_FileUploadDownloadTest.java
src/test/java/com/learning/tests/learning/_17_MouseKeyboardShadowDomTest.java
src/test/resources/module07/advanced-interactions.html
src/test/resources/module07/upload-sample.txt
```

This topic uses The Internet for upload, hover, and key presses:

```text
https://the-internet.herokuapp.com/upload
https://the-internet.herokuapp.com/hovers
https://the-internet.herokuapp.com/key_presses
```

The local fixture remains for exact download content, reliable drag/drop, and
open Shadow DOM click behavior.

## File Upload

Selenium uploads a file by sending an absolute path to an
`<input type="file">` element:

```java
fileInput.sendKeys(uploadFile.toString());
```

It does not automate the operating system file picker. That is important
because OS dialogs are outside normal WebDriver control.

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

After clicking the download link, the test waits for the file to exist:

```java
wait.until(currentDriver -> Files.exists(downloadedFile));
```

This is a file-system wait, not a DOM wait. The browser may need time to write
the file after the click has returned.

## Mouse Actions

The hover example uses The Internet. Selenium's `Actions` class builds
user-like gestures:

```java
new Actions(driver).moveToElement(hoverCard).perform();
```

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

## Drag And Drop

The local fixture uses mouse events so the learner can see the gesture:

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
