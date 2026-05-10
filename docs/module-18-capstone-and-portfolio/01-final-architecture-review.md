# Final Architecture Review

The final framework is intentionally layered. Each layer has a job, and good
framework maintenance means keeping responsibilities in the correct layer.

## Layer Map

```mermaid
flowchart TD
    TestNG[TestNG tests] --> Pages[Page Objects]
    Cucumber[Cucumber feature files] --> Steps[Step definitions]
    Steps --> Pages
    Pages --> Actions[ElementActions]
    Pages --> Waits[WaitUtils]
    Actions --> Driver[WebDriver from DriverFactory]
    Waits --> Driver
    Config[ConfigReader] --> Driver
    Listener[TestNG listener] --> Screenshots[ScreenshotUtils]
    Listener --> Reports[Extent and Allure]
```

## Design Decisions

Dynamic `By` locators are used instead of PageFactory. This keeps locator
ownership explicit and avoids teaching reflection-based magic before the
learner understands normal Selenium calls.

Page Objects do not own browser lifecycle. Tests, Cucumber hooks, and
framework lifecycle classes decide when browsers start and stop. Page Objects
only model page behavior.

Wrapper methods centralize common Selenium mechanics. `ElementActions` and
`WaitUtils` prevent every Page Object from repeating wait-find-click-type
patterns.

`DriverFactory` owns driver creation and cleanup. This keeps browser selection,
headless settings, Grid mode, timeouts, and window size in one framework
service.

`ThreadLocal` protects parallel execution. A WebDriver session is not shared
across TestNG worker threads or Cucumber scenario context.

BDD is a top layer, not a replacement architecture. Cucumber step definitions
reuse the same Page Objects and framework services that TestNG tests use.

## Final Source Responsibilities

| Class Or File | Responsibility |
| --- | --- |
| `DriverFactory` | browser creation, local/Grid execution, cleanup |
| `ConfigReader` | typed access to framework configuration |
| `ElementActions` | reusable Selenium interaction wrapper |
| `WaitUtils` | centralized explicit wait behavior |
| `LoginPage`, `ProductsPage`, `CartPage`, `CheckoutPage` | SauceDemo page behavior |
| `BaseTest` | TestNG browser lifecycle and framework service setup |
| `FrameworkTestListener` | diagnostics, screenshots, reporting hooks |
| `ExtentReportManager` | Extent report lifecycle |
| `AllureReportUtils` | Allure attachment helpers |
| `CucumberTest` | TestNG runner for feature files |
| `CucumberHooks` | Cucumber scenario browser lifecycle |
| `SauceDemoSteps` | Gherkin-to-Page-Object bindings |
| `.github/workflows/ui-tests.yml` | CI execution and artifact upload |

## Maintenance Rules

Add locators to Page Objects, not tests or steps.

Add repeated Selenium commands to wrapper services only after duplication or
behavioral risk is clear.

Keep waits explicit and close to framework services. Do not add `Thread.sleep`
to tests.

Keep retries specific. If one public training page occasionally misses a safe
navigation click, document and contain that retry in the Page Object that owns
the transition. Do not turn every wrapper click into a silent retry.

Keep test data external when it is scenario data, not framework configuration.

Keep CI scopes intentional. Pull request smoke checks should remain fast enough
to be used consistently.
