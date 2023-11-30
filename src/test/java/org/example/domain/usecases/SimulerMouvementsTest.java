package org.example.domain.usecases;

import static org.example.domain.model.aventurier.Orientation.EST;
import static org.example.domain.model.aventurier.Orientation.SUD;
import static org.example.fixtures.AventurierTestBuilder.unAventurier;
import static org.example.fixtures.CarteTestBuilder.uneCarte;

import org.assertj.core.api.Assertions;
import org.example.domain.model.aventurier.Aventurier;
import org.example.domain.model.carte.Carte;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SimulerMouvementsTest {

    @Autowired SimulerMouvements simulerMouvements;

    @Test
    public void devrait_executer_le_fichier_d_instructions() {
        // Given
        Aventurier lara =
                unAventurier()
                        .avecNom("Lara")
                        .avecPosition(1, 1)
                        .avecOrientation(SUD)
                        .avecSequenceMouvement("AADADAGGA")
                        .build();
        Carte carteInitiale = uneCarte().avecAventuriers(lara).build();

        String fichierSortieAttendu =
                """
        C - 3 - 4
        M - 1 - 0
        M - 2 - 1
        T - 1 - 3 - 2
        A - Lara - 0 - 3 - S - 3
        """;

        // When
        String fichierSortieObtenu = simulerMouvements.executer(carteInitiale);

        // Then
        Assertions.assertThat(fichierSortieObtenu).isEqualTo(fichierSortieAttendu);
    }

    @Test
    public void devrait_executer_les_actions_de_plusieurs_aventuriers() {
        // Given
        Aventurier lara =
                unAventurier()
                        .avecNom("Lara")
                        .avecPosition(1, 1)
                        .avecOrientation(SUD)
                        .avecSequenceMouvement("AA")
                        .build();
        Aventurier nathan =
                unAventurier()
                        .avecNom("Nathan")
                        .avecPosition(0, 0)
                        .avecOrientation(SUD)
                        .avecSequenceMouvement("AA")
                        .build();
        Carte carteInitiale = uneCarte().avecAventuriers(lara, nathan).build();

        String fichierSortieAttendu =
                """
        C - 3 - 4
        M - 1 - 0
        M - 2 - 1
        T - 0 - 3 - 2
        T - 1 - 3 - 2
        A - Lara - 1 - 3 - S - 1
        A - Nathan - 0 - 2 - S - 0
        """;

        // When
        String fichierSortieObtenu = simulerMouvements.executer(carteInitiale);

        // Then
        Assertions.assertThat(fichierSortieObtenu).isEqualTo(fichierSortieAttendu);
    }

    @Test
    public void
            devrait_continuer_d_executer_les_actions_des_autres_aventuriers_quand_l_un_d_entre_eux_n_en_a_plus() {
        // Given
        Aventurier lara =
                unAventurier()
                        .avecNom("Lara")
                        .avecPosition(1, 1)
                        .avecOrientation(SUD)
                        .avecSequenceMouvement("AADADAGGA")
                        .build();
        Aventurier nathan =
                unAventurier()
                        .avecNom("Nathan")
                        .avecPosition(0, 0)
                        .avecOrientation(SUD)
                        .avecSequenceMouvement("A")
                        .build();
        Carte carteInitiale = uneCarte().avecAventuriers(lara, nathan).build();

        String fichierSortieAttendu =
                """
        C - 3 - 4
        M - 1 - 0
        M - 2 - 1
        T - 1 - 3 - 2
        A - Lara - 0 - 3 - S - 3
        A - Nathan - 0 - 1 - S - 0
        """;

        // When
        String fichierSortieObtenu = simulerMouvements.executer(carteInitiale);

        // Then
        Assertions.assertThat(fichierSortieObtenu).isEqualTo(fichierSortieAttendu);
    }

    @Test
    public void devrait_executer_le_fichier_d_instructions_complexe_avec_plusieurs_aventuriers() {
        // Given
        Aventurier lara =
                unAventurier()
                        .avecNom("Lara")
                        .avecPosition(1, 1)
                        .avecOrientation(SUD)
                        .avecSequenceMouvement("AADADAGGA")
                        .build();
        Aventurier nathan =
                unAventurier()
                        .avecNom("Nathan")
                        .avecPosition(0, 0)
                        .avecOrientation(EST)
                        .avecSequenceMouvement("ADAAAGADA")
                        .build();
        Carte carteInitiale = uneCarte().avecAventuriers(lara, nathan).build();

        String fichierSortieAttendu =
                """
        C - 3 - 4
        M - 1 - 0
        M - 2 - 1
        T - 0 - 3 - 1
        T - 1 - 3 - 1
        A - Lara - 0 - 3 - S - 2
        A - Nathan - 1 - 3 - S - 1
        """;

        // When
        String fichierSortieObtenu = simulerMouvements.executer(carteInitiale);

        // Then
        Assertions.assertThat(fichierSortieObtenu).isEqualTo(fichierSortieAttendu);
    }
}
