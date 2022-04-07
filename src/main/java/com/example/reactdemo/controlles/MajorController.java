package com.example.reactdemo.controlles;

import com.example.reactdemo.models.Major;
import com.example.reactdemo.services.MajorService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 
 * @author binhtn1
 *
 */
@RestController
@RequestMapping("majors")
@PreAuthorize("hasRole('ADMIN')")
public class MajorController {

    private MajorService majorService;

    /**
     * Injection dependencies
     * @param majorService
     */
    public MajorController(MajorService majorService) {
        this.majorService = majorService;
    }

    /**
     * Get all major
     *
     * @return List<Major>
     */
    @GetMapping
    public List<Major> getAll() {
        return majorService.findAll();
    }
}
