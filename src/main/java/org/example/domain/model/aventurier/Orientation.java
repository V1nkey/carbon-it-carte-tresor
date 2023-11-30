package org.example.domain.model.aventurier;

import java.util.NoSuchElementException;

public enum Orientation {
    NORD("N"),
    SUD("S"),
    EST("E"),
    OUEST("O");

    private final String initiale;

    Orientation(String initiale) {
        this.initiale = initiale;
    }

    public static Orientation fromInitiale(String initiale) {
        return switch (initiale) {
            case "N" -> NORD;
            case "S" -> SUD;
            case "E" -> EST;
            case "O" -> OUEST;
            default -> throw new NoSuchElementException();
        };
    }

    public String getInitiale() {
        return initiale;
    }
}
