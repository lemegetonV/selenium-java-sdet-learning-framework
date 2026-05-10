package com.learning.tests.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import com.learning.framework.config.ConfigReader;

/**
 * Optional retry rule for unstable UI tests.
 *
 * The default retryCount is 0 because retries can hide real product or
 * framework bugs. Learners can enable one retry with -DretryCount=1 to see how
 * TestNG asks this class whether a failed test should run again.
 */
public class FrameworkRetryAnalyzer implements IRetryAnalyzer {

    private static final Logger LOGGER = LogManager.getLogger(FrameworkRetryAnalyzer.class);

    private int attempts;

    @Override
    public boolean retry(ITestResult result) {
        int retryLimit = Math.max(ConfigReader.getRetryCount(), 0);

        if (attempts < retryLimit) {
            attempts++;
            LOGGER.warn("Retrying {}. Attempt {} of {}",
                    result.getMethod().getMethodName(), attempts, retryLimit);
            return true;
        }

        return false;
    }
}
