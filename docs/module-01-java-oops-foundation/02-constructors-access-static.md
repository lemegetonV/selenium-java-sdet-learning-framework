# Constructors, Access Modifiers, and Static Members

## Constructors

A constructor prepares a new object.

[src/main/java/com/learning/examples/module01/_01_BrowserSession.java](../../src/main/java/com/learning/examples/module01/_01_BrowserSession.java)
has two constructors:

```java
public _01_BrowserSession(String baseUrl) {
    this(DEFAULT_BROWSER, baseUrl);
}

public _01_BrowserSession(String browserName, String baseUrl) {
    this.browserName = browserName;
    this.baseUrl = baseUrl;
    this.open = false;
    createdSessionCount++;
}
```

The first constructor is a convenience constructor. It assumes the default
browser.

The second constructor gives full control over the browser and URL.

Later, a Selenium Page Object constructor will look similar:

```java
public LoginPage(WebDriver driver) {
    this.driver = driver;
}
```

That constructor will prepare the page object with the browser driver it needs.

## `this`

`this` means the current object.

```java
this.browserName = browserName;
```

The left side is the field on the object. The right side is the constructor
parameter.

## Access Modifiers

Access modifiers control what other code can reach.

| Modifier | Meaning |
| --- | --- |
| `public` | reachable from other classes |
| `private` | reachable only inside the same class |

In
[src/main/java/com/learning/examples/module01/_02_LoginAttempt.java](../../src/main/java/com/learning/examples/module01/_02_LoginAttempt.java):

```java
private final String username;
private final String password;
```

The fields are private so other classes cannot modify them directly.

Instead, other classes use public methods:

```java
public boolean isReadyToSubmit() {
    return hasUsername() && hasPassword();
}
```

This is the beginning of encapsulation. The object protects its data and
offers controlled behavior.

## Static Members

Static members belong to the class, not to one object.

In
[src/main/java/com/learning/examples/module01/_01_BrowserSession.java](../../src/main/java/com/learning/examples/module01/_01_BrowserSession.java):

```java
public static final String DEFAULT_BROWSER = "chrome";
private static int createdSessionCount;
```

`DEFAULT_BROWSER` is shared by all `_01_BrowserSession` objects.

`createdSessionCount` tracks how many sessions have been created.

Later, framework utilities may use static methods for helpers, but we will be
careful. Static is useful, but overusing it can make code harder to test and
change.

## Java Syntax To Notice

```java
public _01_BrowserSession(String baseUrl) {
    this(DEFAULT_BROWSER, baseUrl);
}
```

`this(...)` calls another constructor in the same class. It must be the first
statement in the constructor. This avoids duplicating setup logic in multiple
constructors.

```java
private final String username;
```

`private` means only the class can access the field directly. `final` means the
field reference is assigned once in the constructor and cannot be reassigned
later. Together, they make simple data objects easier to reason about.

```java
public static final String DEFAULT_BROWSER = "chrome";
```

This is a constant. `static` means the value belongs to the class, not one
object. `final` means the constant cannot be reassigned. `SCREAMING_SNAKE_CASE`
is the standard Java naming style for constants.

## Nuances For Automation Design

- Constructors should leave an object ready to use. A page object constructor
  should receive the dependencies it needs, such as `WebDriver`.
- Access modifiers are a design tool, not just syntax. Private fields prevent
  random test code from corrupting page object or config state.
- Static constants are useful for stable defaults. Static mutable state is more
  dangerous because tests may influence one another through shared data.
- A future `DriverFactory` must be careful with static fields when parallel
  execution is introduced. Module 01 only shows the simple concept.

## Interview Readiness

**Question: What is a constructor?**

A constructor is special code that runs when an object is created. It assigns
the initial state required before methods can safely use the object.

**Question: Why are fields usually private?**

Private fields support encapsulation. The class controls how its state changes
through public methods, so callers cannot accidentally create invalid state.

**Question: What is the difference between instance and static members?**

Instance members belong to one object. Static members belong to the class and
are shared across all objects. In test automation, static constants can be fine,
but mutable static state must be handled carefully.

## Revision Checklist

- Can you explain `this.browserName = browserName`?
- Can you explain why `createdSessionCount` is static but `open` is not?
- Can you explain why `DEFAULT_BROWSER` is `public static final`?
