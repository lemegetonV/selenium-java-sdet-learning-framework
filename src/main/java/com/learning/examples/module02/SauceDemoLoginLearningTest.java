package com.learning.examples.module02;

/**
 * Concrete learning test that specializes LearningTestTemplate.
 *
 * The parent class owns the lifecycle. This child class owns the specific
 * login behavior. That relationship prepares for future TestNG classes that
 * inherit shared setup from BaseTest.
 */
public class SauceDemoLoginLearningTest extends LearningTestTemplate {

    private final LoginCredentials credentials;
    private final LoginPageModel loginPage;

    public SauceDemoLoginLearningTest(BrowserDriver browserDriver, String baseUrl, LoginCredentials credentials) {
        super(browserDriver, baseUrl);
        this.credentials = credentials;
        this.loginPage = new LoginPageModel();
    }

    @Override
    protected void executeTest() {
        System.out.println("Running login learning flow on " + browserDriver.getBrowserName());

        for (String action : loginPage.loginWith(credentials)) {
            System.out.println("- " + action);
        }
    }
}
