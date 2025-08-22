package com.asterexcrisys.tetris.utilities;

import com.asterexcrisys.tetris.constants.GameConstants;
import com.asterexcrisys.tetris.types.Position;
import com.asterexcrisys.tetris.types.TetrominoType;
import javafx.scene.paint.Color;
import java.util.Random;

public class GameUtility {

    private static final Random RANDOM;

    static {
        RANDOM = new Random();
    }

    public static Position randomomisePosition() {
        return Position.of(
                RANDOM.nextInt(GameConstants.BOARD_HEIGHT - 3),
                RANDOM.nextInt(GameConstants.BOARD_WIDTH - 3)
        );
    }

    public static TetrominoType randomiseType() {
        TetrominoType[] types = TetrominoType.values();
        return types[RANDOM.nextInt(types.length)];
    }

    public static Color randomiseColor() {
        Color[] colors = GameConstants.SUPPORTED_COLORS;
        return colors[RANDOM.nextInt(colors.length)];
    }

    public static String toRGBA(Color color) {
        int red = (int) Math.round(color.getRed() * 255);
        int green = (int) Math.round(color.getGreen() * 255);
        int blue = (int) Math.round(color.getBlue() * 255);
        return String.format("rgba(%s, %s, %s, %s)", red, green, blue, color.getOpacity());
    }

}