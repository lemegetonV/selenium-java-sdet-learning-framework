# Windows and Frames

## Files In This Topic

This topic reads these files:

- [src/test/java/com/learning/tests/learning/_15_WindowsAndFramesTest.java](../../src/test/java/com/learning/tests/learning/_15_WindowsAndFramesTest.java)


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

Think of a window handle as Selenium's internal address for one top-level
browser context. It is not the page title, URL, tab index, or visible browser
label. It is an opaque string that Selenium gives you so you can switch to the
right context later.

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
- `driver.close()` closes only the currently selected window, while
  `driver.quit()` closes the full browser session.
- if you close the selected window and forget to switch back, the next command
  can fail because Selenium is pointing at a context that no longer exists.

## Frames

Frames create a separate browsing context inside the page. Elements inside a
frame are not visible to page-level `findElement` calls.

Module 07 uses:

```java
wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("frame-top"));
```

This waits for the frame and switches into it.

This condition is important because it combines two pieces of behavior:

- it waits until the frame exists and is available.
- when the wait succeeds, Selenium is already inside that frame.

After this line, locators search inside the frame, not the top-level page.

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
- assuming frames are the same as windows. A frame is inside the same browser
  window; a window handle points to a separate top-level tab/window.

## Framework Direction

Later framework code can hide some repeated wait/switch patterns, but frame
and window context must still be handled deliberately. A wrapper should not
silently switch contexts in a surprising way.

Good future wrapper design should make the context transition visible in the
method name, for example `switchToWindowWithTitle(...)` or
`switchToFrameWhenAvailable(...)`. Hidden switching inside a generic
`click(...)` wrapper would make tests harder to debug because a later locator
could unexpectedly run inside the wrong window or frame.

## Java Syntax To Notice

```java
Set<String> windowsBeforeClick = driver.getWindowHandles();
```

A `Set` stores unique values. Window handles are unique identifiers, so a set
is the natural collection type.

The type is `Set<String>` because:

- `Set` describes the collection behavior: no duplicate handles.
- `String` describes the element type stored inside the collection.
- the generic type lets Java protect you from accidentally adding a non-handle
  object to the collection.

```java
String newWindow = driver.getWindowHandles().stream()
        .filter(windowHandle -> !windowsBeforeClick.contains(windowHandle))
        .findFirst()
        .orElseThrow();
```

This stream filters all current handles down to the one that did not exist
before the click. `orElseThrow()` is appropriate because the test cannot
continue if no new window exists.

The lambda `windowHandle -> !windowsBeforeClick.contains(windowHandle)` reads
as: "keep the handle if it was not present before the click." This is the raw
Java version of a helper method the framework may eventually centralize.

## Interview Readiness

**Question: Does Selenium automatically switch to a newly opened window?**

No. Selenium stays in the current window until the test calls
`switchTo().window(handle)`.

**Question: Why can't Selenium find elements inside a frame immediately?**

A frame is a separate browsing context. Selenium searches the current context
only, so the test must switch into the frame first.

**Question: What is the difference between `parentFrame()` and
`defaultContent()`?**

`parentFrame()` moves one level up. `defaultContent()` returns all the way to
the top-level page.

## Revision Checklist

- Can you explain how to identify a new window handle?
- Can you explain why nested frames require step-by-step switching?
- Can you explain why wrappers must make context switching explicit?
