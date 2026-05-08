# Module 04 - Locators and Web Elements

## What This Module Adds

Module 04 teaches how Selenium finds elements and performs first element
actions.

Module 03 proved that Selenium can launch Chrome, navigate, and assert title
or URL. Module 04 moves from browser-level commands to page-level element
commands.

```mermaid
flowchart LR
    A["Module 03: browser navigation"] --> B["Module 04: find elements"]
    B --> C["WebElement commands"]
    C --> D["Module 05: waits for dynamic elements"]
```

## Files Added Or Changed

| File | Status | Purpose |
| --- | --- | --- |
| `README.md` | changed | updates current module status |
| `src/test/java/com/learning/tests/learning/_04_LocatorStrategyTest.java` | added | demonstrates locator strategies, locator templates, chained locators, XPath axes, and locator exceptions |
| `src/test/java/com/learning/tests/learning/_05_LinkLocatorTest.java` | added | demonstrates `By.linkText`, `By.partialLinkText`, and click navigation |
| `src/test/java/com/learning/tests/learning/_06_WebElementCommandTest.java` | added | demonstrates `sendKeys`, `clear`, `click`, `getText`, and `getAttribute` |
| `docs/module-04-locators-and-web-elements/00-module-overview.md` | added | module map, file ownership, deferred scope, and quality gate |
| `docs/module-04-locators-and-web-elements/01-locator-strategies.md` | added | explains locator strategies, templates, best practices, and stability rules |
| `docs/module-04-locators-and-web-elements/02-findelement-vs-findelements.md` | added | explains single vs list element lookup behavior |
| `docs/module-04-locators-and-web-elements/03-webelement-commands.md` | added | explains first element commands and gotchas |
| `docs/module-04-locators-and-web-elements/04-xpath-chained-locators-and-exceptions.md` | added | explains XPath syntax, chained XPath, axes, scoped lookups, and locator exceptions |
| `docs/module-04-locators-and-web-elements/99-interview-review.md` | added | interview-ready revision for locator and WebElement concepts |
| `docs/module-04-locators-and-web-elements/exercises.md` | added | practice tasks with hints and expected outcomes |

## Previous Module Files Reused

Module 04 builds directly on the raw Selenium tests from Module 03:

- `src/test/java/com/learning/tests/learning/_01_FirstBrowserTest.java`
- `src/test/java/com/learning/tests/learning/_02_NavigationTest.java`
- `src/test/java/com/learning/tests/learning/_03_SauceDemoPageLoadTest.java`

The setup duplication remains intentional. This module still does not add a
base class.

## Source Ownership

Module 04 tests live under:

```text
src/test/java/com/learning/tests/learning/
```

They are raw learning tests, not framework tests.

Because this is the same shared package introduced in Module 03, Module 04
continues the global learning sequence with `_04_`, `_05_`, and `_06_`.

## Locator Flow

```mermaid
sequenceDiagram
    participant Test as Test method
    participant Driver as WebDriver
    participant By as By locator
    participant Element as WebElement

    Test->>By: By.id("user-name")
    Test->>Driver: findElement(By)
    Driver-->>Test: WebElement
    Test->>Element: sendKeys("locked_out_user")
    Test->>Element: getAttribute("value")
```

## What Is Intentionally Deferred

Module 04 does not add:

- waits.
- dynamic element handling.
- stale element recovery.
- page objects.
- wrapper methods.
- centralized locator storage.
- `BaseTest`.
- screenshots or logging.

Those appear later after raw locator and WebElement behavior is clear.

## Quality Gate

Run:

```bash
mvn test
mvn test -Dheadless=false
```

Expected outcome:

- TestNG runs nine Selenium tests.
- Module 03 browser tests still pass.
- Module 04 locator and WebElement tests pass.
- Chrome opens visibly when `-Dheadless=false` is used.

## Readiness Standard

Before Module 05 adds waits, a learner should be able to explain:

- how `By` represents locator strategy plus locator value.
- why locator stability matters more than copying whatever dev tools gives.
- the difference between `findElement` and `findElements`.
- how scoped lookup from a parent `WebElement` reduces ambiguity.
- why XPath axes are useful for tables and related DOM structures.
- what `NoSuchElementException` and `InvalidSelectorException` mean.
- why raw locators are visible now before page objects centralize them later.

Use `99-interview-review.md` for final revision before dynamic waits.
