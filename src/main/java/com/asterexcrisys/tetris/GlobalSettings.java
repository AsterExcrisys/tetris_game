package com.asterexcrisys.tetris;

import com.asterexcrisys.tetris.constants.ResourceConstants;
import com.asterexcrisys.tetris.constants.SettingsConstants;
import com.asterexcrisys.tetris.handlers.Converter;
import com.asterexcrisys.tetris.handlers.JsonConverter;
import com.asterexcrisys.tetris.utilities.GlobalUtility;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class GlobalSettings {

    private static final Logger LOGGER = Logger.getLogger(GlobalSettings.class.getName());
    private static volatile GlobalSettings INSTANCE;

    private final Converter<Configuration> converter;
    private final AtomicBoolean useFixedColors;
    private final AtomicReference<Double> musicVolume;
    private final AtomicReference<Double> soundVolume;

    private GlobalSettings() {
        converter = new JsonConverter<>(ResourceConstants.CONFIGURATION_FILE);
        Configuration configuration;
        try {
            configuration = converter.deserialize(Configuration.class);
        } catch (IOException exception) {
            LOGGER.log(Level.WARNING, exception.getMessage(), exception);
            configuration = new Configuration();
        }
        useFixedColors = new AtomicBoolean(configuration.useFixedColors());
        musicVolume = new AtomicReference<>(configuration.musicVolume());
        soundVolume = new AtomicReference<>(configuration.soundVolume());
    }

    public boolean getUseFixedColors() {
        return useFixedColors.get();
    }

    public Double getMusicVolume() {
        return musicVolume.get();
    }

    public Double getSoundVolume() {
        return soundVolume.get();
    }

    public void setUseFixedColors(boolean useFixedColors) {
        this.useFixedColors.set(useFixedColors);
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

    public void save() {
        Configuration configuration = Configuration.of(useFixedColors.get(), musicVolume.get(), soundVolume.get());
        try {
            converter.serialize(configuration);
        } catch (IOException exception) {
            LOGGER.log(Level.WARNING, exception.getMessage(), exception);
        }
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

    public record Configuration(
            @JsonProperty(value = "use_fixed_colors") Boolean useFixedColors,
            @JsonProperty(value = "music_volume") Double musicVolume,
            @JsonProperty(value = "sound_volume") Double soundVolume
    ) {

        public Configuration() {
            this(SettingsConstants.INITIAL_OPTION, SettingsConstants.INITIAL_VOLUME, SettingsConstants.INITIAL_VOLUME);
        }

        @JsonCreator
        public Configuration(
                @JsonProperty(value = "use_fixed_colors") Boolean useFixedColors,
                @JsonProperty(value = "music_volume") Double musicVolume,
                @JsonProperty(value = "sound_volume") Double soundVolume
        ) {
            this.useFixedColors = Objects.requireNonNullElse(useFixedColors, SettingsConstants.INITIAL_OPTION);
            this.musicVolume = GlobalUtility.clampVolume(Objects.requireNonNullElse(musicVolume, SettingsConstants.INITIAL_VOLUME));
            this.soundVolume = GlobalUtility.clampVolume(Objects.requireNonNullElse(soundVolume, SettingsConstants.INITIAL_VOLUME));
        }

        public static Configuration of(Boolean useFixedColors, Double musicVolume, Double soundVolume) {
            return new Configuration(useFixedColors, musicVolume, soundVolume);
        }

    }

}