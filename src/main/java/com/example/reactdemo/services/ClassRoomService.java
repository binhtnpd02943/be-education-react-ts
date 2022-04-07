package com.example.reactdemo.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.example.reactdemo.dtos.ClassroomDTO;
import com.example.reactdemo.models.ClassRoom;

/**
 * 
 * @author binhtn1
 *
 */
public interface ClassRoomService {

    /**
     * Get one class room by id
     *
     * @param classRoomId
     * @return Classroom
     */
    ClassRoom findByIdAndDeleteFlagFalse(int classRoomId);

    /**
     * Get one class room by id
     *
     * @param classRoomId
     * @return ResponseEntity with data is a Classroom
     */
    ResponseEntity<?> getOneById(int classRoomId);

    /**
     * Add new class room
     *
     * @param classRoom
     * @return ResponseEntity with message of handle
     */
    ResponseEntity<?> addNew(ClassRoom classRoom);

    /**
     * Update class room
     *
     * @param classRoom
     * @return ResponseEntity with message of handle
     */
    ResponseEntity<?> updateClass(ClassRoom classRoom);

    /**
     * Get all class room
     *
     * @return ResponseEntity with data is a List<Classroom>
     */
    ResponseEntity<?> findAllClassroom();

    /**
     * Find add class room by user name
     *
     * @param username
     * @return List<ClassRoom>
     */
    List<ClassRoom> findAllByUsers_UsernameAndDeleteFlagFalse(String username);

    /**
     * Find all class room by user name (extra data of class)
     *
     * @param username
     * @return List<ClassroomDTO>
     */
    List<ClassroomDTO> findAllClassroomByUsername(String username);

    /**
     * Check user is registered class room
     *
     * @param classId
     * @param username
     * @return boolean
     */
    boolean existsClassRegistered(int classId, String username);

    /**
     * Delete class room
     *
     * @param classRoomId
     * @return ResponseEntity with message of handle
     */
    ResponseEntity<?> deleteClassRoomById(int classRoomId);

    /**
     * Get current student in class room by id
     *
     * @param classId
     * @return number student of class room
     */
    int getCurrentStudentInClassroom(int classId);

    /**
     * Find add class room registered by user name
     *
     * @param username
     * @return List<ClassroomDTO>
     */
    List<ClassroomDTO> findAllRegisteredClassrooms(String username);

    /**
     * Check class room is exists
     *
     * @param classrooms
     * @return String is a list of class room not exists
     */
    String existsClassRoomsByIdIn(List<ClassRoom> classrooms);

}
