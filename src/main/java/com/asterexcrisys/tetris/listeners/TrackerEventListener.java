package com.asterexcrisys.tetris.listeners;

public interface TrackerEventListener {

    default void onLevelCleared(int level) {}

    default void onScoreChanged(int score) {}

}