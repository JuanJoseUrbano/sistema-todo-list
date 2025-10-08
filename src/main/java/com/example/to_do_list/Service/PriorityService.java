package com.example.to_do_list.Service;

import com.example.to_do_list.Entity.Priority;
import com.example.to_do_list.Repository.PriorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriorityService {

    @Autowired
    private PriorityRepository priorityRepository;

    public ResponseEntity<List<Priority>> getAllPriorities() {
        List<Priority> priorities = priorityRepository.findAll();
        if (priorities.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(priorities);
    }

    public ResponseEntity<Priority> getById(Long id) {
        return priorityRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    public ResponseEntity<Priority> getByName(String name) {
        return priorityRepository.findByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    public ResponseEntity<Priority> getByLevel(String level) {
        return priorityRepository.findByLevel(level)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    public ResponseEntity<String> registerPriority(Priority priority) {
        if (!isValidPriority(priority)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("El nombre y el nivel de la prioridad son obligatorios");
        }

        if (priorityRepository.findByName(priority.getName()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Ya existe una prioridad con ese nombre");
        }

        if (priorityRepository.findByLevel(priority.getLevel()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Ya existe una prioridad con ese nivel");
        }

        priorityRepository.save(priority);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Prioridad agregada correctamente");
    }

    public ResponseEntity<String> updatePriority(Long id, Priority priority) {
        return priorityRepository.findById(id)
                .map(existing -> {
                    if (!isValidPriority(priority)) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("El nombre y el nivel de la prioridad son obligatorios");
                    }

                    boolean sameNameExists = priorityRepository.findByName(priority.getName())
                            .filter(p -> !p.getId().equals(id))
                            .isPresent();

                    boolean sameLevelExists = priorityRepository.findByLevel(priority.getLevel())
                            .filter(p -> !p.getId().equals(id))
                            .isPresent();

                    if (sameNameExists) {
                        return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body("Ya existe otra prioridad con ese nombre");
                    }

                    if (sameLevelExists) {
                        return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body("Ya existe otra prioridad con ese nivel");
                    }

                    existing.setName(priority.getName());
                    existing.setLevel(priority.getLevel());
                    priorityRepository.save(existing);

                    return ResponseEntity.ok("Prioridad actualizada correctamente");
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Prioridad no encontrada"));
    }

    public ResponseEntity<String> deletePriority(Long id) {
        return priorityRepository.findById(id)
                .map(priority -> {
                    if (priority.getTasks() != null && !priority.getTasks().isEmpty()) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("No se puede eliminar la prioridad porque hay tareas asociadas");
                    }

                    priorityRepository.delete(priority);
                    return ResponseEntity.ok("Prioridad eliminada correctamente");
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("La prioridad con ID " + id + " no fue encontrada"));
    }

    public ResponseEntity<List<Priority>> getDefaultPriorities() {
        return getAllPriorities();
    }

    private boolean isValidPriority(Priority priority) {
        return priority != null &&
                priority.getName() != null && !priority.getName().trim().isEmpty() &&
                priority.getLevel() != null && !priority.getLevel().trim().isEmpty();
    }
}
