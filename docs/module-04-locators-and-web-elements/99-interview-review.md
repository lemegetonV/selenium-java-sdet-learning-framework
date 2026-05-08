# Module 04 Interview Review

## What You Must Be Able To Explain

Module 04 is the foundation for all page object work. You should be able to
explain:

- what a locator is.
- how `By` and `WebElement` work together.
- locator strategy tradeoffs.
- `findElement` vs `findElements`.
- `sendKeys`, `clear`, `click`, `getText`, and `getAttribute`.
- why XPath axes and scoped lookup matter.
- common locator exceptions and what they mean.

## Strong Answers

**What is a locator?**

A locator is the instruction Selenium uses to find an element in the current
browser context. In Java, Selenium represents locator strategies with `By`.

**What is a WebElement?**

A `WebElement` is Selenium's object representing a found element. Commands such
as `sendKeys`, `click`, `getText`, and `getAttribute` operate on that element.

**Which locator strategy should I prefer?**

Prefer stable, unique, app-owned attributes such as `id` or `data-test`. Use
CSS for readable attribute and parent-child selectors. Use XPath when text,
axes, or DOM relationships make the intent clearer.

**Why does `findElements` not throw when nothing is found?**

An empty list is a valid answer for a collection query. That makes
`findElements` useful for absence checks and list assertions.

## Code Lines To Revise

```java
driver.findElement(By.id("user-name"));
```

Find one expected element. Failure means the test cannot continue.

```java
driver.findElements(By.tagName("input"));
```

Find a collection. The test should assert size or contents.

```java
loginContainer.findElement(By.cssSelector("input[data-test='username']"));
```

Search inside a parent element. This reduces ambiguity on pages with repeated
structures.

```java
By.xpath("//table[@id='table1']//td[normalize-space()='Smith']/ancestor::tr")
```

Find a row by a known cell value. This is a core web-table pattern.

## Common Interview Traps

- Saying XPath is always bad. XPath is powerful when used intentionally.
- Saying CSS is always enough. CSS cannot navigate upward to ancestors.
- Using `getText()` for input values.
- Believing `findElement` proves uniqueness.
- Treating all locator failures as wait problems.

## Connection To Future Framework Modules

Page objects and wrapper methods will not fix poor locator thinking. Module 04
keeps locators raw so the learner understands what later abstractions are
wrapping. Module 09 will move locators into page objects; Module 10 will route
element interactions through reusable wrapper methods.
