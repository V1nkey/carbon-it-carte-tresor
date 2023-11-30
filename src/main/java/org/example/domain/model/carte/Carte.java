package org.example.domain.model.carte;

import java.util.*;
import org.example.domain.model.aventurier.Aventurier;
import org.example.domain.model.exceptions.CaseOccupeeException;

public class Carte {
    private final int largeur;
    private final int hauteur;
    private final Case[][] plateau;
    private final List<Aventurier> aventuriers;

    public Carte(int largeur, int hauteur, Case[][] plateau, List<Aventurier> aventuriers) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.plateau = plateau;
        this.aventuriers = aventuriers;

        this.aventuriers.forEach(
                aventurier -> {
                    Case caseAventurier =
                            this.plateau[aventurier.getAbscisse()][aventurier.getOrdonnee()];
                    if (!caseAventurier.peutAccueillirAventurier()) {
                        throw new CaseOccupeeException(
                                String.format(
                                        "Vous ne pouvez pas placer %s sur cette case car elle est"
                                                + " déjà occupée ou inaccessible",
                                        aventurier.getNom()));
                    }

                    caseAventurier.accueillirAventurier(aventurier);
                });
    }

    public int getLargeur() {
        return largeur;
    }

    public int getHauteur() {
        return hauteur;
    }

    public Case[][] getPlateau() {
        return plateau;
    }

    public List<Aventurier> getAventuriers() {
        return aventuriers;
    }

    public String afficherPlateau() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                Case caseCourante = plateau[i][j];
                switch (caseCourante) {
                    case Montagne montagne -> stringBuilder.append("M |");
                    case Tresor tresor -> {
                        if (tresor.getAventurier() == null) {
                            stringBuilder
                                    .append("T(")
                                    .append(tresor.getNombreTresor())
                                    .append(") |");
                        } else {
                            stringBuilder.append(tresor.getAventurier().getNom()).append(" |");
                        }
                    }
                    case Plaine plaine -> {
                        if (plaine.getAventurier() == null) {
                            stringBuilder.append(". |");
                        } else {
                            stringBuilder.append(plaine.getAventurier().getNom()).append(" |");
                        }
                    }
                    default -> throw new IllegalStateException("Unexpected value: " + caseCourante);
                }
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder =
                new StringBuilder(String.format("C - %d - %d\n", largeur, hauteur));

        Map<String, List<Case>> casesSpeciales = new HashMap<>();
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                Case caseCourante = plateau[i][j];
                if (caseCourante instanceof Montagne || caseCourante instanceof Tresor) {
                    String key = caseCourante.getClass().getName();
                    if (!casesSpeciales.containsKey(key)) {
                        casesSpeciales.put(key, new ArrayList<>());
                    }

                    casesSpeciales.get(key).add(caseCourante);
                }
            }
        }

        if (casesSpeciales.containsKey(Montagne.class.getName())) {
            casesSpeciales
                    .get(Montagne.class.getName())
                    .forEach(montagne -> stringBuilder.append(montagne.toString()).append("\n"));
        }

        if (casesSpeciales.containsKey(Tresor.class.getName())) {
            casesSpeciales
                    .get(Tresor.class.getName())
                    .forEach(
                            tresor -> {
                                if (!"".equals(tresor.toString())) {
                                    stringBuilder.append(tresor).append("\n");
                                }
                            });
        }

        aventuriers.forEach(aventurier -> stringBuilder.append(aventurier.toString()).append("\n"));

        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Carte carte = (Carte) o;
        return largeur == carte.largeur
                && hauteur == carte.hauteur
                && Arrays.deepEquals(plateau, carte.plateau)
                && Objects.equals(aventuriers, carte.aventuriers);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(largeur, hauteur, aventuriers);
        result = 31 * result + Arrays.deepHashCode(plateau);
        return result;
    }
}
