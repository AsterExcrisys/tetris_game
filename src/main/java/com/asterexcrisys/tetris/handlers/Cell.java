package com.asterexcrisys.tetris.handlers;

import com.asterexcrisys.tetris.types.CellType;
import javafx.scene.paint.Color;
import java.util.Objects;

public class Cell {

    private CellType type;
    private Color color;

    public Cell(CellType type, Color color) {
        this.type = Objects.requireNonNull(type);
        this.color = Objects.requireNonNull(color);
    }

    public CellType getType() {
        return type;
    }

    public Color getColor() {
        return color;
    }

    public void setType(CellType type) {
        this.type = Objects.requireNonNull(type);
    }

    public void setColor(Color color) {
        this.color = Objects.requireNonNull(color);
    }

    public static Cell of(CellType type, Color color) {
        return new Cell(type, color);
    }

}