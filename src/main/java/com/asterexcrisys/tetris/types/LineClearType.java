package com.asterexcrisys.tetris.types;

public enum LineClearType {

    SINGLE(1, 40),
    DOUBLE(2, 100),
    TRIPLE(3, 300),
    TETRIS(4, 1200);

    private final int linesCleared;
    private final int basePoints;

    LineClearType(int linesCleared, int basePoints) {
        this.linesCleared = linesCleared;
        this.basePoints = basePoints;
    }

    public int linesCleared() {
        return linesCleared;
    }

    public int basePoints() {
        return basePoints;
    }

    public static LineClearType linesOf(int linesCleared) {
        for (LineClearType type : LineClearType.values()) {
            if (type.linesCleared() == linesCleared) {
                return type;
            }
        }
        return null;
    }

    public static LineClearType pointsOf(int basePoints) {
        for (LineClearType type : LineClearType.values()) {
            if (type.basePoints() == basePoints) {
                return type;
            }
        }
        return null;
    }

}