# Exception Handling and Collections

## Why Exceptions Matter in Test Automation

Automation code should fail clearly.

If test data is invalid, a framework should not continue and produce a vague
browser failure later. It should fail near the real problem.

Module 02 introduces this idea with:

```text
src/main/java/com/learning/examples/module02/_04_InvalidTestDataException.java
src/main/java/com/learning/examples/module02/_05_LoginCredentials.java
```

`_05_LoginCredentials` validates that username and password values are present. If
not, it throws a custom runtime exception.

## Custom Exception

`_04_InvalidTestDataException` gives invalid test data a specific name.

That is more useful than throwing a generic exception everywhere because the
failure message can communicate the category of problem:

```text
test data is wrong
```

Later framework modules use the same idea for framework-specific failures,
such as invalid config, wait timeouts, screenshot failures, or unsupported
browser names.

## Where Exceptions Are Handled

`_09_Module02Demo` includes a small invalid-data example so the learner can see
where an exception is caught and reported:

```text
src/main/java/com/learning/examples/module02/_09_Module02Demo.java
```

This is still beginner code, so it prints a message to the console. Later,
framework code will use Log4j2 and test reports instead of console output.

## Collections in Automation

Collections appear constantly in UI automation.

Examples:

- all browser configurations to run.
- all products on a catalog page.
- all rows in a web table.
- all login data rows in a DataProvider.
- all steps attached to a report.

Module 02 uses a `List<_01_BrowserDriver>` so one loop can run the same learning
test with multiple browser implementations.

That list demonstrates polymorphism and collections at the same time:

```java
List<_01_BrowserDriver> browsers = List.of(
        new _02_ChromeBrowserDriver(),
        new _03_FirefoxBrowserDriver()
);
```

Each object is different, but the loop treats each one through the same
`_01_BrowserDriver` interface.

## Why This Matters Later

In a mature framework, the same ideas become:

| Module 02 Concept | Later Framework Use |
| --- | --- |
| custom invalid data exception | framework-specific exception types |
| list of browser implementations | browser matrix or cross-browser execution |
| loop over browsers | repeated test execution across configurations |
| fail early on bad credentials | fail early on bad config or test data |

## Key Takeaways

- Exceptions should explain the real failure category.
- Catch exceptions only when the code can add useful context or recover.
- Collections let automation code handle groups of browsers, data rows, or UI
  elements.
- Module 02 keeps this simple so later framework utilities have a clear
  foundation.

## Java Syntax To Notice

```java
throw new _04_InvalidTestDataException("Password must not be blank");
```

`throw` stops normal execution and creates a failure object with a clear
message. The goal is to fail at the real cause, not later in an unrelated
browser step.

```java
try {
    new _05_LoginCredentials("standard_user", "");
} catch (_04_InvalidTestDataException exception) {
    System.out.println("Rejected invalid login data: " + exception.getMessage());
}
```

`try/catch` is used when the code can handle the failure meaningfully. Here the
demo catches the exception only to show the learner the message. Later tests
usually let invalid setup fail the test.

```java
List<_01_BrowserDriver> browsers = List.of(...);
```

This combines generics and polymorphism: every item in the list must behave
like `_01_BrowserDriver`, even though the concrete objects are different.

## Interview Readiness

**Question: Why create custom exceptions in a framework?**

Custom exceptions make failure categories explicit. `InvalidTestDataException`
or `InvalidBrowserException` tells the reader what kind of problem occurred
before they inspect the stack trace.

**Question: Should automation code catch every exception?**

No. Catch an exception only when you can add useful context, recover safely, or
perform required cleanup. Blind catching hides real failures and creates false
passes.

**Question: Why are collections important for cross-browser testing?**

A browser matrix is a collection of configurations. The same test logic can run
against each configuration when the framework depends on common behavior such
as the `WebDriver` interface.

## Revision Checklist

- Can you explain where invalid login data fails in Module 02?
- Can you explain why the exception message is better than a later generic
  browser failure?
- Can you explain how `List<_01_BrowserDriver>` demonstrates both collections
  and polymorphism?
