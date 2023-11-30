package org.example.domain.model.aventurier;

import java.util.NoSuchElementException;

public enum Action {
    AVANCER("A"),
    TOURNER_A_DROITE("D"),
    TOURNER_A_GAUCHE("G");

    private final String initiale;

    Action(String initiale) {
        this.initiale = initiale;
    }

    public static Action fromInitiale(String initiale) {
        return switch (initiale) {
            case "A" -> AVANCER;
            case "D" -> TOURNER_A_DROITE;
            case "G" -> TOURNER_A_GAUCHE;
            default -> throw new NoSuchElementException();
        };
    }
}
