# Exception Handling and Collections

## Why Exceptions Matter in Test Automation

Automation code should fail clearly.

If test data is invalid, a framework should not continue and produce a vague
browser failure later. It should fail near the real problem.

Module 02 introduces this idea with:

```text
src/main/java/com/learning/examples/module02/InvalidTestDataException.java
src/main/java/com/learning/examples/module02/LoginCredentials.java
```

`LoginCredentials` validates that username and password values are present. If
not, it throws a custom runtime exception.

## Custom Exception

`InvalidTestDataException` gives invalid test data a specific name.

That is more useful than throwing a generic exception everywhere because the
failure message can communicate the category of problem:

```text
test data is wrong
```

Later framework modules use the same idea for framework-specific failures,
such as invalid config, wait timeouts, screenshot failures, or unsupported
browser names.

## Where Exceptions Are Handled

`Module02Demo` includes a small invalid-data example so the learner can see
where an exception is caught and reported:

```text
src/main/java/com/learning/examples/module02/Module02Demo.java
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

Module 02 uses a `List<BrowserDriver>` so one loop can run the same learning
test with multiple browser implementations.

That list demonstrates polymorphism and collections at the same time:

```java
List<BrowserDriver> browsers = List.of(
        new ChromeBrowserDriver(),
        new FirefoxBrowserDriver()
);
```

Each object is different, but the loop treats each one through the same
`BrowserDriver` interface.

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
