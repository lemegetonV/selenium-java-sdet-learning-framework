# Windows and Frames

## Files In This Topic

```text
src/test/java/com/learning/tests/learning/_15_WindowsAndFramesTest.java
```

This topic uses The Internet because it has clean public pages for windows
and nested frames:

```text
https://the-internet.herokuapp.com/windows
https://the-internet.herokuapp.com/nested_frames
```

## Window Handles

Selenium identifies each browser window or tab with a window handle:

```java
String originalWindow = driver.getWindowHandle();
Set<String> windowsBeforeClick = driver.getWindowHandles();
```

The test opens The Internet windows page and clicks `Click Here`. After that,
it waits for a second window:

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
wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("frame-top"));
```

This waits for the frame and switches into it.

## Nested Frames

Nested frames require step-by-step switching:

```java
driver.switchTo().frame("frame-top");
driver.switchTo().frame("frame-left");
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
