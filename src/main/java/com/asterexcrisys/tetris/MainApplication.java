package com.asterexcrisys.tetris;

import com.asterexcrisys.tetris.constants.GlobalConstants;
import com.asterexcrisys.tetris.constants.ResourceConstants;
import com.asterexcrisys.tetris.constants.WindowConstants;
import com.asterexcrisys.tetris.controllers.GameController;
import com.asterexcrisys.tetris.utilities.GlobalUtility;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.*;

public final class MainApplication extends Application {

    private static final Logger LOGGER = Logger.getLogger(MainApplication.class.getName());
    private static final boolean DEBUG = false;
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
                Objects.requireNonNull(MainApplication.class.getResourceAsStream(ResourceConstants.ICON_IMAGE))
        ));
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static Optional<Application> getInstance() {
        return Optional.ofNullable(INSTANCE);
    }

    public static void main(String[] arguments) {
        try {
            Files.createDirectories(Paths.get("./data/"));
            LogManager.getLogManager().reset();
            configureLogger(LogManager.getLogManager().getLogger(GlobalConstants.ROOT_LOGGER));
            launch(arguments);
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, exception.getMessage(), exception);
            System.exit(1);
        }
    }

    private static void configureLogger(Logger logger) throws IOException {
        for (Handler handler : logger.getHandlers()) {
            logger.removeHandler(handler);
        }
        Files.createDirectories(Paths.get("./data/logs/"));
        if (DEBUG) {
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new SimpleFormatter());
            consoleHandler.setLevel(Level.ALL);
            logger.addHandler(consoleHandler);
        }
        FileHandler fileHandler = new FileHandler("./data/logs/%s.log".formatted(GlobalUtility.getCurrentDate()), true);
        fileHandler.setLevel(Level.CONFIG);
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);
    }

}