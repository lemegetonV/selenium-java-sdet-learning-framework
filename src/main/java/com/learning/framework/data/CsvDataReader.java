package com.learning.framework.data;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Small CSV reader for module test data.
 *
 * The reader intentionally supports a simple, deterministic CSV shape: one
 * header row and comma-separated values without embedded commas. That is enough
 * for Module 12 login data. A production framework could replace this with a
 * dedicated CSV library when more complex CSV rules are needed.
 */
public final class CsvDataReader {

    private CsvDataReader() {
        // Utility class: do not instantiate.
    }

    public static List<Map<String, String>> readRows(String classpathResource) {
        try {
            java.net.URL resource = ClassLoader.getSystemResource(classpathResource);
            if (resource == null) {
                throw new IllegalArgumentException("CSV resource not found: " + classpathResource);
            }

            Path path = Path.of(resource.toURI());
            List<String> lines = Files.readAllLines(path);

            if (lines.isEmpty()) {
                throw new IllegalArgumentException("CSV file is empty: " + classpathResource);
            }

            String[] headers = splitLine(lines.get(0));
            List<Map<String, String>> rows = new ArrayList<>();

            for (int index = 1; index < lines.size(); index++) {
                String line = lines.get(index);
                if (line.isBlank()) {
                    continue;
                }

                String[] values = splitLine(line);
                if (values.length != headers.length) {
                    throw new IllegalArgumentException("CSV row " + (index + 1)
                            + " has " + values.length + " values but expected " + headers.length);
                }

                Map<String, String> row = new LinkedHashMap<>();
                for (int column = 0; column < headers.length; column++) {
                    row.put(headers[column], values[column]);
                }
                rows.add(row);
            }

            return rows;
        } catch (IOException exception) {
            throw new UncheckedIOException("Unable to read CSV resource: " + classpathResource, exception);
        } catch (URISyntaxException exception) {
            throw new IllegalArgumentException("Invalid CSV resource path: " + classpathResource, exception);
        }
    }

    private static String[] splitLine(String line) {
        return line.split(",", -1);
    }
}
