package com.learning.examples.module02;

/**
 * Small abstract test template for demonstrating inheritance.
 *
 * This is deliberately not named BaseTest because Module 02 should not create
 * the real framework base class early. The goal is only to show how a parent
 * class can own a common setup, execution, and cleanup sequence.
 */
public abstract class _07_LearningTestTemplate {

    protected final _01_BrowserDriver browserDriver;
    private final String baseUrl;

    protected _07_LearningTestTemplate(_01_BrowserDriver browserDriver, String baseUrl) {
        this.browserDriver = browserDriver;
        this.baseUrl = baseUrl;
    }

    /**
     * Template method: parent controls the lifecycle, child supplies the test
     * behavior through executeTest().
     *
     * final is intentional here. A child class can customize executeTest(), but
     * it cannot skip setup or cleanup by replacing the lifecycle method.
     */
    public final void run() {
        beforeTest();
        executeTest();
        afterTest();
    }

    /*
     * protected means subclasses can implement this method, while unrelated
     * outside code cannot call it directly. abstract means this class defines
     * the required hook but leaves the concrete behavior to child classes.
     */
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
