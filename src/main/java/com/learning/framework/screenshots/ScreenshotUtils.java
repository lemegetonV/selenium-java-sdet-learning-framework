package com.learning.framework.screenshots;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.learning.framework.exceptions.FrameworkException;

/**
 * Central screenshot utility for the framework.
 *
 * Selenium exposes screenshots through the TakesScreenshot interface. Keeping
 * this logic here prevents listeners, tests, and future reporting code from
 * repeating file naming, folder creation, and exception handling rules.
 */
public final class ScreenshotUtils {

    private static final Path SCREENSHOT_DIRECTORY = Paths.get("target", "screenshots");
    private static final DateTimeFormatter FILE_TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS");

    private ScreenshotUtils() {
        // Utility class: do not instantiate.
    }

    public static Path capture(WebDriver driver, String logicalName) {
        if (!(driver instanceof TakesScreenshot screenshotDriver)) {
            throw new FrameworkException("Current WebDriver does not support screenshots: "
                    + driver.getClass().getName());
        }

        try {
            Files.createDirectories(SCREENSHOT_DIRECTORY);

            /*
             * Selenium first writes the screenshot to a temporary file. The
             * framework chooses the final target path so screenshots have names
             * connected to the failing test.
             */
            File sourceFile = screenshotDriver.getScreenshotAs(OutputType.FILE);
            Path targetFile = SCREENSHOT_DIRECTORY.resolve(fileNameFor(logicalName));
            Files.copy(sourceFile.toPath(), targetFile, StandardCopyOption.REPLACE_EXISTING);
            return targetFile.toAbsolutePath();
        } catch (IOException exception) {
            throw new FrameworkException("Unable to save screenshot for: " + logicalName, exception);
        }
    }

    private static String fileNameFor(String logicalName) {
        return LocalDateTime.now().format(FILE_TIMESTAMP) + "-" + sanitize(logicalName) + ".png";
    }

    private static String sanitize(String value) {
        String sanitized = value.replaceAll("[^a-zA-Z0-9._-]", "_");
        if (sanitized.isBlank()) {
            return "screenshot";
        }
        return sanitized;
    }
}
