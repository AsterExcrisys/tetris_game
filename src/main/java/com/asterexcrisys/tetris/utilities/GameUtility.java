package com.asterexcrisys.tetris.utilities;

import com.asterexcrisys.tetris.constants.GameConstants;
import com.asterexcrisys.tetris.types.Element;
import com.asterexcrisys.tetris.types.Position;
import com.asterexcrisys.tetris.types.TetrominoType;
import javafx.scene.paint.Color;
import java.util.*;
import java.util.regex.Pattern;

public final class GameUtility {

    private static final Random RANDOM;
    private static final Pattern IDENTIFIER_PATTERN;

    static {
        RANDOM = new Random();
        IDENTIFIER_PATTERN = Pattern.compile("[a-zA-Z0-9_]{5,20}");
    }

    public static List<Element> shuffleTetrominoes(boolean useFixedColors) {
        TetrominoType[] types = TetrominoType.values();
        for (int i = 0; i < types.length; i++) {
            int index = RANDOM.nextInt(types.length);
            TetrominoType type = types[index];
            types[index] = types[i];
            types[i] = type;
        }
        List<Element> list = new ArrayList<>();
        if (useFixedColors) {
            for (TetrominoType type : types) {
                list.add(Element.of(randomisePosition(), type, type.color()));
            }
        } else {
            for (TetrominoType type : types) {
                list.add(Element.of(randomisePosition(), type, randomiseColor()));
            }
        }
        return list;
    }

    public static Position randomisePosition() {
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

    public static boolean validateIdentifier(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            return false;
        }
        return IDENTIFIER_PATTERN.matcher(identifier).matches();
    }

    public static String toRGBA(Color color) {
        int red = (int) Math.round(color.getRed() * 255);
        int green = (int) Math.round(color.getGreen() * 255);
        int blue = (int) Math.round(color.getBlue() * 255);
        return String.format("rgba(%s, %s, %s, %s)", red, green, blue, color.getOpacity());
    }

}