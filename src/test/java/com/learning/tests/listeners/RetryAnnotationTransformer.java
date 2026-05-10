package com.learning.tests.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import com.learning.framework.config.ConfigReader;

/**
 * Attaches the retry analyzer to TestNG tests when retries are enabled.
 *
 * Without a transformer, every @Test method would need
 * retryAnalyzer = FrameworkRetryAnalyzer.class. The transformer keeps retry
 * policy central, which is the same design direction used by listeners and
 * reporting modules.
 */
public class RetryAnnotationTransformer implements IAnnotationTransformer {

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        if (ConfigReader.getRetryCount() > 0 && annotation.getRetryAnalyzerClass() == null) {
            annotation.setRetryAnalyzer(FrameworkRetryAnalyzer.class);
        }
    }
}
