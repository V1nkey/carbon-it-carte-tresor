package org.example.domain.model.carte;

import org.example.domain.model.aventurier.Aventurier;

public class Plaine extends Case {

    public Plaine(int abscisse, int ordonnee) {
        super(abscisse, ordonnee);
    }

    public Plaine(int abscisse, int ordonnee, Aventurier aventurier) {
        super(abscisse, ordonnee, aventurier);
    }

    @Override
    public boolean peutAccueillirAventurier() {
        return getAventurier() == null;
    }
}
