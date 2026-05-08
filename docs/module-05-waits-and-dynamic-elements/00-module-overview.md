# Module 05 - Waits and Dynamic Elements

## What This Module Adds

Module 05 teaches timing problems in browser automation and introduces
Selenium waits.

Module 04 found elements immediately. Module 05 shows what happens when the UI
changes after the test action:

```mermaid
flowchart LR
    A["Module 04: immediate element lookup"] --> B["Dynamic UI changes after action"]
    B --> C["Explicit waits"]
    C --> D["Fluent waits"]
    D --> E["Module 10: centralized wait wrappers"]
```

The module still keeps waits inside raw test classes. Centralized wait
utilities are intentionally deferred until the framework wrapper module.

## Files Added Or Changed

| File | Status | Purpose |
| --- | --- | --- |
| `README.md` | changed | updates current module status |
| `src/test/java/com/learning/tests/learning/_07_ExplicitWaitTest.java` | added | waits for hidden dynamic text to become visible |
| `src/test/java/com/learning/tests/learning/_08_DynamicControlsWaitTest.java` | added | waits for checkbox removal/addition and input enablement |
| `src/test/java/com/learning/tests/learning/_09_FluentWaitTest.java` | added | demonstrates custom timeout, polling, and ignored exceptions |
| `src/test/java/com/learning/tests/learning/_10_ImplicitWaitAndTimeoutTest.java` | added | demonstrates implicit wait setup, controlled timeout assertion, and stale element behavior |
| `docs/module-05-waits-and-dynamic-elements/00-module-overview.md` | added | module map, file ownership, deferred scope, and quality gate |
| `docs/module-05-waits-and-dynamic-elements/01-implicit-explicit-fluent-waits.md` | added | explains wait types and usage tradeoffs |
| `docs/module-05-waits-and-dynamic-elements/02-expected-conditions.md` | added | explains conditions used in this module |
| `docs/module-05-waits-and-dynamic-elements/03-dynamic-elements-and-timeouts.md` | added | explains dynamic UI, changing DOM shape, and timeout failures |
| `docs/module-05-waits-and-dynamic-elements/99-interview-review.md` | added | interview-ready revision for synchronization, waits, timeouts, and stale elements |
| `docs/module-05-waits-and-dynamic-elements/exercises.md` | added | practice tasks with hints and expected outcomes |

## Previous Module Files Reused

Module 05 builds on raw browser and locator tests:

- `src/test/java/com/learning/tests/learning/_01_FirstBrowserTest.java`
- `src/test/java/com/learning/tests/learning/_04_LocatorStrategyTest.java`
- `src/test/java/com/learning/tests/learning/_06_WebElementCommandTest.java`

The new wait tests still duplicate driver creation and cleanup intentionally.

## Source Ownership

Module 05 tests live under:

```text
src/test/java/com/learning/tests/learning/
```

These are raw learning tests, not framework wait utilities.

Because this is the same shared package used by Modules 03 and 04, Module 05
continues the global learning sequence with `_07_`, `_08_`, `_09_`, and
`_10_`.

## Wait Flow

```mermaid
sequenceDiagram
    participant Test as Test method
    participant UI as Dynamic page
    participant Wait as WebDriverWait
    participant Condition as ExpectedCondition

    Test->>UI: click Start
    Test->>Wait: until(condition)
    loop poll until timeout
        Wait->>Condition: check browser state
        Condition->>UI: find/read element
    end
    Wait-->>Test: element or timeout
```

## What Is Intentionally Deferred

Module 05 does not add:

- centralized `WaitUtils`.
- wrapper methods.
- page objects.
- retry logic.
- screenshot on timeout.
- custom framework exceptions for timeouts.
- JavaScript fallback clicks.

Those appear after the learner has seen raw wait duplication and timing
failures directly.

## Quality Gate

Run:

```bash
mvn test
mvn test -Dheadless=false
```

Expected outcome:

- TestNG runs fifteen Selenium tests.
- dynamic loading and dynamic controls tests pass.
- the controlled timeout test catches and asserts `TimeoutException`.
- the stale element test demonstrates a saved `WebElement` becoming detached
  after the DOM changes.
- visible mode passes when `-Dheadless=false` is used.

## Readiness Standard

Before Module 06 adds more form controls, a learner should be able to explain:

- what synchronization means in Selenium.
- why waits should target browser state instead of sleeping.
- implicit vs explicit vs fluent waits.
- why long implicit waits make framework timing harder to reason about.
- how expected conditions map to visibility, invisibility, text, clickability,
  and stale element state.
- what `TimeoutException` and `StaleElementReferenceException` mean.
- why later modules centralize wait behavior only after raw waits are clear.

Use `99-interview-review.md` before moving into richer interactions.
