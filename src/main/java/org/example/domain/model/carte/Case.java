package org.example.domain.model.carte;

import java.util.Objects;
import org.example.domain.model.aventurier.Aventurier;

public abstract class Case {
    private int abscisse;
    private int ordonnee;
    private Aventurier aventurier;

    public Case(int abscisse, int ordonnee) {
        this.abscisse = abscisse;
        this.ordonnee = ordonnee;
    }

    public Case(int abscisse, int ordonnee, Aventurier aventurier) {
        this.abscisse = abscisse;
        this.ordonnee = ordonnee;
        this.aventurier = aventurier;
    }

    public abstract boolean peutAccueillirAventurier();

    public void accueillirAventurier(Aventurier aventurier) {
        if (peutAccueillirAventurier()) {
            this.aventurier = aventurier;
        }
    }

    public void libererAventurier() {
        this.aventurier = null;
    }

    public int getAbscisse() {
        return abscisse;
    }

    public int getOrdonnee() {
        return ordonnee;
    }

    public Aventurier getAventurier() {
        return aventurier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Case aCase = (Case) o;
        return abscisse == aCase.abscisse
                && ordonnee == aCase.ordonnee
                && Objects.equals(aventurier, aCase.aventurier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(abscisse, ordonnee, aventurier);
    }
}
