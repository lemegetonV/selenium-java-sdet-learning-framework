# Module 02 Interview Review

## What You Must Be Able To Explain

Module 02 connects plain Java OOP to Selenium framework design. You should be
able to explain:

- interface vs concrete class.
- inheritance vs polymorphism.
- why `WebDriver driver = new ChromeDriver()` is an OOP line.
- why page-style classes hide internals behind public actions.
- why invalid test data should fail early.
- why a real `BaseTest` is intentionally deferred.

## Strong Answers

**What is an interface?**

An interface defines a contract. It says what behavior an object must provide
without deciding how the behavior is implemented. Selenium's `WebDriver`
interface defines browser behavior that ChromeDriver, FirefoxDriver, remote
drivers, and other implementations can provide.

**What is polymorphism?**

Polymorphism lets code use a common reference type for different concrete
objects. In Selenium, the test can use `WebDriver` while the runtime object is
`ChromeDriver`, `FirefoxDriver`, or another driver.

**Why should page details be private?**

Tests should express intent, not page mechanics. If locators and low-level
steps are private inside a page object, the page can change without forcing
every test to change.

**What is the risk of putting too much into `BaseTest`?**

A bloated `BaseTest` becomes a hidden dependency for every test. It should own
shared lifecycle and setup, not unrelated page actions, assertions, data logic,
or reporting shortcuts.

## Code Lines To Revise

```java
public interface _01_BrowserDriver
```

This creates a behavior contract, similar in idea to Selenium's `WebDriver`.

```java
List<_01_BrowserDriver> browsers = List.of(...)
```

The list accepts any object that implements the interface. That is why one loop
can execute the same learning flow with Chrome-style and Firefox-style drivers.

```java
protected abstract void executeTest();
```

The parent class controls the lifecycle, but the child class supplies the
specific test behavior.

```java
throw new _04_InvalidTestDataException(...)
```

This fails early with a domain-specific message instead of letting bad data
cause a confusing downstream failure.

## Common Interview Traps

- Saying inheritance and polymorphism are the same. Inheritance is a class
  relationship. Polymorphism is using one reference type for multiple concrete
  implementations.
- Creating interfaces when there is only one real implementation and no design
  need.
- Claiming page objects are only for reducing duplicate locators. They also
  improve readability, encapsulation, and change isolation.
- Catching exceptions too broadly and hiding real test failures.

## Connection To Future Framework Modules

Module 03 introduces real WebDriver. Module 08 introduces shared TestNG setup.
Module 09 introduces real page objects. Module 02 prepares for those modules by
making the OOP vocabulary explicit before the Selenium API adds more moving
parts.
