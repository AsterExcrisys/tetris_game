package com.asterexcrisys.tetris.utilities;

import com.asterexcrisys.tetris.constants.GlobalConstants;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class GlobalUtility {

    public static String getCurrentDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(GlobalConstants.DATE_FORMAT);
        return now.format(formatter);
    }

    public static double clampVolume(double volume) {
        return Math.max(0.0, Math.min(1.0, volume));
    }

    public static String getStackTrace(Throwable throwable) {
        StringBuilder builder = new StringBuilder();
        for (StackTraceElement element : throwable.getStackTrace()) {
            builder.append(element.toString()).append("\n");
        }
        return builder.toString();
    }

}