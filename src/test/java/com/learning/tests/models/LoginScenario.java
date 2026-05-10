package com.learning.tests.models;

/**
 * Immutable test-data row for SauceDemo login scenarios.
 *
 * Module 12 introduces POJOs as the bridge between external files and tests.
 * Tests receive meaningful Java objects instead of raw String arrays whose
 * column order must be remembered.
 */
public record LoginScenario(
        String scenarioName,
        String username,
        String password,
        boolean successfulLogin,
        String expectedMessage
) {

    /**
     * TestNG and reporting tools often call toString() when displaying
     * DataProvider parameters. The password must stay available to the test,
     * but it should never be written into reports or logs.
     */
    @Override
    public String toString() {
        return "LoginScenario[scenarioName=" + scenarioName
                + ", username=" + username
                + ", password=****"
                + ", successfulLogin=" + successfulLogin
                + ", expectedMessage=" + expectedMessage
                + "]";
    }
}
