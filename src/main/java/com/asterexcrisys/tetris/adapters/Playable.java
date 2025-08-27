package com.asterexcrisys.tetris.adapters;

public interface Playable extends AutoCloseable {

    int getCycleCount();

    void setCycleCount(int cycleCount);

    double getVolume();

    void setVolume(double volume);

    Runnable getListener();

    void setListener(Runnable listener);

    void play();

    void pause();

    void resume();

    void stop();

    void dispose();

}