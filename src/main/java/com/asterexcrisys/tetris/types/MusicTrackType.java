package com.asterexcrisys.tetris.types;

import com.asterexcrisys.tetris.MainApplication;
import com.asterexcrisys.tetris.constants.ResourceConstants;
import java.util.Objects;

public enum MusicTrackType {

    MAIN_THEME(ResourceConstants.MAIN_THEME_TRACK),
    PRIMARY_THEME(ResourceConstants.PRIMARY_THEME_TRACK),
    SECONDARY_THEME(ResourceConstants.SECONDARY_THEME_TRACK),
    TERTIARY_THEME(ResourceConstants.TERTIARY_THEME_TRACK);

    private final String resource;

    MusicTrackType(String resource) {
        this.resource = Objects.requireNonNull(MainApplication.class.getResource(resource)).toExternalForm();
    }

    public String resource() {
        return resource;
    }

    public static MusicTrackType resourceOf(String resource) {
        for (MusicTrackType type : MusicTrackType.values()) {
            if (type.resource().equals(resource)) {
                return type;
            }
        }
        return null;
    }

}