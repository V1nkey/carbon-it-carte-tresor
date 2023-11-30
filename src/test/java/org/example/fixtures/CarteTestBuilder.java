package org.example.fixtures;

import java.util.Arrays;
import java.util.List;
import org.example.domain.model.aventurier.Aventurier;
import org.example.domain.model.carte.Carte;
import org.example.domain.model.carte.Case;
import org.example.domain.model.carte.Montagne;
import org.example.domain.model.carte.Plaine;
import org.example.domain.model.carte.Tresor;

public class CarteTestBuilder {
    private int largeur = 3;
    private int hauteur = 4;
    private Case[][] plateau = {
        {new Plaine(0, 0), new Montagne(1, 0), new Plaine(2, 0)},
        {new Plaine(0, 1), new Plaine(1, 1), new Montagne(2, 1)},
        {new Plaine(0, 2), new Plaine(1, 2), new Plaine(2, 2)},
        {new Tresor(0, 3, 2), new Tresor(1, 3, 3), new Plaine(2, 3)}
    };
    private List<Aventurier> aventuriers = List.of();

    public static CarteTestBuilder uneCarte() {
        return new CarteTestBuilder();
    }

    public Carte build() {
        return new Carte(largeur, hauteur, plateau, aventuriers);
    }

    public CarteTestBuilder avecDimensions(int largeur, int hauteur) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        return this;
    }

    public CarteTestBuilder avecPlateau(Case[][] plateau) {
        this.plateau = plateau;
        return this;
    }

    public CarteTestBuilder avecAventuriers(Aventurier... aventuriers) {
        this.aventuriers = Arrays.asList(aventuriers);
        return this;
    }
}
