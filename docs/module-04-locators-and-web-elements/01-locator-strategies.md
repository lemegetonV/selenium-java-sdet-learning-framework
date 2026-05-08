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
src/test/java/com/learning/tests/learning/_04_LocatorStrategyTest.java
src/test/java/com/learning/tests/learning/_05_LinkLocatorTest.java
```

Because `src/test/java/com/learning/tests/learning/` is a shared package, the
class prefixes continue from Module 03. Module 04 starts at `_04_`, not
`_01_`.

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

## Locator Template Intro

A locator template is the repeatable syntax shape behind a selector. Templates
help learners understand what they are writing instead of copying selectors
blindly from browser dev tools.

Common CSS templates:

| Template | Example | Meaning |
| --- | --- | --- |
| `tag` | `input` | all elements with that tag |
| `#id` | `#user-name` | element with an id |
| `.class` | `.login_logo` | element with one class token |
| `tag[attribute='value']` | `input[data-test='login-button']` | tag with exact attribute value |
| `parent child` | `form input` | descendant inside another element |

Common XPath templates:

| Template | Example | Meaning |
| --- | --- | --- |
| `//tag` | `//input` | any matching tag in the document |
| `//tag[@attribute='value']` | `//input[@id='login-button']` | tag with exact attribute value |
| `//*[text()='value']` | `//*[text()='Login']` | exact visible text |
| `//*[contains(text(),'value')]` | `//*[contains(text(),'Log')]` | partial visible text |
| `//*[contains(@attribute,'value')]` | `//*[contains(@class,'btn')]` | partial attribute value |

Use templates as a thinking tool. The final locator should still be checked
against the actual page and the behavior under test.

## Locator Stability

A good locator should be:

- unique enough to find the intended element.
- stable across UI styling changes.
- readable to another engineer.
- scoped to the behavior under test.

For this project, prefer stable attributes such as `id`, `name`, or
`data-test` when available.

## Locator Best Practices

Prefer this order of thinking:

1. Use a stable, unique, app-owned attribute if available, such as `id`,
   `data-test`, `data-testid`, or `data-qa`.
2. Use CSS for clear attribute relationships and simple parent-child
   structures.
3. Use XPath when text matching, DOM relationships, or axes make the intent
   clearer than CSS.
4. Scope the locator to a parent element when the page has repeated cards,
   forms, rows, or panels.

Avoid:

- absolute XPath copied from dev tools, such as `/html/body/div/...`.
- styling-only class names when the style is not the behavior under test.
- broad locators that depend on `findElement` returning the first match.
- hidden coupling to text that product owners frequently rename.

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

## Chained Locators

`_04_LocatorStrategyTest` also introduces chained or scoped locators:

```java
WebElement loginContainer = driver.findElement(By.id("login_button_container"));
WebElement usernameInput = loginContainer.findElement(By.cssSelector("input[data-test='username']"));
```

The second lookup starts from `loginContainer`, not from the full page. This
matters when the page has repeated structures, such as product cards, table
rows, or multiple forms with similar child elements.

## Common Beginner Mistakes

- using `By.className("btn primary")`; className accepts one class token.
- using a broad tag name with `findElement`, which returns only the first
  match.
- choosing an XPath copied from browser dev tools without checking stability.
- using visible text that changes across environments or translations.
- treating locator choice as a framework abstraction too early.

## Java And Selenium Syntax To Notice

```java
WebElement usernameInput = driver.findElement(By.id("user-name"));
```

`By.id("user-name")` creates a locator object. `findElement(...)` sends that
locator to the browser session and returns a `WebElement` reference for the
first matching element.

```java
List<WebElement> inputElements = driver.findElements(By.tagName("input"));
```

The return type is `List<WebElement>` because the page may contain many
matching elements. This is the first bridge from Java generics to Selenium
element collections.

## Interview Readiness

**Question: Which locator is best in Selenium?**

There is no universal best locator. Prefer stable, unique, app-owned attributes
such as `id` or `data-test`. Use CSS for readable attribute relationships. Use
XPath when DOM relationships, text, or axes make the intent clearer.

**Question: Why should we avoid absolute XPath?**

Absolute XPath depends on exact DOM nesting. Small layout changes can break it
even when the user-facing behavior is unchanged.

**Question: What makes a locator maintainable?**

It should be stable, readable, unique enough, scoped to the behavior under
test, and resistant to cosmetic UI changes.

## Revision Checklist

- Can you explain every locator strategy in `_04_LocatorStrategyTest`?
- Can you explain why `data-test` is automation-friendly?
- Can you explain when XPath is justified instead of CSS?
