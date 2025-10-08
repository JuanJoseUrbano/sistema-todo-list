package com.example.to_do_list.Controller;

import com.example.to_do_list.Entity.Priority;
import com.example.to_do_list.Service.PriorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/priorities")
public class PriorityController {

    @Autowired
    private PriorityService priorityService;

    @GetMapping
    public ResponseEntity<List<Priority>> getAllPriorities() {
        return priorityService.getAllPriorities();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Priority> getById(@PathVariable Long id) {
        return priorityService.getById(id);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Priority> getByName(@PathVariable String name) {
        return priorityService.getByName(name);
    }

    @GetMapping("/level/{level}")
    public ResponseEntity<Priority> getByLevel(@PathVariable String level) {
        return priorityService.getByLevel(level);
    }

    @PostMapping
    public ResponseEntity<String> registerPriority(@RequestBody Priority priority) {
        return priorityService.registerPriority(priority);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePriority(@PathVariable Long id, @RequestBody Priority priority) {
        return priorityService.updatePriority(id, priority);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePriority(@PathVariable Long id) {
        return priorityService.deletePriority(id);
    }

    @GetMapping("/default")
    public ResponseEntity<List<Priority>> getDefaultPriorities() {
        return priorityService.getDefaultPriorities();
    }
}
