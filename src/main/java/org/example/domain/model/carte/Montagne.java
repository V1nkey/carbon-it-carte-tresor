package org.example.domain.model.carte;

public class Montagne extends Case {
    public Montagne(int abscisse, int ordonnee) {
        super(abscisse, ordonnee);
    }

    @Override
    public boolean peutAccueillirAventurier() {
        return false;
    }

    @Override
    public String toString() {
        return String.format("M - %d - %d", getAbscisse(), getOrdonnee());
    }
}
