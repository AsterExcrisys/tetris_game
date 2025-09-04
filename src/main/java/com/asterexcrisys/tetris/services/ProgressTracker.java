package com.asterexcrisys.tetris.services;

import com.asterexcrisys.tetris.constants.GameConstants;
import com.asterexcrisys.tetris.listeners.TrackerEventListener;
import com.asterexcrisys.tetris.types.LineClearType;

public class ProgressTracker {

    private int progress;
    private int level;
    private int score;
    private TrackerEventListener listener;

    public ProgressTracker(int level) {
        progress = 0;
        this.level = Math.max(1, level);
        score = 0;
        listener = null;
    }

    public int level() {
        return level;
    }

    public int score() {
        return score;
    }

    public TrackerEventListener getListener() {
        return listener;
    }

    public void setListener(TrackerEventListener listener) {
        this.listener = listener;
    }

    public void track(LineClearType type) {
        if (type == null) {
            return;
        }
        switch (type) {
            case SINGLE, DOUBLE, TRIPLE, TETRIS -> {
                progress += type.linesCleared();
                score += type.basePoints() * level;
                if (listener != null) {
                    listener.onScoreChanged(score);
                }
                if (progress % GameConstants.LINE_CLEARS_PER_LEVEL != 0) {
                    break;
                }
                level++;
                if (listener != null) {
                    listener.onLevelCleared(level);
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