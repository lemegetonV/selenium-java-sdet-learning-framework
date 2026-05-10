package com.learning.tests.reports;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.qameta.allure.Allure;

/**
 * Small adapter around Allure's static attachment API.
 *
 * Keeping Allure calls here prevents the TestNG listener from knowing file
 * stream details. Module 14 keeps report integration thin so later modules can
 * reuse the same attachment behavior in Cucumber or CI.
 */
public final class AllureReportUtils {

    private static final Logger LOGGER = LogManager.getLogger(AllureReportUtils.class);

    private AllureReportUtils() {
        // Utility class: do not instantiate.
    }

    public static void attachScreenshot(Path screenshotPath) {
        if (screenshotPath == null) {
            return;
        }

        try (InputStream screenshotStream = Files.newInputStream(screenshotPath)) {
            Allure.addAttachment("Failure screenshot", "image/png", screenshotStream, ".png");
        } catch (IOException exception) {
            LOGGER.warn("Could not attach screenshot to Allure: {}", screenshotPath, exception);
        }
    }
}
