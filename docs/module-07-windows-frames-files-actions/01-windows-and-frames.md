# Windows and Frames

## Files In This Topic

```text
src/test/java/com/learning/tests/learning/_15_WindowsAndFramesTest.java
src/test/resources/module07/advanced-interactions.html
src/test/resources/module07/window-target.html
src/test/resources/module07/frame-child.html
src/test/resources/module07/frame-grandchild.html
```

## Window Handles

Selenium identifies each browser window or tab with a window handle:

```java
String originalWindow = driver.getWindowHandle();
Set<String> windowsBeforeClick = driver.getWindowHandles();
```

After clicking a `target="_blank"` link, the test waits for a second window:

```java
wait.until(ExpectedConditions.numberOfWindowsToBe(windowsBeforeClick.size() + 1));
```

Then it finds the new handle and switches:

```java
driver.switchTo().window(newWindow);
```

Important nuance:

- Selenium commands operate against the currently selected window.
- opening a window does not automatically switch Selenium into it.
- closing the new window requires switching back to the original handle before
  continuing.

## Frames

Frames create a separate browsing context inside the page. Elements inside a
frame are not visible to page-level `findElement` calls.

Module 07 uses:

```java
wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("profile-frame")));
```

This waits for the frame and switches into it.

## Nested Frames

Nested frames require step-by-step switching:

```java
driver.switchTo().frame("profile-frame");
driver.switchTo().frame("nested-frame");
```

To return to the top page:

```java
driver.switchTo().defaultContent();
```

Common mistakes:

- trying to locate frame contents before switching.
- switching directly to a nested frame from the top page when Selenium is not
  inside its parent frame.
- forgetting to return to default content before locating top-page elements.

## Framework Direction

Later framework code can hide some repeated wait/switch patterns, but frame
and window context must still be handled deliberately. A wrapper should not
silently switch contexts in a surprising way.
