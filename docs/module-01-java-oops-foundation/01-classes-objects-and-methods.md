# Classes, Objects, Fields, and Methods

## Class

A class is a blueprint. It defines what data an object has and what behavior
the object can perform.

In this module:

```java
public class BrowserSession {
    // fields and methods live here
}
```

`BrowserSession` is not a real browser. It is a Java model that helps explain
how a future Selenium browser session will be represented.

## Object

An object is an instance of a class.

```java
BrowserSession session = new BrowserSession("https://www.saucedemo.com");
```

This line says:

- use the `BrowserSession` blueprint.
- create a real object with `new`.
- store that object in a variable named `session`.

Later in Selenium, this idea becomes:

```java
ChromeDriver driver = new ChromeDriver();
```

The syntax is the same idea: create an object from a class.

## Fields

Fields store object state.

In `BrowserSession`:

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

In `BrowserSession`:

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
