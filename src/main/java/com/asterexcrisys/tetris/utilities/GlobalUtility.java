package com.asterexcrisys.tetris.utilities;

import com.asterexcrisys.tetris.MainApplication;
import com.asterexcrisys.tetris.constants.GlobalConstants;
import com.asterexcrisys.tetris.constants.WindowConstants;
import com.asterexcrisys.tetris.controllers.BaseController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class GlobalUtility {

    public static String getCurrentDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(GlobalConstants.DATE_FORMAT);
        return now.format(formatter);
    }

    public static double clampVolume(double volume) {
        return Math.max(0.0, Math.min(1.0, volume));
    }

    public static String getStackTrace(Throwable throwable) {
        StringBuilder builder = new StringBuilder();
        for (StackTraceElement element : throwable.getStackTrace()) {
            builder.append(element.toString()).append("\n");
        }
        return builder.toString();
    }

    public static void changeView(String view, Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource(view));
        Scene scene = new Scene(loader.load(), WindowConstants.WIDTH, WindowConstants.HEIGHT);
        BaseController controller = loader.getController();
        controller.setStage(stage);
        stage.setScene(scene);
    }

}