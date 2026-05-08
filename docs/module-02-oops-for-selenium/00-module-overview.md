# Module 02 - OOP for Selenium

## What This Module Adds

Module 02 connects Java OOP concepts from Module 01 to the way Selenium
frameworks are designed.

Module 01 answered: what are classes, objects, fields, methods, constructors,
collections, and static members?

Module 02 answers: how do those same ideas show up when we design browser
automation code?

```mermaid
flowchart LR
    A["Module 01: Java OOP basics"] --> B["Module 02: OOP mapped to Selenium ideas"]
    B --> C["Module 03: first real Selenium tests"]
```

The module still does not launch a real browser. That is intentional. The goal
is to understand the OOP shape before Selenium WebDriver is added in Module 03.

## Why This Module Exists Before Selenium

Real Selenium code often starts with a line like this:

```java
WebDriver driver = new ChromeDriver();
```

That one line contains several OOP ideas:

- `WebDriver` is an interface.
- `ChromeDriver` is a concrete class.
- `driver` is a polymorphic reference.
- the test talks to the browser through a shared abstraction.

If those ideas are unclear, Selenium framework classes such as `BaseTest`,
`LoginPage`, `DriverFactory`, and wrapper methods will feel like memorized
patterns instead of understandable design.

## Files Added Or Changed

| File | Status | Purpose |
| --- | --- | --- |
| `pom.xml` | changed | updates `mvn exec:java` to run the Module 02 demo |
| `README.md` | changed | updates the current module status and commands |
| `src/main/java/com/learning/examples/module02/_01_BrowserDriver.java` | added | interface that models the future `WebDriver` abstraction |
| `src/main/java/com/learning/examples/module02/_02_ChromeBrowserDriver.java` | added | concrete browser implementation, similar in role to `ChromeDriver` |
| `src/main/java/com/learning/examples/module02/_03_FirefoxBrowserDriver.java` | added | second implementation to make polymorphism visible |
| `src/main/java/com/learning/examples/module02/_05_LoginCredentials.java` | added | encapsulated test data object with validation |
| `src/main/java/com/learning/examples/module02/_06_LoginPageModel.java` | added | page-style class that hides login mechanics behind public methods |
| `src/main/java/com/learning/examples/module02/_07_LearningTestTemplate.java` | added | small abstract template showing inheritance without building `BaseTest` yet |
| `src/main/java/com/learning/examples/module02/_08_SauceDemoLoginLearningTest.java` | added | concrete learning test that extends the template |
| `src/main/java/com/learning/examples/module02/_04_InvalidTestDataException.java` | added | custom exception for invalid learning test data |
| `src/main/java/com/learning/examples/module02/_09_Module02Demo.java` | added | runnable demo for interfaces, polymorphism, inheritance, exceptions, and collections |
| `docs/module-02-oops-for-selenium/00-module-overview.md` | added | module map, file ownership, deferred scope, and quality gate |
| `docs/module-02-oops-for-selenium/01-encapsulation-and-abstraction.md` | added | explains private data and public actions in Selenium-style design |
| `docs/module-02-oops-for-selenium/02-inheritance-interfaces-polymorphism.md` | added | explains Selenium's `WebDriver driver = new ChromeDriver()` pattern |
| `docs/module-02-oops-for-selenium/03-exception-handling-and-collections.md` | added | explains validation, custom exceptions, and lists in automation |
| `docs/module-02-oops-for-selenium/99-interview-review.md` | added | interview-ready OOP and Selenium-design revision notes |
| `docs/module-02-oops-for-selenium/exercises.md` | added | practice tasks with hints and expected outcomes |

## Previous Module Files Reused

Module 02 does not import Module 01 classes directly. It builds on the same
concepts and keeps the old examples available for comparison:

- `src/main/java/com/learning/examples/module01/_01_BrowserSession.java`
- `src/main/java/com/learning/examples/module01/_02_LoginAttempt.java`
- `src/main/java/com/learning/examples/module01/_03_TestCaseSummary.java`
- `src/main/java/com/learning/examples/module01/_04_Module01Demo.java`

## Source Ownership

All Module 02 source code lives under:

```text
src/main/java/com/learning/examples/module02/
```

These files are learning examples, not reusable framework classes. Real
framework packages under `com.learning.framework` are still deferred until the
curriculum reaches framework construction.

## Module 02 Dependency Map

```mermaid
classDiagram
    class _01_BrowserDriver {
        <<interface>>
        +getBrowserName()
        +open(baseUrl)
        +close()
        +isOpen()
    }
    class _02_ChromeBrowserDriver
    class _03_FirefoxBrowserDriver
    class _05_LoginCredentials
    class _06_LoginPageModel
    class _07_LearningTestTemplate {
        <<abstract>>
        +run()
        #executeTest()
    }
    class _08_SauceDemoLoginLearningTest
    class _09_Module02Demo

    _01_BrowserDriver <|.. _02_ChromeBrowserDriver
    _01_BrowserDriver <|.. _03_FirefoxBrowserDriver
    _07_LearningTestTemplate <|-- _08_SauceDemoLoginLearningTest
    _08_SauceDemoLoginLearningTest --> _01_BrowserDriver
    _08_SauceDemoLoginLearningTest --> _05_LoginCredentials
    _08_SauceDemoLoginLearningTest --> _06_LoginPageModel
    _09_Module02Demo --> _01_BrowserDriver
    _09_Module02Demo --> _08_SauceDemoLoginLearningTest
```

## What Is Intentionally Deferred

Module 02 does not add:

- Selenium WebDriver dependency.
- real `ChromeDriver` browser launch.
- TestNG dependency.
- real assertions.
- `BaseTest`.
- Page Object Model implementation.
- framework wrappers, waits, config, reports, or screenshots.

Those pieces start in later modules after the raw OOP mapping is clear.

## Quality Gate

Run:

```bash
mvn compile
mvn exec:java
```

Expected outcome:

- the project compiles with Java 21.
- `mvn exec:java` runs `_09_Module02Demo`.
- the console output shows the same login learning flow executed through two
  different browser implementations.

## Readiness Standard

Before Module 03 introduces real Selenium WebDriver, a learner should be able
to explain why this module created fake browser drivers first:

- interfaces let code depend on behavior instead of one implementation.
- polymorphism is the reason `WebDriver driver = new ChromeDriver()` works.
- inheritance can share lifecycle steps, but it should not become a dumping
  ground for unrelated utilities.
- encapsulation is the reason page-style classes expose actions instead of
  page internals.
- custom exceptions make invalid test data fail with a meaningful message.

Use `99-interview-review.md` to revise these points before starting the first
real Selenium tests.
