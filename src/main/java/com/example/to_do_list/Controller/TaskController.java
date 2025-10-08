package com.example.to_do_list.Controller;

import com.example.to_do_list.Entity.Task;
import com.example.to_do_list.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return taskService.getById(id);
    }

    @GetMapping("/completed")
    public ResponseEntity<List<Task>> getCompletedTasks() {
        return taskService.getCompletedTasks();
    }

    @GetMapping("/title")
    public ResponseEntity<List<Task>> getTasksByTitle(@RequestParam String title) {
        return taskService.getTasksByTitle(title);
    }

    @GetMapping("/priority/{priorityId}")
    public ResponseEntity<List<Task>> getTasksByPriority(@PathVariable Long priorityId) {
        return taskService.getTasksByPriority(priorityId);
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
        return taskService.deleteTask(id);
    }
}
