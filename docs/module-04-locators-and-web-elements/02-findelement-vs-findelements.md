# `findElement` vs `findElements`

## `findElement`

`findElement` returns one `WebElement`.

Example:

```java
WebElement usernameInput = driver.findElement(By.id("user-name"));
```

Behavior:

- returns the first matching element.
- throws an exception if no matching element exists.
- is best when the test expects exactly one important element.

Module 04 uses it in:

```text
src/test/java/com/learning/tests/learning/_04_LocatorStrategyTest.java
src/test/java/com/learning/tests/learning/_06_WebElementCommandTest.java
```

## `findElements`

`findElements` returns a `List<WebElement>`.

Example:

```java
List<WebElement> inputElements = driver.findElements(By.tagName("input"));
```

Behavior:

- returns all matching elements.
- returns an empty list if no elements match.
- does not throw only because the list is empty.
- is best when the test expects a group of elements.

## Why The Difference Matters

Use `findElement` when absence should immediately fail the test.

Use `findElements` when the count or collection content is what you want to
assert.

This difference becomes important later for:

- product lists.
- cart rows.
- tables.
- data-driven assertions.
- conditional UI checks.

## Common Beginner Mistakes

- using `findElement` to check whether something exists, then being surprised
  by an exception.
- using `findElements(...).get(0)` without checking the list size.
- assuming `findElement` verifies uniqueness. It does not; it returns the first
  match.

Module 05 will add waits. In Module 04, lookups happen immediately.

## Interview Readiness

**Question: What is the difference between `findElement` and `findElements`?**

`findElement` returns the first matching element and throws
`NoSuchElementException` if none is found. `findElements` returns a list of all
matches and returns an empty list when none are found.

**Question: Does `findElement` prove the locator is unique?**

No. It returns the first matching element. If uniqueness matters, use a more
specific locator or assert the size from `findElements`.

**Question: When is `findElements` useful?**

Use it for collections such as product cards, table rows, dropdown options, or
checking that zero matching elements are present.

## Revision Checklist

- Can you explain why `findElements(...).isEmpty()` is safer than
  `findElement(...)` for absence checks?
- Can you explain why list size should be checked before `.get(0)`?
- Can you explain why waits will change lookup timing in Module 05?
