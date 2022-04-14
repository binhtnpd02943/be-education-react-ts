package com.example.reactdemo.controlles;

import com.example.reactdemo.dtos.IdsModel;
import com.example.reactdemo.dtos.ProcessStatusModel;
import com.example.reactdemo.models.ClassRoom;
import com.example.reactdemo.services.ClassRoomService;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author binhtn1
 */
@RestController
@RequestMapping("classrooms")
@PreAuthorize("hasRole('ADMIN')")
public class ClassRoomController {

    private ClassRoomService classRoomService;

    /**
     * Injection dependencies
     *
     * @param classRoomService
     */
    public ClassRoomController(ClassRoomService classRoomService) {
        this.classRoomService = classRoomService;
    }

    /**
     * Get all classroom
     *
     * @return ResponseEntity with data is a List<Classroom>
     */
    @Operation(summary = "Lấy danh sách classroom")
    @GetMapping
    public ResponseEntity<?> getClassrooms() {
        return classRoomService.findAllClassroom();
    }

    /**
     * Get one class room by id
     *
     * @param classRoomId
     * @return ResponseEntity with data is a Classroom
     */
    @Operation(summary = "Lấy data classroom theo Id")
    @GetMapping("{classRoomId}")
    public ResponseEntity<?> getOne(@PathVariable("classRoomId") int classRoomId) {
        return classRoomService.getOneById(classRoomId);
    }

    /**
     * Add new class room
     *
     * @param classRoom
     * @return ResponseEntity with message of handle
     */
    @Operation(summary = "Tạo mới classroom")
    @PostMapping()
    public ResponseEntity<?> addClassroom(@RequestBody @Valid ClassRoom classRoom) {
        return classRoomService.addNew(classRoom);
    }

    /**
     * Update class room
     *
     * @param classRoom
     * @return ResponseEntity with message of handle
     */
    @Operation(summary = "Cập nhập classroom")
    @PutMapping()
    public ResponseEntity<?> EditClassroom(@RequestBody @Valid ClassRoom classRoom) {
        return classRoomService.updateClass(classRoom);
    }

    /**
     * Delete class room
     *
     * @param classRoomId
     * @return ResponseEntity with message of handle
     */
    @Operation(summary = "Xóa classroom")
    @DeleteMapping("{classRoomId}")
    public ResponseEntity<?> deleteByClassId(@PathVariable("classRoomId") int classRoomId) {
        return classRoomService.deleteClassRoomById(classRoomId);
    }
//    @DeleteMapping
//    public ProcessStatusModel deletes(@Validated @RequestBody IdsModel ids) {
//        return classRoomService.deletes(ids);
//    }
}
