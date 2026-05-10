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
}
