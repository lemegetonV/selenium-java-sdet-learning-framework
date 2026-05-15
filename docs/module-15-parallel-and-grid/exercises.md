# Module 15 Exercises

## Reading List

Before doing the exercises, read these files in order:

1. [testng-parallel.xml](../../testng-parallel.xml)
2. [DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java)
3. [BaseTest.java](../../src/test/java/com/learning/tests/base/BaseTest.java)
4. [SauceDemoPageObjectTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoPageObjectTest.java)
5. [SauceDemoDataDrivenTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoDataDrivenTest.java)
6. [ExtentReportManager.java](../../src/test/java/com/learning/tests/reports/ExtentReportManager.java)
7. [ScreenshotUtils.java](../../src/main/java/com/learning/framework/screenshots/ScreenshotUtils.java)
8. [config.properties](../../src/test/resources/config/config.properties)

## Exercise 1 - Compare Sequential And Parallel Runtime

Run:

```bash
mvn test -DsuiteXmlFile=testng.xml
mvn test -DsuiteXmlFile=testng-parallel.xml
```

Expected outcome:

The parallel suite should finish faster because multiple browser sessions run
at the same time.

Questions to answer:

- Which suite file controls sequential execution?
- Which suite file controls method-level parallel execution?
- Did the parallel run create browser sessions on more than one thread?
- If parallel is not faster on your machine, what local resource limit might be
  responsible?

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

Hints:

- Search for `Created local chrome browser session on thread`.
- Compare those thread IDs with the test names in square brackets.
- Interleaved log lines are normal in parallel execution. The goal is not to
  make logs sequential; the goal is to preserve test identity.

## Exercise 3 - Explain The BaseTest Refactor

Read:

- [src/test/java/com/learning/tests/base/BaseTest.java](../../src/test/java/com/learning/tests/base/BaseTest.java)
- [src/test/java/com/learning/tests/saucedemo/SauceDemoPageObjectTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoPageObjectTest.java)

Answer:

- why did `protected WebDriver driver` become unsafe?
- why do tests call `driver()` now?
- why does teardown call `remove()`?

Add these answers:

- why does [BaseTest.java](../../src/test/java/com/learning/tests/base/BaseTest.java)
  store `WaitUtils` and `ElementActions` in thread-local variables too?
- why are page objects created inside test methods in
  [SauceDemoPageObjectTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoPageObjectTest.java)?
- what could happen if one test quits a browser session another test is using?

## Exercise 4 - Grid Dry Run Reasoning

Without starting Grid, inspect this command:

```bash
mvn test -DsuiteXmlFile=testng-parallel.xml -DexecutionMode=grid -DgridUrl=http://localhost:4444
```

Expected outcome:

You can explain that it requires a separately running Selenium Grid. If Grid is
not running, the failure is infrastructure/configuration, not an application
bug.

Questions to answer:

- Which value switches [DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java)
  from local drivers to `RemoteWebDriver`?
- Which value tells the framework where Grid is running?
- Why do page objects not need to change for Grid?
- Why should this command not be treated as a normal SauceDemo failure if Grid
  is unavailable?

## Exercise 5 - Parallel-Safety Checklist

For each item, decide whether it is safe in parallel:

- immutable `LoginScenario` records.
- static shared `WebDriver`.
- `ThreadLocal<ExtentTest>`.
- one screenshot filename for all failures.
- page objects created inside each test method.

Expected outcome:

You can identify which framework objects must be isolated per test thread.

Explain each answer in one sentence. Do not only mark safe/unsafe.

## Exercise 6 - Inspect Report Artifacts After A Clean Parallel Run

Run:

```bash
mvn clean test -DsuiteXmlFile=testng-parallel.xml
```

Open or inspect:

- `target/logs/test-execution.log`
- `target/extent-report/extent.html`
- `target/allure-results`

Expected outcome:

You can explain which artifacts are shared suite-level outputs and which ones
are per-test outputs.

Questions to answer:

- Why does [ExtentReportManager.java](../../src/test/java/com/learning/tests/reports/ExtentReportManager.java)
  synchronize report writes?
- Why is `CURRENT_TEST` still thread-local if methods are synchronized?
- Why does [ScreenshotUtils.java](../../src/main/java/com/learning/framework/screenshots/ScreenshotUtils.java)
  include a thread ID in filenames?
- What would a report contamination bug look like?

## Exercise 7 - Find The Safe And Unsafe Fields

Read:

- [SauceDemoPageObjectTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoPageObjectTest.java)
- [BaseTest.java](../../src/test/java/com/learning/tests/base/BaseTest.java)

Expected outcome:

You can distinguish stable class data from unsafe per-test service state.

Questions to answer:

- Why are `standardUser`, `lockedOutUser`, and `password` less risky than
  `WebDriver` fields?
- Why would a class-level `LoginPage loginPage` field be unsafe here?
- Why does `@BeforeMethod` fit method-level parallelism better than a shared
  `@BeforeClass` browser?
