# Module 02 Exercises

These exercises extend the Module 02 learning examples. They should not add a
real Selenium dependency yet.

## Exercise 1 - Add Another Browser Implementation

Create a new class named `_10_EdgeBrowserDriver` under:

```text
src/main/java/com/learning/examples/module02/
```

Make it implement `_01_BrowserDriver`.

Hint:
- follow the same structure as `_02_ChromeBrowserDriver`.
- return `edge` from `getBrowserName()`.
- add it to the browser list in `_09_Module02Demo`.

Expected outcome:
- `mvn exec:java` runs the learning test for Chrome, Firefox, and Edge-style
  drivers.

## Exercise 2 - Add A Login Page Action

Add a method to `_06_LoginPageModel`:

```java
public List<String> clearLoginForm()
```

Hint:
- return action descriptions such as clearing username and password fields.
- do not add real Selenium code yet.

Expected outcome:
- the method shows how page objects can expose public actions while hiding
  field details.

## Exercise 3 - Add A Negative Test Data Example

Add another invalid credential scenario in `_09_Module02Demo`.

Hint:
- try a blank username with a valid password.
- catch `_04_InvalidTestDataException`.

Expected outcome:
- the console output explains why the data was rejected.

## Exercise 4 - Explain Polymorphism

In your own words, explain this line from Module 02:

```java
List<_01_BrowserDriver> browsers = List.of(...);
```

Answer these questions:

1. Why is the list type `_01_BrowserDriver`?
2. Why can the list hold both Chrome and Firefox implementations?
3. How does this prepare for `WebDriver driver = new ChromeDriver()`?

## Exercise 5 - Compare Template Inheritance To Future `BaseTest`

Read:

```text
src/main/java/com/learning/examples/module02/_07_LearningTestTemplate.java
src/main/java/com/learning/examples/module02/_08_SauceDemoLoginLearningTest.java
```

Then answer:

1. What behavior is shared by the parent class?
2. What behavior is supplied by the child class?
3. Why does Module 02 avoid creating a real `BaseTest`?
