package org.example.application;

import static org.example.domain.model.aventurier.Orientation.SUD;
import static org.example.fixtures.AventurierTestBuilder.unAventurier;
import static org.example.fixtures.CarteTestBuilder.uneCarte;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.example.domain.model.aventurier.Aventurier;
import org.example.domain.model.carte.Carte;
import org.example.domain.usecases.SimulerMouvements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CarteController.class)
public class CarteControllerITest {

    @Autowired private MockMvc mockMvc;

    @MockBean private SimulerMouvements simulerMouvements;

    @Test
    void devrait_renvoyer_le_fichier_de_sortie_apres_execution() throws Exception {
        // Given
        String fichierEntree =
                """
        C - 3 - 4
        M - 1 - 0
        M - 2 - 1
        T - 0 - 3 - 2
        T - 1 - 3 - 3
        A - Lara - 1 - 1 - S - AADADAGGA
        """;

        String fichierSortieAttendu =
                """
        C - 3 - 4
        M - 1 - 0
        M - 2 - 1
        T - 1 - 3 - 2
        A - Lara - 0 - 3 - S - 3
        """;

        Aventurier lara =
                unAventurier()
                        .avecNom("Lara")
                        .avecPosition(1, 1)
                        .avecOrientation(SUD)
                        .avecSequenceMouvement("AADADAGGA")
                        .build();

        Carte build = uneCarte().avecAventuriers(lara).build();
        Carte test = CarteMapper.map(fichierEntree);

        when(simulerMouvements.executer(build)).thenReturn(fichierSortieAttendu);

        // When Then
        mockMvc.perform(
                        post("/api/carte")
                                .content(fichierEntree)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(fichierSortieAttendu));
    }

    @Test
    void devrait_lire_et_executer_le_fichier_de_configuration() throws Exception {
        // Given
        MockMultipartFile fichierEntree =
                new MockMultipartFile(
                        "fichier",
                        "config.txt",
                        "text/plain",
                        """
        C - 3 - 4
        M - 1 - 0
        M - 2 - 1
        T - 0 - 3 - 2
        T - 1 - 3 - 3
        A - Lara - 1 - 1 - S - AADADAGGA
        """
                                .getBytes());

        String fichierSortieAttendu =
                """
        C - 3 - 4
        M - 1 - 0
        M - 2 - 1
        T - 1 - 3 - 2
        A - Lara - 0 - 3 - S - 3
        """;

        Aventurier lara =
                unAventurier()
                        .avecNom("Lara")
                        .avecPosition(1, 1)
                        .avecOrientation(SUD)
                        .avecSequenceMouvement("AADADAGGA")
                        .build();

        when(simulerMouvements.executer(uneCarte().avecAventuriers(lara).build()))
                .thenReturn(fichierSortieAttendu);

        // Effectue la requête POST avec le fichier simulé
        mockMvc.perform(multipart("/api/carte/fichier").file(fichierEntree))
                .andExpect(status().isOk())
                .andExpect(content().string(fichierSortieAttendu));
    }

    @Test
    void devrait_ignorer_les_commentaires_et_executer_le_fichier() throws Exception {
        // Given
        String fichierEntree =
                """
        C - 3 - 4
        M - 1 - 0
        M - 2 - 1
        T - 0 - 3 - 2
        T - 1 - 3 - 3
        A - Lara - 1 - 1 - S - AADADAGGA
        """;

        String fichierSortieAttendu =
                """
        # {C comme Carte} - {Nb. de case en largeur} - {Nb. de case en hauteur}
        C - 3 - 4
        # {M comme Montagne} - {Axe horizontal} - {Axe vertical}
        M - 1 - 0
        M - 2 - 1
        # {T comme Trésor} - {Axe horizontal} - {Axe vertical} - {Nb. de trésors}
        T - 1 - 3 - 2
        # {A comme Aventurier} - {Nom de l’aventurier} - {Axe horizontal} - {Axe vertical} - {Orientation} - {Séquence de mouvement}
        A - Lara - 0 - 3 - S - 3
        """;

        Aventurier lara =
                unAventurier()
                        .avecNom("Lara")
                        .avecPosition(1, 1)
                        .avecOrientation(SUD)
                        .avecSequenceMouvement("AADADAGGA")
                        .build();

        when(simulerMouvements.executer(uneCarte().avecAventuriers(lara).build()))
                .thenReturn(fichierSortieAttendu);

        // When Then
        mockMvc.perform(
                        post("/api/carte")
                                .content(fichierEntree)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void devrait_renvoyer_une_erreur_si_une_ligne_du_fichier_ne_commence_pas_par_une_lettre_connue()
            throws Exception {
        // Given
        String fichierEntree =
                """
        C - 3 - 4
        Z - 0 - 3 - 2
        T - 1 - 3 - 3
        A - Lara - 1 - 1 - S - AADADAGGA
        """;

        // When Then
        mockMvc.perform(
                        post("/api/carte")
                                .content(fichierEntree)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        verify(simulerMouvements, never()).executer(any(Carte.class));
    }
}
