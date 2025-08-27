package com.example.to_do_list.Controller;

import com.example.to_do_list.Entity.Task;
import com.example.to_do_list.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @GetMapping
    public List<Task> getTasks() {
        return taskService.getTask();
    }

    @GetMapping("/{id}")
    public Optional<Task> getTaskById(@PathVariable Long id) {
        return taskService.getById(id);
    }

    @GetMapping("/completed")
    public List<Task> getTaksCompleted(){
        return taskService.getByComplete();
    }

    @GetMapping("/title")
    public List<Task> getTaskByTitle(@RequestParam String title) {
        return taskService.getTaskByTitle(title);
    }

    @PostMapping
    public ResponseEntity<String> createTask(@RequestBody Task task) {
        return taskService.registerTask(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateTask(@PathVariable Long id, @RequestBody Task task) {
        return taskService.updateTask(id, task);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        return taskService.eliminarTask(id);
    }
}