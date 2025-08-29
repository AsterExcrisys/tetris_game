package com.asterexcrisys.tetris.services;

import com.asterexcrisys.tetris.handlers.Cell;
import com.asterexcrisys.tetris.handlers.Tetromino;
import com.asterexcrisys.tetris.types.*;
import com.asterexcrisys.tetris.utilities.GameUtility;
import javafx.scene.paint.Color;
import java.util.List;
import java.util.Objects;

public class ActiveTetromino {

    private final Tetromino tetromino;

    public ActiveTetromino() {
        tetromino = Tetromino.of(
                GameUtility.randomisePosition(),
                GameUtility.randomiseType(),
                GameUtility.randomiseColor()
        );
    }

    public ActiveTetromino(Element element) {
        Objects.requireNonNull(element);
        tetromino = Tetromino.of(element.position(), element.type(), element.color());
    }

    public ActiveTetromino(Position position, TetrominoType type, Color color) {
        tetromino = Tetromino.of(position, type, color);
    }

    public Position[] position() {
        return tetromino.all();
    }

    public Position pivot() {
        return tetromino.getPivot();
    }

    public TetrominoType type() {
        return tetromino.getType();
    }

    public Color color() {
        return tetromino.getColor();
    }

    public boolean canSpawn(Cell[][] board) {
        for (Position position : tetromino.all()) {
            if (!isPlaceable(board, position.x(), position.y())) {
                return false;
            }
        }
        return true;
    }

    public boolean canMoveLeft(Cell[][] board) {
        for (Position position : tetromino.all()) {
            if (!isPlaceable(board, position.x(), position.y() - 1)) {
                return false;
            }
        }
        return true;
    }

    public boolean canMoveRight(Cell[][] board) {
        for (Position position : tetromino.all()) {
            if (!isPlaceable(board, position.x(), position.y() + 1)) {
                return false;
            }
        }
        return true;
    }

    public boolean canMoveUp(Cell[][] board) {
        for (Position position : tetromino.all()) {
            if (!isPlaceable(board, position.x() - 1, position.y())) {
                return false;
            }
        }
        return true;
    }

    public boolean canMoveDown(Cell[][] board) {
        for (Position position : tetromino.all()) {
            if (!isPlaceable(board, position.x() + 1, position.y())) {
                return false;
            }
        }
        return true;
    }

    public boolean canRotateLeft(Cell[][] board) {
        if (tetromino.getType() == TetrominoType.O) {
            return true;
        }
        for (Position position : tetromino.all()) {
            int x = tetromino.getPivot().x() - (position.y() - tetromino.getPivot().y());
            int y = tetromino.getPivot().y() + (position.x() - tetromino.getPivot().x());
            if (!isPlaceable(board, x, y)) {
                return false;
            }
        }
        return true;
    }

    public boolean canRotateRight(Cell[][] board) {
        if (tetromino.getType() == TetrominoType.O) {
            return true;
        }
        for (Position position : tetromino.all()) {
            int x = tetromino.getPivot().x() + (position.y() - tetromino.getPivot().y());
            int y = tetromino.getPivot().y() - (position.x() - tetromino.getPivot().x());
            if (!isPlaceable(board, x, y)) {
                return false;
            }
        }
        return true;
    }

    public boolean canKickLeft(Cell[][] board, List<MovementType> types) {
        if (tetromino.getType() == TetrominoType.O) {
            return true;
        }
        for (MovementType type : new MovementType[] {MovementType.GO_LEFT, MovementType.GO_RIGHT, MovementType.GO_UP, MovementType.GO_DOWN}) {
            Position candidatePivot = type.apply(tetromino.getPivot(), null);
            boolean isAllowed = true;
            for (Position position : tetromino.all()) {
                Position candidatePosition = type.apply(position, null);
                if (!isPlaceable(board, candidatePosition.x(), candidatePosition.y())) {
                    isAllowed = false;
                    break;
                }
                int x = candidatePivot.x() - (candidatePosition.y() - candidatePivot.y());
                int y = candidatePivot.y() + (candidatePosition.x() - candidatePivot.x());
                if (!isPlaceable(board, x, y)) {
                    isAllowed = false;
                    break;
                }
            }
            if (isAllowed) {
                types.add(type);
                return true;
            }
        }
        return false;
    }

    public boolean canKickRight(Cell[][] board, List<MovementType> types) {
        if (tetromino.getType() == TetrominoType.O) {
            return true;
        }
        for (MovementType type : new MovementType[] {MovementType.GO_LEFT, MovementType.GO_RIGHT, MovementType.GO_UP, MovementType.GO_DOWN}) {
            Position candidatePivot = type.apply(tetromino.getPivot(), null);
            boolean isAllowed = true;
            for (Position position : tetromino.all()) {
                Position candidatePosition = type.apply(position, null);
                if (!isPlaceable(board, candidatePosition.x(), candidatePosition.y())) {
                    isAllowed = false;
                    break;
                }
                int x = candidatePivot.x() + (candidatePosition.y() - candidatePivot.y());
                int y = candidatePivot.y() - (candidatePosition.x() - candidatePivot.x());
                if (!isPlaceable(board, x, y)) {
                    isAllowed = false;
                    break;
                }
            }
            if (isAllowed) {
                types.add(type);
                return true;
            }
        }
        return false;
    }

    public void applyMovement(MovementType type) {
        switch (type) {
            case GO_LEFT, GO_RIGHT, GO_UP, GO_DOWN -> tetromino.setPivot(type.apply(tetromino.getPivot(), null));
            case ROTATE_LEFT, ROTATE_RIGHT -> {
                if (tetromino.getType() == TetrominoType.O) {
                    break;
                }
                tetromino.changeOffset(type.formula());
            }
        }
    }

    public void dropLock(Cell[][] board) {
        while (canMoveDown(board)) {
            tetromino.setPivot(MovementType.DROP_LOCK.apply(tetromino.getPivot(), null));
        }
    }

    public void reset() {
        tetromino.resetAll(
                GameUtility.randomisePosition(),
                GameUtility.randomiseType(),
                GameUtility.randomiseColor()
        );
    }

    public void reset(Element element) {
        Objects.requireNonNull(element);
        tetromino.resetAll(element.position(), element.type(), element.color());
    }

    public void reset(Position position, TetrominoType type, Color color) {
        tetromino.resetAll(position, type, color);
    }

    private boolean isPlaceable(Cell[][] board, int x, int y) {
        if (x < 0 || x > board.length - 1 || y < 0 || y > board[0].length - 1) {
            return false;
        }
        return board[x][y].getType() != CellType.IMMOVABLE;
    }

}