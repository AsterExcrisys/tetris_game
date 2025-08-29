package com.asterexcrisys.tetris.types;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import java.util.Objects;

public record Record(int rank, String player, int level, int score) {

    public  Record {
        Objects.requireNonNull(player);
    }

    public SimpleIntegerProperty rankProperty() {
        return new SimpleIntegerProperty(rank);
    }

    public SimpleStringProperty playerProperty() {
        return new SimpleStringProperty(player);
    }

    public SimpleIntegerProperty levelProperty() {
        return new SimpleIntegerProperty(level);
    }

    public SimpleIntegerProperty scoreProperty() {
        return new SimpleIntegerProperty(score);
    }

    public static Record of(int rank, String player, int level, int score) {
        return new Record(rank, player, level, score);
    }

}