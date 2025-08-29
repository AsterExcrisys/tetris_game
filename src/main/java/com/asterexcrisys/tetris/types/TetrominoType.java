package com.asterexcrisys.tetris.types;

import javafx.scene.paint.Color;

public enum TetrominoType {

    I(
            Position.of(0, 0),
            Position.of(0, 1),
            Position.of(0, 2),
            Position.of(0, 3),
            Color.rgb(0, 255, 255, 1.0),
            Position.of(0, 0),
            148,
            74
    ),
    J(
            Position.of(0, 0),
            Position.of(1, 0),
            Position.of(1, 1),
            Position.of(1, 2),
            Color.rgb(0, 0, 255, 1.0),
            Position.of(185, 0),
            111,
            74
    ),
    L(
            Position.of(1, 0),
            Position.of(1, 1),
            Position.of(1, 2),
            Position.of(0, 2),
            Color.rgb(255, 127, 0, 1.0),
            Position.of(333, 0),
            111,
            74
    ),
    O(
            Position.of(0, 0),
            Position.of(1, 0),
            Position.of(0, 1),
            Position.of(1, 1),
            Color.rgb(255, 255, 0, 1.0),
            Position.of(481, 0),
            74,
            74
    ),
    S(
            Position.of(0, 1),
            Position.of(0, 2),
            Position.of(1, 0),
            Position.of(1, 1),
            Color.rgb(0, 255, 0, 1.0),
            Position.of(592, 0),
            111,
            74
    ),
    Z(
            Position.of(0, 0),
            Position.of(0, 1),
            Position.of(1, 1),
            Position.of(1, 2),
            Color.rgb(255, 0, 0, 1.0),
            Position.of(740, 0),
            111,
            74
    ),
    T(
            Position.of(1, 0),
            Position.of(1, 1),
            Position.of(1, 2),
            Position.of(0, 1),
            Color.rgb(128, 0, 128, 1.0),
            Position.of(888, 0),
            111,
            74
    );

    private final Position[] offset;
    private final Color color;
    private final Position corner;
    private final int width;
    private final int height;

    TetrominoType(Position first, Position second, Position third, Position fourth, Color color, Position corner, int width, int height) {
        offset = new Position[4];
        offset[0] = first;
        offset[1] = second;
        offset[2] = third;
        offset[3] = fourth;
        this.color = color;
        this.corner = corner;
        this.width = width;
        this.height = height;
    }

    public Position first() {
        return offset[0];
    }

    public Position second() {
        return offset[1];
    }

    public Position third() {
        return offset[2];
    }

    public Position fourth() {
        return offset[3];
    }

    public Position[] all() {
        return offset;
    }

    public Color color() {
        return color;
    }

    public Position corner() {
        return corner;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

}