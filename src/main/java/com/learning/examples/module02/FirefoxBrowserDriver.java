package com.learning.examples.module02;

/**
 * Firefox-style implementation of the BrowserDriver learning interface.
 *
 * Having a second implementation makes polymorphism visible: the same demo can
 * work with ChromeBrowserDriver or FirefoxBrowserDriver through the shared
 * BrowserDriver type.
 */
public class FirefoxBrowserDriver implements BrowserDriver {

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
