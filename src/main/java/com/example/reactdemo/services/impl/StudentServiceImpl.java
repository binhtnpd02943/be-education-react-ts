package com.example.reactdemo.services.impl;

import com.example.reactdemo.apiresponse.ResponseCustom;
import com.example.reactdemo.models.ApplicationUser;
import com.example.reactdemo.models.ClassRoom;
import com.example.reactdemo.services.ClassRoomService;
import com.example.reactdemo.services.StudentService;
import com.example.reactdemo.services.UserService;
import com.example.reactdemo.utils.JwtUtils;
import com.example.reactdemo.utils.MessageUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 
 * @author binhtn1
 *
 */
@Service
public class StudentServiceImpl implements StudentService {

    private ClassRoomService classRoomService;
    private JwtUtils jwtUtils;
    private UserService userService;

    public StudentServiceImpl(ClassRoomService classRoomService, JwtUtils jwtUtils, UserService userService) {
        this.classRoomService = classRoomService;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    /**
     * Get all available class room for student
     *
     * @param header
     * @return ResponseEntity with data is a List<ClassRoomDTO>
     */
    @Override
    public ResponseEntity<?> getAllAvailableClassroom(String header) {
        String username = jwtUtils.getUsernameFromBearerToken(header);
        ResponseCustom<?> response = new ResponseCustom<>(HttpStatus.OK, MessageUtil.getMessage("success"),
                classRoomService.findAllClassroomByUsername(username));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get all registered class room of student
     *
     * @param header
     * @return ResponseEntity with data is a List<ClassRoomDTO>
     */
    @Override
    public ResponseEntity<?> getRegisteredClassroom(String header) {
        String username = jwtUtils.getUsernameFromBearerToken(header);
        ResponseCustom<?> response = new ResponseCustom<>(HttpStatus.OK, MessageUtil.getMessage("success"),
                classRoomService.findAllRegisteredClassrooms(username));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Register class room for student
     *
     * @param header
     * @return ResponseEntity with data is a message of handle
     */
    @Override
    public ResponseEntity<?> registerClassroom(ClassRoom classRoom, String header) {
        ClassRoom classInDB = classRoomService.findByIdAndDeleteFlagFalse(classRoom.getId());
        ResponseCustom<Object> response = new ResponseCustom<Object>(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("error"));
        if (classInDB == null) {
            response.setMessage(MessageUtil.getMessageWithParam("Classroom.notFoundWithParam", classRoom.getName()));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        int currentStudentInClassroom = classRoomService.getCurrentStudentInClassroom(classInDB.getId());
        if (currentStudentInClassroom >= classInDB.getAmountStudent()) {
            response.setMessage(MessageUtil.getMessageWithParam("Classroom.filledWithParam", classRoom.getName()));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        if (new Date().getTime() > classInDB.getStartDate().getTime()) {
            response.setMessage(MessageUtil.getMessageWithParam("Classroom.expiredWithParam", classRoom.getName()));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        String username = jwtUtils.getUsernameFromBearerToken(header);

        if (classRoomService.existsClassRegistered(classRoom.getId(), username)) {
            response.setMessage(MessageUtil.getMessageWithParam("Classroom.wasRegisteredWithParam", classRoom.getName()));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        ApplicationUser user = userService.registerStudentToClassroom(username, classInDB);
        if (user != null) {
            response.setStatus(HttpStatus.OK);
            response.setMessage(MessageUtil.getMessage("Classroom.registerSuccess"));
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
