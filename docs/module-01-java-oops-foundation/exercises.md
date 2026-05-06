# Module 01 Exercises

Try these after reading the docs and running the demo.

## Exercise 1 - Add a Browser Name

Create another `_01_BrowserSession` in `_04_Module01Demo` using the two-argument
constructor.

Hint:

```java
_01_BrowserSession firefoxSession = new _01_BrowserSession("firefox", "https://the-internet.herokuapp.com");
```

Expected outcome:
- You can print both session summaries.
- The session count increases.

## Exercise 2 - Add Another Login Validation

Add a method to `_02_LoginAttempt`:

```java
public boolean isPasswordLongEnough()
```

Make it return true when the password has at least 8 characters.

Hint:
- Reuse `hasPassword()` before checking length.

Expected outcome:
- You can print the result from `_04_Module01Demo`.

## Exercise 3 - Add More Test Steps

Add two more steps to `_03_TestCaseSummary` from `_04_Module01Demo`.

Expected outcome:
- `describe()` reports the new step count.

## Exercise 4 - Add Another Environment Key

Add this pair to the environment map:

```text
headless = false
```

Expected outcome:
- The environment section prints the new key and value.

## Exercise 5 - Explain the OOP Mapping

In your own words, answer:

1. What class in this module feels most similar to a future Selenium browser?
2. What class feels most similar to future test data?
3. Why are the fields private?
4. Why is `DEFAULT_BROWSER` static?
