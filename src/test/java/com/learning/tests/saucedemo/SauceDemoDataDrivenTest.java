package com.learning.tests.saucedemo;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.learning.framework.pages.saucedemo.LoginPage;
import com.learning.framework.pages.saucedemo.ProductsPage;
import com.learning.tests.base.BaseTest;
import com.learning.tests.dataproviders.LoginDataProviders;
import com.learning.tests.models.LoginScenario;

/**
 * SauceDemo login tests driven by TestNG DataProviders.
 *
 * The test logic stays the same while data sources change. That is the core
 * value of data-driven testing: separate scenario rows from automation flow.
 */
public class SauceDemoDataDrivenTest extends BaseTest {

    @Test(dataProvider = "hardcodedLoginScenarios", dataProviderClass = LoginDataProviders.class,
            groups = {"smoke", "regression"})
    public void loginWorksWithHardcodedDataProvider(LoginScenario scenario) {
        runLoginScenario(scenario);
    }

    @Test(dataProvider = "jsonLoginScenarios", dataProviderClass = LoginDataProviders.class,
            groups = "regression")
    public void loginWorksWithJsonDataProvider(LoginScenario scenario) {
        runLoginScenario(scenario);
    }

    @Test(dataProvider = "csvLoginScenarios", dataProviderClass = LoginDataProviders.class,
            groups = "regression")
    public void loginWorksWithCsvDataProvider(LoginScenario scenario) {
        runLoginScenario(scenario);
    }

    @Test(dataProvider = "excelLoginScenarios", dataProviderClass = LoginDataProviders.class,
            groups = "regression")
    public void loginWorksWithExcelDataProvider(LoginScenario scenario) {
        runLoginScenario(scenario);
    }

    private void runLoginScenario(LoginScenario scenario) {
        LoginPage loginPage = new LoginPage(driver, elementActions, waits).open();

        if (scenario.successfulLogin()) {
            ProductsPage productsPage = loginPage.loginAs(scenario.username(), scenario.password());
            Assert.assertEquals(productsPage.getTitle(), scenario.expectedMessage(), scenario.scenarioName());
        } else {
            loginPage.loginExpectingError(scenario.username(), scenario.password());
            Assert.assertTrue(
                    loginPage.getErrorMessage().contains(scenario.expectedMessage()),
                    scenario.scenarioName()
            );
        }
    }
}
