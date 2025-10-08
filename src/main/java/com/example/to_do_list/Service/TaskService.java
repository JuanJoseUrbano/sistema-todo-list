package com.example.to_do_list.Service;

import com.example.to_do_list.Entity.Task;
import com.example.to_do_list.Repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = (List<Task>) taskRepository.findAll();
        if (tasks.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(tasks);
    }

    public ResponseEntity<Task> getById(Long id) {
        return taskRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null));
    }

    public ResponseEntity<List<Task>> getCompletedTasks() {
        List<Task> completed = taskRepository.findByCompleted(true);
        if (completed.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(completed);
    }

    public ResponseEntity<List<Task>> getTasksByTitle(String title) {
        List<Task> tasks = taskRepository.findByTitle(title);
        if (tasks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
        return ResponseEntity.ok(tasks);
    }
    public ResponseEntity<List<Task>> getTasksByPriority(Long priorityId) {
        List<Task> tasks = taskRepository.findByPriorityId(priorityId);

        if (tasks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }

        return ResponseEntity.ok(tasks);
    }


    public ResponseEntity<String> registerTask(Task task) {
        if (!isValidTask(task)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("El título y la descripción son obligatorios");
        }

        taskRepository.save(task);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Tarea agregada correctamente");
    }

    public ResponseEntity<String> updateTask(Long id, Task task) {
        return taskRepository.findById(id)
                .map(existing -> {
                    if (!isValidTask(task)) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("El título y la descripción son obligatorios");
                    }

                    existing.setTitle(task.getTitle());
                    existing.setDescription(task.getDescription());
                    existing.setCompleted(task.isCompleted());
                    taskRepository.save(existing);

                    return ResponseEntity.ok("Tarea actualizada correctamente");
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Tarea no encontrada"));
    }

    public ResponseEntity<String> deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("La tarea con ID " + id + " no fue encontrada");
        }

        taskRepository.deleteById(id);
        return ResponseEntity.ok("Tarea eliminada correctamente");
    }

    private boolean isValidTask(Task task) {
        return task != null &&
                task.getTitle() != null && !task.getTitle().trim().isEmpty() &&
                task.getDescription() != null && !task.getDescription().trim().isEmpty();
    }
}
