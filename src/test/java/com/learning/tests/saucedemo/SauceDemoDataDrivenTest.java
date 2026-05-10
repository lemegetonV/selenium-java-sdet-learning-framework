package com.learning.tests.saucedemo;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.learning.framework.pages.saucedemo.LoginPage;
import com.learning.framework.pages.saucedemo.ProductsPage;
import com.learning.tests.base.BaseTest;
import com.learning.tests.dataproviders.LoginDataProviders;
import com.learning.tests.models.LoginScenario;

import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;

/**
 * SauceDemo login tests driven by TestNG DataProviders.
 *
 * The test logic stays the same while data sources change. That is the core
 * value of data-driven testing: separate scenario rows from automation flow.
 */
@Epic("SauceDemo UI")
@Feature("Data-driven login")
public class SauceDemoDataDrivenTest extends BaseTest {

    @Test(dataProvider = "hardcodedLoginScenarios", dataProviderClass = LoginDataProviders.class,
            groups = {"smoke", "regression"})
    @Story("Hardcoded DataProvider rows")
    @Severity(SeverityLevel.CRITICAL)
    public void loginWorksWithHardcodedDataProvider(LoginScenario scenario) {
        runLoginScenario(scenario);
    }

    @Test(dataProvider = "jsonLoginScenarios", dataProviderClass = LoginDataProviders.class,
            groups = "regression")
    @Story("JSON login data")
    @Severity(SeverityLevel.NORMAL)
    public void loginWorksWithJsonDataProvider(LoginScenario scenario) {
        runLoginScenario(scenario);
    }

    @Test(dataProvider = "csvLoginScenarios", dataProviderClass = LoginDataProviders.class,
            groups = "regression")
    @Story("CSV login data")
    @Severity(SeverityLevel.NORMAL)
    public void loginWorksWithCsvDataProvider(LoginScenario scenario) {
        runLoginScenario(scenario);
    }

    @Test(dataProvider = "excelLoginScenarios", dataProviderClass = LoginDataProviders.class,
            groups = "regression")
    @Story("Excel login data")
    @Severity(SeverityLevel.NORMAL)
    public void loginWorksWithExcelDataProvider(LoginScenario scenario) {
        runLoginScenario(scenario);
    }

    private void runLoginScenario(LoginScenario scenario) {
        Allure.step("Run login scenario: " + scenario.scenarioName());
        Allure.step("Open SauceDemo login page");
        LoginPage loginPage = new LoginPage(driver, elementActions, waits).open();

        if (scenario.successfulLogin()) {
            Allure.step("Submit valid credentials and verify product page");
            ProductsPage productsPage = loginPage.loginAs(scenario.username(), scenario.password());
            Assert.assertEquals(productsPage.getTitle(), scenario.expectedMessage(), scenario.scenarioName());
        } else {
            Allure.step("Submit invalid credentials and verify login error");
            loginPage.loginExpectingError(scenario.username(), scenario.password());
            Assert.assertTrue(
                    loginPage.getErrorMessage().contains(scenario.expectedMessage()),
                    scenario.scenarioName()
            );
        }
    }
}
