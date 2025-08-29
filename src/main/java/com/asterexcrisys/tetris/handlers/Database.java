package com.asterexcrisys.tetris.handlers;

import com.asterexcrisys.tetris.constants.LeaderboardConstants;
import com.asterexcrisys.tetris.types.ConnectionState;
import com.asterexcrisys.tetris.types.QueryParameter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database implements AutoCloseable {

    private static final Logger LOGGER = Logger.getLogger(Database.class.getName());

    private final Path path;
    private Connection connection;
    private ConnectionState state;

    public Database(String path) {
        this.path = Paths.get(Objects.requireNonNull(path));
        connection = null;
        state = ConnectionState.IDLE;
    }

    public ConnectionState state() {
        return state;
    }

    public boolean connect() {
        if (state != ConnectionState.IDLE) {
            return false;
        }
        try {
            Files.createDirectories(path.getParent());
            Class.forName(LeaderboardConstants.JDBC_DRIVER);
            connection = DriverManager.getConnection("jdbc:sqlite:%s".formatted(path.toAbsolutePath().toString()));
            state = ConnectionState.ACTIVE;
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, exception.getMessage(), exception);
            return false;
        }
        return true;
    }

    public boolean disconnect() {
        if (state != ConnectionState.ACTIVE) {
            return false;
        }
        try {
            connection.close();
            connection = null;
            state = ConnectionState.INACTIVE;
        } catch (SQLException exception) {
            LOGGER.log(Level.SEVERE, exception.getMessage(), exception);
            return false;
        }
        return true;
    }

    public boolean beginTransaction() {
        if (state != ConnectionState.ACTIVE) {
            return false;
        }
        try {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            return true;
        } catch (SQLException exception) {
            LOGGER.log(Level.WARNING, exception.getMessage(), exception);
            return false;
        }
    }

    public boolean endTransaction(boolean shouldCommit) {
        if (state != ConnectionState.ACTIVE) {
            return false;
        }
        try {
            if (shouldCommit) {
                connection.commit();
            } else {
                connection.rollback();
            }
            return true;
        } catch (SQLException exception) {
            LOGGER.log(Level.WARNING, exception.getMessage(), exception);
            return false;
        }
    }

    public Optional<ResultSet> executeQuery(String query, QueryParameter... parameters) {
        if (state != ConnectionState.ACTIVE) {
            return Optional.empty();
        }
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            for (int i = 0; i < parameters.length; i++) {
                parseQueryParameter(statement, i + 1, parameters[i]);
            }
            return Optional.ofNullable(statement.executeQuery());
        } catch (SQLException exception) {
            LOGGER.log(Level.WARNING, exception.getMessage(), exception);
            return Optional.empty();
        }
    }

    public Optional<Integer> executeUpdate(String query, QueryParameter... parameters) {
        if (state != ConnectionState.ACTIVE) {
            return Optional.empty();
        }
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            for (int i = 0; i < parameters.length; i++) {
                parseQueryParameter(statement, i + 1, parameters[i]);
            }
            return Optional.of(statement.executeUpdate());
        } catch (SQLException exception) {
            LOGGER.log(Level.WARNING, exception.getMessage(), exception);
            return Optional.empty();
        }
    }

    public void reset() {
        if (state == ConnectionState.IDLE || state == ConnectionState.DISPOSED) {
            return;
        }
        disconnect();
        state = ConnectionState.IDLE;
    }

    @Override
    public void close() {
        if (state == ConnectionState.DISPOSED) {
            return;
        }
        disconnect();
        state = ConnectionState.DISPOSED;
    }

    private void parseQueryParameter(PreparedStatement statement, int index, QueryParameter parameter) throws SQLException {
        Object value = parameter.value();
        if (value == null) {
            statement.setNull(index, Types.NULL);
            return;
        }
        switch (parameter.type()) {
            case INTEGER, SMALLINT, TINYINT -> statement.setInt(index, ((Number) value).intValue());
            case BIGINT -> statement.setLong(index, ((Number) value).longValue());
            case FLOAT, REAL -> statement.setFloat(index, ((Number) value).floatValue());
            case DOUBLE -> statement.setDouble(index, ((Number) value).doubleValue());
            case BOOLEAN, BIT -> statement.setBoolean(index, (Boolean) value);
            case BINARY, VARBINARY, LONGVARBINARY, BLOB -> statement.setBytes(index, (byte[]) value);
            case DATE -> statement.setDate(index, (Date) value);
            case TIME -> statement.setTime(index, (Time) value);
            case TIMESTAMP, TIMESTAMP_WITH_TIMEZONE -> statement.setTimestamp(index, (Timestamp) value);
            case NULL -> statement.setNull(index, Types.NULL);
            default -> statement.setString(index, value.toString());
        }
    }

}