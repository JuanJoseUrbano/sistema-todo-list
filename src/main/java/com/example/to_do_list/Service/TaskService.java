package com.example.to_do_list.Service;

import com.example.to_do_list.Entity.Task;
import com.example.to_do_list.Repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;

    public List<Task> getTask() {
        return (List<Task>) taskRepository.findAll();
    }

    public Optional<Task> getById(Long id) {
        return taskRepository.findById(id);
    }

    public List<Task> getByComplete(){
        return taskRepository.findByCompleted(true);
    }

    public List<Task> getTaskByTitle(String title) {
        return taskRepository.findByTitle(title);
    }

    public ResponseEntity<String> registerTask(Task task) {
        try {
            if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La tarea deberia de tener un titulo");
            }
            if (task.getDescription() == null || task.getDescription().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La tarea deberia de tener una descripcion");
            }
            taskRepository.save(task);
            return ResponseEntity.status(HttpStatus.CREATED).body("Tarea agregada correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo realizar la peticion");
        }
    }

    public ResponseEntity<String> updateTask(Long id, Task task) {
        try {
            Optional<Task> taskOptional = taskRepository.findById(id);
            if (!taskOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarea no encontrada");
            }
            Task existingTask = taskOptional.get();

            if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La tarea debe tener un título");
            }
            if (task.getDescription() == null || task.getDescription().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La tarea debe tener una descripción");
            }

            existingTask.setTitle(task.getTitle());
            existingTask.setDescription(task.getDescription());
            existingTask.setCompleted(task.isCompleted());

            taskRepository.save(existingTask);

            return ResponseEntity.ok("Tarea actualizada correctamente");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo realizar la petición");
        }
    }


    public ResponseEntity<String> eliminarTask(Long id) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isPresent()) {
            taskRepository.deleteById(id);
            return ResponseEntity.ok("Tarea Eliminada correctamente");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La Task con el ID " + id + " no fue encontrado");
    }
}
