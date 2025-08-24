package com.asterexcrisys.tetris;

import com.asterexcrisys.tetris.constants.WindowConstants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public final class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource(WindowConstants.GAME_VIEW));
        Scene scene = new Scene(loader.load(), WindowConstants.WIDTH, WindowConstants.HEIGHT);
        stage.setTitle(WindowConstants.TITLE);
        stage.getIcons().add(new Image(
                Objects.requireNonNull(MainApplication.class.getResourceAsStream(WindowConstants.ICON))
        ));
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}