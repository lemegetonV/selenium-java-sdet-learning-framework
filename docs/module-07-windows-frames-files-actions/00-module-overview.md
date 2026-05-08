# Module 07 - Windows, Frames, Files, Actions

## What This Module Adds

Module 07 is the final raw Selenium mechanics module before the project starts
building a reusable TestNG framework foundation.

Modules 03 through 06 taught browser launch, locators, waits, forms,
dropdowns, and alerts. Module 07 adds browser-context changes, files, mouse
and keyboard gestures, Shadow DOM, date pickers, tables, JavaScriptExecutor,
and exception patterns.

```mermaid
flowchart LR
    A["Module 04: locators and WebElement commands"] --> B["Module 05: waits and dynamic state"]
    B --> C["Module 06: forms, dropdowns, alerts"]
    C --> D["Module 07: windows, frames, files, actions"]
    D --> E["Module 08: TestNG framework foundation"]
```

The tests are still raw learning tests. The duplication is intentional because
Module 08 will make the need for `BaseTest` visible.

## Files Added Or Changed

| File | Status | Purpose |
| --- | --- | --- |
| `src/test/resources/module07/advanced-interactions.html` | added | local fixture for windows, frames, upload/download, actions, calendar, tables, Shadow DOM, images, and exception triggers |
| `src/test/resources/module07/window-target.html` | added | target page opened in a new window/tab |
| `src/test/resources/module07/frame-child.html` | added | child frame fixture |
| `src/test/resources/module07/frame-grandchild.html` | added | nested frame fixture |
| `src/test/resources/module07/upload-sample.txt` | added | file uploaded through `<input type="file">` |
| `src/test/java/com/learning/tests/learning/_15_WindowsAndFramesTest.java` | added | demonstrates window handles and nested frame switching |
| `src/test/java/com/learning/tests/learning/_16_FileUploadDownloadTest.java` | added | demonstrates file upload and download validation |
| `src/test/java/com/learning/tests/learning/_17_MouseKeyboardShadowDomTest.java` | added | demonstrates hover, keyboard keys, drag/drop, and Shadow DOM |
| `src/test/java/com/learning/tests/learning/_18_CalendarAndWebTableTest.java` | added | demonstrates date picker strategies, table extraction, row actions, and sorting |
| `src/test/java/com/learning/tests/learning/_19_JavaScriptAndExceptionsTest.java` | added | demonstrates JavaScriptExecutor, broken image detection, and advanced Selenium exceptions |
| `docs/module-07-windows-frames-files-actions/00-module-overview.md` | added | module map, file ownership, deferred scope, and quality gate |
| `docs/module-07-windows-frames-files-actions/01-windows-and-frames.md` | added | explains window handles, frame switching, and nested frames |
| `docs/module-07-windows-frames-files-actions/02-files-actions-shadow-dom.md` | added | explains file upload/download, mouse/keyboard actions, drag/drop, and Shadow DOM |
| `docs/module-07-windows-frames-files-actions/03-calendars-tables-javascript.md` | added | explains date pickers, web tables, sortable tables, JavaScriptExecutor, and broken images |
| `docs/module-07-windows-frames-files-actions/04-selenium-exceptions-map.md` | added | maps advanced Selenium exceptions to causes and later framework handling |
| `docs/module-07-windows-frames-files-actions/exercises.md` | added | practice tasks with hints and expected outcomes |

## Previous Module Files Reused

Module 07 builds on these raw learning examples:

- `src/test/java/com/learning/tests/learning/_04_LocatorStrategyTest.java`
- `src/test/java/com/learning/tests/learning/_07_ExplicitWaitTest.java`
- `src/test/java/com/learning/tests/learning/_10_ImplicitWaitAndTimeoutTest.java`
- `src/test/java/com/learning/tests/learning/_13_CheckboxDropdownTest.java`
- `src/test/java/com/learning/tests/learning/_14_AlertsAndAuthenticationTest.java`

The shared `learning/` package continues its global sequence. Module 07 owns
`_15_` through `_19_`.

## Source Ownership

Module 07 tests live under:

```text
src/test/java/com/learning/tests/learning/
```

They are raw Selenium concept tests, not framework tests.

Module 07 fixture files live under:

```text
src/test/resources/module07/
```

They are local test pages used to make advanced browser mechanics stable and
inspectable.

## Context Switching Map

```mermaid
flowchart TD
    A["Default page context"] --> B["switchTo().window(handle)"]
    A --> C["switchTo().frame(...)"]
    A --> D["switchTo().alert()"]
    C --> E["switchTo().defaultContent()"]
    B --> A
```

The main lesson is that Selenium can only interact with the currently active
browser context. Windows, frames, alerts, and Shadow DOM each require a
different access pattern.

## What Is Intentionally Deferred

Module 07 does not add:

- `BaseTest`.
- page objects.
- driver factory.
- reusable file utilities.
- reusable table helpers.
- reusable action wrappers.
- screenshot-on-failure.
- logging or reporting.
- Selenium Grid.

Those start after this raw mechanics phase.

## Quality Gate

Run:

```bash
mvn test
mvn test -Dheadless=false
```

Expected outcome:

- TestNG runs thirty-nine Selenium tests.
- Module 07 local fixture tests pass.
- the file upload test reads `upload-sample.txt`.
- the download test creates and validates `module07-download.txt`.
- window, frame, action, table, Shadow DOM, JavaScript, and exception tests
  pass.
