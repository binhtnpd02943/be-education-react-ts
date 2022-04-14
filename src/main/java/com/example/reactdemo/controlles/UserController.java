package com.example.reactdemo.controlles;

import com.example.reactdemo.dtos.IdsModel;
import com.example.reactdemo.dtos.ProcessStatusModel;
import com.example.reactdemo.dtos.UserDTO;
import com.example.reactdemo.models.ApplicationUser;
import com.example.reactdemo.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.example.reactdemo.configs.SecurityConstants.HEADER_STRING;

/**
 * @author binhtn1
 */
@RestController
@RequestMapping("users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private UserService userService;

    /**
     * Injection dependencies
     *
     * @param userService
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Get all users and search name
     *
     * @param name
     * @return ResponseEntity with data is a List<ApplicationUser>
     */
    @GetMapping
    @Operation(summary = "Lấy danh sách user và tìm kiếm theo tên user")
    public ResponseEntity<?> getUsers(@RequestParam(required = false, defaultValue = "") String name) {
        return userService.findAll(name);
    }

    /**
     * Get user with username
     *
     * @param username
     * @return ResponseEntity with data is a ApplicationUser
     */
    @Operation(summary = "Lấy data user theo username")
    @GetMapping("{username}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    public ResponseEntity<?> getOne(@PathVariable("username") String username) {
        return userService.getOne(username);
    }

    /**
     * Add new user
     *
     * @param applicationUser
     * @return ResponseEntity with message of handle
     */
    @Operation(summary = "Tạo mới user")
    @PostMapping()
    public ResponseEntity<?> addUser(@RequestBody @Valid ApplicationUser applicationUser) {
        return userService.save(applicationUser);
    }

    /**
     * Change password of user
     *
     * @param header
     * @param user
     * @return ResponseEntity with message of handle
     */
    @Operation(summary = "Cập nhập password của user")
    @PostMapping("change-password")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT') or hasRole('TEACHER')")
    public ResponseEntity<?> changePassword(@RequestHeader(HEADER_STRING) String header, @RequestBody UserDTO user) {
        return userService.saveChangePassword(header, user);
    }

    /**
     * Update user
     *
     * @param applicationUser
     * @return ResponseEntity with message of handle
     */
    @Operation(summary = "Cập nhập user")
    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody @Valid ApplicationUser applicationUser) {
        return userService.saveEdit(applicationUser);
    }

    /**
     * Delete user
     *
     * @param username
     * @return ResponseEntity with message of handle
     */
    @Operation(summary = "Delete user theo username")
    @DeleteMapping("{username}")
    public ResponseEntity<?> deleteByUsername(@PathVariable("username") String username) {
        return userService.deleteByUsername(username);
    }

    /**
     * Delete Multiple users
     *
     * @param ids
     * @return
     */
    @Operation(summary = "Delete Multiple users")
    @DeleteMapping
    public ProcessStatusModel deletes(@Validated @RequestBody IdsModel ids) {
        return userService.deletes(ids);
    }

    /**
     * In chi tiết user
     *
     * @param ids
     * @return
     */
    @Operation(summary = "In chi tiết user theo Ids")
    @PostMapping("/print")
    public ResponseEntity<?> print(@Validated @RequestBody IdsModel ids) {
        return userService.getListPrint(ids);
    }

    /**
     * Handle import file excel to add or edit user
     *
     * @param users
     * @return ResponseEntity with data is a List<UserImportResponse>
     */
    @Operation(summary = "Import file excel to add or update user")
    @PostMapping("import-excel")
    public ResponseEntity<?> importFileExcel(@RequestBody List<ApplicationUser> users) {
        return userService.importFileExcel(users);
    }
}
