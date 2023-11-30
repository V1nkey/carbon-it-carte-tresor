package org.example.fixtures;

import org.example.domain.model.aventurier.Aventurier;
import org.example.domain.model.aventurier.Orientation;

public class AventurierTestBuilder {
    private int abscisse = 1;
    private int ordonnee = 1;
    private String nom = "Nathan";
    private Orientation orientation = Orientation.EST;
    private String sequenceMouvement = "AADADA";

    public static AventurierTestBuilder unAventurier() {
        return new AventurierTestBuilder();
    }

    public Aventurier build() {
        return new Aventurier(abscisse, ordonnee, nom, orientation, sequenceMouvement);
    }

    public AventurierTestBuilder avecPosition(int abscisse, int ordonnee) {
        this.abscisse = abscisse;
        this.ordonnee = ordonnee;
        return this;
    }

    public AventurierTestBuilder avecOrientation(Orientation orientation) {
        this.orientation = orientation;
        return this;
    }

    public AventurierTestBuilder avecSequenceMouvement(String sequenceMouvement) {
        this.sequenceMouvement = sequenceMouvement;
        return this;
    }

    public AventurierTestBuilder avecNom(String nom) {
        this.nom = nom;
        return this;
    }
}
