package com.example.reactdemo.repositories;

import com.example.reactdemo.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author binhtn1
 *
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
}
