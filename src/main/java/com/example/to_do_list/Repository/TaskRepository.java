package com.example.to_do_list.Repository;

import com.example.to_do_list.Entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT t FROM Task t WHERE t.title LIKE %:title%")
    Page<Task> findByTitle(String title, Pageable pageable);
    Page<Task> findByCompleted(boolean status, Pageable pageable);
    Page<Task> findByPriorityId(Long priorityId, Pageable pageable);
}
