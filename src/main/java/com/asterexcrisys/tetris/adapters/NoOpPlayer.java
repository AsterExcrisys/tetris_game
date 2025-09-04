package com.asterexcrisys.tetris.adapters;

public class NoOpPlayer implements Playable {

    @Override
    public int getCycleCount() {
        return 0;
    }

    @Override
    public void setCycleCount(int cycleCount) {}

    @Override
    public double getVolume() {
        return 0.0;
    }

    @Override
    public void setVolume(double volume) {}

    @Override
    public Runnable getListener() {
        return null;
    }

    @Override
    public void setListener(Runnable listener) {}

    @Override
    public void play() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void stop() {}

    @Override
    public void dispose() {}

    @Override
    public void close() {}

}