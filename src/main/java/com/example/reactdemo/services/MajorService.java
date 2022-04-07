package com.example.reactdemo.services;

import com.example.reactdemo.models.Major;

import java.util.List;

/**
 * 
 * @author binhtn1
 *
 */
public interface MajorService {

    /**
     * Get all major
     *
     * @return List<Major>
     */
    List<Major> findAll();

    /**
     * Check major is exists
     *
     * @param integer
     * @return boolean
     */
    boolean existsById(Integer integer);
}
