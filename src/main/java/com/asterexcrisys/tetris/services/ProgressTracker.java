package com.asterexcrisys.tetris.services;

import com.asterexcrisys.tetris.constants.GameConstants;
import com.asterexcrisys.tetris.types.LineClearType;

public class ProgressTracker {

    private int progress;
    private int level;
    private int score;

    public ProgressTracker(int level) {
        progress = 0;
        this.level = Math.max(1, level);
        score = 0;
    }

    public int level() {
        return level;
    }

    public int score() {
        return score;
    }

    public void track(LineClearType type) {
        if (type == null) {
            return;
        }
        switch (type) {
            case SINGLE, DOUBLE, TRIPLE, TETRIS -> {
                progress += type.linesCleared();
                score += type.basePoints() * level;
                if (progress % GameConstants.LINE_CLEARS_PER_LEVEL == 0) {
                    level++;
                }
            }
        }
    }

    public void reset() {
        progress = 0;
        level = 1;
        score = 0;
    }

    public void reset(int level) {
        progress = 0;
        this.level = Math.max(1, level);
        score = 0;
    }

}