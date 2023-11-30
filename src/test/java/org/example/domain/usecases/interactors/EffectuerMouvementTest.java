package org.example.domain.usecases.interactors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.domain.model.aventurier.Orientation.NORD;
import static org.example.fixtures.AventurierTestBuilder.unAventurier;
import static org.example.fixtures.CarteTestBuilder.uneCarte;

import org.example.domain.model.aventurier.Aventurier;
import org.example.domain.model.aventurier.Orientation;
import org.example.domain.model.carte.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EffectuerMouvementTest {

    @Autowired private EffectuerMouvement effectuerMouvement;

    @ParameterizedTest
    @CsvSource({"NORD, 1, 0", "SUD, 1, 2", "EST, 2, 1", "OUEST, 0, 1"})
    void un_aventurier_devrait_se_deplacer_dans_la_bonne_direction(
            Orientation orientation, int abscisseAttendue, int ordonneeAttendue) {
        // Given
        int largeur = 3;
        int hauteur = 3;
        Aventurier aventurier =
                unAventurier()
                        .avecOrientation(orientation)
                        .avecPosition(1, 1)
                        .avecSequenceMouvement("A")
                        .build();
        Case[][] plateau = {
            {new Plaine(0, 0), new Plaine(1, 0), new Plaine(2, 0)},
            {new Plaine(0, 1), new Plaine(1, 1, aventurier), new Plaine(2, 1)},
            {new Plaine(0, 2), new Plaine(1, 2), new Plaine(2, 2)}
        };
        Carte carte =
                uneCarte()
                        .avecDimensions(largeur, hauteur)
                        .avecPlateau(plateau)
                        .avecAventuriers(aventurier)
                        .build();

        // When
        effectuerMouvement.executer(aventurier, carte);

        // Then
        assertThat(aventurier.getAbscisse()).isEqualTo(abscisseAttendue);
        assertThat(aventurier.getOrdonnee()).isEqualTo(ordonneeAttendue);
    }

    @Test
    void un_aventurier_devrait_pouvoir_se_deplacer_sur_une_plaine() {
        // Given
        int largeur = 2;
        int hauteur = 2;
        Aventurier aventurier =
                unAventurier()
                        .avecOrientation(NORD)
                        .avecPosition(1, 1)
                        .avecSequenceMouvement("A")
                        .build();
        Case[][] plateau = {
            {new Plaine(0, 0), new Plaine(1, 0)},
            {new Plaine(0, 1), new Plaine(1, 1, aventurier)}
        };
        Carte carte =
                uneCarte()
                        .avecDimensions(largeur, hauteur)
                        .avecPlateau(plateau)
                        .avecAventuriers(aventurier)
                        .build();

        // When
        effectuerMouvement.executer(aventurier, carte);

        // Then
        // On vérifie si la case occupée sur laquelle l'aventurier est bien arrivé sur sa nouvelle
        // case
        assertThat(carte.getPlateau()[0][1].getAventurier()).isEqualTo(aventurier);
        // On vérifie si la case de départ de l'aventurier ne contient plus l'aventurier
        assertThat(carte.getPlateau()[1][1].getAventurier()).isNull();
        // On vérifie si les coordoonées de l'aventurier se déplaçant sont bien conformes à sa case
        assertThat(aventurier.getAbscisse()).isEqualTo(1);
        assertThat(aventurier.getOrdonnee()).isEqualTo(0);
    }

    @Test
    void un_aventurier_devrait_pouvoir_se_deplacer_sur_un_tresor() {
        // Given
        int largeur = 2;
        int hauteur = 2;
        Aventurier aventurier =
                unAventurier()
                        .avecOrientation(NORD)
                        .avecPosition(1, 1)
                        .avecSequenceMouvement("A")
                        .build();
        Case[][] plateau = {
            {new Plaine(0, 0), new Tresor(1, 0, 0)},
            {new Plaine(0, 1), new Plaine(1, 1, aventurier)}
        };
        Carte carte =
                uneCarte()
                        .avecDimensions(largeur, hauteur)
                        .avecPlateau(plateau)
                        .avecAventuriers(aventurier)
                        .build();

        // When
        effectuerMouvement.executer(aventurier, carte);

        // Then
        // On vérifie si la case occupée sur laquelle l'aventurier est bien arrivé sur sa nouvelle
        // case
        assertThat(carte.getPlateau()[0][1].getAventurier()).isEqualTo(aventurier);
        // On vérifie si la case de départ de l'aventurier ne contient plus l'aventurier
        assertThat(carte.getPlateau()[1][1].getAventurier()).isNull();
        // On vérifie si les coordoonées de l'aventurier se déplaçant sont bien conformes à sa case
        assertThat(aventurier.getAbscisse()).isEqualTo(1);
        assertThat(aventurier.getOrdonnee()).isEqualTo(0);
    }

    @Test
    void un_aventurier_devrait_ramasser_un_tresor_s_il_y_en_a() {
        // Given
        int largeur = 2;
        int hauteur = 2;
        Aventurier aventurier =
                unAventurier()
                        .avecOrientation(NORD)
                        .avecPosition(1, 1)
                        .avecSequenceMouvement("A")
                        .build();
        Case[][] plateau = {
            {new Plaine(0, 0), new Tresor(1, 0, 2)},
            {new Plaine(0, 1), new Plaine(1, 1, aventurier)}
        };
        Carte carte =
                uneCarte()
                        .avecDimensions(largeur, hauteur)
                        .avecPlateau(plateau)
                        .avecAventuriers(aventurier)
                        .build();

        // When
        effectuerMouvement.executer(aventurier, carte);

        // Then
        // On vérifie que l'aventurier a bien récupéré son trésor
        assertThat(aventurier.getNombreTresors()).isEqualTo(1);
        // On vérifie si la case trésor a bien perdu un trésor
        assertThat(((Tresor) carte.getPlateau()[0][1]).getNombreTresor()).isEqualTo(1);
    }

    @Test
    void un_aventurier_ne_devrait_pas_ramasser_un_tresor_s_il_n_y_en_a_pas() {
        // Given
        int largeur = 2;
        int hauteur = 2;
        Aventurier aventurier =
                unAventurier()
                        .avecOrientation(NORD)
                        .avecPosition(1, 1)
                        .avecSequenceMouvement("A")
                        .build();
        Case[][] plateau = {
            {new Plaine(0, 0), new Tresor(1, 0, 0)},
            {new Plaine(0, 1), new Plaine(1, 1, aventurier)}
        };
        Carte carte =
                uneCarte()
                        .avecDimensions(largeur, hauteur)
                        .avecPlateau(plateau)
                        .avecAventuriers(aventurier)
                        .build();

        // When
        effectuerMouvement.executer(aventurier, carte);

        // Then
        // On vérifie que l'aventurier n'a toujours pas de trésor
        assertThat(aventurier.getNombreTresors()).isEqualTo(0);
    }

    @Test
    void un_aventurier_ne_devrait_pas_pouvoir_se_deplacer_sur_une_montagne() {
        // Given
        int largeur = 2;
        int hauteur = 2;

        Aventurier aventurier =
                unAventurier()
                        .avecOrientation(NORD)
                        .avecPosition(1, 1)
                        .avecSequenceMouvement("A")
                        .build();
        Case[][] plateau = {
            {new Plaine(0, 0), new Montagne(1, 0)},
            {new Plaine(0, 1), new Plaine(1, 1, aventurier)}
        };
        Carte carte =
                uneCarte()
                        .avecDimensions(largeur, hauteur)
                        .avecPlateau(plateau)
                        .avecAventuriers(aventurier)
                        .build();

        // When
        effectuerMouvement.executer(aventurier, carte);

        // Then
        // On vérifie si la montagne sur laquelle l'aventurier était censé arriver est bien sans
        // aventurier dessus
        assertThat(carte.getPlateau()[0][1].getAventurier()).isNull();
        // On vérifie si la case de départ de l'aventurier contient toujours l'aventurier
        assertThat(carte.getPlateau()[1][1].getAventurier()).isEqualTo(aventurier);
        // On vérifie si les coordoonées de l'aventurier sont bien conformes à sa case
        assertThat(aventurier.getAbscisse()).isEqualTo(1);
        assertThat(aventurier.getOrdonnee()).isEqualTo(1);
    }

    @Test
    void
            un_aventurier_ne_devrait_pas_pouvoir_se_deplacer_sur_une_case_occupee_par_un_autre_aventurier() {
        // Given
        int largeur = 2;
        int hauteur = 2;
        Aventurier aventurierOccupantLaCase = unAventurier().avecPosition(1, 0).build();
        Aventurier aventurierSeDeplacant =
                unAventurier().avecOrientation(NORD).avecPosition(1, 1).build();
        Case[][] plateau = {
            {new Plaine(0, 0), new Plaine(1, 0, aventurierOccupantLaCase)},
            {new Plaine(1, 0), new Plaine(1, 1, aventurierSeDeplacant)}
        };
        Carte carte =
                uneCarte()
                        .avecDimensions(largeur, hauteur)
                        .avecPlateau(plateau)
                        .avecAventuriers(aventurierOccupantLaCase, aventurierSeDeplacant)
                        .build();

        // When
        effectuerMouvement.executer(aventurierSeDeplacant, carte);

        // Then
        // On vérifie si la case occupée sur laquelle l'aventurier était censé arriver contient bien
        // l'aventurier déjà présent
        assertThat(carte.getPlateau()[0][1].getAventurier()).isEqualTo(aventurierOccupantLaCase);
        // On vérifie si la case de départ de l'aventurier se déplaçant contient toujours
        // l'aventurier
        assertThat(carte.getPlateau()[1][1].getAventurier()).isEqualTo(aventurierSeDeplacant);
        // On vérifie si les coordoonées de l'aventurier se déplaçant sont bien conformes à sa case
        assertThat(aventurierSeDeplacant.getAbscisse()).isEqualTo(1);
        assertThat(aventurierSeDeplacant.getOrdonnee()).isEqualTo(1);
    }
}
