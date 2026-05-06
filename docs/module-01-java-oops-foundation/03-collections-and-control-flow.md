# Collections and Control Flow

## Lists

A `List` stores ordered values.

In `_04_Module01Demo`:

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

`_03_TestCaseSummary` uses an `ArrayList` because steps are added one by one:

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

In `_04_Module01Demo`:

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

`_02_LoginAttempt` uses if-style checks through boolean methods:

```java
public boolean hasUsername() {
    return username != null && !username.isBlank();
}
```

This validates the data before submitting.

In Selenium tests, similar logic might check whether an element exists before
reading its text.

## Loops

`_04_Module01Demo` prints each step:

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
