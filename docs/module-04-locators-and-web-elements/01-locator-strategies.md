# Locator Strategies

## What A Locator Does

A Selenium locator tells WebDriver how to find an element.

The Java class is:

```java
By
```

The lookup shape is:

```java
WebElement element = driver.findElement(By.id("user-name"));
```

Module 04 introduces locators in:

```text
src/test/java/com/learning/tests/learning/_01_LocatorStrategyTest.java
src/test/java/com/learning/tests/learning/_02_LinkLocatorTest.java
```

## Locator Strategy Table

| Strategy | Example | Good Use | Gotcha |
| --- | --- | --- | --- |
| `By.id` | `By.id("user-name")` | stable unique IDs | some apps generate dynamic IDs |
| `By.name` | `By.name("password")` | form fields | names may not be unique |
| `By.className` | `By.className("login_logo")` | one stable class token | cannot pass compound classes with spaces |
| `By.tagName` | `By.tagName("input")` | finding groups of common elements | often too broad for one element |
| `By.linkText` | `By.linkText("Checkboxes")` | exact visible link text | breaks if text changes |
| `By.partialLinkText` | `By.partialLinkText("Dropdown")` | quick partial link match | can match the wrong link if ambiguous |
| `By.cssSelector` | `By.cssSelector("input[data-test='login-button']")` | stable attributes and readable selectors | CSS cannot navigate upward to parents |
| `By.xpath` | `By.xpath("//input[@id='login-button']")` | complex relationships when CSS is weak | XPath can become brittle if overused |

## Locator Stability

A good locator should be:

- unique enough to find the intended element.
- stable across UI styling changes.
- readable to another engineer.
- scoped to the behavior under test.

For this project, prefer stable attributes such as `id`, `name`, or
`data-test` when available.

## Why CSS Is Often Preferred

CSS selectors are readable and flexible:

```java
By.cssSelector("input[data-test='login-button']")
```

This means:

- find an `input`.
- whose `data-test` attribute is `login-button`.

SauceDemo uses `data-test` attributes, which are intentionally automation
friendly.

## Why XPath Is Not The Default

XPath is powerful, but it can become hard to read and easy to break.

Use XPath when it solves a real locator problem, not as the first habit for
every element.

Module 04 includes XPath because learners should recognize it, but the final
framework direction prefers dynamic `By` locators with readable selector
choices.

## Common Beginner Mistakes

- using `By.className("btn primary")`; className accepts one class token.
- using a broad tag name with `findElement`, which returns only the first
  match.
- choosing an XPath copied from browser dev tools without checking stability.
- using visible text that changes across environments or translations.
- treating locator choice as a framework abstraction too early.
