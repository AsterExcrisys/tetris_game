package com.asterexcrisys.tetris.types;

import java.sql.JDBCType;
import java.util.Objects;

public record QueryParameter(Object value, JDBCType type) {

    public QueryParameter {
        Objects.requireNonNull(value);
        Objects.requireNonNull(type);
    }

    public static QueryParameter of(Object value, JDBCType type) {
        return new QueryParameter(value, type);
    }

}