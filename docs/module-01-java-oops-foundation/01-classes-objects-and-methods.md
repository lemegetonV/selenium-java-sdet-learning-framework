# Classes, Objects, Fields, and Methods

## Class

A class is a blueprint. It defines what data an object has and what behavior
the object can perform.

In this module:

```java
public class _01_BrowserSession {
    // fields and methods live here
}
```

`_01_BrowserSession` is not a real browser. It is a Java model that helps explain
how a future Selenium browser session will be represented.

## Object

An object is an instance of a class.

```java
_01_BrowserSession session = new _01_BrowserSession("https://www.saucedemo.com");
```

This line says:

- use the `_01_BrowserSession` blueprint.
- create a real object with `new`.
- store that object in a variable named `session`.

Later in Selenium, this idea becomes:

```java
ChromeDriver driver = new ChromeDriver();
```

The syntax is the same idea: create an object from a class.

## Fields

Fields store object state.

In `_01_BrowserSession`:

```java
private final String browserName;
private final String baseUrl;
private boolean open;
```

These fields answer: what does this object know about itself?

- which browser it represents.
- which URL it targets.
- whether the session is open.

## Methods

Methods define object behavior.

In `_01_BrowserSession`:

```java
public void open() {
    open = true;
}
```

The method changes the object state. The object was closed, then `open()` made
it open.

In Selenium, similar method calls will look like:

```java
driver.get("https://www.saucedemo.com");
driver.quit();
```

The important pattern is:

```text
object.method()
```

## Why This Matters for Frameworks

Page Object Model is just this same idea applied to web pages:

```java
LoginPage loginPage = new LoginPage(driver);
loginPage.login("standard_user", "secret_sauce");
```

The page object will be a class. The `loginPage` variable will hold an object.
The `login` method will perform behavior.

## Java Syntax To Notice

```java
_01_BrowserSession session = new _01_BrowserSession("https://www.saucedemo.com");
```

Read this line in four parts:

- `_01_BrowserSession` on the left is the variable type. It tells Java what
  kind of object the variable can reference.
- `session` is the variable name. Good variable names should describe the role
  the object plays in the test.
- `new` creates an object in memory.
- `_01_BrowserSession(...)` calls a constructor so the object starts with valid
  initial state.

This is the same mental model needed later for:

```java
WebDriver driver = new ChromeDriver(options);
LoginPage loginPage = new LoginPage(driver);
```

The specific class names change, but the Java shape stays the same.

## Common Mistakes

- Thinking a class and object are the same thing. A class is the definition; an
  object is a created instance of that definition.
- Adding all code to `main` because it is the first method that runs. Framework
  code needs focused classes so setup, page behavior, data, and assertions do
  not become tangled.
- Making fields public because it feels easier. Public fields let any caller
  put the object into an invalid state.
- Writing method names that describe implementation instead of intent. For
  example, `login()` is better than `typeUsernameTypePasswordClickButton()` for
  a future page object because it describes the business action.

## Interview Readiness

**Question: What is the difference between a class and an object?**

A class is the blueprint that defines fields and methods. An object is a
runtime instance created from that class with `new`. In automation, a
`LoginPage` class can describe login behavior, while a `loginPage` object is the
actual page helper used by one test.

**Question: Why do Selenium frameworks use many classes instead of one large
test file?**

Separate classes keep responsibilities clear. Driver setup, page behavior,
test data, waits, and assertions change for different reasons, so they should
not all live in one method. This improves readability and makes later changes
safer.

## Revision Checklist

- Can you identify the class name, object variable, constructor call, field,
  and method call in `_01_BrowserSession`?
- Can you explain how `object.method()` maps to `driver.get(...)` and
  `loginPage.login(...)` later?
- Can you explain why fields should be private before the project reaches page
  objects?
