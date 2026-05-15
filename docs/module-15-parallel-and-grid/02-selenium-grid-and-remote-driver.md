# Selenium Grid And RemoteWebDriver

## Mental Model

Local parallel execution means multiple browser sessions start on the same
machine. Selenium Grid means the test code sends WebDriver commands to a remote
browser provider.

Grid is about distribution. Parallel execution is about concurrency. They are
often used together, but they are not the same concept.

Use this distinction:

- TestNG answers: "How many tests should run at the same time?"
- DriverFactory answers: "Where should each browser session be created?"
- Selenium Grid answers: "Which remote machine or container should receive the
  browser session request?"

You can run parallel tests locally without Grid. You can also send a single
sequential test to Grid. Combining both is common in mature automation suites,
but the concepts are separate.

## Code Walkthrough

Configuration:

[src/test/resources/config/config.properties](../../src/test/resources/config/config.properties)

```properties
executionMode=local
gridUrl=http://localhost:4444
```

[config.properties](../../src/test/resources/config/config.properties) keeps
local execution as the default because this learning repo should run without
extra infrastructure. `gridUrl` is still present so the framework has a stable
place to read the endpoint when `executionMode=grid`.

[ConfigReader.java](../../src/main/java/com/learning/framework/config/ConfigReader.java)
exposes the values through:

```java
public static String getExecutionMode()
public static String getGridUrl()
```

These methods still use the Module 11 precedence rule: Maven system properties
override file defaults. That is why this works without editing the config file:

```bash
mvn test -DsuiteXmlFile=testng-parallel.xml -DexecutionMode=grid -DgridUrl=http://localhost:4444
```

Driver factory:

[src/main/java/com/learning/framework/driver/DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java)

Local path:

```java
case "local" -> createLocalDriver();
```

Grid path:

```java
case "grid" -> createRemoteDriver();
```

Remote driver creation:

```java
new RemoteWebDriver(remoteUrl, chromeOptions())
```

`RemoteWebDriver` sends the same WebDriver commands over HTTP to a Grid server
instead of launching the browser directly in the current JVM.

The important design choice is that the public API does not change for tests.
[SauceDemoPageObjectTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoPageObjectTest.java)
and [SauceDemoDataDrivenTest.java](../../src/test/java/com/learning/tests/saucedemo/SauceDemoDataDrivenTest.java)
still call `driver()`. The decision to use local `ChromeDriver` or remote
`RemoteWebDriver` stays inside [DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java).

### Local Path

When `executionMode=local`, `createLocalDriver()` chooses one of:

- `new ChromeDriver(chromeOptions())`
- `new FirefoxDriver(firefoxOptions())`
- `new EdgeDriver(edgeOptions())`

This launches the browser on the same machine running Maven.

### Grid Path

When `executionMode=grid`, `createRemoteDriver()` chooses one of:

- `new RemoteWebDriver(remoteUrl, chromeOptions())`
- `new RemoteWebDriver(remoteUrl, firefoxOptions())`
- `new RemoteWebDriver(remoteUrl, edgeOptions())`

This sends a session request to the Grid endpoint. The options object becomes
the browser capability request that Grid uses to select a compatible node.

## How To Run Against Grid

First, start a Selenium Grid separately. The official Selenium Grid docs show
current setup options:

- https://www.selenium.dev/documentation/grid/
- https://www.selenium.dev/documentation/grid/getting_started/

Then run:

```bash
mvn test -DsuiteXmlFile=testng-parallel.xml -DexecutionMode=grid -DgridUrl=http://localhost:4444
```

If no Grid is running at that URL, this command should fail quickly with a
connection problem. That is expected.

That failure should be read as an infrastructure check, not a product bug. The
SauceDemo application has not even been opened yet if the framework cannot
create a remote browser session.

For local learning, the normal Module 15 command remains:

```bash
mvn clean test -DsuiteXmlFile=testng-parallel.xml
```

That command proves the parallel framework design without requiring Grid.

## Java Syntax To Notice

`URI.create(ConfigReader.getGridUrl()).toURL()` converts the configured text
URL into the `URL` object required by `RemoteWebDriver`.

The driver factory catches invalid URL input and throws `FrameworkException`.
That keeps configuration failures clearly separated from product assertion
failures.

The switch expression in [DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java)
keeps unsupported modes explicit:

```java
case "local" -> createLocalDriver();
case "grid" -> createRemoteDriver();
default -> throw new FrameworkException(...)
```

This is better than silently defaulting to local execution, because a typo such
as `-DexecutionMode=remote` should fail loudly.

`RemoteWebDriver` implements the same `WebDriver` interface as local drivers.
That is the OOP reason page objects do not need to know whether the browser is
local or remote.

## Selenium Or Framework Nuances

Browser options still matter with Grid. The framework passes `ChromeOptions`,
`FirefoxOptions`, or `EdgeOptions` to the remote driver. Grid uses those
capabilities to decide which browser session to create.

A Grid URL should point to the Grid endpoint, commonly `http://localhost:4444`
for a local standalone Grid.

Local browsers and Grid browsers can render differently because operating
system, browser version, window size, and node capacity may differ.

[DriverFactory.java](../../src/main/java/com/learning/framework/driver/DriverFactory.java)
still calls `configureDriver(driver)` after the driver is created. That means
timeouts and window sizing are applied to both local and remote sessions.

Under parallel Grid execution, there are two capacity limits:

- TestNG `thread-count` controls how many test methods this JVM tries to run at
  once.
- Grid capacity controls how many browser sessions the Grid can actually
  provide.

If `thread-count` is higher than Grid capacity, tests may wait for sessions or
fail due to infrastructure limits. Increasing threads is not automatically a
performance improvement.

Module 15 does not add Docker Compose or cloud-provider configuration because
the learning target is the framework switch point: local driver vs remote
driver. CI and environment provisioning come later.

## Common Mistakes

- Thinking Grid is required for local parallel execution.
- Pointing `gridUrl` at an application URL instead of the Grid endpoint.
- Forgetting to pass browser options to `RemoteWebDriver`.
- Running more parallel tests than Grid nodes can handle.
- Treating Grid failures as application failures.
- Hard-coding Grid inside tests instead of keeping it behind `DriverFactory`.
- Forgetting that local and remote browsers may have different versions.
- Assuming Grid changes how page objects should be written.

## Interview Readiness

Strong answer:

"Selenium Grid lets tests run browser sessions on remote machines. In Java,
the framework creates a `RemoteWebDriver` with a Grid URL and browser
capabilities. TestNG still controls parallelism; Grid provides remote browser
capacity."

Follow-up framing:

"I keep Grid behind the driver factory so tests and page objects continue to
depend only on the `WebDriver` interface. The execution mode can be selected by
configuration, usually with Maven `-D` overrides in local runs or CI."

## Revision Checklist

- Can you explain the difference between local parallel execution and Grid?
- Can you show the config keys that switch from local to Grid?
- Can you explain what `RemoteWebDriver` receives in its constructor?
- Can you describe what happens if Grid is not running?
- Can you explain why tests do not import `RemoteWebDriver` directly?
- Can you explain how browser options become Grid capabilities?
