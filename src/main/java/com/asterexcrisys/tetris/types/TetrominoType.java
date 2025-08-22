package com.asterexcrisys.tetris.types;

public enum TetrominoType {

    O(
            Position.of(0, 0),
            Position.of(1, 0),
            Position.of(0, 1),
            Position.of(1, 1)
    ),
    I(
            Position.of(0, 0),
            Position.of(0, 1),
            Position.of(0, 2),
            Position.of(0, 3)
    ),
    S(
            Position.of(0, 1),
            Position.of(0, 2),
            Position.of(1, 0),
            Position.of(1, 1)
    ),
    Z(
            Position.of(0, 0),
            Position.of(0, 1),
            Position.of(1, 1),
            Position.of(1, 2)
    ),
    L(
            Position.of(1, 0),
            Position.of(1, 1),
            Position.of(1, 2),
            Position.of(0, 2)
    ),
    J(
            Position.of(0, 0),
            Position.of(1, 0),
            Position.of(1, 1),
            Position.of(1, 2)
    ),
    T(
            Position.of(1, 0),
            Position.of(1, 1),
            Position.of(1, 2),
            Position.of(0, 1)
    );

    private final Position[] offset;

    TetrominoType(Position first, Position second, Position third, Position fourth) {
        offset = new Position[4];
        offset[0] = first;
        offset[1] = second;
        offset[2] = third;
        offset[3] = fourth;
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

}