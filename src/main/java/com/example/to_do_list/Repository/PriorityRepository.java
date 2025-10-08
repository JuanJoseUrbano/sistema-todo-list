package com.example.to_do_list.Repository;

import com.example.to_do_list.Entity.Priority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PriorityRepository extends JpaRepository<Priority,Long> {
    Optional<Priority> findByName(String name);
    Optional<Priority> findByLevel(String level);
}
