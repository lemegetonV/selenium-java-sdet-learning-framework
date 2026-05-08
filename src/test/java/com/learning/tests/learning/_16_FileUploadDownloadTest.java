package com.learning.tests.learning;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Introduces file upload with sendKeys and simple download validation.
 */
public class _16_FileUploadDownloadTest {

    @Test
    public void uploadsFileThroughInputElement() {
        WebDriver driver = createChromeDriver();

        try {
            driver.get(module07FixtureUrl("advanced-interactions.html"));

            Path uploadFile = Path.of("src/test/resources/module07/upload-sample.txt").toAbsolutePath();
            WebElement fileInput = driver.findElement(By.id("upload-file"));

            /*
             * Selenium uploads files by sending the absolute file path to an
             * <input type="file"> element. It does not automate the OS file picker.
             */
            fileInput.sendKeys(uploadFile.toString());

            Assert.assertEquals(driver.findElement(By.id("uploaded-file-name")).getText(), "upload-sample.txt");
        } finally {
            driver.quit();
        }
    }

    @Test
    public void downloadsFileAndValidatesItExists() throws IOException {
        Path downloadDirectory = Files.createTempDirectory("module07-downloads-");
        WebDriver driver = createChromeDriver(downloadDirectory);

        try {
            driver.get(module07FixtureUrl("advanced-interactions.html"));
            driver.findElement(By.id("download-link")).click();

            Path downloadedFile = downloadDirectory.resolve("module07-download.txt");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            /*
             * Download completion is browser-side state, so the test waits for the
             * file system effect instead of assuming the click finished the write.
             */
            wait.until(currentDriver -> Files.exists(downloadedFile));

            Assert.assertEquals(Files.readString(downloadedFile), "Downloaded from Module 07");
        } finally {
            driver.quit();
            deleteIfExists(downloadDirectory.resolve("module07-download.txt"));
            deleteIfExists(downloadDirectory);
        }
    }

    private String module07FixtureUrl(String fileName) {
        return Path.of("src/test/resources/module07", fileName).toUri().toString();
    }

    private WebDriver createChromeDriver() {
        return createChromeDriver(null);
    }

    private WebDriver createChromeDriver(Path downloadDirectory) {
        ChromeOptions options = new ChromeOptions();

        if (Boolean.parseBoolean(System.getProperty("headless", "true"))) {
            options.addArguments("--headless=new");
        }

        options.addArguments("--window-size=1440,900");

        if (downloadDirectory != null) {
            Map<String, Object> preferences = new HashMap<>();
            preferences.put("download.default_directory", downloadDirectory.toString());
            preferences.put("download.prompt_for_download", false);
            options.setExperimentalOption("prefs", preferences);
        }

        return new ChromeDriver(options);
    }

    private void deleteIfExists(Path path) throws IOException {
        if (Files.exists(path)) {
            Files.delete(path);
        }
    }
}
