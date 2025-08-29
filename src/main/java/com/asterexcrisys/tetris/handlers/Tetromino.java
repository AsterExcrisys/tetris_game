package com.asterexcrisys.tetris.handlers;

import com.asterexcrisys.tetris.types.Position;
import com.asterexcrisys.tetris.types.TetrominoType;
import javafx.scene.paint.Color;
import java.util.Objects;
import java.util.function.BiFunction;

public class Tetromino {

    private final Position[] offset;
    private final Position[] position;
    private Position pivot;
    private TetrominoType type;
    private Color color;

    public Tetromino(Position pivot, TetrominoType type, Color color) {
        offset = new Position[4];
        position = new Position[4];
        this.pivot = Objects.requireNonNull(pivot);
        this.type = Objects.requireNonNull(type);
        this.color = Objects.requireNonNull(color);
        System.arraycopy(this.type.all(), 0, offset, 0, offset.length);
        updatePosition();
    }

    public Position first() {
        return position[0];
    }

    public Position second() {
        return position[1];
    }

    public Position third() {
        return position[2];
    }

    public Position fourth() {
        return position[3];
    }

    public Position[] all() {
        return position;
    }

    public Position getPivot() {
        return pivot;
    }

    public TetrominoType getType() {
        return type;
    }

    public Color getColor() {
        return color;
    }

    public void setPivot(Position pivot) {
        this.pivot = Objects.requireNonNull(pivot);
        updatePosition();
    }

    public void setType(TetrominoType type) {
        this.type = Objects.requireNonNull(type);
        System.arraycopy(this.type.all(), 0, offset, 0, offset.length);
        updatePosition();
    }

    public void setColor(Color color) {
        this.color = Objects.requireNonNull(color);
    }

    public void changeOffset(BiFunction<Position, Position, Position> formula) {
        Objects.requireNonNull(formula);
        for (int i = 0; i < offset.length; i++) {
            offset[i] = Objects.requireNonNull(formula.apply(pivot, position[i]));
        }
        updatePosition();
    }

    public void resetAll(Position pivot, TetrominoType type, Color color) {
        this.pivot = Objects.requireNonNull(pivot);
        this.type = Objects.requireNonNull(type);
        this.color = Objects.requireNonNull(color);
        System.arraycopy(this.type.all(), 0, offset, 0, offset.length);
        updatePosition();
    }

    private void updatePosition() {
        for (int i = 0; i < position.length; i++) {
            position[i] = Position.of(pivot.x() + offset[i].x(), pivot.y() + offset[i].y());
        }
    }

    public static Tetromino of(Position pivot, TetrominoType type, Color color) {
        return new Tetromino(pivot, type, color);
    }

}