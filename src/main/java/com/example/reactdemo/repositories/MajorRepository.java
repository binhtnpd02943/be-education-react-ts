package com.example.reactdemo.repositories;

import com.example.reactdemo.models.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author binhtn1
 *
 */
@Repository
public interface MajorRepository extends JpaRepository<Major, Integer> {
}
