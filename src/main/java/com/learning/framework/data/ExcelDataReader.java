package com.learning.framework.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Excel reader for .xlsx test data files.
 *
 * Apache POI reads workbook structure instead of treating Excel as text.
 * Module 12 uses it to demonstrate how external business-style test data can
 * become Java rows for TestNG DataProviders.
 */
public final class ExcelDataReader {

    private ExcelDataReader() {
        // Utility class: do not instantiate.
    }

    public static List<Map<String, String>> readRows(String classpathResource, String sheetName) {
        try (InputStream inputStream = ClassLoader.getSystemResourceAsStream(classpathResource)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Excel resource not found: " + classpathResource);
            }

            try (Workbook workbook = new XSSFWorkbook(inputStream)) {
                Sheet sheet = workbook.getSheet(sheetName);
                if (sheet == null) {
                    throw new IllegalArgumentException("Sheet '" + sheetName + "' not found in " + classpathResource);
                }

                return rowsFromSheet(sheet);
            }
        } catch (IOException exception) {
            throw new UncheckedIOException("Unable to read Excel resource: " + classpathResource, exception);
        }
    }

    private static List<Map<String, String>> rowsFromSheet(Sheet sheet) {
        DataFormatter formatter = new DataFormatter();
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            throw new IllegalArgumentException("Excel sheet has no header row: " + sheet.getSheetName());
        }

        List<String> headers = new ArrayList<>();
        headerRow.forEach(cell -> headers.add(formatter.formatCellValue(cell)));

        List<Map<String, String>> rows = new ArrayList<>();
        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                continue;
            }

            Map<String, String> rowValues = new LinkedHashMap<>();
            for (int column = 0; column < headers.size(); column++) {
                rowValues.put(headers.get(column), formatter.formatCellValue(row.getCell(column)));
            }
            rows.add(rowValues);
        }
        return rows;
    }
}
