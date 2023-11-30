package org.example.domain.model.carte;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.fixtures.AventurierTestBuilder.unAventurier;
import static org.example.fixtures.CarteTestBuilder.uneCarte;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.example.domain.model.aventurier.Aventurier;
import org.example.domain.model.exceptions.CaseOccupeeException;
import org.example.fixtures.CarteTestBuilder;
import org.junit.jupiter.api.Test;

public class CarteTest {

    @Test
    void afficher_carte_devrait_afficher_les_cases_de_la_carte() {
        // Given
        Carte carte = uneCarte().build();

        // When
        String plateau = carte.afficherPlateau();

        // Then
        assertThat(plateau)
                .isEqualTo(
                        """
        . |M |. |
        . |Lara |M |
        . |. |. |
        T(2) |T(3) |. |
        """);
    }

    @Test
    void
            le_constructeur_devrait_lancer_une_exception_quand_deux_aventuriers_commencent_le_jeu_sur_la_meme_case() {
        // Given
        Aventurier aventurier1 = unAventurier().avecNom("Mario").avecPosition(1, 1).build();
        Aventurier aventurier2 = unAventurier().avecNom("Link").avecPosition(1, 1).build();

        CarteTestBuilder carteTestBuilder = uneCarte().avecAventuriers(aventurier1, aventurier2);

        // When Then
        assertThrows(CaseOccupeeException.class, carteTestBuilder::build);
    }

    @Test
    void
            le_constructeur_devrait_lancer_une_exception_quand_un_aventurier_commence_le_jeu_sur_une_montagne() {
        // Given
        Case[][] plateau = {{new Montagne(0, 0)}};
        Aventurier aventurier = unAventurier().avecNom("Mario").avecPosition(0, 0).build();

        CarteTestBuilder carteTestBuilder =
                uneCarte().avecDimensions(1, 1).avecAventuriers(aventurier).avecPlateau(plateau);

        // When Then
        assertThrows(CaseOccupeeException.class, carteTestBuilder::build);
    }
}
