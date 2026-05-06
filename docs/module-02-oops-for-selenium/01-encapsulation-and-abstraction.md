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
