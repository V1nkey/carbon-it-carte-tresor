package org.example.application;

import java.io.IOException;
import org.example.domain.usecases.SimulerMouvements;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/carte")
public class CarteController {

    private final SimulerMouvements simulerMouvements;

    public CarteController(SimulerMouvements simulerMouvements) {
        this.simulerMouvements = simulerMouvements;
    }

    @PostMapping
    public ResponseEntity<String> lireFichierConfiguration(@RequestBody String configuration) {
        try {
            String configurationCarteApresExecutionMouvements =
                    simulerMouvements.executer(CarteMapper.map(configuration));
            return ResponseEntity.ok(configurationCarteApresExecutionMouvements);
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping(value = "/fichier", consumes = "multipart/form-data")
    public ResponseEntity<String> lireFichierConfiguration(@RequestBody MultipartFile fichier) {
        try {
            String configurationCarteApresExecutionMouvements =
                    simulerMouvements.executer(CarteMapper.map(new String(fichier.getBytes())));
            return ResponseEntity.ok(configurationCarteApresExecutionMouvements);
        } catch (RuntimeException | IOException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
