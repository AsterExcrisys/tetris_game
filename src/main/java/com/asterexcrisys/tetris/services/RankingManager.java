package com.asterexcrisys.tetris.services;

import com.asterexcrisys.tetris.constants.LeaderboardConstants;
import com.asterexcrisys.tetris.constants.ResourceConstants;
import com.asterexcrisys.tetris.handlers.Database;
import com.asterexcrisys.tetris.types.QueryParameter;
import com.asterexcrisys.tetris.types.Record;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RankingManager implements AutoCloseable {

    private static final Logger LOGGER = Logger.getLogger(RankingManager.class.getName());

    private final Database database;

    public RankingManager() {
        database = new Database(ResourceConstants.LEADERBOARD_DATABASE);
        database.connect();
        database.executeUpdate(LeaderboardConstants.CREATE_TABLE_QUERY);
    }

    public List<Record> getRecords(int limit) {
        Optional<ResultSet> optional = database.executeQuery(LeaderboardConstants.SELECT_RECORDS_QUERY, QueryParameter.of(limit, JDBCType.INTEGER));
        if (optional.isEmpty()) {
            return Collections.emptyList();
        }
        List<Record> records = new ArrayList<>();
        try (ResultSet result = optional.get()) {
            while (result.next()) {
                records.add(Record.of(
                        result.getInt("rank"),
                        result.getString("player"),
                        result.getInt("level"),
                        result.getInt("score")
                ));
            }
        } catch (SQLException exception) {
            LOGGER.log(Level.WARNING, exception.getMessage(), exception);
            return Collections.emptyList();
        }
        return records;
    }

    public boolean putRecord(Record record) {
        Optional<Integer> optional = database.executeUpdate(
                LeaderboardConstants.UPSERT_RECORD_QUERY,
                QueryParameter.of(record.player(), JDBCType.VARCHAR),
                QueryParameter.of(record.level(), JDBCType.INTEGER),
                QueryParameter.of(record.score(), JDBCType.INTEGER)
        );
        return optional.filter((result) -> result == 1).isPresent();
    }

    public void reset() {
        database.reset();
        database.connect();
    }

    @Override
    public void close() {
        database.close();
    }

}