# Module 11 Exercises

## Exercise 1 - Run Headed Locally

Run the SauceDemo framework tests with a visible browser.

Command:

```bash
mvn test -Dtest=SauceDemoPageObjectTest -Dheadless=false
```

Expected outcome:

- the same tests pass.
- Chrome opens visibly.
- no source file is edited.

## Exercise 2 - Add A Config Getter

Add a typed getter for a new config value named `retryCount`.

Hint:

- follow the style of `getBaseUrl()` and `isHeadless()`.
- internally call `getInt(...)`.

Expected outcome:

- framework code can call a named method instead of passing a raw string key.

## Exercise 3 - Explain ThreadLocal

Write a short explanation of why `ThreadLocal<WebDriver>` is useful for future
parallel execution.

Expected outcome:

- the explanation mentions one driver per thread.
- the explanation also says ThreadLocal does not automatically make tests
  parallel.

## Exercise 4 - Try An Invalid Browser

Run:

```bash
mvn test -Dtest=SauceDemoPageObjectTest -Dbrowser=safari
```

Expected outcome:

- the framework fails clearly with an unsupported-browser message.
- this is better than silently falling back to Chrome.
