package com.learning.tests.bdd.hooks;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.learning.framework.screenshots.ScreenshotUtils;
import com.learning.tests.bdd.context.CucumberScenarioContext;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

/**
 * Cucumber lifecycle hooks for browser setup, failure evidence, and cleanup.
 *
 * Hooks are the BDD equivalent of TestNG setup/teardown methods. They run
 * around every scenario, so each scenario receives a clean browser session.
 */
public class CucumberHooks {

    private static final Logger LOGGER = LogManager.getLogger(CucumberHooks.class);

    @Before
    public void beforeScenario(Scenario scenario) {
        LOGGER.info("Starting Cucumber scenario: {}", scenario.getName());
        CucumberScenarioContext.openBrowser();
    }

    @After
    public void afterScenario(Scenario scenario) throws IOException {
        try {
            if (scenario.isFailed() && CucumberScenarioContext.hasDriver()) {
                Path screenshot = ScreenshotUtils.capture(
                        CucumberScenarioContext.driver(),
                        scenario.getName()
                );
                scenario.attach(Files.readAllBytes(screenshot), "image/png", "Failure screenshot");
                LOGGER.info("Attached failure screenshot for Cucumber scenario: {}", scenario.getName());
            }
        } finally {
            CucumberScenarioContext.closeBrowser();
            LOGGER.info("Finished Cucumber scenario: {}", scenario.getName());
        }
    }
}
