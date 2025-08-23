package com.asterexcrisys.tetris.controllers;

import com.asterexcrisys.tetris.MainApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;

public class CreditsController {

    @FXML
    private AnchorPane creditsPane;

    @FXML
    protected void onBackButtonClick() {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("views/game-view.fxml"));
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