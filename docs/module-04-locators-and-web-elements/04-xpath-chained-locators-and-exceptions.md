# XPath, Chained Locators, and Locator Exceptions

## Why This Exists In Module 04

Module 04 is the right place to slow down on locator design. Later page
objects and wrapper methods will hide repeated lookup code, but they cannot
fix weak locator thinking.

The examples in this doc refer to:

```text
src/test/java/com/learning/tests/learning/_04_LocatorStrategyTest.java
```

## XPath Basics

XPath locates elements by walking the HTML document tree.

Basic syntax:

```java
By.xpath("//input[@id='login-button']")
```

Read it as:

- `//input`: find any `input` element in the document.
- `[@id='login-button']`: keep only inputs whose `id` attribute is
  `login-button`.

Prefer relative XPath that starts with `//` over absolute XPath copied from
browser tools. Absolute XPath usually depends on exact page nesting and breaks
when harmless layout wrappers are added.

## Dynamic XPath

Dynamic XPath means the locator is written to survive predictable UI changes.

Useful patterns:

| Pattern | Example | Use |
| --- | --- | --- |
| exact attribute | `//input[@id='user-name']` | stable unique attributes |
| partial attribute | `//*[contains(@class,'btn')]` | generated class or id fragments |
| exact text | `//*[normalize-space()='Login']` | stable visible labels |
| partial text | `//*[contains(normalize-space(),'Login')]` | text with changing suffix/prefix |

Do not make every XPath dynamic by habit. A simple exact locator is better
when the attribute is stable.

## Chained XPath

Chained XPath means each part narrows the search by relationship:

```java
By.xpath("//table[@id='table1']//td[normalize-space()='Smith']")
```

Read it as:

- find the table with id `table1`.
- inside that table, find a cell whose normalized text is `Smith`.

The double slash after the table keeps the lookup scoped to that table instead
of searching unrelated tables.

## XPath Axes

Axes describe relationships around the current node.

`_04_LocatorStrategyTest` uses:

```java
By.xpath("//table[@id='table1']//td[normalize-space()='Smith']/following-sibling::td[1]")
```

This finds the `Smith` cell, then moves to the next sibling cell in the same
row.

Common axes:

| Axis | Meaning | Common use |
| --- | --- | --- |
| `parent::` | direct parent | move from child text to wrapper |
| `ancestor::` | any parent above | find the row/card containing a known value |
| `following-sibling::` | next sibling nodes | get another cell in the same row |
| `preceding-sibling::` | previous sibling nodes | get the label before a value |
| `descendant::` | child nodes under current node | find nested controls inside a panel |

Use axes when the page gives you a stable nearby anchor but not a stable
attribute on the exact target element.

## Chained Locators With `WebElement.findElement`

CSS and XPath are not the only way to scope a search. Selenium also allows
element-level lookup:

```java
WebElement loginContainer = driver.findElement(By.id("login_button_container"));
WebElement usernameInput = loginContainer.findElement(By.cssSelector("input[data-test='username']"));
```

This matters for future framework design because page objects and component
objects often represent one page area, then find child elements inside that
area.

## Locator Exceptions

`_04_LocatorStrategyTest` introduces two common locator failures.

`NoSuchElementException` means:

- the selector syntax is valid.
- Selenium searched the current page state.
- no matching element was found.

Common causes:

- wrong locator value.
- page has not loaded the element yet.
- element is inside a frame.
- element appears only after an action.

`InvalidSelectorException` means:

- Selenium could not parse the selector.
- the locator syntax is invalid before the page search can succeed.

Common causes:

- broken XPath syntax.
- unsupported CSS selector syntax.
- copying a selector with missing quotes or brackets.

Module 05 handles timing-related lookup failures with waits. Module 04 only
teaches the difference between a valid locator that matches nothing and an
invalid locator that Selenium cannot execute.

## Java And Selenium Syntax To Notice

```java
By.xpath("//table[@id='table1']//td[normalize-space()='Smith']/ancestor::tr")
```

This locator starts from a known cell and walks up to the containing row. That
pattern is common in table automation: find the row by a stable cell value,
then interact with another cell or action inside the same row.

```java
NoSuchElementException missingElement = Assert.expectThrows(...);
```

`Assert.expectThrows` is a TestNG assertion for expected failures. Module 04
uses it to teach exception categories without causing the whole test suite to
fail.

## Interview Readiness

**Question: When should you use XPath axes?**

Use axes when the target element has no stable locator but a nearby related
element does. Tables, cards, labels, and row-level actions often need this.

**Question: What is the difference between `NoSuchElementException` and
`InvalidSelectorException`?**

`NoSuchElementException` means the locator syntax was valid but matched no
element in the current context. `InvalidSelectorException` means Selenium could
not parse the selector itself.

**Question: Why are chained locators useful?**

They reduce ambiguity by searching inside a known parent element instead of the
whole page. This is important for repeated cards, rows, and forms.

## Revision Checklist

- Can you read the table XPath axes out loud from left to right?
- Can you explain when a locator problem is syntax vs no match?
- Can you explain how scoped lookup prepares for page components?
