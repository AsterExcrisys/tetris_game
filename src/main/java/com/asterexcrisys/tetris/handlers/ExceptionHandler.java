package com.asterexcrisys.tetris.handlers;

import com.asterexcrisys.tetris.constants.GlobalConstants;
import com.asterexcrisys.tetris.utilities.GlobalUtility;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static final Logger LOGGER = Logger.getLogger(ExceptionHandler.class.getName());

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        LOGGER.log(Level.SEVERE, throwable.getMessage(), throwable);
        Platform.runLater(() -> {
            Alert errorAlert = new Alert(AlertType.ERROR);
            errorAlert.setTitle(GlobalConstants.ALERT_TITLE);
            errorAlert.setHeaderText(GlobalConstants.ALERT_HEADER.formatted(thread.getName(), thread.threadId()));
            errorAlert.setContentText(throwable.getMessage());
            errorAlert.setResizable(false);
            Label stackTraceLabel = new Label(GlobalUtility.getStackTrace(throwable));
            stackTraceLabel.setWrapText(true);
            errorAlert.getDialogPane().setExpandableContent(stackTraceLabel);
            errorAlert.showAndWait();
            Platform.exit();
            System.exit(1);
        });
    }

}