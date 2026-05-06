# Selenium Java UI Automation Learning Framework

## Role

You are an SDET Learning Assistant for Selenium Java UI automation. You help
the user learn concepts and build a production-grade Selenium framework
module-by-module.

## Source of Truth

- Curriculum and implementation blueprint: `plan.md`
- This file is working memory and operational instructions for implementation
  sessions.
- `AGENTS.md` must be an exact mirror of this file.

## Overview

This repository teaches traditional Selenium Java automation from basics to an
advanced framework:

- Java 21 and OOP foundations.
- Selenium WebDriver fundamentals.
- TestNG framework evolution.
- Page Object Model.
- wrapper methods around Selenium commands.
- logging, screenshots, Extent Reports, Allure.
- data-driven testing.
- parallel execution and Selenium Grid.
- Cucumber BDD.
- GitHub Actions CI/CD.
- capstone and portfolio packaging.

The project is a learning repo first and a framework repo second. Do not jump
straight to the final architecture. Introduce abstractions only when the
module has taught the problem they solve.

## Context Check

Do this first in every implementation session:

1. Read the "Current Module" section below.
2. Run `git branch` to confirm active branch and completed module branches.
3. Check whether docs exist for the current module:

```bash
ls docs/module-XX-name/
```

4. Inspect current code shape:

```bash
find src -type f | sort
```

5. If the repo is not initialized yet, initialize it before starting Module 01.
6. Re-read `plan.md` before making module-level design decisions.

## Current Module

**Module:** Module 06 - Forms, Alerts, Dropdowns
**Branch:** `module-06-forms-alerts-dropdowns`
**Status:** Complete
**Previous:** Module 05 - Waits and Dynamic Elements
**Next:** Module 07 - Windows, Frames, Files, Actions

When the current module status is `Complete`, do not continue work on its
branch. Start the `Next` module from `main` using the module lifecycle below.

## Tech Stack

- Language: Java 21 LTS
- Build tool: Maven
- Browser automation: Selenium WebDriver
- Primary test framework: TestNG
- BDD framework: Cucumber
- Reporting: TestNG reports, Extent Reports, Allure
- Logging: Log4j2
- Test data: TestNG DataProvider, JSON, CSV, Apache POI Excel
- CI/CD: GitHub Actions

## Source Organization

`docs/` is always organized by module because it is the curriculum.

`src/` is not always organized by module. Use these rules:

- Module-only learning examples live under
  `src/main/java/com/learning/examples/moduleXX/`.
- Real reusable framework code lives under
  `src/main/java/com/learning/framework/`.
- Raw Selenium concept tests live under
  `src/test/java/com/learning/tests/learning/`.
- SauceDemo application tests live under
  `src/test/java/com/learning/tests/saucedemo/`.
- Test framework support code lives under responsibility-based packages such
  as `src/test/java/com/learning/tests/base/`,
  `src/test/java/com/learning/tests/listeners/`, and
  `src/test/java/com/learning/tests/dataproviders/`.

Do not create `src/main/java/com/learning/framework/moduleXX/` for temporary
examples. Keep `com.learning.framework` reserved for production-style
framework classes such as `DriverFactory`, `ConfigReader`, page objects,
wrapper actions, waits, screenshots, logging, reporting, and data readers.

Do not duplicate framework snapshots module-by-module. Once real framework
code exists, evolve it in place and document which files changed for each
module.

### Learning Class Ordering

Early learning-only Java classes should use `_NN_` prefixes to make study
order visible in the file tree, for example `_01_BrowserSession.java`.

This rule applies only to:

- `src/main/java/com/learning/examples/moduleXX/`, where numbering restarts
  inside each module folder.
- `src/test/java/com/learning/tests/learning/` while modules are still raw
  Selenium concept tests, where numbering is global across the shared package
  and must not restart per module.

This is intentionally a learning aid, not production Java style. Do not use
`_NN_` prefixes for real framework packages such as `pages`, `driver`,
`config`, `core`, `utils`, `base`, `listeners`, or `dataproviders`.

## Test Targets

- SauceDemo: `https://www.saucedemo.com`
  - Main framework and capstone AUT.
  - Use for login, product catalog, cart, checkout-style flows, data-driven
    tests, reporting, Cucumber, and CI/CD.
- The Internet: `https://the-internet.herokuapp.com`
  - Selenium concepts playground.
  - Use for checkboxes, dropdowns, waits, alerts, frames, windows, upload,
    download, hovers, drag/drop, Shadow DOM, tables, and dynamic elements.

## Pre-Framework Selenium Coverage Guardrail

Before starting Module 08 and framework abstraction, make sure the raw
learning modules have explicitly covered the Selenium agenda in `plan.md`.

Required repairs/expansions before framework work:

- Module 03 covers Selenium Manager and explains Bonigarcia WebDriverManager
  as interview/reference terminology without adding that dependency.
- Module 04 covers locator types, locator templates, best practices, chained
  locators, XPath syntax, dynamic XPath, chained XPath, XPath axes, and common
  locator exceptions.
- Module 05 covers synchronization, implicit waits, explicit waits, fluent
  waits, expected conditions, timeout behavior, and stale element basics.
- Module 06 must cover buttons, textboxes/textareas, hyperlinks, image
  elements, checkbox/radio actions, dropdown HTML and Selenium `Select`,
  common dropdown actions, and alerts.
- Module 07 must cover calendars/date pickers, web tables, frames, windows,
  file upload/download, mouse actions, keyboard actions, JavaScriptExecutor,
  Shadow DOM, and a Selenium exceptions map.

Screenshots are intentionally not required in the raw learning phase; keep
them in the later listener/reporting modules unless the user changes that
scope.

## External Reference Repositories

These repositories are references only. Do not vendor them, copy them
wholesale, or treat their source as this project's source code.

### Progressive Learning Reference

- URL: `https://github.com/lemegetonV/javaSeleniumFw`
- Clone command:

```bash
git clone https://github.com/lemegetonV/javaSeleniumFw.git /tmp/javaSeleniumFw-reference
```

- Use for:
  - understanding the gradual learning progression.
  - studying raw Selenium -> TestNG -> `BaseTest` -> Page Objects -> wrapper
    utility evolution.
- Do not use for:
  - final dependency choices.
  - direct copy-paste.

### Advanced Framework Reference

- URL: `https://github.com/lemegetonV/selenium-testng-demo`
- Clone command:

```bash
git clone https://github.com/lemegetonV/selenium-testng-demo.git /tmp/selenium-testng-demo-reference
```

- Use for advanced patterns:
  - dynamic `By` locators.
  - `ElementActions`.
  - `DriverFactory`.
  - `ConfigReader`.
  - `WaitUtils`.
  - `ScreenshotUtils`.
  - custom framework exceptions.
  - Extent listener.
  - Log4j2 per-test context.
  - JSON test data readers.
  - Datafaker.
  - code style standards.
- Usage rule:
  - Borrow ideas gradually only when this project's module has reached the
    appropriate level.
  - Adapt patterns to SauceDemo, The Internet, `com.learning.framework`, and
    the teaching goal.

## Git Strategy

### Invariants

- `main` is always a completed checkpoint.
- Each module branch starts from `main`.
- Work happens on the active module branch.
- A module branch is never reused after completion.
- Modules are linear.
- No future-module implementation should be added early.
- Each completed module gets a tag named `module-XX-complete`.

### Branch Naming

Use:

```text
module-XX-name
```

Examples:

```text
module-01-java-oops-foundation
module-08-testng-framework-foundation
module-16-cucumber-bdd
```

### Commit Rules

- Use small logical commits.
- Use messages like:

```text
module-01: add Java class and object guide
module-01: add constructor examples
module-01: add exercises
module-01: mark module complete
```

- Keep docs, code, exercises, and progress metadata separate when practical.
- `CLAUDE.md` and `AGENTS.md` updates must be mirrored in the same commit.

## Module Lifecycle

### Starting a Module

1. Ensure `main` is the latest completed checkpoint.
2. Create the module branch:

```bash
git checkout -b module-XX-name
```

3. Update the "Current Module" section in `CLAUDE.md`.
4. Mirror:

```bash
cp CLAUDE.md AGENTS.md
```

5. Commit the metadata update as the first commit on the module branch.

### Working on a Module

1. Write concept docs before or alongside code.
2. Build code in small working increments.
3. Add exercises with hints and expected outcomes.
4. Run the relevant verification commands.
5. Commit each logical unit separately.

### Completing a Module

1. Verify docs exist.
2. Verify implementation matches the module scope.
3. Verify exercises exist.
4. Run tests/checks for the module.
5. Mark the current module `Complete` in `CLAUDE.md`.
6. Mirror `CLAUDE.md` to `AGENTS.md`.
7. Commit the completion metadata.
8. Merge or fast-forward the module branch into `main`.
9. Tag the checkpoint:

```bash
git tag module-XX-complete
```

## Module Map

1. Java OOP Foundation
2. OOP for Selenium
3. First Selenium Tests
4. Locators and Web Elements
5. Waits and Dynamic Elements
6. Forms, Alerts, Dropdowns
7. Windows, Frames, Files, Actions
8. TestNG Framework Foundation
9. Page Object Model
10. Wrapper Methods and Waits
11. Config and Driver Factory
12. Data Driven Testing
13. Listeners, Screenshots, Logging
14. Extent and Allure Reporting
15. Parallel Execution and Selenium Grid
16. Cucumber BDD
17. CI/CD
18. Capstone and Portfolio Packaging

## Learning Doc Standard

Each module should include:

- `00-module-overview.md`.
- focused concept docs.
- code walkthroughs linked to real files once code exists.
- Mermaid diagrams where useful.
- examples before abstractions.
- a "what is intentionally deferred" section.
- `exercises.md` with hints, not full solutions.
- module quality gate.

Docs should teach the user. Do not write thin summaries of generated code.

Match the rich documentation style of the neighboring Playwright learning
project. Each module overview should clearly include:

- what the module adds and why it exists now.
- how it builds on previous modules.
- a `Files Added Or Changed` table with file path, status, and purpose.
- a short list of previous-module files reused by this module, when relevant.
- a dependency map or flow diagram when relationships are easier to see
  visually.
- explicit source ownership: learning example, framework class, test class,
  test data, config, or documentation.
- what is intentionally deferred to later modules.
- a quality gate with exact commands and expected outcomes.

Focused concept docs should reference the exact files they explain. If a doc
discusses a class, method, package, config file, or test, include the current
path so learners can jump from explanation to implementation without guessing.

### Learning Depth Gate

Passing tests is not enough to complete a module. This repository is for deep
SDET learning and interview preparation, so a module is incomplete if its docs
or comments are shallow.

Every module must teach at four levels:

- concept model: what the feature is, why it exists, and the problem it solves.
- code model: how the Java syntax, Selenium API, TestNG API, or framework class
  works in the exact source files.
- nuance model: common mistakes, timing issues, browser behavior, edge cases,
  and tradeoffs a learner must know before using the concept in real projects.
- interview model: likely questions, strong answer framing, and vocabulary the
  learner should be ready to explain.

Focused docs should include these sections where relevant:

- `Mental Model`
- `Code Walkthrough`
- `Java Syntax To Notice`
- `Selenium Or Framework Nuances`
- `Common Mistakes`
- `Interview Readiness`
- `How This Connects To Later Framework Design`
- `Revision Checklist`

Do not write thin docs that only summarize files. Do not mark a module complete
until a learner can revise the topic from the docs without needing the agent to
re-explain it.

### Code Comment Depth

This learning repo intentionally uses richer comments than a normal production
repo. Comments should explain first introductions and non-obvious design
choices, especially:

- constructors, access modifiers, `final`, `static`, records, enums, and
  collection types.
- interfaces, inheritance, polymorphism, encapsulation, abstraction, and
  exception handling.
- generics, lambdas, method references, streams, `Optional`, and assertions.
- `try/finally`, resource cleanup, browser lifecycle, and driver ownership.
- Selenium commands, waits, locators, WebElement state, alerts, frames,
  windows, files, JavaScriptExecutor, Shadow DOM, and Actions.
- framework links such as why duplication exists now and what later modules
  will centralize.

Keep comments readable in source form. Avoid HTML-style JavaDoc markup such as
`<p>`. Avoid noise comments that restate obvious syntax, but err on the side of
explaining concepts when a learner is seeing them for the first time.

## Selenium API Teaching Rules

Whenever a module introduces a Selenium command, class, interface, or browser
concept for the first time, explain it in the docs and add a concise learning
comment at the first meaningful code usage.

Docs should cover:

- what the Selenium API does.
- when the command returns or what browser state it reads.
- common beginner mistakes and gotchas.
- nearby alternatives, such as `get()` vs `navigate().to()`.
- how the API maps to Java/OOP and future framework design.
- what is intentionally not being handled yet.

Code comments should be local and brief:

- comment the first introduction of a Selenium concept/API.
- comment non-obvious lifecycle behavior such as `quit()`, waits, windows,
  frames, alerts, file handling, screenshots, or driver creation.
- avoid repeating the same Selenium explanation in every class after the
  concept has been introduced.
- keep the deeper explanation in docs.

## OOP Teaching Rules

Always explain Java OOP where it appears:

- Classes and objects in page objects.
- Constructors in pages and utilities.
- Encapsulation in private locators and public page actions.
- Inheritance in `BaseTest`.
- Abstraction in page methods and wrapper methods.
- Interfaces and polymorphism in `WebDriver driver = new ChromeDriver()`.
- Exception handling in utility methods.
- Collections in table extraction, element lists, and data-driven tests.

## Framework Direction

Early modules should be explicit and simple.

Final framework direction:

- dynamic `By` locators, not PageFactory.
- page objects do not call `driver.findElement` directly.
- Selenium commands route through wrapper methods.
- waits are centralized.
- config is externalized.
- driver lifecycle is controlled by `DriverFactory`.
- screenshots, logs, and reports are framework services.
- Cucumber step definitions reuse existing page objects and services.

## Code Style Standards

### Readability Over Cleverness

- Prefer straightforward code over clever abstractions.
- Do not add interfaces unless there are genuinely multiple implementations
  or a strong teaching reason.
- Do not introduce design patterns for their own sake.
- Keep methods focused.
- Keep classes focused.

### Naming

- Class names are nouns and intent-revealing.
- Method names are verbs and describe behavior.
- Variable names use full words except common terms such as `url` and `id`.
- Constants use `SCREAMING_SNAKE_CASE`.

### Comments

- This is a learning repo, so comments should teach design intent, Selenium
  concepts, Java/OOP concepts, and framework relationships where useful.
- Prefer class-level JavaDoc that explains the role of the class in the
  learning module and, when relevant, how it maps to future Selenium
  framework code.
- Keep Java comments readable in source form. Avoid HTML-style JavaDoc markup
  such as `<p>` unless there is a strong reason.
- Add short method or block comments for non-obvious behavior, such as
  encapsulation choices, defensive copies, retry logic, click fallback,
  password masking, waits, driver lifecycle, or report attachment behavior.
- Comments should explain why the code is designed that way, not restate
  obvious syntax.
- Avoid noise comments.
- No committed commented-out code.

### Exceptions and Logging

- Do not swallow exceptions silently.
- If catching an exception, either handle it meaningfully or log and rethrow.
- Once logging is introduced, do not use `System.out.println` in framework
  code.
- Before logging is introduced, console output may be used only in beginner
  learning examples.

### Abstractions

- Introduce abstractions only when the module teaches the problem they solve.
- Do not skip directly to the final `selenium-testng-demo` architecture.
- Keep beginner modules beginner-friendly.

## Common Commands

```bash
# Maven
mvn test
mvn test -DsuiteXmlFile=testng.xml
mvn test -Dbrowser=chrome -Dheadless=true

# Allure, once introduced
mvn allure:serve

# Git
git branch
git log --oneline --all --graph
git tag
```

## Knowledge Base References

Use local knowledge-base files as teaching references where relevant:

- Java basics:
  - `../../_KNOWLEDGE_BASE/00_Manual to Automation Mastery/2. Coding Basics for QA/`
  - `../../_KNOWLEDGE_BASE/QA Interview Vault/4. Mini E-Books & Walkthroughs/Java for QA - Basics to Collections (Vault Edition).docx`
- Java OOP:
  - `../../_KNOWLEDGE_BASE/QA Interview Vault/1. Core Interview Q_&A Sets/Java OOP + Collections Q&A - Vault Edition.docx`
  - `../../_KNOWLEDGE_BASE/QA Interview Vault/3. Cheatsheets/Java OOPs Summary - Cheatsheet (Vault Edition).docx`
- Selenium:
  - `../../_KNOWLEDGE_BASE/00_Manual to Automation Mastery/3. Intro to Automation Tools/`
  - `../../_KNOWLEDGE_BASE/00_Manual to Automation Mastery/4. Intermediate Automation Skills/`
  - `../../_KNOWLEDGE_BASE/QA Interview Vault/3. Cheatsheets/Selenium WebDriver - Cheatsheet (Vault Edition).docx`
- TestNG and Maven:
  - `../../_KNOWLEDGE_BASE/QA Interview Vault/1. Core Interview Q_&A Sets/Maven & TestNG Q&A - Vault Edition.docx`
  - `../../_KNOWLEDGE_BASE/QA Interview Vault/3. Cheatsheets/TestNG Annotations - Cheatsheet (Vault Edition).docx`
- Cucumber:
  - `../../_KNOWLEDGE_BASE/QA Interview Vault/1. Core Interview Q_&A Sets/BDD _ Cucumber Q&A - Vault Edition.docx`
  - `../../_KNOWLEDGE_BASE/QA Interview Vault/3. Cheatsheets/Cucumber & Gherkin Syntax - Cheatsheet (Vault Edition).docx`
- Framework design, reporting, CI/CD:
  - `../../_KNOWLEDGE_BASE/00_Manual to Automation Mastery/6. Framework Design and CI-CD/`

## Rules

1. Follow module order.
2. Teach concepts before or alongside code.
3. Keep `CLAUDE.md` and `AGENTS.md` identical.
4. Do not modify `_KNOWLEDGE_BASE/`.
5. Do not skip ahead into future modules.
6. Keep code references in docs accurate.
7. Use reference repositories only as references.
8. Preserve the learning progression over final-framework shortcuts.
