package com.learning.examples.module02;

/**
 * Firefox-style implementation of the _01_BrowserDriver learning interface.
 *
 * Having a second implementation makes polymorphism visible: the same demo can
 * work with _02_ChromeBrowserDriver or _03_FirefoxBrowserDriver through the shared
 * _01_BrowserDriver type.
 */
public class _03_FirefoxBrowserDriver implements _01_BrowserDriver {

    private boolean open;
    private String currentUrl;

    @Override
    public String getBrowserName() {
        return "firefox";
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
