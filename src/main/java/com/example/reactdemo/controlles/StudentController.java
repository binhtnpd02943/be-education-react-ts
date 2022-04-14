package com.example.reactdemo.controlles;

import com.example.reactdemo.models.ClassRoom;
import com.example.reactdemo.services.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.reactdemo.configs.SecurityConstants.HEADER_STRING;

/**
 * @author binhtn1
 */
@RestController
@RequestMapping("/students")
@PreAuthorize("hasRole('STUDENT')")
public class StudentController {

    private StudentService studentService;

    /**
     * Injection dependencies
     *
     * @param studentService
     */
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * Get all available classroom for student
     *
     * @param header
     * @return ResponseEntity with data is a List<ClassRoomDTO>
     */
    @Operation(summary = "Lấy danh sách tất cả các lớp học có sẵn cho sinh viên")
    @GetMapping("/available-classrooms")
    public ResponseEntity<?> getAvailableClassrooms(@RequestHeader(HEADER_STRING) String header) {
        return studentService.getAllAvailableClassroom(header);
    }

    /**
     * Get all registered classroom of student
     *
     * @param header
     * @return ResponseEntity with data is a List<ClassRoomDTO>
     */
    @Operation(summary = "Lấy danh sách tất cả các lớp học đã đăng ký của sinh viên")
    @GetMapping("/registered-classrooms")
    public ResponseEntity<?> getRegisteredClassrooms(@RequestHeader(HEADER_STRING) String header) {
        return studentService.getRegisteredClassroom(header);
    }

    /**
     * Register classroom for student
     *
     * @param header
     * @return ResponseEntity with data is a message of handle
     */
    @Operation(summary = "Đăng ký lớp học cho sinh viên")
    @PostMapping("register-classroom")
    public ResponseEntity<?> registerClassroom(@RequestBody @Valid ClassRoom classRoom, @RequestHeader(HEADER_STRING) String header) {
        return studentService.registerClassroom(classRoom, header);
    }
}
