package org.example.domain.usecases;

import java.util.List;
import org.example.domain.model.aventurier.Aventurier;
import org.example.domain.model.carte.Carte;
import org.example.domain.usecases.interactors.EffectuerMouvement;
import org.springframework.stereotype.Component;

@Component
public class SimulerMouvements {

    private final EffectuerMouvement effectuerMouvement;

    public SimulerMouvements(EffectuerMouvement effectuerMouvement) {
        this.effectuerMouvement = effectuerMouvement;
    }

    public String executer(Carte carte) {
        List<Aventurier> aventuriers = carte.getAventuriers();

        while (aventuriers.stream().anyMatch(Aventurier::peutEffectuerAction)) {
            aventuriers.stream()
                    .filter(Aventurier::peutEffectuerAction)
                    .forEach(aventurier -> effectuerMouvement.executer(aventurier, carte));
        }

        return carte.toString();
    }
}
