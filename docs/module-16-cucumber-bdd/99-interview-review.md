# Module 16 Interview Review

## Core Vocabulary

- BDD: behavior-driven development, focused on shared examples of behavior.
- Gherkin: plain-language syntax used by Cucumber feature files.
- Feature: a business capability under test.
- Scenario: one concrete example of behavior.
- Scenario Outline: one behavior executed with multiple example rows.
- Step definition: Java method bound to a Gherkin sentence.
- Glue: packages where Cucumber searches for hooks and step definitions.
- Hook: setup or teardown method around Cucumber scenarios.
- Tag: scenario metadata used for filtering and organization.
- DataTable: structured step input passed from Gherkin to Java.

## Strong Answers

What is Cucumber?

Cucumber is a BDD tool that runs Gherkin scenarios by matching each step to a
step definition. It is not a browser automation tool. In UI automation,
Cucumber usually sits above Selenium and a test framework such as TestNG or
JUnit.

What does `AbstractTestNGCucumberTests` do?

It adapts Cucumber scenarios into TestNG tests. This lets Maven Surefire,
TestNG suites, TestNG reporting, and TestNG-compatible CI execution run
Cucumber scenarios.

Where should Selenium code live in a Cucumber framework?

Selenium code should stay in Page Objects, wrapper methods, waits, and driver
services. Step definitions should call those abstractions instead of locating
elements directly.

Why do we use hooks?

Hooks run before or after scenarios. In UI automation, they are commonly used
to create a browser, reset state, capture failure evidence, and quit the
browser.

What is the risk of overusing Cucumber?

If non-technical stakeholders do not read feature files, Cucumber can become an
extra maintenance layer. It is valuable when scenarios express business rules
clearly and step definitions remain reusable.

## Revision Checklist

- I can explain how a feature file reaches Selenium code.
- I can explain why Cucumber does not replace Page Objects.
- I can explain `Scenario` vs `Scenario Outline`.
- I can explain how `Examples` values replace placeholders.
- I can explain why hooks are needed for browser lifecycle.
- I can explain why scenario state must not be stored in one static driver.
- I can explain how tags help CI/CD select test subsets.
