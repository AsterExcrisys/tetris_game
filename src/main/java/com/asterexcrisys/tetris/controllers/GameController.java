package com.asterexcrisys.tetris.controllers;

import com.asterexcrisys.tetris.MainApplication;
import com.asterexcrisys.tetris.constants.GameConstants;
import com.asterexcrisys.tetris.constants.ResourceConstants;
import com.asterexcrisys.tetris.services.GravityCounter;
import com.asterexcrisys.tetris.services.TetrisBoard;
import com.asterexcrisys.tetris.types.*;
import com.asterexcrisys.tetris.utilities.GameUtility;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

public final class GameController {

    // TODO: add the level and point system (including higher difficulty as level advances)

    // TODO: add the tetromino preview to show the next in line

    // TODO: add the tetromino hold system

    // TODO: add music and graphic effects to line collapsing and/or tetris (https://downloads.khinsider.com/game-soundtracks/album/tetris-gb)

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

    private static final Image DEFAULT_PREVIEW;
    private static final Image TETROMINOES_PREVIEW;

    private final TetrisBoard game;
    private final GravityCounter counter;
    private final Pane[][] board;
    private final EventHandler<KeyEvent> handler;
    private Timeline timeline;

    static {
        DEFAULT_PREVIEW = new Image(
                Objects.requireNonNull(MainApplication.class.getResourceAsStream(ResourceConstants.ICON))
        );
        TETROMINOES_PREVIEW = new Image(
                Objects.requireNonNull(MainApplication.class.getResourceAsStream(ResourceConstants.TETROMINOES))
        );
    }

    public GameController() {
        game = new TetrisBoard();
        counter = new GravityCounter(GameConstants.GRAVITY_TIME);
        board = new Pane[GameConstants.BOARD_HEIGHT][GameConstants.BOARD_WIDTH];
        handler = this::onKeyPressed;
        timeline = null;
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
        previewImage.setImage(DEFAULT_PREVIEW);
        previewImage.setViewport(null);
    }

    @FXML
    private void onStartButtonClick() {
        if (game.state() != GameState.IDLE) {
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
        throw new UnsupportedOperationException("yet to be implemented");
    }

    @FXML
    private void onLeaderboardButtonClick() {
        throw new UnsupportedOperationException("yet to be implemented");
    }

    @FXML
    private void onCreditsButtonClick() {
        if (game.state() != GameState.IDLE) {
            gamePane.removeEventHandler(KeyEvent.KEY_PRESSED, handler);
            timeline.stop();
            game.reset();
            counter.reset();
            updateBoard();
            updateProgress();
            updatePreview();
            startButton.setText("Start");
            menuPane.setVisible(false);
        }
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource(ResourceConstants.CREDITS_VIEW));
        Scene scene;
        try {
            scene = new Scene(loader.load(), 600, 800);
        } catch (IOException ignored) {
            return;
        }
        Stage stage = (Stage) gamePane.getScene().getWindow();
        stage.setScene(scene);
    }

    private void onKeyPressed(KeyEvent event) {
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

    // TODO: fix this to make it more fair
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
        if (game.state() == GameState.IDLE) {
            previewImage.setImage(DEFAULT_PREVIEW);
            previewImage.setViewport(null);
            return;
        }
        TetrominoType type = game.queue().peek().type();
        previewImage.setImage(TETROMINOES_PREVIEW);
        previewImage.setViewport(new Rectangle2D(
                type.corner().x(),
                type.corner().y(),
                type.width(),
                type.height()
        ));
    }

}