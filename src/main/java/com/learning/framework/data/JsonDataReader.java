package com.learning.framework.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JSON data reader backed by Jackson.
 *
 * Module 12 uses a real JSON parser instead of string manipulation so learners
 * see how framework code should treat structured data.
 */
public final class JsonDataReader {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JsonDataReader() {
        // Utility class: do not instantiate.
    }

    public static <T> List<T> readList(String classpathResource, TypeReference<List<T>> typeReference) {
        try (InputStream inputStream = ClassLoader.getSystemResourceAsStream(classpathResource)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("JSON resource not found: " + classpathResource);
            }
            return OBJECT_MAPPER.readValue(inputStream, typeReference);
        } catch (IOException exception) {
            throw new UncheckedIOException("Unable to read JSON resource: " + classpathResource, exception);
        }
    }
}
