package com.asterexcrisys.tetris.controllers;

import com.asterexcrisys.tetris.MainApplication;
import com.asterexcrisys.tetris.constants.CreditsConstants;
import com.asterexcrisys.tetris.constants.ResourceConstants;
import com.asterexcrisys.tetris.utilities.GlobalUtility;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class CreditsController implements BaseController {

    private static final Logger LOGGER = Logger.getLogger(CreditsController.class.getName());

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
        try {
            GlobalUtility.changeView(ResourceConstants.GAME_VIEW, stage);
        } catch (Exception exception) {
            LOGGER.log(Level.WARNING, exception.getMessage(), exception);
        }
    }

}