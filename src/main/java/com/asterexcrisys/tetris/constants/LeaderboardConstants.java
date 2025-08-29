package com.asterexcrisys.tetris.constants;

public final class LeaderboardConstants {

    public static final String JDBC_DRIVER = "org.sqlite.JDBC";
    public static final String CREATE_TABLE_QUERY = """
            CREATE TABLE IF NOT EXISTS records (
                record_id INTEGER PRIMARY KEY AUTOINCREMENT,
                player TEXT UNIQUE NOT NULL,
                level INTEGER NOT NULL,
                score INTEGER NOT NULL
            )""";
    public static final String SELECT_RECORDS_QUERY = """
            SELECT RANK() OVER(ORDER BY level DESC, score DESC) AS rank, player, level, score
            FROM records
            ORDER BY RANK ASC
            LIMIT ?
            """;
    public static final String UPSERT_RECORD_QUERY = """
            INSERT INTO records (player, level, score)
            VALUES (?, ?, ?)
            ON CONFLICT(player) DO UPDATE
            SET level = excluded.level, score = excluded.score
            WHERE excluded.level > records.level OR (excluded.level = records.level AND excluded.score > records.score);
            """;

}