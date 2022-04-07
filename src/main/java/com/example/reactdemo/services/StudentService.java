package com.example.reactdemo.services;

import com.example.reactdemo.models.ClassRoom;
import org.springframework.http.ResponseEntity;

/**
 * 
 * @author binhtn1
 *
 */
public interface StudentService {

    /**
     * Get all available class room for student
     *
     * @param header
     * @return ResponseEntity with data is a List<ClassRoomDTO>
     */
    ResponseEntity<?> getAllAvailableClassroom(String header);

    /**
     * Get all registered class room of student
     *
     * @param header
     * @return ResponseEntity with data is a List<ClassRoomDTO>
     */
    ResponseEntity<?> getRegisteredClassroom(String header);

    /**
     * Register class room for student
     *
     * @param header
     * @return ResponseEntity with data is a message of handle
     */
    ResponseEntity<?> registerClassroom(ClassRoom classRoom, String header);
}
