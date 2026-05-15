# Selenium Grid And RemoteWebDriver

## Mental Model

Local parallel execution means multiple browser sessions start on the same
machine. Selenium Grid means the test code sends WebDriver commands to a remote
browser provider.

Grid is about distribution. Parallel execution is about concurrency. They are
often used together, but they are not the same concept.

## Code Walkthrough

Configuration:

[src/test/resources/config/config.properties](../../src/test/resources/config/config.properties)

```properties
executionMode=local
gridUrl=http://localhost:4444
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

## Java Syntax To Notice

`URI.create(ConfigReader.getGridUrl()).toURL()` converts the configured text
URL into the `URL` object required by `RemoteWebDriver`.

The driver factory catches invalid URL input and throws `FrameworkException`.
That keeps configuration failures clearly separated from product assertion
failures.

## Selenium Or Framework Nuances

Browser options still matter with Grid. The framework passes `ChromeOptions`,
`FirefoxOptions`, or `EdgeOptions` to the remote driver. Grid uses those
capabilities to decide which browser session to create.

A Grid URL should point to the Grid endpoint, commonly `http://localhost:4444`
for a local standalone Grid.

Local browsers and Grid browsers can render differently because operating
system, browser version, window size, and node capacity may differ.

## Common Mistakes

- Thinking Grid is required for local parallel execution.
- Pointing `gridUrl` at an application URL instead of the Grid endpoint.
- Forgetting to pass browser options to `RemoteWebDriver`.
- Running more parallel tests than Grid nodes can handle.
- Treating Grid failures as application failures.

## Interview Readiness

Strong answer:

"Selenium Grid lets tests run browser sessions on remote machines. In Java,
the framework creates a `RemoteWebDriver` with a Grid URL and browser
capabilities. TestNG still controls parallelism; Grid provides remote browser
capacity."

## Revision Checklist

- Can you explain the difference between local parallel execution and Grid?
- Can you show the config keys that switch from local to Grid?
- Can you explain what `RemoteWebDriver` receives in its constructor?
- Can you describe what happens if Grid is not running?

