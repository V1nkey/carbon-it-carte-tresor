package org.example.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.example.domain.model.aventurier.Aventurier;
import org.example.domain.model.aventurier.Orientation;
import org.example.domain.model.carte.*;
import org.example.domain.model.carte.Carte;
import org.example.domain.model.exceptions.InstructionInconnueException;

public class CarteMapper {

    public static Carte map(String fichier) {
        String[] lignes = fichier.split("\n");

        // Retrouve la ligne du fichier qui commence par C pour retrouver les dimensions de la carte
        String initialisationCarte =
                Arrays.stream(lignes)
                        .filter(ligne -> ligne.trim().startsWith("C"))
                        .findFirst()
                        .orElseThrow();

        String[] instructionsCarte = initialisationCarte.split("-");
        int largeurCarte = Integer.parseInt(instructionsCarte[1].trim());
        int hauteurCarte = Integer.parseInt(instructionsCarte[2].trim());

        List<Case> casesSpeciales =
                Arrays.stream(lignes).map(CarteMapper::initCase).filter(Objects::nonNull).toList();

        List<Aventurier> aventuriers =
                Arrays.stream(lignes)
                        .filter(ligne -> ligne.trim().startsWith("A"))
                        .map(CarteMapper::mapAventurier)
                        .toList();

        Case[][] plateau = initPlateau(largeurCarte, hauteurCarte, casesSpeciales);

        return new Carte(largeurCarte, hauteurCarte, plateau, aventuriers);
    }

    private static Aventurier mapAventurier(String ligne) {

        String[] instructionsAventurier = ligne.split("-");
        String nom = instructionsAventurier[1];
        int abscisse = Integer.parseInt(instructionsAventurier[2].trim());
        int ordonnee = Integer.parseInt(instructionsAventurier[3].trim());
        Orientation orientation = Orientation.fromInitiale(instructionsAventurier[4].trim());
        String sequenceMouvement = instructionsAventurier[5].trim();

        return new Aventurier(abscisse, ordonnee, nom, orientation, sequenceMouvement);
    }

    private static Case initCase(String ligne) {
        // On ne traite ici que les lignes correspondant à des cases : Montagne et Trésor
        return switch (ligne.trim().charAt(0)) {
            case 'M' -> {
                String[] instructions = ligne.split("-");
                int abscisse = Integer.parseInt(instructions[1].trim());
                int ordonnee = Integer.parseInt(instructions[2].trim());
                yield new Montagne(abscisse, ordonnee);
            }
            case 'T' -> {
                String[] instructions = ligne.split("-");
                int abscisse = Integer.parseInt(instructions[1].trim());
                int ordonnee = Integer.parseInt(instructions[2].trim());
                int nombreTresor = Integer.parseInt(instructions[3].trim());
                yield new Tresor(abscisse, ordonnee, nombreTresor);
            }
            case '#', 'C', 'A' -> null;
            default -> throw new InstructionInconnueException(
                    String.format("La ligne %s ne commence pas par une instruction valide", ligne));
        };
    }

    private static Case[][] initPlateau(int largeur, int hauteur, List<Case> casesSpeciales) {
        Case[][] plateau = new Case[hauteur][largeur];

        for (Case caseSpeciale : casesSpeciales) {
            plateau[caseSpeciale.getOrdonnee()][caseSpeciale.getAbscisse()] = caseSpeciale;
        }

        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                if (plateau[i][j] == null) {
                    plateau[i][j] = new Plaine(j, i);
                }
            }
        }

        return plateau;
    }
}
