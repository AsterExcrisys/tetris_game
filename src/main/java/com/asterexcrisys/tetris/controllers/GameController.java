package com.asterexcrisys.tetris.controllers;

import com.asterexcrisys.tetris.MainApplication;
import com.asterexcrisys.tetris.constants.GameConstants;
import com.asterexcrisys.tetris.constants.ResourceConstants;
import com.asterexcrisys.tetris.handlers.Cell;
import com.asterexcrisys.tetris.services.GravityCounter;
import com.asterexcrisys.tetris.services.RankingManager;
import com.asterexcrisys.tetris.services.TetrisBoard;
import com.asterexcrisys.tetris.types.*;
import com.asterexcrisys.tetris.types.Record;
import com.asterexcrisys.tetris.utilities.GameUtility;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class GameController {

    // TODO: add a view of the currently held tetromino

    // TODO: add sound and graphical effects when one or more lines are cleared

    @FXML
    private AnchorPane gamePane;

    @FXML
    private GridPane boardPane;

    @FXML
    private AnchorPane menuPane;

    @FXML
    private ImageView previewImage;

    @FXML
    private Label levelLabel;

    @FXML
    private Label scoreLabel;

    @FXML
    private Button startButton;

    @FXML
    private Label menuLabel;

    private static final Logger LOGGER = Logger.getLogger(GameController.class.getName());

    private final TetrisBoard game;
    private final GravityCounter counter;
    private final Pane[][] board;
    private final EventHandler<KeyEvent> handler;
    private final Image defaultPreview;
    private final Image spritePreview;
    private final RankingManager manager;
    private final TextInputDialog dialog;
    private Stage stage;
    private Timeline timeline;

    public GameController() {
        game = new TetrisBoard();
        counter = new GravityCounter(GameConstants.GRAVITY_TIME);
        board = new Pane[GameConstants.BOARD_HEIGHT][GameConstants.BOARD_WIDTH];
        handler = this::onKeyPressed;
        defaultPreview = new Image(
                Objects.requireNonNull(MainApplication.class.getResourceAsStream(ResourceConstants.ICON_IMAGE))
        );
        spritePreview = new Image(
                Objects.requireNonNull(MainApplication.class.getResourceAsStream(ResourceConstants.TETROMINOES_IMAGE))
        );
        manager = new RankingManager();
        dialog = new TextInputDialog();
        stage = null;
        timeline = null;
    }

    public void setStage(Stage stage) {
        this.stage = Objects.requireNonNull(stage);
    }

    @FXML
    private void initialize() {
        gamePane.requestFocus();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = new Pane();
                board[i][j].setStyle("-fx-background-color: %s;".formatted(GameUtility.toRGBA(Color.TRANSPARENT)));
                boardPane.add(board[i][j], j, i);
            }
        }
        menuPane.setVisible(false);
        previewImage.setPreserveRatio(false);
        previewImage.setImage(defaultPreview);
        previewImage.setViewport(null);
        dialog.setTitle(GameConstants.DIALOG_TITLE);
        dialog.setHeaderText(GameConstants.DIALOG_HEADER);
        dialog.setContentText(GameConstants.DIALOG_CONTENT);
        BooleanBinding isInvalid = Bindings.createBooleanBinding(
                () -> GameUtility.validateIdentifier(dialog.getEditor().getText()),
                dialog.getEditor().textProperty()
        ).not();
        dialog.getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(isInvalid);
    }

    @FXML
    private void onStartButtonClick() {
        if (game.state() != GameState.IDLE) {
            if (game.state() == GameState.DISPOSED) {
                return;
            }
            gamePane.removeEventHandler(KeyEvent.KEY_PRESSED, handler);
            timeline.stop();
            game.reset();
            counter.reset();
            updateBoard();
            updateProgress();
            updatePreview();
            startButton.setText("Start");
            menuPane.setVisible(false);
            return;
        }
        game.start();
        updateBoard();
        updateProgress();
        updatePreview();
        timeline = new Timeline(new KeyFrame(Duration.millis(100), (event) -> {
            if (game.state() != GameState.RUNNING) {
                if (game.state() == GameState.OVER) {
                    gamePane.removeEventHandler(KeyEvent.KEY_PRESSED, handler);
                    timeline.stop();
                    game.reset();
                    counter.reset();
                    startButton.setText("Start");
                    menuLabel.setText("Game Over");
                    menuPane.setVisible(true);
                }
                return;
            }
            if (counter.countdown()) {
                game.moveTetromino(MovementType.GO_DOWN);
                counter.reset(computeDifficulty());
            }
            updateBoard();
            updateProgress();
            updatePreview();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        gamePane.addEventHandler(KeyEvent.KEY_PRESSED, handler);
        startButton.setText("End");
        menuPane.setVisible(false);
    }

    @FXML
    private void onSettingsButtonClick() {
        if (game.state() != GameState.IDLE && game.state() != GameState.DISPOSED) {
            gamePane.removeEventHandler(KeyEvent.KEY_PRESSED, handler);
            timeline.stop();
            game.close();
            counter.reset();
            updateBoard();
            updateProgress();
            updatePreview();
            manager.close();
            startButton.setText("Start");
            menuPane.setVisible(false);
        }
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource(ResourceConstants.SETTINGS_VIEW));
        Scene scene;
        try {
            scene = new Scene(loader.load(), 600, 800);
        } catch (IOException exception) {
            LOGGER.log(Level.WARNING, exception.getMessage(), exception);
            return;
        }
        SettingsController controller = loader.getController();
        controller.setStage(stage);
        stage.setScene(scene);
    }

    @FXML
    private void onLeaderboardButtonClick() {
        if (game.state() != GameState.IDLE && game.state() != GameState.DISPOSED) {
            gamePane.removeEventHandler(KeyEvent.KEY_PRESSED, handler);
            timeline.stop();
            game.close();
            counter.reset();
            updateBoard();
            updateProgress();
            updatePreview();
            manager.close();
            startButton.setText("Start");
            menuPane.setVisible(false);
        }
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource(ResourceConstants.LEADERBOARD_VIEW));
        Scene scene;
        try {
            scene = new Scene(loader.load(), 600, 800);
        } catch (IOException exception) {
            LOGGER.log(Level.WARNING, exception.getMessage(), exception);
            return;
        }
        LeaderboardController controller = loader.getController();
        controller.setStage(stage);
        stage.setScene(scene);
    }

    @FXML
    private void onCreditsButtonClick() {
        if (game.state() != GameState.IDLE && game.state() != GameState.DISPOSED) {
            gamePane.removeEventHandler(KeyEvent.KEY_PRESSED, handler);
            timeline.stop();
            game.close();
            counter.reset();
            updateBoard();
            updateProgress();
            updatePreview();
            manager.close();
            startButton.setText("Start");
            menuPane.setVisible(false);
        }
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource(ResourceConstants.CREDITS_VIEW));
        Scene scene;
        try {
            scene = new Scene(loader.load(), 600, 800);
        } catch (IOException exception) {
            LOGGER.log(Level.WARNING, exception.getMessage(), exception);
            return;
        }
        CreditsController controller = loader.getController();
        controller.setStage(stage);
        stage.setScene(scene);
    }

    @FXML
    private void onRegisterButtonClick() {
        if (!menuPane.isVisible()) {
            return;
        }
        if (game.state() != GameState.IDLE && game.state() != GameState.PAUSED) {
            return;
        }
        Optional<String> result = dialog.showAndWait();
        if (result.isEmpty()) {
            return;
        }
        if (!GameUtility.validateIdentifier(result.get())) {
            return;
        }
        // TODO: make sure to get the correct level and score before they reset in the 'OVER' game state
        manager.putRecord(Record.of(
                0,
                result.get(),
                game.tracker().level(),
                game.tracker().score()
        ));
    }

    private void onKeyPressed(KeyEvent event) {
        if (game.state() == GameState.DISPOSED) {
            return;
        }
        if (event.getCode() == KeyCode.P) {
            switch (game.state()) {
                case RUNNING -> {
                    game.pause();
                    menuLabel.setText("Game Paused");
                    menuPane.setVisible(true);
                }
                case PAUSED -> {
                    game.resume();
                    menuPane.setVisible(false);
                }
            }
            event.consume();
            return;
        }
        if (game.state() != GameState.RUNNING) {
            return;
        }
        switch (event.getCode()) {
            case A -> game.moveTetromino(MovementType.GO_LEFT);
            case D -> game.moveTetromino(MovementType.GO_RIGHT);
            case S -> game.moveTetromino(MovementType.GO_DOWN);
            case W -> game.moveTetromino(MovementType.DROP_LOCK);
            case Q -> game.moveTetromino(MovementType.ROTATE_LEFT);
            case E -> game.moveTetromino(MovementType.ROTATE_RIGHT);
            case H -> game.holdTetromino();
        }
        event.consume();
    }

    private int computeDifficulty() {
        int percentage = Math.max(10, Math.min(100, game.tracker().level()));
        return GameConstants.GRAVITY_TIME - ((GameConstants.GRAVITY_TIME * (percentage - 10)) / 100);
    }

    private void updateBoard() {
        Cell[][] cells = game.board();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j].setStyle("-fx-background-color: %s;".formatted(
                        GameUtility.toRGBA(cells[i + GameConstants.BOARD_HEIGHT][j].getColor()))
                );
            }
        }
    }

    private void updateProgress() {
        levelLabel.setText("Level: %s".formatted(game.tracker().level()));
        scoreLabel.setText("Score: %d".formatted(game.tracker().score()));
    }

    private void updatePreview() {
        if (game.state() == GameState.IDLE || game.state() == GameState.DISPOSED) {
            previewImage.setImage(defaultPreview);
            previewImage.setViewport(null);
            return;
        }
        TetrominoType type = game.queue().peek().type();
        previewImage.setImage(spritePreview);
        previewImage.setViewport(new Rectangle2D(
                type.corner().x(),
                type.corner().y(),
                type.width(),
                type.height()
        ));
    }

}