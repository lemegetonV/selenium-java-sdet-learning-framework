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

[src/main/java/com/learning/examples/module02/_01_BrowserDriver.java](../../src/main/java/com/learning/examples/module02/_01_BrowserDriver.java)

It says what browser-like objects must be able to do:

- return a browser name.
- open a URL.
- close.
- report whether they are open.

The interface does not say how Chrome or Firefox should do those things. It
only defines the common contract.

## Concrete Classes

Two classes implement the interface:

- [src/main/java/com/learning/examples/module02/_02_ChromeBrowserDriver.java](../../src/main/java/com/learning/examples/module02/_02_ChromeBrowserDriver.java)
- [src/main/java/com/learning/examples/module02/_03_FirefoxBrowserDriver.java](../../src/main/java/com/learning/examples/module02/_03_FirefoxBrowserDriver.java)

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

[src/main/java/com/learning/examples/module02/_09_Module02Demo.java](../../src/main/java/com/learning/examples/module02/_09_Module02Demo.java)

The loop can run the same learning test against Chrome-style and
Firefox-style drivers because both implement `_01_BrowserDriver`.

This is why framework code usually depends on `WebDriver`, not `ChromeDriver`
directly. It keeps the test design flexible for multiple browsers.

## Inheritance

Inheritance means one class can reuse and specialize behavior from another
class.

Module 02 uses:

- [src/main/java/com/learning/examples/module02/_07_LearningTestTemplate.java](../../src/main/java/com/learning/examples/module02/_07_LearningTestTemplate.java)
- [src/main/java/com/learning/examples/module02/_08_SauceDemoLoginLearningTest.java](../../src/main/java/com/learning/examples/module02/_08_SauceDemoLoginLearningTest.java)

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

## Java Syntax To Notice

```java
public interface _01_BrowserDriver {
    void open(String baseUrl);
}
```

An interface method has no body here. It defines the behavior every
implementation must provide.

```java
public class _02_ChromeBrowserDriver implements _01_BrowserDriver
```

`implements` means the class promises to provide concrete code for the
interface methods.

```java
List<_01_BrowserDriver> browsers = List.of(
        new _02_ChromeBrowserDriver(),
        new _03_FirefoxBrowserDriver()
);
```

The list type is the interface, so the same collection can hold multiple
implementations. This is the exact skill needed when a framework runs the same
test against Chrome, Firefox, or remote Grid sessions.

```java
protected abstract void executeTest();
```

`abstract` means the parent class declares the method but does not implement
it. A child class must supply the test-specific behavior.

## Framework Nuances

- Prefer depending on interfaces or stable abstractions when multiple
  implementations are realistic. That is why tests usually refer to
  `WebDriver`, not `ChromeDriver`.
- Do not create interfaces for every class. An interface has value when it
  captures a real contract, such as multiple browser implementations.
- Inheritance is useful for lifecycle, but overusing inheritance creates rigid
  test hierarchies. Later modules should keep `BaseTest` focused on setup and
  cleanup, not every helper method.
- `final` on `run()` in the template prevents child classes from changing the
  lifecycle order. That is a deliberate design choice.

## Interview Readiness

**Question: Why do we write `WebDriver driver = new ChromeDriver()` instead of
`ChromeDriver driver = new ChromeDriver()`?**

Using the `WebDriver` interface keeps the test code focused on common browser
behavior. The concrete implementation can be Chrome, Firefox, Edge, or a remote
driver without changing the higher-level test logic.

**Question: What is polymorphism in Selenium?**

Polymorphism means the same `WebDriver` reference can point to different
browser implementation objects. The test calls common methods such as `get`,
`findElement`, and `quit` without caring about the concrete browser class.

**Question: Is inheritance always good for test frameworks?**

No. Inheritance is useful for shared lifecycle, but too much inheritance hides
behavior and makes tests hard to change. Use it carefully and only when it
solves visible duplication.

## Revision Checklist

- Can you point to the interface, the concrete classes, and the polymorphic
  reference in Module 02?
- Can you explain why Module 02 does not create the real `BaseTest` yet?
- Can you explain how `_07_LearningTestTemplate` prepares for TestNG setup and
  cleanup without jumping into framework implementation?
