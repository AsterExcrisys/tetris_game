package com.asterexcrisys.tetris.controllers;

import com.asterexcrisys.tetris.MainApplication;
import com.asterexcrisys.tetris.constants.ResourceConstants;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;

public final class CreditsController {

    @FXML
    private AnchorPane creditsPane;

    @FXML
    private void onBackButtonClick() {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource(ResourceConstants.GAME_VIEW));
        Scene scene;
        try {
            scene = new Scene(loader.load(), 600, 800);
        } catch (IOException ignored) {
            return;
        }
        Stage stage = (Stage) creditsPane.getScene().getWindow();
        stage.setScene(scene);
    }

}