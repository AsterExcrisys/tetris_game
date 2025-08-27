package com.asterexcrisys.tetris.controllers;

import com.asterexcrisys.tetris.MainApplication;
import com.asterexcrisys.tetris.constants.CreditsConstants;
import com.asterexcrisys.tetris.constants.ResourceConstants;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public final class CreditsController {

    private Stage stage;

    public CreditsController() {
        stage = null;
    }

    public void setStage(Stage stage) {
        this.stage = Objects.requireNonNull(stage);
    }

    @FXML
    private void onVisitButtonClick() {
        MainApplication.getInstance().ifPresent(((application) -> {
            application.getHostServices().showDocument(CreditsConstants.GITHUB_ACCOUNT);
        }));
    }

    @FXML
    private void onBackButtonClick() {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource(ResourceConstants.GAME_VIEW));
        Scene scene;
        try {
            scene = new Scene(loader.load(), 600, 800);
        } catch (IOException ignored) {
            return;
        }
        GameController controller = loader.getController();
        controller.setStage(stage);
        stage.setScene(scene);
    }

}