# Selenium Java UI Automation Learning Framework Plan

## Project Intent

This repository is a progressive learning project for traditional Selenium
Java UI automation. It starts from Java and OOP fundamentals, moves through
basic Selenium WebDriver usage, evolves into a TestNG framework, and then adds
advanced framework features such as wrapper methods, logging, reporting,
data-driven testing, parallel execution, Cucumber BDD, and CI/CD.

The goal is not only to produce a final framework. The goal is to teach why
each layer exists and how Java OOP concepts are applied in real framework
design.

This project should follow the same learning-repo style as the neighboring
Playwright and pytest projects:

- One repository grows from beginner modules to capstone.
- Each module has its own branch.
- `main` remains the latest completed checkpoint.
- Each completed module gets a `module-XX-complete` tag.
- Concept docs are written before or alongside code.
- The learner can check out any module branch to study that stage.

## Fixed Decisions

| Area | Decision |
| --- | --- |
| Language | Java 21 LTS |
| Build tool | Maven |
| Browser automation | Selenium WebDriver |
| Primary test framework | TestNG |
| BDD framework | Cucumber, introduced after TestNG framework maturity |
| Primary AUT | SauceDemo: `https://www.saucedemo.com` |
| Selenium concept playground | The Internet: `https://the-internet.herokuapp.com` |
| Logging | Log4j2, later with per-test context |
| Reporting | TestNG reports, screenshots, Extent Reports, Allure |
| Test data | TestNG DataProvider, JSON, CSV, Apache POI Excel |
| CI/CD | GitHub Actions |
| Final locator architecture | Dynamic `By` locators, not PageFactory |
| Final interaction architecture | Page objects call framework wrapper methods, not raw Selenium directly |

## External Reference Repositories

These repositories are reference material only. Do not vendor them, copy them
wholesale, or treat their source as the source code for this project.

### Progressive Learning Reference

- URL: `https://github.com/lemegetonV/javaSeleniumFw`
- Clone command:

```bash
git clone https://github.com/lemegetonV/javaSeleniumFw.git /tmp/javaSeleniumFw-reference
```

- Purpose:
  - Shows the gradual learning style to preserve.
  - Useful for studying progression from raw Selenium to TestNG, `BaseTest`,
    Page Objects, logging, and wrapper utilities.
- Usage rule:
  - Refer to its commit history and teaching progression.
  - Do not copy its final code quality or old dependency choices blindly.

### Advanced Framework Reference

- URL: `https://github.com/lemegetonV/selenium-testng-demo`
- Clone command:

```bash
git clone https://github.com/lemegetonV/selenium-testng-demo.git /tmp/selenium-testng-demo-reference
```

- Purpose:
  - Reference for mature Java Selenium TestNG framework methods.
  - Useful for `ElementActions`, `DriverFactory`, `ConfigReader`,
    `WaitUtils`, `ScreenshotUtils`, custom exceptions, Extent listener,
    Log4j2/MDC, JSON data readers, Datafaker, and code style standards.
- Usage rule:
  - Borrow ideas gradually only when the curriculum reaches that level.
  - Do not introduce final-state abstractions in beginner modules.
  - Adapt patterns to SauceDemo, The Internet, this package structure, and
    this project's teaching goals.

## Git Strategy

### Branch Model

```text
main: scaffold -> M01 complete -> M02 complete -> M03 complete -> ...
        |              |              |
        M01 branch     M02 branch     M03 branch
```

### Invariants

- `main` is always a completed checkpoint.
- Each module branch is created from `main` after the previous module is
  complete.
- When a module is marked complete, new implementation work starts from
  `main` on the next module branch.
- Work happens on the active module branch.
- A module branch is never reused after completion.
- Modules are linear. Do not start Module N+1 until Module N is complete.
- `main` moves only when a module is complete.
- Each completed module gets a tag named `module-XX-complete`.

### Branch Naming

Use `module-XX-name`, for example:

- `module-01-java-oops-foundation`
- `module-08-testng-framework-foundation`
- `module-16-cucumber-bdd`

### Commit Style

Use small learning checkpoints:

```text
module-01: add Java class and object guide
module-01: add constructor examples
module-01: add practice exercises
module-01: mark module complete
```

## Target Project Structure

The final project should evolve toward this shape. Do not create all folders
too early unless the current module needs them.

```text
ui-testing-selenium-framework/
├── README.md
├── plan.md
├── CLAUDE.md
├── AGENTS.md
├── pom.xml
├── testng.xml
├── docs/
│   └── module-XX-name/
│       ├── 00-module-overview.md
│       ├── 01-focused-concept.md
│       └── exercises.md
├── src/
│   ├── main/java/com/learning/framework/
│   │   ├── config/
│   │   ├── core/
│   │   ├── driver/
│   │   ├── pages/
│   │   ├── utils/
│   │   └── models/
│   ├── main/resources/
│   │   └── log4j2.xml
│   └── test/
│       ├── java/com/learning/tests/
│       │   ├── learning/
│       │   ├── saucedemo/
│       │   ├── cucumber/
│       │   ├── base/
│       │   ├── listeners/
│       │   └── dataproviders/
│       └── resources/
│           ├── config.properties
│           ├── features/
│           └── testdata/
├── test-data/
├── reports/
└── .github/workflows/
```

## Source Organization Rules

`docs/` is module-by-module because it is the learning curriculum.

`src/` should become production-style over time. It should not mirror `docs/`
module-by-module once real framework code exists.

Use these rules:

- Module-only Java examples live under
  `src/main/java/com/learning/examples/moduleXX/`.
- Real reusable framework classes live under
  `src/main/java/com/learning/framework/`.
- Raw Selenium concept tests live under
  `src/test/java/com/learning/tests/learning/`.
- SauceDemo application tests live under
  `src/test/java/com/learning/tests/saucedemo/`.
- Test support code is grouped by responsibility, such as `base`,
  `listeners`, `dataproviders`, `cucumber`, and future support packages.

Do not place temporary learning examples under
`com.learning.framework.moduleXX`. Keep `com.learning.framework` for real
framework classes such as `DriverFactory`, `ConfigReader`, page objects,
wrapper actions, waits, screenshots, logging, reporting, and data readers.

Do not duplicate framework snapshots for each module. Once a framework class
exists, evolve that class in place and explain the change in that module's
docs.

### Learning Class Ordering

Early learning-only Java classes may use `_NN_` prefixes to show concept
sequence in the file tree, for example `_01_BrowserSession.java`.

This is allowed only for:

- `src/main/java/com/learning/examples/moduleXX/`.
- `src/test/java/com/learning/tests/learning/` while modules are still raw
  Selenium concept tests.

This is a learning aid, not production Java style. Do not use `_NN_` prefixes
for real framework packages/classes such as page objects, `BaseTest`,
`DriverFactory`, `ElementActions`, config, utilities, listeners, or data
providers.

## Module Map

### Module 01 - Java OOP Foundation

Branch: `module-01-java-oops-foundation`

Purpose:
- Teach beginner Java needed before Selenium.

Concepts:
- JDK, Maven, IDE/project structure.
- Classes, objects, fields, methods.
- Constructors.
- Packages and imports.
- Access modifiers.
- `static` vs instance members.
- Primitive types, strings, arrays, lists, maps.
- Basic control flow and methods.

Implementation:
- Small Java examples under a learning package.
- No Selenium framework yet.
- Maven project introduced only as much as needed to run examples/tests.

OOP focus:
- What is an object?
- What is state?
- What is behavior?
- Why classes are blueprints.

### Module 02 - OOP for Selenium

Branch: `module-02-oops-for-selenium`

Purpose:
- Connect Java OOP concepts directly to Selenium and framework design.

Concepts:
- Encapsulation.
- Inheritance.
- Abstraction.
- Interfaces.
- Polymorphism.
- Exception handling.
- Collections in test automation.

Implementation:
- Simple Java examples first.
- Then map concepts to Selenium examples such as:
  - `WebDriver driver = new ChromeDriver();`
  - `ChromeDriver` as concrete class.
  - `WebDriver` as interface.
  - test classes inheriting common setup later.

Deferred:
- Do not build a full `BaseTest` yet.

### Module 03 - First Selenium Tests

Branch: `module-03-first-selenium-tests`

Purpose:
- Launch browser and write first Selenium scripts with TestNG kept minimal.

Concepts:
- Selenium Manager.
- `ChromeDriver`.
- `driver.get`.
- title, URL, navigation.
- basic assertions.
- setup and teardown without a framework.

AUTs:
- The Internet for simple pages.
- SauceDemo for the first real app page load.

Implementation:
- First raw Selenium tests.
- Show duplication intentionally so later modules can remove it.

### Module 04 - Locators and Web Elements

Branch: `module-04-locators-and-web-elements`

Purpose:
- Teach finding and interacting with elements.

Concepts:
- `By.id`, `By.name`, `By.className`, `By.tagName`, `By.linkText`,
  `By.partialLinkText`, `By.cssSelector`, `By.xpath`.
- `findElement` vs `findElements`.
- `click`, `sendKeys`, `clear`, `getText`, `getAttribute`.
- locator stability and readable selectors.

AUTs:
- The Internet.
- SauceDemo login page.

Implementation:
- Raw Selenium locator examples.
- Locator strategy doc.

### Module 05 - Waits and Dynamic Elements

Branch: `module-05-waits-and-dynamic-elements`

Purpose:
- Teach timing, dynamic UI, and why sleeps are poor automation design.

Concepts:
- implicit wait.
- explicit wait.
- fluent wait.
- `ExpectedConditions`.
- dynamic loading.
- dynamic controls.
- stale element basics.
- timeout failures.

AUT:
- The Internet dynamic loading and dynamic controls pages.

Implementation:
- Start with brittle code.
- Fix with explicit waits.
- Explain why the final framework will centralize waits.

### Module 06 - Forms, Alerts, Dropdowns

Branch: `module-06-forms-alerts-dropdowns`

Purpose:
- Cover common browser form interactions.

Concepts:
- inputs.
- checkboxes.
- radio buttons.
- dropdowns with Selenium `Select`.
- JavaScript alerts, confirms, prompts.
- form authentication.

AUT:
- The Internet.

Implementation:
- Focused tests per browser mechanic.
- Keep framework abstraction minimal.

### Module 07 - Windows, Frames, Files, Actions

Branch: `module-07-windows-frames-files-actions`

Purpose:
- Cover advanced browser interaction mechanics before framework abstraction.

Concepts:
- windows and tabs.
- frames and nested frames.
- file upload.
- file download validation.
- hovers.
- key presses.
- drag and drop.
- JavaScriptExecutor.
- Shadow DOM.
- sortable tables.
- broken images and status-code checks where useful.

AUT:
- The Internet.

Implementation:
- Concept tests grouped under `tests/learning`.
- Explain when Selenium needs context switching.

### Module 08 - TestNG Framework Foundation

Branch: `module-08-testng-framework-foundation`

Purpose:
- Convert raw tests into a reusable TestNG foundation.

Concepts:
- `@Test`.
- `@BeforeMethod`.
- `@AfterMethod`.
- `@BeforeClass` and `@AfterClass`.
- assertions.
- groups.
- `testng.xml`.
- Maven Surefire.
- inheritance through `BaseTest`.

Implementation:
- Add `BaseTest`.
- Move browser setup/teardown out of test methods.
- Explain `protected` driver and why child test classes inherit setup.

### Module 09 - Page Object Model

Branch: `module-09-page-object-model`

Purpose:
- Introduce POM using SauceDemo.

Concepts:
- encapsulation of locators.
- public page actions.
- readable test flow.
- page-to-page returns.
- PageFactory vs dynamic `By` locators.

AUT:
- SauceDemo.

Implementation:
- `LoginPage`.
- `ProductsPage`.
- `CartPage`.
- `CheckoutPage`.
- Keep page actions simple at first.

Final direction:
- Teach PageFactory as a historical/common approach if useful, but final
  framework should use dynamic `By` locators resolved at action time.

### Module 10 - Wrapper Methods and Waits

Branch: `module-10-wrapper-methods-and-waits`

Purpose:
- Introduce framework wrapper methods around Selenium commands.

Concepts:
- abstraction.
- reuse.
- centralized waiting.
- centralized logging.
- consistent failure handling.

Implementation:
- Start with simple methods:
  - `click`.
  - `type`.
  - `getText`.
  - `isDisplayed`.
  - `selectByVisibleText`.
- Later in the module, evolve toward an `ElementActions` design inspired by
  `selenium-testng-demo`.

Deferred:
- Full click fallback and screenshot-on-failure can be introduced here or in
  Module 13, depending on learner readiness.

### Module 11 - Config and Driver Factory

Branch: `module-11-config-and-driver-factory`

Purpose:
- Externalize environment and browser configuration.

Concepts:
- `config.properties`.
- typed config getters.
- `System.getProperty` override precedence.
- browser selection.
- headless mode.
- page-load timeouts.
- driver lifecycle.
- `ThreadLocal<WebDriver>` as preparation for parallel execution.

Implementation:
- `ConfigReader`.
- `DriverFactory`.
- improve `BaseTest`.

Reference:
- Use `selenium-testng-demo` as an advanced pattern reference, but keep code
  simpler where the learning path requires it.

### Module 12 - Data Driven Testing

Branch: `module-12-data-driven-testing`

Purpose:
- Teach data-driven test design.

Concepts:
- TestNG `@DataProvider`.
- hardcoded data provider.
- JSON test data.
- CSV test data.
- Excel with Apache POI.
- POJOs and model classes.
- deterministic vs generated data.

AUT:
- SauceDemo login and checkout-style flows where applicable.

Implementation:
- Start with simple login rows.
- Evolve to external files.
- Explain why test data is separated from test logic.

### Module 13 - Listeners, Screenshots, Logging

Branch: `module-13-listeners-screenshots-logging`

Purpose:
- Improve failure diagnosis.

Concepts:
- TestNG `ITestListener`.
- `onTestStart`, `onTestSuccess`, `onTestFailure`, `onTestSkipped`.
- screenshot on failure.
- Log4j2.
- per-test log context.
- retry analyzer.
- custom framework exception.

Implementation:
- `ScreenshotUtils`.
- `FrameworkException`.
- listener registration in `testng.xml`.
- logging in framework actions.

Reference:
- `selenium-testng-demo` for screenshot utilities, framework exception,
  MDC-style context, and listener structure.

### Module 14 - Extent and Allure Reporting

Branch: `module-14-extent-and-allure-reporting`

Purpose:
- Add professional reporting.

Concepts:
- TestNG default reports and limitations.
- Extent Reports.
- Allure TestNG.
- Allure annotations.
- Allure steps.
- screenshot attachments.
- report artifacts.

Implementation:
- Add Extent HTML report.
- Add Allure results and local report commands.
- Compare what each report is good for.

### Module 15 - Parallel Execution and Selenium Grid

Branch: `module-15-parallel-and-grid`

Purpose:
- Teach scalable execution.

Concepts:
- TestNG parallel modes.
- `ThreadLocal<WebDriver>`.
- thread safety.
- isolated test data.
- cross-browser strategy.
- Selenium Grid.
- local vs CI execution tradeoffs.

Implementation:
- Enable controlled parallel execution.
- Ensure driver and reports do not cross-contaminate tests.

### Module 16 - Cucumber BDD

Branch: `module-16-cucumber-bdd`

Purpose:
- Evolve the TestNG framework into a Cucumber-capable framework.

Concepts:
- BDD.
- Gherkin.
- feature files.
- scenarios.
- scenario outlines.
- data tables.
- step definitions.
- glue.
- hooks.
- tags.
- `cucumber-testng`.
- `AbstractTestNGCucumberTests`.

Implementation:
- Keep existing Page Objects and framework services.
- Add Cucumber as a new top layer:

```text
Feature file -> Step definition -> Page Object -> ElementActions -> WebDriver
```

Important rule:
- Cucumber should not replace the whole framework.
- It replaces the test-class layer for BDD scenarios while reusing the same
  page objects, driver management, config, logging, and reporting concepts.

### Module 17 - CI/CD

Branch: `module-17-cicd`

Purpose:
- Run the framework in GitHub Actions.

Concepts:
- CI triggers.
- Maven test command.
- headless browser execution.
- report artifacts.
- Allure artifacts.
- environment variables and system properties.

Implementation:
- `.github/workflows/ui-tests.yml`.
- headless execution.
- upload reports and screenshots.

### Module 18 - Capstone and Portfolio Packaging

Branch: `module-18-capstone-and-portfolio`

Purpose:
- Package the project as a portfolio-ready Selenium Java framework.

Concepts:
- final architecture review.
- framework README.
- badges.
- module navigation.
- interview talking points.
- resume bullets.
- known limitations.

Implementation:
- Full SauceDemo regression suite.
- Smoke and regression groups.
- final README.
- final docs index.

## Learning Doc Standard

Each module must include:

- `00-module-overview.md`.
- focused concept docs.
- code walkthroughs that reference real files once code exists.
- diagrams where useful.
- examples before abstractions.
- "what is intentionally deferred" section.
- `exercises.md` with hints, not full solutions.
- quality gate for completing the module.

Docs should teach the user, not just describe generated code.

Match the documentation depth of the neighboring Playwright learning project.
Each module overview should include:

- what the module adds and why it appears at this point in the curriculum.
- how it builds on earlier modules.
- a `Files Added Or Changed` table with file path, status, and purpose.
- reused previous-module files, when relevant.
- dependency maps or flow diagrams where useful.
- explicit source ownership for files: learning example, framework class,
  test class, test data, config, or documentation.
- what is intentionally deferred.
- exact quality-gate commands and expected outcomes.

Focused concept docs should reference the exact source files, classes,
methods, tests, config, or test data they explain.

### Learning Depth Gate

Passing tests is not enough to complete a module. The curriculum is meant for
deep SDET learning and interview readiness, so each module must teach at four
levels:

- concept model: what the feature is, why it exists, and the problem it solves.
- code model: how the Java syntax, Selenium API, TestNG API, or framework class
  works in the exact source files.
- nuance model: common mistakes, browser behavior, timing issues, edge cases,
  and design tradeoffs.
- interview model: likely questions, strong answer framing, and vocabulary the
  learner should be ready to explain.

Focused concept docs should include these sections where relevant:

- `Mental Model`
- `Code Walkthrough`
- `Java Syntax To Notice`
- `Selenium Or Framework Nuances`
- `Common Mistakes`
- `Interview Readiness`
- `How This Connects To Later Framework Design`
- `Revision Checklist`

Do not mark a module complete until the learner can revise the topic from the
docs without asking the agent to re-explain the basics.

## Learning Comment Standard

This is a learning framework, so source files should be more explanatory than
a normal production repo.

Use comments to teach:

- why a class exists in the current module.
- how a class connects to future Selenium framework concepts.
- which Java/OOP concept is being demonstrated.
- why important design choices were made, such as encapsulation, inheritance,
  abstraction, defensive copies, waits, driver lifecycle, retry behavior, and
  reporting attachments.
- first-time Java syntax such as constructors, access modifiers, `final`,
  `static`, records, generics, lambdas, method references, streams,
  `try/finally`, assertions, and exceptions.
- first-time Selenium concepts such as locators, WebElement state, waits,
  alerts, frames, windows, file handling, JavaScriptExecutor, Shadow DOM,
  Actions, screenshots, and driver cleanup.

Prefer concise JavaDoc on classes and important methods. Avoid noisy comments
that only repeat the syntax. Keep Java comments readable in source form and
avoid HTML-style JavaDoc markup such as `<p>` unless there is a strong reason.
Do not commit commented-out code.

## OOP Teaching Standard

Java OOP must be explained repeatedly where it appears:

- classes and objects in page objects.
- constructors in page setup.
- encapsulation in private locators and public page actions.
- inheritance in `BaseTest`.
- abstraction in `ElementActions` and page methods.
- interfaces and polymorphism in `WebDriver driver = new ChromeDriver()`.
- exception handling in framework utilities.
- collections in table extraction, element lists, and data providers.

## Framework Design Direction

Early modules should be simple and explicit. Advanced abstractions should only
appear after the learner has felt the duplication or fragility they solve.

Final framework direction:

- page objects use dynamic `By` locators.
- page objects do not call `driver.findElement` directly.
- Selenium interactions go through wrapper methods.
- waits are centralized.
- config is externalized.
- driver lifecycle is controlled by `DriverFactory`.
- screenshots/logs/reports are generated by framework services.
- Cucumber step definitions reuse page objects and services.

## Historical Acceptance Criteria for Initial Planning Phase

- `plan.md` exists and contains this full blueprint.
- `CLAUDE.md` exists as the working-memory file.
- `AGENTS.md` exists and is byte-for-byte identical to `CLAUDE.md`.
- Reference repo Git URLs and clone commands are included.
- Before Module 01 began, no Maven framework implementation existed yet.
