package com.asterexcrisys.tetris.services;

import com.asterexcrisys.tetris.types.*;
import com.asterexcrisys.tetris.utilities.GameUtility;
import javafx.scene.paint.Color;

public class ActiveTetromino {

    private final Tetromino tetromino;

    public ActiveTetromino() {
        tetromino = Tetromino.of(
                GameUtility.randomomisePosition(),
                GameUtility.randomiseType(),
                GameUtility.randomiseColor()
        );
    }

    public Position[] position() {
        return tetromino.all();
    }

    public TetrominoType type() {
        return tetromino.getType();
    }

    public Color color() {
        return tetromino.getColor();
    }

    public boolean canSpawn(Cell[][] board) {
        for (Position position : tetromino.all()) {
            if (board[position.x()][position.y()].getType() == CellType.IMMOVABLE) {
                return false;
            }
        }
        return true;
    }

    public boolean canMoveLeft(Cell[][] board) {
        for (Position position : tetromino.all()) {
            if (position.y() - 1 < 0) {
                return false;
            }
            if (board[position.x()][position.y() - 1].getType() == CellType.IMMOVABLE) {
                return false;
            }
        }
        return true;
    }

    public boolean canMoveRight(Cell[][] board) {
        for (Position position : tetromino.all()) {
            if (position.y() + 1 > board[0].length - 1) {
                return false;
            }
            if (board[position.x()][position.y() + 1].getType() == CellType.IMMOVABLE) {
                return false;
            }
        }
        return true;
    }

    public boolean canMoveUp(Cell[][] board) {
        for (Position position : tetromino.all()) {
            if (position.x() - 1 < 0) {
                return false;
            }
            if (board[position.x() - 1][position.y()].getType() == CellType.IMMOVABLE) {
                return false;
            }
        }
        return true;
    }

    public boolean canMoveDown(Cell[][] board) {
        for (Position position : tetromino.all()) {
            if (position.x() + 1 > board.length - 1) {
                return false;
            }
            if (board[position.x() + 1][position.y()].getType() == CellType.IMMOVABLE) {
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
            if (x < 0 || x > board.length - 1 || y < 0 || y > board[0].length - 1) {
                return false;
            }
            if (board[x][y].getType() == CellType.IMMOVABLE) {
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
            if (x < 0 || x > board.length - 1 || y < 0 || y > board[0].length - 1) {
                return false;
            }
            if (board[x][y].getType() == CellType.IMMOVABLE) {
                return false;
            }
        }
        return true;
    }

    //TODO: add wall kicks to left and right rotations
    public void applyMovement(MovementType type) {
        switch (type) {
            case GO_LEFT -> tetromino.setPivot(
                    Position.of(tetromino.getPivot().x(), tetromino.getPivot().y() - 1)
            );
            case GO_RIGHT -> tetromino.setPivot(
                    Position.of(tetromino.getPivot().x(), tetromino.getPivot().y() + 1)
            );
            case GO_UP -> tetromino.setPivot(
                    Position.of(tetromino.getPivot().x() - 1, tetromino.getPivot().y())
            );
            case GO_DOWN -> tetromino.setPivot(
                    Position.of(tetromino.getPivot().x() + 1, tetromino.getPivot().y())
            );
            case ROTATE_LEFT -> {
                if (tetromino.getType() == TetrominoType.O) {
                    break;
                }
                tetromino.changeOffset(this::computeLeftRotation);
            }
            case ROTATE_RIGHT -> {
                if (tetromino.getType() == TetrominoType.O) {
                    break;
                }
                tetromino.changeOffset(this::computeRightRotation);
            }
        }
    }

    public void dropLock(Cell[][] board) {
        while (canMoveDown(board)) {
            applyMovement(MovementType.GO_DOWN);
        }
    }

    public void reset() {
        tetromino.resetAll(
                GameUtility.randomomisePosition(),
                GameUtility.randomiseType(),
                GameUtility.randomiseColor()
        );
    }

    private Position computeLeftRotation(Position pivot, Position position) {
        return Position.of(pivot.y() - position.y(), position.x() - pivot.x());
    }

    private Position computeRightRotation(Position pivot, Position position) {
        return Position.of(position.y() - pivot.y(), pivot.x() - position.x());
    }

}