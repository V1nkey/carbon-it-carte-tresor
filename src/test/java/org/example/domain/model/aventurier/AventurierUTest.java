package org.example.domain.model.aventurier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.fixtures.AventurierTestBuilder.unAventurier;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class AventurierUTest {

    @ParameterizedTest
    @CsvSource({"NORD, OUEST", "SUD, EST", "EST, NORD", "OUEST, SUD"})
    void tourner_a_gauche_devrait_orienter_l_aventurier_dans_la_bonne_direction(
            Orientation orientationInitiale, Orientation orientationFinaleAttendue) {
        // Given
        Aventurier aventurier = unAventurier().avecOrientation(orientationInitiale).build();

        // When
        aventurier.tournerGauche();

        // Then
        assertThat(aventurier.getOrientation()).isEqualTo(orientationFinaleAttendue);
    }

    @ParameterizedTest
    @CsvSource({"NORD, EST", "SUD, OUEST", "EST, SUD", "OUEST, NORD"})
    void tourner_a_droite_devrait_orienter_l_aventurier_dans_la_bonne_direction(
            Orientation orientationInitiale, Orientation orientationFinaleAttendue) {
        // Given
        Aventurier aventurier = unAventurier().avecOrientation(orientationInitiale).build();

        // When
        aventurier.tournerDroite();

        // Then
        assertThat(aventurier.getOrientation()).isEqualTo(orientationFinaleAttendue);
    }
}
