package com.example.voitureservice.controllers;

import com.example.voitureservice.clients.ClientRestClient;
import com.example.voitureservice.entities.Client;
import com.example.voitureservice.entities.Voiture;
import com.example.voitureservice.repositories.VoitureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/voitures")
public class VoitureController {

    @Autowired
    private VoitureRepository voitureRepository;

    @Autowired
    private ClientRestClient clientRestClient;

    @GetMapping
    public List<Voiture> findAll() {
        List<Voiture> voitures = voitureRepository.findAll();
        voitures.forEach(voiture -> {
            try {
                Client client = clientRestClient.findClientById(voiture.getIdClient());
                voiture.setClient(client);
            } catch (Exception e) {
                voiture.setClient(null);
            }
        });
        return voitures;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Voiture> findById(@PathVariable Long id) {
        return voitureRepository.findById(id)
                .map(voiture -> {
                    try {
                        Client client = clientRestClient.findClientById(voiture.getIdClient());
                        voiture.setClient(client);
                    } catch (Exception e) {
                        voiture.setClient(null);
                    }
                    return ResponseEntity.ok(voiture);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/client/{idClient}")
    public List<Voiture> findByClientId(@PathVariable Long idClient) {
        List<Voiture> voitures = voitureRepository.findByIdClient(idClient);
        try {
            Client client = clientRestClient.findClientById(idClient);
            voitures.forEach(v -> v.setClient(client));
        } catch (Exception e) {
            // Client non trouvé
        }
        return voitures;
    }

    @PostMapping
    public Voiture save(@RequestBody Voiture voiture) {
        return voitureRepository.save(voiture);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Voiture> update(@PathVariable Long id, @RequestBody Voiture voiture) {
        return voitureRepository.findById(id)
                .map(existingVoiture -> {
                    existingVoiture.setMarque(voiture.getMarque());
                    existingVoiture.setMatricule(voiture.getMatricule());
                    existingVoiture.setModel(voiture.getModel());
                    existingVoiture.setIdClient(voiture.getIdClient());
                    return ResponseEntity.ok(voitureRepository.save(existingVoiture));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (voitureRepository.existsById(id)) {
            voitureRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

