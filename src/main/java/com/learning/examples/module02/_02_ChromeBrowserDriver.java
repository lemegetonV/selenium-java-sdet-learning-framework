package com.learning.examples.module02;

/**
 * Chrome-style implementation of the _01_BrowserDriver learning interface.
 *
 * This class is not Selenium's ChromeDriver. It is a compileable learning
 * stand-in that lets Module 02 explain how a concrete browser class can be
 * handled through an interface reference.
 */
public class _02_ChromeBrowserDriver implements _01_BrowserDriver {

    private boolean open;
    private String currentUrl;

    @Override
    public String getBrowserName() {
        return "chrome";
    }

    @Override
    public void open(String baseUrl) {
        currentUrl = baseUrl;
        open = true;
    }

    @Override
    public void close() {
        open = false;
    }

    @Override
    public boolean isOpen() {
        return open;
    }

    /**
     * Exposes current URL as read-only state for the demo.
     */
    public String getCurrentUrl() {
        return currentUrl;
    }
}
