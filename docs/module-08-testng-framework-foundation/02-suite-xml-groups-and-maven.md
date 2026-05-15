# Suite XML, Groups, and Maven

## Files In This Topic

This topic reads these files:

- [testng.xml](../../testng.xml)
- [pom.xml](../../pom.xml)
- [src/test/java/com/learning/tests/saucedemo/LoginFoundationTest.java](../../src/test/java/com/learning/tests/saucedemo/LoginFoundationTest.java)


## Why [testng.xml](../../testng.xml) Exists

Earlier modules let Maven discover every `*Test.java` class automatically.
That is useful for small learning tests, but frameworks need more control.

[testng.xml](../../testng.xml) lets the project define:

- which test classes belong to a suite.
- which groups should run.
- suite and test names visible in reports.
- future parameters such as browser, environment, or parallel mode.

Module 08 adds:

```xml
<suite name="Module 08 TestNG Framework Foundation" verbose="1">
    <test name="SauceDemo Framework Regression">
        <groups>
            <run>
                <include name="regression"/>
            </run>
        </groups>
        <classes>
            <class name="com.learning.tests.saucedemo.LoginFoundationTest"/>
        </classes>
    </test>
</suite>
```

This suite runs the `regression` group from `LoginFoundationTest`.

## Execution Path From Maven To Test Method

When you run:

```bash
mvn test -DsuiteXmlFile=testng.xml
```

the path is:

```text
Maven test phase
        |
        v
Maven Surefire plugin
        |
        v
testng-suite Maven profile activates because suiteXmlFile exists
        |
        v
Surefire loads testng.xml
        |
        v
TestNG reads suite, test, groups, and classes
        |
        v
LoginFoundationTest methods in the regression group run
```

This is why Module 08 changes both [pom.xml](../../pom.xml) and
[testng.xml](../../testng.xml). The XML defines the TestNG suite. The Maven
profile tells Surefire when to use that XML.

Without the Maven profile, the `-DsuiteXmlFile=testng.xml` property would just
be a text value passed to Maven. Surefire still needs configuration that says:
"use this property as the TestNG suite file."

## What Groups Mean

`LoginFoundationTest` marks tests like this:

```java
@Test(groups = {"smoke", "regression"})
public void standardUserCanReachProductsPage() {
}

@Test(groups = "regression")
public void lockedOutUserSeesErrorMessage() {
}
```

Groups are labels. A smoke test is usually a small, high-confidence check. A
regression test is broader and protects behavior that should keep working.

The standard-user login belongs to both groups because it is critical enough
for smoke and regression. The locked-out-user validation belongs to regression
only because it is useful coverage but not the first health check.

The important rule is that a group is not a folder and not a package. It is
metadata attached to a test method.

In this module:

| Test Method | Groups | Runs In `testng.xml`? | Why |
| --- | --- | --- | --- |
| `standardUserCanReachProductsPage` | `smoke`, `regression` | yes | it includes `regression` |
| `lockedOutUserSeesErrorMessage` | `regression` | yes | it includes `regression` |

If a future test had only `groups = "smoke"`, it would not run in this suite
because [testng.xml](../../testng.xml) includes only `regression`.

That distinction matters in interviews. Maven chooses *how* tests are launched.
TestNG groups choose *which test methods* are included after TestNG has loaded
the classes.

## Maven Surefire

Maven does not run TestNG by itself. The Surefire plugin runs the tests during:

```bash
mvn test
```

Module 08 keeps default test discovery intact:

```xml
<includes>
    <include>**/*Test.java</include>
</includes>
```

That means full `mvn test` still runs the previous raw learning tests plus the
new framework tests.

There are now two useful commands:

```bash
mvn test
```

This is broad discovery. Surefire searches for test classes matching
`**/*Test.java`. It is useful when you want to confirm the whole learning repo
still works.

```bash
mvn test -DsuiteXmlFile=testng.xml
```

This is focused suite execution. It is useful when you want only the first
framework-style SauceDemo regression suite.

Both commands use Maven Surefire. The difference is whether Surefire discovers
tests from naming patterns or receives a TestNG suite file.

## Running A Named Suite

Module 08 adds a Maven profile that activates when `suiteXmlFile` is supplied:

```bash
mvn test -DsuiteXmlFile=testng.xml
```

The profile tells Surefire to use the provided TestNG XML suite.

Why use a profile instead of making [testng.xml](../../testng.xml) the default?

- full `mvn test` should still verify the whole learning repo.
- named suites are useful when you want focused framework execution.
- later CI can choose a suite deliberately instead of changing local defaults.

The profile activation is this part of [pom.xml](../../pom.xml):

```xml
<activation>
    <property>
        <name>suiteXmlFile</name>
    </property>
</activation>
```

That means "turn this profile on only when the JVM/Maven property
`suiteXmlFile` exists."

The profile then passes the value into Surefire:

```xml
<suiteXmlFiles>
    <suiteXmlFile>${suiteXmlFile}</suiteXmlFile>
</suiteXmlFiles>
```

So this command:

```bash
mvn test -DsuiteXmlFile=testng.xml
```

sets:

```text
suiteXmlFile = testng.xml
```

and Surefire loads that file as the TestNG suite definition.

## Java and XML Concepts

XML is configuration, not Java code. The class name in [testng.xml](../../testng.xml) must be the
fully qualified Java class name:

```text
com.learning.tests.saucedemo.LoginFoundationTest
```

That includes:

- package: `com.learning.tests.saucedemo`
- class: `LoginFoundationTest`

If the package or class is renamed, the XML must be updated.

The XML has three nested levels:

```text
suite: the overall TestNG run
test: a named group of classes inside the suite
classes: the Java test classes TestNG should inspect
```

In Module 08 there is one suite, one test, and one class. That is intentional.
The structure is simple enough to read before the project grows more suite
files later.

## Headless Property Flow

The `headless` Maven property flows through Surefire into the test JVM.

[pom.xml](../../pom.xml) contains:

```xml
<systemPropertyVariables>
    <headless>${headless}</headless>
</systemPropertyVariables>
```

`BaseTest` reads the same property:

```java
System.getProperty("headless", "true")
```

That means:

```bash
mvn test -Dtest=LoginFoundationTest -Dheadless=false
```

starts Chrome visibly, while the default remains headless.

This is a small but important bridge between Maven configuration and Java test
code.

## Common Beginner Mistakes

- forgetting to include the full package name in [testng.xml](../../testng.xml).
- expecting a test to run when its group is not included.
- putting every possible suite into one giant XML file.
- making Maven run only [testng.xml](../../testng.xml) by default and accidentally skipping raw
  learning tests.
- confusing Maven profiles with TestNG groups. Maven chooses execution
  configuration; TestNG groups choose tests inside that configuration.
- expecting `-Dgroups=smoke` to matter for this module's suite unless Surefire
  and TestNG are configured to consume that property.
- changing the suite XML but running plain `mvn test`, then wondering why the
  suite change had no effect.
- renaming a class without updating the fully qualified class name in XML.

## Debugging Suite Execution

If the suite does not run what you expect, check in this order:

1. Did you run `mvn test -DsuiteXmlFile=testng.xml`, not only `mvn test`?
2. Does [testng.xml](../../testng.xml) list the correct fully qualified class?
3. Does the test method have a group included by the XML?
4. Does the class name end with `Test` if you are using default discovery?
5. Is the Maven profile active because `suiteXmlFile` was supplied?

## Framework Bridge

Later modules can add more suite files:

```text
testng-smoke.xml
testng-regression.xml
testng-parallel.xml
```

Module 08 starts with one [testng.xml](../../testng.xml) so the learner understands the mechanism
before the suite strategy grows.
