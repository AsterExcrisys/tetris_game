package com.asterexcrisys.tetris.adapters;

import javafx.scene.media.MediaPlayer;
import java.util.Objects;

public class MusicPlayer implements Playable {

    private final MediaPlayer player;

    public MusicPlayer(MediaPlayer player) {
        this.player = Objects.requireNonNull(player);
    }

    @Override
    public int getCycleCount() {
        return player.getCycleCount();
    }

    @Override
    public void setCycleCount(int cycleCount) {
        player.setCycleCount(cycleCount);
    }

    @Override
    public double getVolume() {
        return player.getVolume();
    }

    @Override
    public void setVolume(double volume) {
        player.setVolume(volume);
    }

    @Override
    public Runnable getListener() {
        return player.getOnEndOfMedia();
    }

    @Override
    public void setListener(Runnable listener) {
        player.setOnEndOfMedia(listener);
    }

    @Override
    public void play() {
        player.play();
    }

    @Override
    public void pause() {
        player.pause();
    }

    @Override
    public void resume() {
        player.play();
    }

    @Override
    public void stop() {
        player.stop();
    }

    @Override
    public void dispose() {
        player.dispose();
    }

    @Override
    public void close() {
        dispose();
    }

}