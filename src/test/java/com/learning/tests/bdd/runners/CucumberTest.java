package com.learning.tests.bdd.runners;

import org.testng.annotations.DataProvider;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * TestNG entry point for Cucumber feature execution.
 *
 * AbstractTestNGCucumberTests adapts every Cucumber scenario into a TestNG test
 * row. Maven Surefire can then run BDD scenarios through the same TestNG-based
 * build pipeline used by the rest of the framework.
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {
                "com.learning.tests.bdd.hooks",
                "com.learning.tests.bdd.steps"
        },
        plugin = {
                "pretty",
                "html:target/cucumber-report/cucumber.html",
                "json:target/cucumber-report/cucumber.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        tags = "@bdd",
        monochrome = true
)
public class CucumberTest extends AbstractTestNGCucumberTests {

    /**
     * Cucumber-TestNG exposes scenarios through a TestNG DataProvider. Module
     * 16 keeps BDD execution sequential so learners can debug feature-to-step
     * flow first; Module 15's ThreadLocal design still protects the framework
     * when this is later changed to parallel = true.
     */
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
