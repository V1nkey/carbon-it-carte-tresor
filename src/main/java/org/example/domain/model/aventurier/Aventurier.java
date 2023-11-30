package org.example.domain.model.aventurier;

import static org.example.domain.model.aventurier.Orientation.NORD;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.stream.Collectors;

public class Aventurier {
    private int abscisse;
    private int ordonnee;
    private final String nom;
    private Orientation orientation;
    private List<Action> sequenceMouvementInitiale;
    private Queue<Action> actionsRestantes;
    private int nombreTresors = 0;

    public Aventurier(
            int abscisse,
            int ordonnee,
            String nom,
            Orientation orientation,
            String sequenceMouvement) {
        this.abscisse = abscisse;
        this.ordonnee = ordonnee;
        this.nom = nom.trim();
        this.orientation = orientation;
        this.sequenceMouvementInitiale =
                sequenceMouvement
                        .chars()
                        .mapToObj(action -> Action.fromInitiale(String.valueOf((char) action)))
                        .collect(Collectors.toList());
        this.actionsRestantes = new LinkedList<>(sequenceMouvementInitiale);
    }

    public void tournerGauche() {
        switch (orientation) {
            case NORD -> this.orientation = Orientation.OUEST;
            case SUD -> this.orientation = Orientation.EST;
            case EST -> this.orientation = NORD;
            case OUEST -> this.orientation = Orientation.SUD;
        }
    }

    public void tournerDroite() {
        switch (orientation) {
            case NORD -> this.orientation = Orientation.EST;
            case SUD -> this.orientation = Orientation.OUEST;
            case EST -> this.orientation = Orientation.SUD;
            case OUEST -> this.orientation = NORD;
        }
    }

    public void recupererTresor() {
        this.nombreTresors++;
    }

    public Action determinerProchaineAction() {
        return actionsRestantes.poll();
    }

    public boolean peutEffectuerAction() {
        return !actionsRestantes.isEmpty();
    }

    public void deplacer(int nouvelleAbscisse, int nouvelleOrdonnee) {
        abscisse = nouvelleAbscisse;
        ordonnee = nouvelleOrdonnee;
    }

    public String getNom() {
        return nom;
    }

    public int getAbscisse() {
        return abscisse;
    }

    public int getOrdonnee() {
        return ordonnee;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public int getNombreTresors() {
        return nombreTresors;
    }

    @Override
    public String toString() {
        return String.format(
                "A - %s - %d - %d - %s - %d",
                nom, abscisse, ordonnee, orientation.getInitiale(), nombreTresors);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aventurier that = (Aventurier) o;
        return abscisse == that.abscisse
                && ordonnee == that.ordonnee
                && nombreTresors == that.nombreTresors
                && Objects.equals(nom, that.nom)
                && orientation == that.orientation
                && Objects.equals(sequenceMouvementInitiale, that.sequenceMouvementInitiale)
                && Objects.equals(actionsRestantes, that.actionsRestantes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                abscisse,
                ordonnee,
                nom,
                orientation,
                sequenceMouvementInitiale,
                actionsRestantes,
                nombreTresors);
    }
}
