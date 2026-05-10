package com.learning.tests.dataproviders;

import java.util.List;
import java.util.Map;

import org.testng.annotations.DataProvider;

import com.fasterxml.jackson.core.type.TypeReference;
import com.learning.framework.data.CsvDataReader;
import com.learning.framework.data.ExcelDataReader;
import com.learning.framework.data.JsonDataReader;
import com.learning.tests.models.LoginScenario;

/**
 * TestNG DataProviders for SauceDemo login scenarios.
 *
 * DataProvider methods convert different data sources into the same Java shape
 * expected by the tests. This keeps test methods focused on behavior instead
 * of file parsing.
 */
public final class LoginDataProviders {

    private static final String JSON_LOGIN_DATA = "testdata/login-data.json";
    private static final String CSV_LOGIN_DATA = "testdata/login-data.csv";
    private static final String EXCEL_LOGIN_DATA = "testdata/login-data.xlsx";
    private static final String EXCEL_LOGIN_SHEET = "login";

    private LoginDataProviders() {
        // Utility class: TestNG calls static provider methods.
    }

    @DataProvider(name = "hardcodedLoginScenarios")
    public static Object[][] hardcodedLoginScenarios() {
        return new Object[][] {
                {
                        new LoginScenario(
                                "standard user hardcoded login",
                                "standard_user",
                                "secret_sauce",
                                true,
                                "Products"
                        )
                },
                {
                        new LoginScenario(
                                "locked out hardcoded login",
                                "locked_out_user",
                                "secret_sauce",
                                false,
                                "locked out"
                        )
                }
        };
    }

    @DataProvider(name = "jsonLoginScenarios")
    public static Object[][] jsonLoginScenarios() {
        List<LoginScenario> scenarios = JsonDataReader.readList(
                JSON_LOGIN_DATA,
                new TypeReference<>() {
                }
        );
        return toDataProviderRows(scenarios);
    }

    @DataProvider(name = "csvLoginScenarios")
    public static Object[][] csvLoginScenarios() {
        List<LoginScenario> scenarios = CsvDataReader.readRows(CSV_LOGIN_DATA)
                .stream()
                .map(LoginDataProviders::toLoginScenario)
                .toList();
        return toDataProviderRows(scenarios);
    }

    @DataProvider(name = "excelLoginScenarios")
    public static Object[][] excelLoginScenarios() {
        List<LoginScenario> scenarios = ExcelDataReader.readRows(EXCEL_LOGIN_DATA, EXCEL_LOGIN_SHEET)
                .stream()
                .map(LoginDataProviders::toLoginScenario)
                .toList();
        return toDataProviderRows(scenarios);
    }

    private static Object[][] toDataProviderRows(List<LoginScenario> scenarios) {
        Object[][] rows = new Object[scenarios.size()][1];

        for (int index = 0; index < scenarios.size(); index++) {
            rows[index][0] = scenarios.get(index);
        }

        return rows;
    }

    private static LoginScenario toLoginScenario(Map<String, String> row) {
        return new LoginScenario(
                row.get("scenarioName"),
                row.get("username"),
                row.get("password"),
                Boolean.parseBoolean(row.get("successfulLogin")),
                row.get("expectedMessage")
        );
    }
}
