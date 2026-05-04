package com.learning.examples.module02;

/**
 * Small abstract test template for demonstrating inheritance.
 *
 * This is deliberately not named BaseTest because Module 02 should not create
 * the real framework base class early. The goal is only to show how a parent
 * class can own a common setup, execution, and cleanup sequence.
 */
public abstract class LearningTestTemplate {

    protected final BrowserDriver browserDriver;
    private final String baseUrl;

    protected LearningTestTemplate(BrowserDriver browserDriver, String baseUrl) {
        this.browserDriver = browserDriver;
        this.baseUrl = baseUrl;
    }

    /**
     * Template method: parent controls the lifecycle, child supplies the test
     * behavior through executeTest().
     */
    public final void run() {
        beforeTest();
        executeTest();
        afterTest();
    }

    protected abstract void executeTest();

    protected void beforeTest() {
        browserDriver.open(baseUrl);
        System.out.println("Opened " + browserDriver.getBrowserName() + " at " + baseUrl);
    }

    protected void afterTest() {
        browserDriver.close();
        System.out.println("Closed " + browserDriver.getBrowserName());
    }
}
