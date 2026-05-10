package com.learning.tests.reports;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

/**
 * Owns ExtentReports setup and per-test ExtentTest access.
 *
 * ExtentReports is a reporting library, but the framework still needs to
 * control when the report starts, which test is active on the current thread,
 * and when the final HTML file is flushed to disk.
 */
public final class ExtentReportManager {

    private static final Logger LOGGER = LogManager.getLogger(ExtentReportManager.class);
    private static final Path REPORT_PATH = Paths.get("target", "extent-report", "extent.html");
    private static final ThreadLocal<ExtentTest> CURRENT_TEST = new ThreadLocal<>();

    private static ExtentReports extentReports;

    private ExtentReportManager() {
        // Utility class: do not instantiate.
    }

    public static synchronized void initialize(String suiteName) {
        if (extentReports != null) {
            return;
        }

        try {
            Files.createDirectories(REPORT_PATH.getParent());
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to create Extent report directory: " + REPORT_PATH, exception);
        }

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(REPORT_PATH.toString());
        sparkReporter.config().setDocumentTitle("Selenium Framework Report");
        sparkReporter.config().setReportName(suiteName);
        sparkReporter.config().setTheme(Theme.STANDARD);

        extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);
        extentReports.setSystemInfo("Project", "Selenium Java UI Automation Learning Framework");
        extentReports.setSystemInfo("Module", "Module 15 - Parallel Execution and Selenium Grid");

        LOGGER.info("Initialized Extent report at {}", REPORT_PATH.toAbsolutePath());
    }

    public static synchronized void startTest(ITestResult result, String displayName) {
        ExtentTest extentTest = extentReports.createTest(displayName)
                .assignCategory(result.getMethod().getGroups());

        CURRENT_TEST.set(extentTest);
    }

    public static synchronized void pass(String message) {
        log(Status.PASS, message);
        CURRENT_TEST.remove();
    }

    public static synchronized void fail(Throwable throwable, Path screenshotPath) {
        ExtentTest currentTest = CURRENT_TEST.get();
        if (currentTest != null) {
            currentTest.fail(throwable);
            attachScreenshot(currentTest, screenshotPath);
        }
        CURRENT_TEST.remove();
    }

    public static synchronized void skip(Throwable throwable) {
        ExtentTest currentTest = CURRENT_TEST.get();
        if (currentTest != null) {
            currentTest.skip(throwable);
        }
        CURRENT_TEST.remove();
    }

    public static synchronized void flush() {
        if (extentReports != null) {
            extentReports.flush();
            LOGGER.info("Flushed Extent report to {}", REPORT_PATH.toAbsolutePath());
            extentReports = null;
        }
    }

    private static void log(Status status, String message) {
        ExtentTest currentTest = CURRENT_TEST.get();
        if (currentTest != null) {
            currentTest.log(status, message);
        }
    }

    private static void attachScreenshot(ExtentTest currentTest, Path screenshotPath) {
        if (screenshotPath == null) {
            return;
        }

        currentTest.addScreenCaptureFromPath(screenshotPath.toString(), "Failure screenshot");
    }
}
