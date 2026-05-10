# Module 18 Interview Review

## Final Framework Pitch

This is a progressive Selenium Java framework. It begins with Java/OOP and raw
Selenium concepts, then evolves into a TestNG framework with Page Objects,
wrapper actions, waits, configuration, data-driven testing, diagnostics,
reporting, parallel execution, Grid support, Cucumber BDD, and GitHub Actions
CI.

## Strong Answers

Why did you use Page Objects?

Page Objects keep page-specific locators and behavior in one place. Tests and
steps can describe business flow without depending on HTML details. If a
locator changes, the Page Object absorbs the change.

Why wrapper methods?

Wrapper methods centralize repeated Selenium mechanics such as waiting,
finding, clicking, typing, display checks, and child-element lookup. They make
Page Objects cleaner and give the framework one place to improve diagnostics or
fallback behavior later.

Why `ThreadLocal` WebDriver?

Parallel TestNG methods can run on different worker threads. A single shared
driver would cause browser sessions to overwrite each other. ThreadLocal keeps
one driver and related services per executing thread.

How does Cucumber fit?

Cucumber is the BDD top layer. Feature files describe behavior, step
definitions bind Gherkin to Java, and the steps reuse existing Page Objects and
framework services. Cucumber does not replace Selenium or Page Objects.

What does CI prove?

CI proves the framework can run from a clean machine with declared
dependencies, headless browser settings, selected test scopes, and uploaded
evidence. It also makes test feedback visible to the team.

## Final Revision Checklist

- I can draw the final architecture from test to WebDriver.
- I can explain each package under `com.learning.framework`.
- I can explain why Page Objects do not create drivers.
- I can explain local versus Grid execution mode.
- I can explain how screenshots reach reports.
- I can explain how data providers feed login tests.
- I can explain how Cucumber and TestNG coexist.
- I can explain which CI scope I would run for a PR versus a release.
