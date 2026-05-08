# Alerts and Authentication

## Files In This Topic

```text
src/test/java/com/learning/tests/learning/_14_AlertsAndAuthenticationTest.java
```

The alert tests use:

```text
https://the-internet.herokuapp.com/javascript_alerts
```

The authentication test uses:

```text
https://the-internet.herokuapp.com/login
```

## JavaScript Alerts

Browser JavaScript dialogs are not normal DOM elements. You do not locate
them with `findElement`.

Module 06 introduces:

```java
Alert alert = driver.switchTo().alert();
```

This switches Selenium from the page to the browser alert dialog.

## Alert Types

### Alert

A simple alert has a message and an OK button:

```java
alert.getText();
alert.accept();
```

### Confirm

A confirm dialog has OK and Cancel:

```java
confirm.accept();
confirm.dismiss();
```

### Prompt

A prompt accepts text:

```java
prompt.sendKeys("Module 06 prompt");
prompt.accept();
```

## Alert Gotchas

- An open alert blocks normal page interaction.
- `findElement` cannot inspect alert text.
- `NoAlertPresentException` happens when the test switches before an alert
  exists or after it has already been handled.
- alerts are browser dialogs, not HTML modals.

HTML modals are normal page elements and should be handled with locators and
waits instead.

## Form Authentication

The login test is intentionally still raw Selenium:

```java
username.sendKeys("tomsmith");
password.sendKeys("SuperSecretPassword!");
loginButton.click();
```

After clicking Login, the page navigates. The test reuses explicit waits from
Module 05:

```java
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
wait.until(ExpectedConditions.urlContains("/secure"));
```

This is not a framework abstraction. It is a direct application of the wait
concept already learned.

## Beginner Mistakes

- treating JavaScript alerts like HTML elements.
- accepting an alert without asserting its text.
- typing credentials and immediately asserting the next page without waiting
  for navigation.
- logging passwords to console or reports. Logging is introduced later, and
  credentials should be handled carefully when that happens.

## Interview Readiness

**Question: Why can't JavaScript alerts be located with `findElement`?**

Browser JavaScript alerts are not DOM elements. They are browser-level dialogs,
so Selenium handles them through `driver.switchTo().alert()`.

**Question: What is the difference between alert, confirm, and prompt?**

A simple alert has text and OK. A confirm has OK and Cancel, so it can be
accepted or dismissed. A prompt accepts text before accept or dismiss.

**Question: Why wait after login submit?**

Submitting a login form can trigger navigation and delayed messages. The test
should wait for URL or visible page state before asserting the secure page.

## Revision Checklist

- Can you explain why an open alert blocks normal page interaction?
- Can you explain when to use `accept`, `dismiss`, and `sendKeys` on alerts?
- Can you explain why credentials should be treated carefully before logging
  and reporting modules exist?
