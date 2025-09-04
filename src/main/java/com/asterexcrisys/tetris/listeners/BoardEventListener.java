package com.asterexcrisys.tetris.listeners;

import com.asterexcrisys.tetris.types.LineClearType;
import com.asterexcrisys.tetris.types.MovementType;

public interface BoardEventListener {

    default void onTetrominoMoved(MovementType type) {}

    default void onTetrominoHeld() {}

    default void onLinesCleared(LineClearType type) {}

    default void onGameOver() {}

}