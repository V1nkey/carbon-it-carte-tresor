package org.example.domain.model.carte;

import org.example.domain.model.aventurier.Aventurier;

public class Tresor extends Plaine {
    private int nombreTresor;

    public Tresor(int abscisse, int ordonnee, int nombreTresor) {
        super(abscisse, ordonnee);
        this.nombreTresor = nombreTresor;
    }

    public Tresor(int abscisse, int ordonnee, Aventurier aventurier, int nombreTresor) {
        super(abscisse, ordonnee, aventurier);
        this.nombreTresor = nombreTresor;
    }

    public int perdreTresor() {
        if (nombreTresor == 0) {
            return 0;
        }
        nombreTresor--;
        return 1;
    }

    public int getNombreTresor() {
        return nombreTresor;
    }

    @Override
    public String toString() {
        if (nombreTresor == 0) {
            return "";
        }

        return String.format("T - %d - %d - %d", getAbscisse(), getOrdonnee(), nombreTresor);
    }
}
