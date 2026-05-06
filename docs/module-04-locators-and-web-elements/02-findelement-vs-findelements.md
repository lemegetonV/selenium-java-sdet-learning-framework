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
