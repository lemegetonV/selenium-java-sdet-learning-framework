# Constructors, Access Modifiers, and Static Members

## Constructors

A constructor prepares a new object.

`_01_BrowserSession` has two constructors:

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

In `_02_LoginAttempt`:

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

In `_01_BrowserSession`:

```java
public static final String DEFAULT_BROWSER = "chrome";
private static int createdSessionCount;
```

`DEFAULT_BROWSER` is shared by all `_01_BrowserSession` objects.

`createdSessionCount` tracks how many sessions have been created.

Later, framework utilities may use static methods for helpers, but we will be
careful. Static is useful, but overusing it can make code harder to test and
change.
