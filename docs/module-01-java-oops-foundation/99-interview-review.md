# Module 01 Interview Review

## What You Must Be Able To Explain

Module 01 is not about Selenium commands yet. It is about the Java vocabulary
that makes Selenium framework code readable later.

You should be able to explain:

- class vs object.
- field vs local variable.
- constructor vs method.
- private state and public behavior.
- `this`, `final`, `static`, constants, and access modifiers.
- `List`, `ArrayList`, `Map`, loops, and defensive copies.
- why a Selenium framework should be split into focused classes.

## Strong Answers

**What is a class?**

A class is a blueprint that defines state and behavior. In automation, a class
can model a browser session, login data, page object, driver factory, or test
data reader.

**What is an object?**

An object is a runtime instance of a class. The line
`_01_BrowserSession session = new _01_BrowserSession(...)` creates an object and
stores a reference to it in the `session` variable.

**Why do we use constructors?**

Constructors make sure an object starts with the data it needs. A later
`LoginPage(WebDriver driver)` constructor will make sure the page object has a
browser driver before any page action method is called.

**Why should fields be private?**

Private fields protect object state. Other classes should ask the object to do
something through methods instead of changing its data directly. That is the
foundation of page object design.

**What is the risk of static state?**

Static state is shared across the class. Static constants are usually fine, but
mutable static values can cause tests to affect one another, especially when
parallel execution is introduced.

## Code Lines To Revise

```java
private final String baseUrl;
```

This means the object owns a URL value that is assigned once and cannot be
changed directly from outside the class.

```java
this(DEFAULT_BROWSER, baseUrl);
```

This calls another constructor in the same class and avoids duplicated setup.

```java
return List.copyOf(steps);
```

This returns a defensive copy so callers cannot mutate the object's internal
list.

```java
Map<String, String> environment = new LinkedHashMap<>();
```

This creates an ordered key-value collection, similar to how later framework
config will store `browser`, `baseUrl`, and `headless`.

## Common Interview Traps

- Saying a class is an object. A class is the definition; an object is the
  created instance.
- Saying `final` makes an object immutable. `final` prevents reassignment of the
  variable or field. Full immutability also depends on the object's contents.
- Saying static means constant. Static means class-level. A static value can be
  mutable unless it is also designed as a constant.
- Forgetting that constructors do not have a return type.
- Exposing internal collections directly from getters.

## Connection To Future Framework Modules

Module 08 will introduce shared test setup. Module 09 will introduce page
objects. Module 11 will introduce driver/config ownership. Those topics are all
applications of Module 01 ideas:

- objects own state.
- constructors receive required dependencies.
- public methods expose intent.
- private fields hide implementation details.
- collections represent repeated data.

If Module 01 feels unclear, later framework code will feel like ceremony. If it
is clear, the framework phase becomes a natural extension of Java basics.
