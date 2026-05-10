package com.learning.tests.listeners;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.learning.framework.driver.DriverFactory;
import com.learning.framework.screenshots.ScreenshotUtils;
import com.learning.tests.models.LoginScenario;
import com.learning.tests.reports.AllureReportUtils;
import com.learning.tests.reports.ExtentReportManager;

/**
 * TestNG listener that records lifecycle events around each test method.
 *
 * A listener is framework code that TestNG calls automatically. Tests stay
 * focused on assertions, while cross-cutting diagnostics such as logging,
 * screenshots, and future report attachments live in one place.
 */
public class FrameworkTestListener implements ITestListener {

    private static final Logger LOGGER = LogManager.getLogger(FrameworkTestListener.class);
    public static final String SCREENSHOT_PATH_ATTRIBUTE = "screenshotPath";

    @Override
    public void onStart(ITestContext context) {
        ExtentReportManager.initialize(context.getSuite().getName());
        LOGGER.info("Starting TestNG context: {}", context.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = displayName(result);

        /*
         * ThreadContext is Log4j2's per-thread diagnostic map. It prepares the
         * framework for parallel execution because each test thread can carry
         * its own testName into every log line.
         */
        ThreadContext.put("testName", testName);
        ExtentReportManager.startTest(result, testName);
        LOGGER.info("START {}", testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LOGGER.info("PASS {}", displayName(result));
        ExtentReportManager.pass("Test passed");
        ThreadContext.clearMap();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = displayName(result);
        LOGGER.error("FAIL {}", testName, result.getThrowable());
        Optional<Path> screenshotPath = captureFailureScreenshot(result, testName);
        ExtentReportManager.fail(result.getThrowable(), screenshotPath.orElse(null));
        screenshotPath.ifPresent(AllureReportUtils::attachScreenshot);
        ThreadContext.clearMap();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LOGGER.warn("SKIP {}", displayName(result), result.getThrowable());
        ExtentReportManager.skip(result.getThrowable());
        ThreadContext.clearMap();
    }

    @Override
    public void onFinish(ITestContext context) {
        LOGGER.info("Finished TestNG context: {}", context.getName());
        ExtentReportManager.flush();
    }

    private Optional<Path> captureFailureScreenshot(ITestResult result, String testName) {
        try {
            Path screenshotPath = ScreenshotUtils.capture(DriverFactory.getDriver(), testName);
            result.setAttribute(SCREENSHOT_PATH_ATTRIBUTE, screenshotPath.toString());

            /*
             * Reporter.log writes into TestNG's own report output. Module 14
             * will attach the same screenshot path to richer HTML reports.
             */
            Reporter.log("Failure screenshot: " + screenshotPath, true);
            LOGGER.info("Saved failure screenshot to {}", screenshotPath);
            return Optional.of(screenshotPath);
        } catch (RuntimeException exception) {
            LOGGER.warn("Could not capture failure screenshot for {}", testName, exception);
            return Optional.empty();
        }
    }

    private String displayName(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        String parameters = Arrays.stream(result.getParameters())
                .map(this::safeParameterName)
                .collect(Collectors.joining(", "));

        if (parameters.isBlank()) {
            return methodName;
        }

        return methodName + "[" + parameters + "]";
    }

    private String safeParameterName(Object parameter) {
        if (parameter instanceof LoginScenario scenario) {
            /*
             * LoginScenario contains a password. Logs should identify the data
             * row without dumping credentials into console, files, or reports.
             */
            return scenario.scenarioName();
        }

        return String.valueOf(parameter);
    }
}
