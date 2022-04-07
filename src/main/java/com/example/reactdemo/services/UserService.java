package com.example.reactdemo.services;

import com.example.reactdemo.dtos.IdsModel;
import com.example.reactdemo.dtos.ProcessStatusModel;
import com.example.reactdemo.dtos.UserDTO;
import com.example.reactdemo.models.ApplicationUser;
import com.example.reactdemo.models.ClassRoom;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * 
 * @author binhtn1
 *
 */
public interface UserService {

    /**
     * Get all users and search name
     *
     * @param search
     * @return ResponseEntity with data is a List<ApplicationUser>
     */
    ResponseEntity<?> findAll(String search);

    /**
     * Get user with username
     *
     * @param username
     * @return ApplicationUser
     */
    ApplicationUser findByUsername(String username);

    /**
     * Get user with username
     *
     * @param username
     * @return ResponseEntity with data is a ApplicationUser
     */
    ResponseEntity<?> getOne(String username);

    /**
     * Check phone is exists
     *
     * @param phone
     * @return boolean
     */
    boolean existsByPhone(String phone);

    /**
     * Check user is exists
     *
     * @param username
     * @return boolean
     */
    boolean existByUsername(String username);

    /**
     * Add new user
     *
     * @param user
     * @return ResponseEntity with message of handle
     */
    ResponseEntity<?> save(ApplicationUser user);

    /**
     * Update user
     *
     * @param user
     * @return ResponseEntity with message of handle
     */
    ResponseEntity<?> saveEdit(ApplicationUser user);

    /**
     * Handle import file excel to add or edit user
     *
     * @param users
     * @return ResponseEntity with data is a List<UserImportResponse>
     */
    ResponseEntity<?> importFileExcel(List<ApplicationUser> users);

    /**
     * Register student to class room
     *
     * @param username
     * @param classRoom
     * @return ApplicationUser
     */
    ApplicationUser registerStudentToClassroom(String username, ClassRoom classRoom);

    /**
     * Check password from request and hash password in db
     *
     * @param oldPassword
     * @param oldPasswordInDB
     * @return boolean
     */
    boolean isOldPassword(String oldPassword, String oldPasswordInDB);

    /**
     * Change password of user
     *
     * @param header
     * @param user
     * @return ResponseEntity with message of handle
     */
    ResponseEntity<?> saveChangePassword(String header, UserDTO user);

    /**
     * Delete user
     *
     * @param username
     * @return ResponseEntity with message of handle
     */
    ResponseEntity<?> deleteByUsername(String username);

    /**
     * Delete multiple user
     * 
     * @param username
     * @return
     */
    ProcessStatusModel deletes(IdsModel usernames);

    /**
     * In Danh sách người dùng
     */
    ResponseEntity<?> print(IdsModel ids);
}
