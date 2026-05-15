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

## Common Beginner Mistakes

- forgetting to include the full package name in [testng.xml](../../testng.xml).
- expecting a test to run when its group is not included.
- putting every possible suite into one giant XML file.
- making Maven run only [testng.xml](../../testng.xml) by default and accidentally skipping raw
  learning tests.
- confusing Maven profiles with TestNG groups. Maven chooses execution
  configuration; TestNG groups choose tests inside that configuration.

## Framework Bridge

Later modules can add more suite files:

```text
testng-smoke.xml
testng-regression.xml
testng-parallel.xml
```

Module 08 starts with one [testng.xml](../../testng.xml) so the learner understands the mechanism
before the suite strategy grows.
