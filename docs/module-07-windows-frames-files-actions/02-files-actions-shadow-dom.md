# Files, Actions, and Shadow DOM

## Files In This Topic

```text
src/test/java/com/learning/tests/learning/_16_FileUploadDownloadTest.java
src/test/java/com/learning/tests/learning/_17_MouseKeyboardShadowDomTest.java
src/test/resources/module07/advanced-interactions.html
src/test/resources/module07/upload-sample.txt
```

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

## File Download

Module 07 configures Chrome with a temporary download directory:

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

Selenium's `Actions` class builds user-like gestures:

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

Module 07 sends normal text and a special key:

```java
keyboardInput.sendKeys("abc");
keyboardInput.sendKeys(Keys.ESCAPE);
```

Selenium exposes special keys through the `Keys` enum. Future wrapper methods
may centralize keyboard actions, but this module keeps the raw API visible.

## Drag And Drop

The fixture uses mouse events so the learner can see the gesture:

```java
new Actions(driver)
        .clickAndHold(source)
        .moveToElement(target)
        .release()
        .perform();
```

Real drag/drop widgets can be harder because some applications use custom
JavaScript, HTML5 drag events, or canvas. Module 07 teaches the Selenium API
shape without adding JavaScript fallbacks yet.

## Shadow DOM

Shadow DOM hides internal elements behind a shadow host:

```java
WebElement shadowHost = driver.findElement(By.id("shadow-host"));
SearchContext shadowRoot = shadowHost.getShadowRoot();
```

Normal page-level `findElement` does not search inside the shadow root. For an
open shadow root, Selenium can enter it and search from there.

Closed shadow roots are intentionally not handled in this module.
