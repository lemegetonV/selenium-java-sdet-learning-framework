# Module 15 Exercises

## Exercise 1 - Compare Sequential And Parallel Runtime

Run:

```bash
mvn test -DsuiteXmlFile=testng.xml
mvn test -DsuiteXmlFile=testng-parallel.xml
```

Expected outcome:

The parallel suite should finish faster because multiple browser sessions run
at the same time.

## Exercise 2 - Read Thread IDs In Logs

Run:

```bash
mvn test -DsuiteXmlFile=testng-parallel.xml
```

Open:

`target/logs/test-execution.log`

Find:

- at least two different thread IDs in `DriverFactory` lines.
- interleaved test names in Log4j output.

Expected outcome:

You can prove from logs that tests ran concurrently.

## Exercise 3 - Explain The BaseTest Refactor

Read:

- [src/test/java/com/learning/tests/base/BaseTest.java](../../src/test/java/com/learning/tests/base/BaseTest.java)
- [src/test/java/com/learning/tests/saucedemo/SauceDemoPageObjectTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoPageObjectTest.java)

Answer:

- why did `protected WebDriver driver` become unsafe?
- why do tests call `driver()` now?
- why does teardown call `remove()`?

## Exercise 4 - Grid Dry Run Reasoning

Without starting Grid, inspect this command:

```bash
mvn test -DsuiteXmlFile=testng-parallel.xml -DexecutionMode=grid -DgridUrl=http://localhost:4444
```

Expected outcome:

You can explain that it requires a separately running Selenium Grid. If Grid is
not running, the failure is infrastructure/configuration, not an application
bug.

## Exercise 5 - Parallel-Safety Checklist

For each item, decide whether it is safe in parallel:

- immutable `LoginScenario` records.
- static shared `WebDriver`.
- `ThreadLocal<ExtentTest>`.
- one screenshot filename for all failures.
- page objects created inside each test method.

Expected outcome:

You can identify which framework objects must be isolated per test thread.

