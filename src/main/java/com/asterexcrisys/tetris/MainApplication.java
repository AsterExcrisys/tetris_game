package com.asterexcrisys.tetris;

import com.asterexcrisys.tetris.constants.ResourceConstants;
import com.asterexcrisys.tetris.constants.WindowConstants;
import com.asterexcrisys.tetris.controllers.GameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public final class MainApplication extends Application {

    private static Application INSTANCE;

    public MainApplication() {
        INSTANCE = this;
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource(ResourceConstants.GAME_VIEW));
        Scene scene = new Scene(loader.load(), WindowConstants.WIDTH, WindowConstants.HEIGHT);
        GameController controller = loader.getController();
        controller.setStage(stage);
        stage.setTitle(WindowConstants.TITLE);
        stage.getIcons().add(new Image(
                Objects.requireNonNull(MainApplication.class.getResourceAsStream(ResourceConstants.ICON))
        ));
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static Optional<Application> getInstance() {
        return Optional.ofNullable(INSTANCE);
    }

    public static void main(String[] arguments) {
        launch(arguments);
    }

}