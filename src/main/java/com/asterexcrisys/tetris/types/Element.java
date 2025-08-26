package com.asterexcrisys.tetris.types;

import javafx.scene.paint.Color;
import java.util.Objects;

public record Element(Position position, TetrominoType type, Color color) {

    public Element {
        Objects.requireNonNull(position);
        Objects.requireNonNull(type);
        Objects.requireNonNull(color);
    }

    public static Element of(Position position, TetrominoType type, Color color) {
        return new Element(position, type, color);
    }

}