package com.learning.examples.module01;

/**
 * Small learning model for a browser session.
 *
 * This class does not launch Selenium yet. It introduces the same OOP shape
 * that will appear later when a real WebDriver object controls a real
 * browser: state is stored in fields, behavior is exposed through methods, and
 * callers do not edit the fields directly.
 */
public class _01_BrowserSession {

    /**
     * A class-level constant shared by every _01_BrowserSession object.
     *
     * Later modules use the same idea for default browser/config values
     * before the project introduces external configuration files.
     */
    public static final String DEFAULT_BROWSER = "chrome";

    /*
     * Static state belongs to the class, not to one object. This counter is a
     * beginner-friendly way to see that all _01_BrowserSession instances share the
     * same class-level value.
     */
    private static int createdSessionCount;

    /*
     * Private fields demonstrate encapsulation. Other classes must use public
     * methods instead of changing browser session state directly.
     */
    private final String browserName;
    private final String baseUrl;
    private boolean open;

    /**
     * Convenience constructor that uses the default browser.
     *
     * This mirrors a common framework design: simple defaults for most tests,
     * with a second constructor or config path when more control is needed.
     */
    public _01_BrowserSession(String baseUrl) {
        /*
         * this(...) calls another constructor in the same class. It must be the
         * first statement, and it keeps the default-browser setup in one path
         * instead of duplicating assignment logic.
         */
        this(DEFAULT_BROWSER, baseUrl);
    }

    /**
     * Full constructor for callers that want to choose both browser and URL.
     */
    public _01_BrowserSession(String browserName, String baseUrl) {
        /*
         * this.browserName means "the field on this object". browserName without
         * this is the constructor parameter. The names intentionally match so the
         * learner sees why this is common Java constructor syntax.
         */
        this.browserName = browserName;
        this.baseUrl = baseUrl;
        this.open = false;
        /*
         * createdSessionCount is static, so every new object updates the same
         * class-level counter. The individual open/baseUrl fields above are
         * instance state and belong to one session object.
         */
        createdSessionCount++;
    }

    /**
     * Changes object state from closed to open.
     *
     * In later Selenium modules, this type of behavior maps to opening a
     * real browser and navigating with driver.get(...).
     */
    public void open() {
        open = true;
    }

    /**
     * Changes object state from open to closed.
     *
     * Later this maps to browser cleanup such as driver.quit().
     */
    public void close() {
        open = false;
    }

    /**
     * Returns a readable summary for the demo output.
     */
    public String getSessionSummary() {
        /*
         * This ternary expression is a compact if/else:
         * condition ? valueWhenTrue : valueWhenFalse.
         */
        String status = open ? "open" : "closed";
        return browserName + " session for " + baseUrl + " is " + status;
    }

    /**
     * Public read-only access to private state.
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * Public read-only access to class-level state.
     */
    public static int getCreatedSessionCount() {
        return createdSessionCount;
    }
}
