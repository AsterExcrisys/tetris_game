package com.asterexcrisys.tetris.services;

import com.asterexcrisys.tetris.types.Element;
import com.asterexcrisys.tetris.utilities.GameUtility;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class TetrominoQueue {

    private final Queue<Element> queue;
    private Element slot;
    private boolean canHold;

    public TetrominoQueue() {
        queue = new LinkedList<>();
        slot = null;
        canHold = true;
        queue.addAll(GameUtility.shuffleTetrominos());
    }

    public boolean canHold() {
        return canHold;
    }

    public Element peek() {
        return queue.peek();
    }

    public Element poll() {
        canHold = true;
        Element element = queue.poll();
        if (queue.isEmpty()) {
            queue.addAll(GameUtility.shuffleTetrominos());
        }
        return element;
    }

    public Element hold(Element current) {
        if (!canHold) {
            return current;
        }
        canHold = false;
        if (slot == null) {
            slot = Objects.requireNonNull(current);
            return poll();
        }
        Element previous = slot;
        slot = Objects.requireNonNull(current);
        return Element.of(
                GameUtility.randomisePosition(),
                previous.type(),
                previous.color()
        );
    }

    public void reset() {
        queue.clear();
        slot = null;
        canHold = true;
        queue.addAll(GameUtility.shuffleTetrominos());
    }

}