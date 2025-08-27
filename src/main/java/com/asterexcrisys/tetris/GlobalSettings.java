package com.asterexcrisys.tetris;

import com.asterexcrisys.tetris.constants.SettingsConstants;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class GlobalSettings {

    private static volatile GlobalSettings INSTANCE;

    private final AtomicReference<Double> musicVolume;
    private final AtomicReference<Double> soundVolume;

    public GlobalSettings() {
        musicVolume = new AtomicReference<>(SettingsConstants.INITIAL_VOLUME);
        soundVolume = new AtomicReference<>(SettingsConstants.INITIAL_VOLUME);
    }

    public Double getMusicVolume() {
        return musicVolume.get();
    }

    public Double getSoundVolume() {
        return soundVolume.get();
    }

    public void setMusicVolume(Double musicVolume) {
        Objects.requireNonNull(musicVolume);
        if (musicVolume < 0.0 || musicVolume > 1.0) {
            throw new IllegalArgumentException();
        }
        this.musicVolume.set(musicVolume);
    }

    public void setSoundVolume(Double soundVolume) {
        Objects.requireNonNull(soundVolume);
        if (soundVolume < 0.0 || soundVolume > 1.0) {
            throw new IllegalArgumentException();
        }
        this.soundVolume.set(soundVolume);
    }

    public static GlobalSettings getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        synchronized (GlobalSettings.class) {
            if (INSTANCE == null) {
                INSTANCE = new GlobalSettings();
            }
        }
        return INSTANCE;
    }

    public static void clearInstance() {
        synchronized (GlobalSettings.class) {
            if (INSTANCE != null) {
                INSTANCE = null;
            }
        }
    }

}