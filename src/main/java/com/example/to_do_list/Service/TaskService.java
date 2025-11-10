package com.example.to_do_list.Service;

import com.example.to_do_list.Entity.Task;
import com.example.to_do_list.Repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Page<Task> getTasksPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return taskRepository.findAll(pageable);
    }

    public ResponseEntity<?> getCompletedTasksPaginated(boolean status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Task> completed = taskRepository.findByCompleted(status, pageable);

        if (completed.isEmpty()) {
            return buildResponse(HttpStatus.NO_CONTENT, "No completed tasks found", null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(completed);
    }

    public ResponseEntity<?> getTasksByTitlePaginated(String title, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Task> tasks = taskRepository.findByTitle(title, pageable);

        if (tasks.isEmpty()) {
            return buildResponse(HttpStatus.NOT_FOUND, "No tasks found with that title", null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    public ResponseEntity<?> getTasksByPriorityPaginated(Long priorityId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Task> tasks = taskRepository.findByPriorityId(priorityId, pageable);

        if (tasks.isEmpty()) {
            return buildResponse(HttpStatus.NOT_FOUND, "No tasks found for that priority", null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    public ResponseEntity<?> getById(Long id) {
        return taskRepository.findById(id)
                .<ResponseEntity<?>>map(task -> ResponseEntity.status(HttpStatus.OK).body(task))
                .orElse(buildResponse(HttpStatus.NOT_FOUND, "Task not found", null));
    }

    // Create a new task
    public ResponseEntity<?> createTask(Task task) {
        if (!isValidTask(task)) {
            return buildResponse(HttpStatus.BAD_REQUEST, "Title and description are required", null);
        }

        taskRepository.save(task);
        return buildResponse(HttpStatus.CREATED, "Task successfully created", task);
    }

    // Update a task
    public ResponseEntity<?> updateTask(Long id, Task task) {
        return taskRepository.findById(id)
                .<ResponseEntity<?>>map(existing -> {
                    if (!isValidTask(task)) {
                        return buildResponse(HttpStatus.BAD_REQUEST, "Title and description are required", null);
                    }

                    existing.setTitle(task.getTitle());
                    existing.setDescription(task.getDescription());
                    existing.setCompleted(task.isCompleted());
                    existing.setPriority(task.getPriority());
                    taskRepository.save(existing);

                    return buildResponse(HttpStatus.OK, "Task successfully updated", existing);
                })
                .orElse(buildResponse(HttpStatus.NOT_FOUND, "Task not found", null));
    }

    // Delete a task
    public ResponseEntity<?> deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            return buildResponse(HttpStatus.NOT_FOUND, "Task with ID " + id + " not found", null);
        }

        taskRepository.deleteById(id);
        return buildResponse(HttpStatus.OK, "Task successfully deleted", null);
    }

    private boolean isValidTask(Task task) {
        return task != null &&
                task.getTitle() != null && !task.getTitle().trim().isEmpty() &&
                task.getDescription() != null && !task.getDescription().trim().isEmpty();
    }

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status.value());
        response.put("message", message);
        response.put("data", data);
        return ResponseEntity.status(status).body(response);
    }
}
