package com.asterexcrisys.tetris.controllers;

import com.asterexcrisys.tetris.MainApplication;
import com.asterexcrisys.tetris.constants.ResourceConstants;
import com.asterexcrisys.tetris.services.RankingManager;
import com.asterexcrisys.tetris.types.Record;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class LeaderboardController {

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
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource(ResourceConstants.GAME_VIEW));
        Scene scene;
        try {
            scene = new Scene(loader.load(), 600, 800);
        } catch (IOException exception) {
            LOGGER.log(Level.WARNING, exception.getMessage(), exception);
            return;
        }
        GameController controller = loader.getController();
        controller.setStage(stage);
        stage.setScene(scene);
    }

}