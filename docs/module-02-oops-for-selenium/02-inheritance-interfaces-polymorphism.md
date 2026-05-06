# Inheritance, Interfaces, and Polymorphism

## The Selenium Line We Are Preparing For

Many Selenium examples start with:

```java
WebDriver driver = new ChromeDriver();
```

This is not just Selenium syntax. It is OOP:

- `WebDriver` is an interface.
- `ChromeDriver` is a class that implements that interface.
- the variable `driver` can hold any object that behaves like a `WebDriver`.

Module 02 models that idea with compileable learning code before real Selenium
is added.

## Interface

`_01_BrowserDriver` is the Module 02 learning interface:

```text
src/main/java/com/learning/examples/module02/_01_BrowserDriver.java
```

It says what browser-like objects must be able to do:

- return a browser name.
- open a URL.
- close.
- report whether they are open.

The interface does not say how Chrome or Firefox should do those things. It
only defines the common contract.

## Concrete Classes

Two classes implement the interface:

```text
src/main/java/com/learning/examples/module02/_02_ChromeBrowserDriver.java
src/main/java/com/learning/examples/module02/_03_FirefoxBrowserDriver.java
```

They are intentionally simple. They do not launch real browsers. Their purpose
is to make this design visible:

```java
_01_BrowserDriver browser = new _02_ChromeBrowserDriver();
```

That maps directly to the future Selenium idea:

```java
WebDriver driver = new ChromeDriver();
```

## Polymorphism

Polymorphism means one reference type can point to different concrete objects.

In `_09_Module02Demo`, a list stores both browser implementations:

```text
src/main/java/com/learning/examples/module02/_09_Module02Demo.java
```

The loop can run the same learning test against Chrome-style and
Firefox-style drivers because both implement `_01_BrowserDriver`.

This is why framework code usually depends on `WebDriver`, not `ChromeDriver`
directly. It keeps the test design flexible for multiple browsers.

## Inheritance

Inheritance means one class can reuse and specialize behavior from another
class.

Module 02 uses:

```text
src/main/java/com/learning/examples/module02/_07_LearningTestTemplate.java
src/main/java/com/learning/examples/module02/_08_SauceDemoLoginLearningTest.java
```

`_07_LearningTestTemplate` owns the test sequence:

1. setup.
2. execute the test-specific behavior.
3. cleanup.

`_08_SauceDemoLoginLearningTest` fills in the test-specific behavior.

This prepares for a future `BaseTest`, but it is not a real `BaseTest` yet.
Module 08 introduces that after raw Selenium tests have shown the duplication
that shared setup solves.

## Why We Do Not Build `BaseTest` Yet

Building a full `BaseTest` now would skip the learning progression.

The learner first needs to see:

- raw browser setup in Module 03.
- repeated locator and interaction code in Modules 04 to 07.
- repeated setup and teardown becoming painful.

Only then does `BaseTest` solve a visible problem.

Module 02 only teaches the inheritance concept in a small example.

## Key Takeaways

- Interfaces define behavior without choosing one implementation.
- Concrete classes provide specific implementations.
- Polymorphism lets the same code work with different implementations.
- Inheritance can share setup/cleanup flow, but framework inheritance should
  be introduced only when the duplication is visible.
