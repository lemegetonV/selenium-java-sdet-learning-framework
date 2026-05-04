# Selenium Java UI Automation Learning Framework

This repository is a progressive Selenium Java learning project. It starts
with Java and OOP foundations, then gradually evolves into a TestNG,
Page Object Model, reporting, data-driven, Cucumber, and CI/CD framework.

## Current Status

- Latest completed module: Module 02 - OOP for Selenium
- Current branch checkpoint: Module 02 complete
- Next module: Module 03 - First Selenium Tests
- Primary AUT for later framework modules: `https://www.saucedemo.com`
- Selenium concept playground for later modules: `https://the-internet.herokuapp.com`

## How to Use This Repo

Each module has:

- concept docs under `docs/module-XX-name/`
- small code examples or framework code
- exercises with hints
- a completion checkpoint tag named `module-XX-complete`

The final framework is not built all at once. Each module adds only the next
layer needed for learning.

## Current Module Commands

```bash
mvn compile
mvn exec:java
```

On the Module 02 branch, `mvn exec:java` runs the OOP for Selenium demo.
At the Module 01 checkpoint, the same command runs the Java OOP foundation
demo from that branch/tag.
