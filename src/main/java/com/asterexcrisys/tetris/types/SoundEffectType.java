package com.asterexcrisys.tetris.types;

import com.asterexcrisys.tetris.MainApplication;
import com.asterexcrisys.tetris.constants.ResourceConstants;
import java.util.Objects;

public enum SoundEffectType {

    TETROMINO_MOVED(ResourceConstants.TETROMINO_MOVED_EFFECT),
    TETROMINO_LOCKED(ResourceConstants.TETROMINO_LOCKED_EFFECT),
    ONE_LINE_CLEARED(ResourceConstants.ONE_LINE_CLEARED_EFFECT),
    TWO_LINES_CLEARED(ResourceConstants.TWO_LINES_CLEARED_EFFECT),
    THREE_LINES_CLEARED(ResourceConstants.THREE_LINES_CLEARED_EFFECT),
    FOUR_LINES_CLEARED(ResourceConstants.FOUR_LINES_CLEARED_EFFECT),
    LEVEL_CLEARED(ResourceConstants.LEVEL_CLEARED_EFFECT),
    GAME_OVER(ResourceConstants.GAME_OVER_EFFECT);

    private final String resource;

    SoundEffectType(String resource) {
        this.resource = Objects.requireNonNull(MainApplication.class.getResource(resource)).toExternalForm();
    }

    public String resource() {
        return resource;
    }

    public static SoundEffectType resourceOf(String resource) {
        for (SoundEffectType type : SoundEffectType.values()) {
            if (type.resource().equals(resource)) {
                return type;
            }
        }
        return null;
    }

}