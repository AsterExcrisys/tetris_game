package com.asterexcrisys.tetris.services;

import com.asterexcrisys.tetris.constants.GameConstants;
import com.asterexcrisys.tetris.types.*;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TetrisBoard {

    // TODO: implement the 7-bag technique to reduce the effect of pure RNG

    // TODO: assign a fixed color to each piece instead of randomising it

    // TODO: consider adding listeners for certain events

    private final Cell[][] board;
    private final ActiveTetromino tetromino;
    private final ProgressTracker tracker;
    private final AudioPlayer musicPlayer;
    private final AudioPlayer soundPlayer;
    private GameState state;

    public TetrisBoard() {
        board = new Cell[GameConstants.BOARD_HEIGHT * 2][GameConstants.BOARD_WIDTH];
        tetromino = new ActiveTetromino();
        tracker = new ProgressTracker(GameConstants.INITIAL_LEVEL);
        musicPlayer = new AudioPlayer(AudioType.MUSIC_TRACK);
        soundPlayer = new AudioPlayer(AudioType.SOUND_EFFECT);
        state = GameState.IDLE;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = new Cell(CellType.EMPTY, Color.TRANSPARENT);
            }
        }
    }

    public Cell[][] board() {
        return board;
    }

    public ActiveTetromino tetromino() {
        return tetromino;
    }

    public ProgressTracker tracker() {
        return tracker;
    }

    public GameState state() {
        return state;
    }

    public void start() {
        if (state != GameState.IDLE || !tetromino.canSpawn(board)) {
            return;
        }
        spawnTetromino(false);
        musicPlayer.play(MusicTrackType.MAIN_THEME);
        state = GameState.RUNNING;
    }

    public void pause() {
        if (state != GameState.RUNNING) {
            return;
        }
        musicPlayer.pause();
        state = GameState.PAUSED;
    }

    public void resume() {
        if (state != GameState.PAUSED) {
            return;
        }
        musicPlayer.resume();
        state = GameState.RUNNING;
    }

    public void moveTetromino(MovementType type) {
        if (state != GameState.RUNNING) {
            return;
        }
        switch (type) {
            case GO_LEFT -> {
                if (!tetromino.canMoveLeft(board)) {
                    return;
                }
            }
            case GO_RIGHT -> {
                if (!tetromino.canMoveRight(board)) {
                    return;
                }
            }
            case GO_UP -> {
                if (!tetromino.canMoveUp(board)) {
                    return;
                }
            }
            case GO_DOWN -> {
                if (!tetromino.canMoveDown(board)) {
                    blockTetromino();
                    checkTetrisLines();
                    spawnTetromino(true);
                    soundPlayer.play(SoundEffectType.TETROMINO_LOCKED);
                    return;
                }
            }
            case ROTATE_LEFT -> {
                if (!tetromino.canRotateLeft(board)) {
                    return;
                }
            }
            case ROTATE_RIGHT -> {
                if (!tetromino.canRotateRight(board)) {
                    return;
                }
            }
        }
        updateTetromino(type);
        soundPlayer.play(SoundEffectType.TETROMINO_MOVED);
    }

    public void reset() {
        if (state == GameState.IDLE) {
            return;
        }
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = new Cell(CellType.EMPTY, Color.TRANSPARENT);
            }
        }
        tetromino.reset();
        tracker.reset();
        musicPlayer.reset();
        soundPlayer.reset();
        state = GameState.IDLE;
    }

    private void updateTetromino(MovementType type) {
        for (Position position : tetromino.position()) {
            board[position.x()][position.y()].setType(CellType.EMPTY);
            board[position.x()][position.y()].setColor(Color.TRANSPARENT);
        }
        if (type == MovementType.DROP_LOCK) {
            tetromino.dropLock(board);
            blockTetromino();
            checkTetrisLines();
            spawnTetromino(true);
            return;
        }
        tetromino.applyMovement(type);
        for (Position position : tetromino.position()) {
            board[position.x()][position.y()].setType(CellType.MOVABLE);
            board[position.x()][position.y()].setColor(tetromino.color());
        }
    }

    private void spawnTetromino(boolean shouldReset) {
        if (shouldReset) {
            tetromino.reset();
            if (!tetromino.canSpawn(board)) {
                state = GameState.OVER;
                return;
            }
        }
        for (Position position : tetromino.position()) {
            board[position.x()][position.y()].setType(CellType.MOVABLE);
            board[position.x()][position.y()].setColor(tetromino.color());
        }
    }

    private void blockTetromino() {
        for (Position position : tetromino.position()) {
            board[position.x()][position.y()].setType(CellType.IMMOVABLE);
            board[position.x()][position.y()].setColor(tetromino.color());
        }
    }

    private void checkTetrisLines() {
        List<Integer> indexes = new ArrayList<>(board.length);
        for (Integer i : findAffectedLines()) {
            boolean isFull = true;
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].getType() != CellType.IMMOVABLE) {
                    isFull = false;
                    break;
                }
            }
            if (isFull) {
                indexes.add(i);
            }
        }
        for (Integer index : indexes) {
            clearLine(index);
        }
        LineClearType type = LineClearType.linesOf(indexes.size());
        if (type == null) {
            return;
        }
        tracker.track(type);
        switch (type) {
            case SINGLE -> soundPlayer.play(SoundEffectType.ONE_LINE_CLEARED);
            case DOUBLE -> soundPlayer.play(SoundEffectType.TWO_LINES_CLEARED);
            case TRIPLE -> soundPlayer.play(SoundEffectType.THREE_LINES_CLEARED);
            case TETRIS -> soundPlayer.play(SoundEffectType.FOUR_LINES_CLEARED);
        }
    }

    private List<Integer> findAffectedLines() {
        int minimum = Integer.MAX_VALUE, maximum = Integer.MIN_VALUE;
        for (Position position : tetromino.position()) {
            if (position.x() < minimum) {
                minimum = position.x();
            }
            if (position.x() > maximum) {
                maximum = position.x();
            }
        }
        if (minimum == Integer.MAX_VALUE) {
            minimum = 0;
        }
        if (maximum == Integer.MIN_VALUE) {
            maximum = board.length - 1;
        }
        return IntStream.rangeClosed(minimum, maximum).boxed().collect(Collectors.toList());
    }

    private void clearLine(int index) {
        for (int i = index; i > 0; i--) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j].setType(board[i - 1][j].getType());
                board[i][j].setColor(board[i - 1][j].getColor());
            }
        }
        for (int i = 0; i < board[0].length; i++) {
            board[0][i].setType(CellType.EMPTY);
            board[0][i].setColor(Color.TRANSPARENT);
        }
    }

}