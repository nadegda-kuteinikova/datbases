package com.greenhouse.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class SchemaInitializer {

    private static final String SCHEMA_FILE = "schema.sql";

    private SchemaInitializer() {
    }

    public static void initialize() throws SQLException {

        String sql = loadSchema();

        try (Connection connection =
                     ConnectionManager.getConnection();

             Statement statement =
                     connection.createStatement()) {

            statement.execute(sql);
        }
    }

    private static String loadSchema() {

        try (InputStream is =
                     SchemaInitializer.class
                             .getClassLoader()
                             .getResourceAsStream(SCHEMA_FILE)) {

            if (is == null) {
                throw new RuntimeException(
                        "File schema.sql not found!");
            }

            try (BufferedReader reader =
                         new BufferedReader(
                                 new InputStreamReader(
                                         is,
                                         StandardCharsets.UTF_8))) {

                return reader.lines()
                        .collect(Collectors.joining("\n"));
            }

        } catch (IOException e) {
            throw new RuntimeException(
                    "Error reading schema.sql",
                    e);
        }
    }
}
