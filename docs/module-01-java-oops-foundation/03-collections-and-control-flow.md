# Collections and Control Flow

## Lists

A `List` stores ordered values.

In
[src/main/java/com/learning/examples/module01/_04_Module01Demo.java](../../src/main/java/com/learning/examples/module01/_04_Module01Demo.java):

```java
List<String> smokeTests = List.of("valid login", "product list loads", "cart opens");
```

This is useful in test automation because we often work with groups of things:

- test names.
- page elements.
- product names.
- table rows.
- test data rows.

## Mutable Lists

[src/main/java/com/learning/examples/module01/_03_TestCaseSummary.java](../../src/main/java/com/learning/examples/module01/_03_TestCaseSummary.java)
uses an `ArrayList` because steps are added one by one:

```java
private final List<String> steps;
```

```java
steps.add(step);
```

The public getter returns a copy:

```java
return List.copyOf(steps);
```

That prevents outside code from directly changing the internal list.

## Maps

A `Map` stores key-value pairs.

In
[src/main/java/com/learning/examples/module01/_04_Module01Demo.java](../../src/main/java/com/learning/examples/module01/_04_Module01Demo.java):

```java
Map<String, String> environment = new LinkedHashMap<>();
environment.put("browser", _01_BrowserSession.DEFAULT_BROWSER);
environment.put("baseUrl", "https://www.saucedemo.com");
```

Framework config often feels like a map:

```text
browser = chrome
baseUrl = https://www.saucedemo.com
headless = true
```

Later, this idea will become `config.properties` and `ConfigReader`.

## If Statements

[src/main/java/com/learning/examples/module01/_02_LoginAttempt.java](../../src/main/java/com/learning/examples/module01/_02_LoginAttempt.java)
uses if-style checks through boolean methods:

```java
public boolean hasUsername() {
    return username != null && !username.isBlank();
}
```

This validates the data before submitting.

In Selenium tests, similar logic might check whether an element exists before
reading its text.

## Loops

[src/main/java/com/learning/examples/module01/_04_Module01Demo.java](../../src/main/java/com/learning/examples/module01/_04_Module01Demo.java)
prints each step:

```java
for (String step : testCase.getSteps()) {
    System.out.println("- " + step);
}
```

Later, loops will appear when reading:

- all products on a page.
- all rows in a web table.
- all values from a data file.
- all scenario rows in a data-driven test.

## Beginner Note About `System.out.println`

This module uses `System.out.println` because logging has not been introduced
yet. Later framework modules will replace console printing with Log4j2.

## Java Syntax To Notice

```java
List<String> smokeTests = List.of("valid login", "product list loads", "cart opens");
```

`List<String>` is a generic type. It tells Java that this list should contain
strings. Later Selenium code will use the same syntax with web elements:

```java
List<WebElement> products = driver.findElements(By.cssSelector(".inventory_item"));
```

```java
for (String step : testCase.getSteps()) {
    System.out.println("- " + step);
}
```

This is an enhanced `for` loop. It is useful when you need every item and do
not care about the numeric index.

```java
for (int index = 0; index < smokeTests.size(); index++) {
    System.out.println((index + 1) + ". " + smokeTests.get(index));
}
```

This index-based loop is useful when the position matters, such as printing
step numbers or comparing sorted table rows.

## Nuances For Automation Design

- `List.of(...)` creates an unmodifiable list. That is useful for fixed test
  data, but not for a list you need to build step by step.
- `ArrayList` is mutable. Use it when the object needs to add items over time.
- `List.copyOf(...)` protects internal state. Returning the original list would
  let outside code modify the object's private collection.
- `LinkedHashMap` preserves insertion order, which makes demo output and some
  data-driven reporting easier to read.

## Interview Readiness

**Question: Why do automation engineers need collections?**

Collections represent repeated data: product cards, web table rows, dropdown
options, test data records, and result summaries. A framework that cannot work
with lists and maps will struggle with real application pages.

**Question: Why return a defensive copy from `getSteps()`?**

The class owns its internal list. Returning `List.copyOf(steps)` lets callers
read the values without giving them the ability to mutate the private list.

## Revision Checklist

- Can you explain the difference between `List.of(...)`, `new ArrayList<>()`,
  and `List.copyOf(...)`?
- Can you choose between enhanced `for` and index-based `for` loops?
- Can you explain why maps are a preview of later config and test data?
