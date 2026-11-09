package com.itzsrikanth.streamforge.persist;


import com.itzsrikanth.streamforge.model.Tick;

import java.sql.*;

public class Postgres implements AutoCloseable {

    private static final String URL = "jdbc:postgresql://localhost:5432/streamforge_listener";

    private static final String INSERT_QUERY = """
            INSERT INTO tick (symbol, price, qty, event_time, ingest_time)
                                          VALUES (?, ?, ?, ?, ?)
            """;

    private final Connection connection;
    private final PreparedStatement statement;

    public Postgres() {
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        try {
            this.connection = DriverManager.getConnection(URL, user, password);
            this.statement = connection.prepareStatement(INSERT_QUERY);
        } catch (SQLException e) {
            throw new IllegalStateException("failed to open postgres", e);
        }
    }

    public void persist(Tick value) {
        try {
            statement.setString(1, value.symbol());
            statement.setBigDecimal(2, value.price());
            statement.setBigDecimal(3, value.qty());
            statement.setTimestamp(4, Timestamp.from(value.eventTime()));
            statement.setTimestamp(5, Timestamp.from(value.ingestTime()));
            int rows = statement.executeUpdate();
            System.out.println("inserted rows: " + rows);
        } catch (SQLException e) {
            throw new RuntimeException("failed to persist for " + value.symbol(), e);
        }
    }

    @Override
    public void close() throws Exception {
        try {
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException("failed to close statement", e);
        }
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("failed to close conection", e);
        }
    }
}
