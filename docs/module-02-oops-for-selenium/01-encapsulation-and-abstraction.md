# Encapsulation and Abstraction

## Encapsulation

Encapsulation means an object protects its internal data and exposes safe
behavior through methods.

In Module 02, `_05_LoginCredentials` stores login test data:

```text
src/main/java/com/learning/examples/module02/_05_LoginCredentials.java
```

The username and password are private. Other classes cannot rewrite those
fields directly.

Instead, callers use public methods:

```java
credentials.getUsername();
credentials.getMaskedPassword();
```

That is the same direction a Selenium framework takes later:

- locators become private inside page objects.
- test classes call public page actions.
- framework internals decide how to click, type, wait, and report.

## Why Private Data Matters

In a test framework, uncontrolled data access creates fragile code.

If every test directly manipulates a page's locators or a driver's lifecycle,
one UI change can break many files. Encapsulation keeps responsibility in the
class that owns the detail.

Module 02 uses this same idea in a beginner-friendly way:

| Class | Encapsulated Detail | Public Behavior |
| --- | --- | --- |
| `_05_LoginCredentials` | raw username and password | safe getters and masked password output |
| `_06_LoginPageModel` | simulated login field names and action order | `loginWith(...)` |
| `_07_LearningTestTemplate` | setup and cleanup sequence | `run()` |

## Abstraction

Abstraction means callers work with meaningful actions instead of low-level
steps.

`_06_LoginPageModel` is not a real Selenium page object yet, but it introduces the
same design idea:

```text
src/main/java/com/learning/examples/module02/_06_LoginPageModel.java
```

The demo does not ask callers to know every login step. It exposes:

```java
loginPage.loginWith(credentials);
```

Later, a real Selenium page object will hide lower-level code such as:

```java
driver.findElement(usernameLocator).sendKeys(username);
driver.findElement(passwordLocator).sendKeys(password);
driver.findElement(loginButtonLocator).click();
```

Module 02 keeps those actions simulated because real Selenium element
interaction starts in Module 03 and Module 04.

## OOP Mapping to Future Selenium

| Module 02 Learning Example | Future Selenium Framework Equivalent |
| --- | --- |
| `_05_LoginCredentials` | test data object or DataProvider row |
| `_06_LoginPageModel` | `LoginPage` page object |
| private fields | private locators and private helper state |
| public `loginWith(...)` method | public page action such as `loginAs(...)` |
| masked password output | logging/reporting rule to avoid exposing secrets |

## Key Takeaways

- Encapsulation keeps data and details controlled.
- Abstraction gives callers a simple action name.
- Page objects are not magic; they are classes that apply these two OOP ideas.
- Module 02 teaches the shape before Module 03 adds real WebDriver code.

## Java Syntax To Notice

```java
private final String usernameField = "username field";
```

The field is private because callers should not depend on page internals.
Later, this idea becomes private Selenium locators such as:

```java
private final By usernameInput = By.id("user-name");
```

```java
public List<String> loginWith(_05_LoginCredentials credentials)
```

The method is public because it represents a page action the test is allowed to
perform. The return type is `List<String>` because this module is still
simulating steps instead of driving a real browser.

## Nuances For Page Object Design

- A page object should expose user-intent methods, not every tiny Selenium
  command. `loginWith(credentials)` is easier to understand than separate test
  calls for username field, password field, and button internals.
- Encapsulation does not mean hiding everything from the learner. It means the
  class owns details that should change together.
- Abstraction should not arrive before the learner understands the lower-level
  actions. That is why real page objects are deferred until after raw Selenium
  modules.

## Interview Readiness

**Question: What is encapsulation in a Selenium framework?**

Encapsulation means keeping page details such as locators and low-level actions
inside page or framework classes, while tests call clear public methods such as
`loginWith(...)` or `addProductToCart(...)`.

**Question: What is abstraction in page objects?**

Abstraction means exposing meaningful business actions and hiding mechanical
steps. A test should read like behavior, not like a sequence of DOM operations.

## Revision Checklist

- Can you explain why the simulated fields in `_06_LoginPageModel` are private?
- Can you explain why `loginWith(...)` is public?
- Can you explain what would be premature if we created a real page object in
  Module 02?
