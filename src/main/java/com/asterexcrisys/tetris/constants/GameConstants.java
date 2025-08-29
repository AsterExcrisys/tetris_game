package com.asterexcrisys.tetris.constants;

import javafx.scene.paint.Color;

public final class GameConstants {

    public static final int BOARD_HEIGHT = 20;
    public static final int BOARD_WIDTH = 10;
    public static final int GRAVITY_TIME = 10;
    public static final int INITIAL_LEVEL = 1;
    public static final int LINE_CLEARS_PER_LEVEL = 10;
    public static final String DIALOG_TITLE = "Register Session";
    public static final String DIALOG_HEADER = "Once registered, this record will be added to the local leaderboard.";
    public static final String DIALOG_CONTENT = "Please enter a valid player identifier:";
    public static final Color[] SUPPORTED_COLORS = new Color[] {
            Color.rgb(255, 0, 0, 1.0),
            Color.rgb(255, 127, 0, 1.0),
            Color.rgb(255, 255, 0, 1.0),
            Color.rgb(0, 255, 0, 1.0),
            Color.rgb(0, 255, 255, 1.0),
            Color.rgb(0, 0, 255, 1.0),
            Color.rgb(128, 0, 128, 1.0)
    };

}