package com.learning.examples.module02;

/**
 * Concrete learning test that specializes _07_LearningTestTemplate.
 *
 * The parent class owns the lifecycle. This child class owns the specific
 * login behavior. That relationship prepares for future TestNG classes that
 * inherit shared setup from BaseTest.
 */
public class _08_SauceDemoLoginLearningTest extends _07_LearningTestTemplate {

    private final _05_LoginCredentials credentials;
    private final _06_LoginPageModel loginPage;

    public _08_SauceDemoLoginLearningTest(_01_BrowserDriver browserDriver, String baseUrl, _05_LoginCredentials credentials) {
        super(browserDriver, baseUrl);
        this.credentials = credentials;
        this.loginPage = new _06_LoginPageModel();
    }

    @Override
    protected void executeTest() {
        System.out.println("Running login learning flow on " + browserDriver.getBrowserName());

        for (String action : loginPage.loginWith(credentials)) {
            System.out.println("- " + action);
        }
    }
}
