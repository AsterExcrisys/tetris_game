package com.asterexcrisys.tetris.types;

import java.util.function.BiFunction;

public enum MovementType {

    GO_LEFT((pivot, position) -> Position.of(pivot.x(), pivot.y() - 1)),
    GO_RIGHT((pivot, position) -> Position.of(pivot.x(), pivot.y() + 1)),
    GO_UP((pivot, position) -> Position.of(pivot.x() - 1, pivot.y())),
    GO_DOWN((pivot, position) -> Position.of(pivot.x() + 1, pivot.y())),
    DROP_LOCK((pivot, position) -> Position.of(pivot.x() + 1, pivot.y())),
    ROTATE_LEFT((pivot, position) -> Position.of(pivot.y() - position.y(), position.x() - pivot.x())),
    ROTATE_RIGHT((pivot, position) -> Position.of(position.y() - pivot.y(), pivot.x() - position.x()));

    private final BiFunction<Position, Position, Position> formula;

    MovementType(BiFunction<Position, Position, Position> formula) {
        this.formula = formula;
    }

    public BiFunction<Position, Position, Position> formula() {
        return formula;
    }

    public Position apply(Position pivot, Position position) {
        return formula.apply(pivot, position);
    }

    public static MovementType formulaOf(BiFunction<Position, Position, Position> formula) {
        for (MovementType type : MovementType.values()) {
            if (type.formula().equals(formula)) {
                return type;
            }
        }
        return null;
    }

}