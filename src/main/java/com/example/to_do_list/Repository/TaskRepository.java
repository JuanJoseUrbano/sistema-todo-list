package com.example.to_do_list.Repository;

import com.example.to_do_list.Entity.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskRepository extends CrudRepository<Task,Long> {
    @Query("SELECT t FROM Task t WHERE t.title LIKE %:title%")
    List<Task> findByTitle(String title);
    List<Task> findByCompleted(boolean completed);
    List<Task> findByPriorityId(Long priorityId);
}
