# Module 18 - Capstone And Portfolio Packaging

Module 18 packages the completed learning framework into a portfolio-ready
repository. Earlier modules built the framework. This module makes the final
state easy to inspect, explain, run, and present.

## Why This Module Exists Now

Portfolio packaging should happen only after the framework is real. The repo
now has:

- raw Selenium learning coverage.
- TestNG framework execution.
- Page Objects and wrapper methods.
- configuration and driver factory.
- data-driven tests.
- listeners, screenshots, logging, Extent, and Allure.
- parallel execution and Grid support.
- Cucumber BDD.
- GitHub Actions CI.

Module 18 ties those pieces together with final navigation, architecture
review, interview notes, known limitations, and verified run commands.

## Files Added Or Changed

| File | Status | Purpose |
| --- | --- | --- |
| `README.md` | Changed | Replaces the stale early-module README with the final framework overview. |
| `docs/README.md` | Added | Adds final documentation navigation and source map. |
| `src/main/java/com/learning/framework/pages/saucedemo/CartPage.java` | Changed | Hardens the cart-to-checkout transition after the final parallel audit exposed an intermittent public-site click miss. |
| `docs/module-18-capstone-and-portfolio/00-module-overview.md` | Added | Explains final packaging scope and quality gate. |
| `docs/module-18-capstone-and-portfolio/01-final-architecture-review.md` | Added | Reviews final framework layering and design decisions. |
| `docs/module-18-capstone-and-portfolio/02-runbook-and-portfolio-guide.md` | Added | Gives run commands, report paths, resume bullets, and demo flow. |
| `docs/module-18-capstone-and-portfolio/99-interview-review.md` | Added | Final interview talking points. |
| `docs/module-18-capstone-and-portfolio/exercises.md` | Added | Capstone revision exercises. |

## Quality Gate

Run:

```bash
mvn test -DsuiteXmlFile=testng.xml -Dheadless=true
mvn test -DsuiteXmlFile=testng-parallel.xml -Dheadless=true
mvn test -DsuiteXmlFile=testng-cucumber.xml -Dheadless=true
mvn test -Dheadless=true
mvn allure:report
```

Expected result:

- sequential framework suite passes.
- parallel framework suite passes.
- Cucumber suite passes.
- full discovered-test regression passes.
- Allure report is generated.
- root README and docs index point to the final curriculum.

## What Is Intentionally Deferred

- GitHub Pages publication for Allure.
- cross-browser CI matrix.
- Selenium Grid service containers in CI.
- environment-specific secrets and private AUT support.
- Dockerized local execution.

These are strong future enhancements, but the completed learning framework is
already ready to present as a Selenium Java SDET portfolio project.
