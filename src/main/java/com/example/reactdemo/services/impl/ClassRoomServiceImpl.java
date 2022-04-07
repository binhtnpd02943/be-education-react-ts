package com.example.reactdemo.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.reactdemo.apiresponse.ResponseCustom;
import com.example.reactdemo.dtos.ClassroomDTO;
import com.example.reactdemo.models.ClassRoom;
import com.example.reactdemo.repositories.ClassRoomRepository;
import com.example.reactdemo.services.ClassRoomService;
import com.example.reactdemo.utils.MessageUtil;

/**
 * 
 * @author binhtn1
 *
 */
@Service
public class ClassRoomServiceImpl implements ClassRoomService {

    private ClassRoomRepository classRoomRepository;

    public ClassRoomServiceImpl(ClassRoomRepository classRoomRepository) {
        this.classRoomRepository = classRoomRepository;
    }

    /**
     * Get one class room by id
     *
     * @param classRoomId
     * @return Classroom
     */
    @Override
    public ClassRoom findByIdAndDeleteFlagFalse(int classRoomId) {
        return classRoomRepository.findByIdAndDeleteFlagFalse(classRoomId);
    }

    /**
     * Get one class room by id
     *
     * @param classRoomId
     * @return ResponseEntity with data is a Classroom
     */
    @Override
    public ResponseEntity<?> getOneById(int classRoomId) {
        ClassRoom classRoom = findByIdAndDeleteFlagFalse(classRoomId);
        ResponseCustom<?> response = new ResponseCustom<>(HttpStatus.OK, MessageUtil.getMessage("Classroom.found"),
                classRoom);
        if (classRoom == null) {
            response.setStatus(HttpStatus.NOT_FOUND);
            response.setMessage(MessageUtil.getMessage("Classroom.notFound"));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Add new class room
     *
     * @param classRoom
     * @return ResponseEntity with message of handle
     */
    @Override
    public ResponseEntity<?> addNew(ClassRoom classRoom) {
        ResponseCustom<?> response = new ResponseCustom<>(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("error"));
        ClassRoom savedClassroom = classRoomRepository.save(classRoom);
        if (savedClassroom != null) {
            response.setMessage(MessageUtil.getMessage("Classroom.addSuccess"));
            response.setStatus(HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Update class room
     *
     * @param classRoom
     * @return ResponseEntity with message of handle
     */
    @Override
    public ResponseEntity<?> updateClass(ClassRoom classRoom) {
        ClassRoom classInDB = classRoomRepository.findByIdAndDeleteFlagFalse(classRoom.getId());

        ResponseCustom<Object> response = new ResponseCustom<Object>(HttpStatus.BAD_REQUEST,
                MessageUtil.getMessage("error"));

        if (classInDB == null) {
            response.setMessage(MessageUtil.getMessageWithParam("Classroom.notFoundWithParam", classRoom.getName()));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        int currentStudentInClassroom = classRoomRepository.getCurrentStudentInClassroom(classInDB.getId());
        if (classRoom.getAmountStudent() < currentStudentInClassroom) {
            response.setMessage(MessageUtil.getMessageWithParam("Classroom.updateErrorWithCurrentAmountStudent",
                    currentStudentInClassroom + "", currentStudentInClassroom + ""));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        ClassRoom savedClassroom = classRoomRepository.save(classRoom);

        if (savedClassroom != null) {
            response.setMessage(MessageUtil.getMessage("Classroom.updateSuccess"));
            response.setStatus(HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get all class room
     *
     * @return ResponseEntity with data is a List<Classroom>
     */
    @Override
    public ResponseEntity<?> findAllClassroom() {
        ResponseCustom<?> response = new ResponseCustom<>(HttpStatus.OK, MessageUtil.getMessage("success"),
                classRoomRepository.findAllClassroom());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Find add class room by user name
     *
     * @param username
     * @return List<ClassRoom>
     */
    @Override
    public List<ClassRoom> findAllByUsers_UsernameAndDeleteFlagFalse(String username) {
        return classRoomRepository.findAllByUsers_UsernameAndDeleteFlagFalse(username);
    }

    /**
     * Find all class room by user name (extra data of class)
     *
     * @param username
     * @return List<ClassroomDTO>
     */
    @Override
    public List<ClassroomDTO> findAllClassroomByUsername(String username) {
        List<ClassroomDTO> result = classRoomRepository.findAllClassroomByUsername(username).stream()
                .map(item -> new ClassroomDTO(Integer.parseInt(item.get("id").toString()), item.get("name").toString(),
                        Integer.parseInt(item.get("amount_student").toString()), item.get("start_date").toString(),
                        item.get("end_date").toString(), Boolean.parseBoolean(item.get("delete_flag").toString())))
                .collect(Collectors.toList());
        return result;
    }

    /**
     * Check user is registered class room
     *
     * @param classId
     * @param username
     * @return boolean
     */
    @Override
    public boolean existsClassRegistered(int classId, String username) {
        return classRoomRepository.existsClassRegistered(classId, username) != null;
    }

    /**
     * Delete class room
     *
     * @param classRoomId
     * @return ResponseEntity with message of handle
     */
    @Override
    public ResponseEntity<?> deleteClassRoomById(int classRoomId) {
        ResponseCustom<?> response = new ResponseCustom<>(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("error"));
        ClassRoom classRoomInDB = classRoomRepository.findByIdAndDeleteFlagFalse(classRoomId);
        if (classRoomInDB == null) {
            response.setStatus(HttpStatus.NOT_FOUND);
            response.setMessage(MessageUtil.getMessage("Classroom.notFound"));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        if (classRoomRepository.getCurrentStudentInClassroom(classRoomId) > 0) {
            response.setMessage(MessageUtil.getMessage("Classroom.removeErrorWhenRegistered"));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        if (classRoomRepository.deleteClassRoomById(classRoomId) > 0) {
            response.setMessage(MessageUtil.getMessage("Classroom.removeSuccess"));
            response.setStatus(HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get current student in class room by id
     *
     * @param classId
     * @return number student of class room
     */
    @Override
    public int getCurrentStudentInClassroom(int classId) {
        return classRoomRepository.getCurrentStudentInClassroom(classId);
    }

    /**
     * Find add class room registered by user name
     *
     * @param username
     * @return List<ClassroomDTO>
     */
    @Override
    public List<ClassroomDTO> findAllRegisteredClassrooms(String username) {
        return classRoomRepository.findAllRegisteredClassrooms(username);
    }

    /**
     * Check class room is exists
     *
     * @param classRooms
     * @return String is a list of class room not exists
     */
    @Override
    public String existsClassRoomsByIdIn(List<ClassRoom> classRooms) {
        if (classRooms == null)
            return null;
        StringBuilder sb = new StringBuilder();
        Map<Integer, String> mapClassRoom = new HashMap<>();
        for (ClassRoom classRoom : classRooms) {
            sb.append(classRoom.getId()).append(",");
            mapClassRoom.put(classRoom.getId(), classRoom.getName());
        }
        List<Integer> notExist = classRoomRepository.existsClassRoomsByIdIn(sb.toString());
        sb.setLength(0);
        for (Integer id : notExist) {
            sb.append(mapClassRoom.get(id)).append(", ");
        }
        return sb.length() == 0 ? null : sb.deleteCharAt(sb.length() - 2).toString();
    }
}
