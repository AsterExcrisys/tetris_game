package com.asterexcrisys.tetris.controllers;

import com.asterexcrisys.tetris.constants.ResourceConstants;
import com.asterexcrisys.tetris.services.RankingManager;
import com.asterexcrisys.tetris.types.Record;
import com.asterexcrisys.tetris.utilities.GlobalUtility;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class LeaderboardController implements BaseController {

    @FXML
    private TableView<Record> leaderboardTable;

    @FXML
    private TableColumn<Record, Integer> rankColumn;

    @FXML
    private TableColumn<Record, String> playerColumn;

    @FXML
    private TableColumn<Record, Integer> levelColumn;

    @FXML
    private TableColumn<Record, Integer> scoreColumn;

    private static final Logger LOGGER = Logger.getLogger(LeaderboardController.class.getName());

    private final RankingManager manager;
    private Stage stage;

    public LeaderboardController() {
        manager = new RankingManager();
        stage = null;
    }

    public void setStage(Stage stage) {
        this.stage = Objects.requireNonNull(stage);
    }

    @FXML
    private void initialize() {
        rankColumn.setCellValueFactory((data) -> data.getValue().rankProperty().asObject());
        playerColumn.setCellValueFactory((data) -> data.getValue().playerProperty());
        levelColumn.setCellValueFactory((data) -> data.getValue().levelProperty().asObject());
        scoreColumn.setCellValueFactory((data) -> data.getValue().scoreProperty().asObject());
        leaderboardTable.getItems().setAll(manager.getRecords(10));
    }

    @FXML
    private void onUpdateButtonClick() {
        leaderboardTable.getItems().setAll(manager.getRecords(10));
    }

    @FXML
    private void onBackButtonClick() {
        manager.close();
        try {
            GlobalUtility.changeView(ResourceConstants.GAME_VIEW, stage);
        } catch (Exception exception) {
            LOGGER.log(Level.WARNING, exception.getMessage(), exception);
        }
    }

}