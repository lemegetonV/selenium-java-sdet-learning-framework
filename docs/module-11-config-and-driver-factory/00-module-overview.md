# Module 11 - Config and Driver Factory

## What This Module Adds

Module 11 externalizes browser and environment configuration and moves browser
creation out of `BaseTest`.

The framework flow now becomes:

```text
Test -> BaseTest -> DriverFactory -> ConfigReader -> WebDriver
Test -> Page Object -> ElementActions -> WaitUtils -> WebDriver
```

```mermaid
flowchart TD
    A["BaseTest"] --> B["DriverFactory"]
    B --> C["ConfigReader"]
    B --> D["ThreadLocal WebDriver"]
    A --> E["WaitUtils"]
    A --> F["ElementActions"]
    G["Page Objects"] --> F
    G --> E
```

Module 10 centralized element actions and waits. Module 11 centralizes browser
creation, timeout configuration, base URL, and runtime overrides.

## Why This Module Exists Now

Before Module 11, `BaseTest` still knew too much about browser setup:

```java
ChromeOptions options = new ChromeOptions();
options.addArguments("--headless=new");
options.addArguments("--window-size=1440,900");
driver = new ChromeDriver(options);
```

That was acceptable while learning TestNG lifecycle. It becomes limiting once
the framework needs:

- different browsers.
- headless on/off control.
- environment URLs.
- page-load timeout settings.
- future CI overrides.
- future parallel execution.

Module 11 moves those concerns into `ConfigReader` and `DriverFactory`.

## Files Added Or Changed

| File | Status | Purpose |
| --- | --- | --- |
| [CLAUDE.md](../../CLAUDE.md) | changed | marks Module 11 as the active module and keeps future sessions aligned |
| [AGENTS.md](../../AGENTS.md) | changed | exact mirror of [CLAUDE.md](../../CLAUDE.md) |
| [src/test/resources/config/config.properties](../../src/test/resources/config/config.properties) | added | default browser, base URL, timeout, and window-size configuration |
| [src/main/java/com/learning/framework/config/ConfigReader.java](../../src/main/java/com/learning/framework/config/ConfigReader.java) | added | reads config values and applies `System.getProperty` override precedence |
| [src/main/java/com/learning/framework/driver/DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java) | added | creates, returns, and quits browser sessions through `ThreadLocal<WebDriver>` |
| [src/test/java/com/learning/tests/base/BaseTest.java](../../src/test/java/com/learning/tests/base/BaseTest.java) | changed | delegates driver lifecycle to `DriverFactory` and reads wait timeout from config |
| [src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java) | changed | opens the configured `baseUrl` instead of a hardcoded URL |
| [testng.xml](../../testng.xml) | changed | renames the suite to match the Module 11 configured-driver checkpoint |
| [docs/module-11-config-and-driver-factory/00-module-overview.md](00-module-overview.md) | added | module purpose, file map, dependency map, and quality gate |
| [docs/module-11-config-and-driver-factory/01-config-reader.md](01-config-reader.md) | added | explains config files, typed getters, and override precedence |
| [docs/module-11-config-and-driver-factory/02-driver-factory.md](02-driver-factory.md) | added | explains browser creation, lifecycle, timeouts, and `ThreadLocal` |
| [docs/module-11-config-and-driver-factory/03-basetest-refactor.md](03-basetest-refactor.md) | added | explains how `BaseTest` changed and what stayed out of it |
| [docs/module-11-config-and-driver-factory/99-interview-review.md](99-interview-review.md) | added | interview-ready Module 11 revision guide |
| [docs/module-11-config-and-driver-factory/exercises.md](exercises.md) | added | practice tasks with hints and expected outcomes |

## Module Source Links

Use these links as the source-reading checklist for this checkpoint. They point only to files that exist at Module 11.

| File | Status | Why It Matters |
| --- | --- | --- |
| [AGENTS.md](../../AGENTS.md) | Changed | Module session metadata |
| [CLAUDE.md](../../CLAUDE.md) | Changed | Module session metadata |
| [src/main/java/com/learning/framework/config/ConfigReader.java](../../src/main/java/com/learning/framework/config/ConfigReader.java) | Added | Framework configuration source |
| [src/main/java/com/learning/framework/driver/DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java) | Added | Framework driver lifecycle source |
| [src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java](../../src/main/java/com/learning/framework/pages/saucedemo/LoginPage.java) | Changed | Framework Page Object source |
| [src/test/java/com/learning/tests/base/BaseTest.java](../../src/test/java/com/learning/tests/base/BaseTest.java) | Changed | Test framework base class |
| [src/test/resources/config/config.properties](../../src/test/resources/config/config.properties) | Added | Runtime test configuration |
| [testng.xml](../../testng.xml) | Changed | TestNG suite configuration |

## Previous Module Files Reused

Module 11 builds directly on:

- [src/test/java/com/learning/tests/base/BaseTest.java](../../src/test/java/com/learning/tests/base/BaseTest.java)
- [src/main/java/com/learning/framework/actions/ElementActions.java](../../src/main/java/com/learning/framework/actions/ElementActions.java)
- [src/main/java/com/learning/framework/waits/WaitUtils.java](../../src/main/java/com/learning/framework/waits/WaitUtils.java)
- SauceDemo page objects under `src/main/java/com/learning/framework/pages/saucedemo/`
- [src/test/java/com/learning/tests/saucedemo/SauceDemoPageObjectTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoPageObjectTest.java)

The wrapper and page-object layers remain intact. Only browser setup and
configuration ownership change.

## Source Ownership

```text
src/main/java/com/learning/framework/config/
```

Framework configuration readers and typed accessors.

```text
src/main/java/com/learning/framework/driver/
```

Driver lifecycle and browser creation.

```text
src/test/resources/config/
```

Test framework runtime configuration for this learning project.

## What Is Intentionally Deferred

Module 11 does not add:

- true parallel suite execution.
- Selenium Grid.
- environment-specific config files.
- secrets management.
- remote WebDriver.
- Docker browser execution.
- logging and screenshots.

`ThreadLocal<WebDriver>` is introduced now as preparation. Actual parallel
execution waits until Module 15.

## Quality Gate

Run:

```bash
mvn test -Dtest=SauceDemoPageObjectTest
mvn test -Dtest=SauceDemoPageObjectTest -Dbrowser=chrome -Dheadless=true
mvn test -DsuiteXmlFile=testng.xml
mvn test
```

Expected outcome:

- framework tests pass with default config.
- framework tests pass when browser/headless are supplied as system property
  overrides.
- full `mvn test` still runs raw learning tests plus framework tests.
- `BaseTest` no longer creates `ChromeDriver` directly.
- `LoginPage` opens the configured `baseUrl`.

## Framework Readiness Standard

Before moving to Module 12, a learner should be able to explain:

- why configuration belongs outside source code.
- how system property overrides beat file defaults.
- why driver creation moved from `BaseTest` to `DriverFactory`.
- how `ThreadLocal<WebDriver>` prepares for parallel execution.
- why page-load timeout and implicit wait are driver-level settings.
- why explicit wait timeout still feeds `WebDriverWait`.
- what data-driven testing problem remains for Module 12.
