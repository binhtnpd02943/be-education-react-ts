package com.example.reactdemo.services.impl;

import com.example.reactdemo.models.Major;
import com.example.reactdemo.repositories.MajorRepository;
import com.example.reactdemo.services.MajorService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 
 * @author binhtn1
 *
 */
@Service
public class MajorServiceImpl implements MajorService {

    private MajorRepository majorRepository;

    public MajorServiceImpl(MajorRepository majorRepository) {
        this.majorRepository = majorRepository;
    }

    /**
     * Get all major
     *
     * @return List<Major>
     */
    @Override
    public List<Major> findAll() {
        return majorRepository.findAll();
    }

    /**
     * Check major is exists
     *
     * @param integer
     * @return boolean
     */
    @Override
    public boolean existsById(Integer integer) {
        return majorRepository.existsById(integer);
    }
}
