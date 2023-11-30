package org.example.domain.usecases.interactors;

import org.example.domain.model.aventurier.Action;
import org.example.domain.model.aventurier.Aventurier;
import org.example.domain.model.carte.*;
import org.springframework.stereotype.Component;

@Component
public class EffectuerMouvement {

    public void executer(Aventurier aventurier, Carte carte) {
        Action prochaineAction = aventurier.determinerProchaineAction();

        switch (prochaineAction) {
            case TOURNER_A_GAUCHE -> aventurier.tournerGauche();
            case TOURNER_A_DROITE -> aventurier.tournerDroite();
            case AVANCER -> avancer(aventurier, carte);
        }
    }

    private void avancer(Aventurier aventurier, Carte carte) {
        Case[][] plateau = carte.getPlateau();

        int ancienneAbscisse = aventurier.getAbscisse();
        int ancienneOrdonnee = aventurier.getOrdonnee();

        int nouvelleOrdonnee = aventurier.getOrdonnee();
        int nouvelleAbscisse = aventurier.getAbscisse();

        Case caseArrivee = determinerPotentielleCaseArrivee(aventurier, carte);

        // Si la case d'arrivée est la même que celle de départ (this), c'est que l'aventurier est
        // sur le bord de la carte et veut se déplacer en dehors, on l'en empêche donc
        if (ancienneAbscisse == nouvelleAbscisse && ancienneOrdonnee == nouvelleOrdonnee) {
            return;
        }

        // Si l'aventurier est bloqué par une montagne ou un autre aventurier, il ne peut donc pas
        // bouger
        if (!caseArrivee.peutAccueillirAventurier()) {
            return;
        }

        // On effectue la mise à jour du plateau et de la position de l'aventurier suite à ses
        // déplacements
        plateau[ancienneOrdonnee][ancienneAbscisse].libererAventurier();
        aventurier.deplacer(nouvelleAbscisse, nouvelleOrdonnee);
        caseArrivee.accueillirAventurier(aventurier);

        if (caseArrivee instanceof Tresor tresor) {
            int tresorARecuperer = tresor.perdreTresor();
            if (tresorARecuperer != 0) {
                aventurier.recupererTresor();
            }
        }
    }

    private Case determinerPotentielleCaseArrivee(Aventurier aventurier, Carte carte) {
        int ancienneAbscisse = aventurier.getAbscisse();
        int ancienneOrdonnee = aventurier.getOrdonnee();

        int nouvelleOrdonnee = aventurier.getOrdonnee();
        int nouvelleAbscisse = aventurier.getAbscisse();

        Case[][] plateau = carte.getPlateau();

        return switch (aventurier.getOrientation()) {
            case NORD -> {
                // Si l'aventurier est positionné sur la première ligne, il ne peut pas se
                // déplacer vers le nord et reste où il est
                if (ancienneOrdonnee == 0) {
                    yield plateau[ancienneOrdonnee][ancienneAbscisse];
                }

                nouvelleOrdonnee = ancienneOrdonnee - 1;
                yield plateau[nouvelleOrdonnee][ancienneAbscisse];
            }
            case SUD -> {
                // Si l'aventurier est positionné sur la dernière ligne, il ne peut pas se
                // déplacer vers le sud et reste où il est
                if (ancienneOrdonnee == carte.getHauteur() - 1) {
                    yield plateau[ancienneOrdonnee][ancienneAbscisse];
                }

                nouvelleOrdonnee = ancienneOrdonnee + 1;
                yield plateau[nouvelleOrdonnee][ancienneAbscisse];
            }
            case EST -> {
                // Si l'aventurier est positionné sur la dernière colonne, il ne peut pas se
                // déplacer vers l'est et reste où il est
                if (ancienneAbscisse == carte.getLargeur() - 1) {
                    yield plateau[ancienneAbscisse][ancienneOrdonnee];
                }

                nouvelleAbscisse = ancienneAbscisse + 1;
                yield plateau[ancienneOrdonnee][nouvelleAbscisse];
            }
            case OUEST -> {
                // Si l'aventurier est positionné sur la première colonne, il ne peut pas se
                // déplacer vers l'ouest et reste où il est
                if (ancienneAbscisse == 0) {
                    yield plateau[ancienneOrdonnee][ancienneAbscisse];
                }

                nouvelleAbscisse = ancienneAbscisse - 1;
                yield plateau[ancienneOrdonnee][nouvelleAbscisse];
            }
        };
    }
}
