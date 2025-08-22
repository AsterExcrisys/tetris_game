package com.asterexcrisys.tetris.services;

public class GravityCounter {

    private int limit;
    private int counter;

    public GravityCounter(int limit) {
        this.limit = Math.max(1, limit);
        counter = this.limit;
    }

    public int limit() {
        return limit;
    }

    public boolean countdown() {
        if (counter > 0) {
            counter--;
        }
        return counter == 0;
    }

    public void reset() {
        counter = limit;
    }

    public void reset(int limit) {
        this.limit = Math.max(1, limit);
        counter = this.limit;
    }

}