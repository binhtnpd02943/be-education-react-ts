package com.example.reactdemo.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.reactdemo.apiresponse.ResponseCustom;
import com.example.reactdemo.dtos.IdsModel;
import com.example.reactdemo.dtos.ProcessStatusModel;
import com.example.reactdemo.dtos.UserDTO;
import com.example.reactdemo.dtos.UserImportResponse;
import com.example.reactdemo.handling.CollectionUtil;
import com.example.reactdemo.models.ApplicationUser;
import com.example.reactdemo.models.ClassRoom;
import com.example.reactdemo.models.Major;
import com.example.reactdemo.repositories.UserRepository;
import com.example.reactdemo.services.ClassRoomService;
import com.example.reactdemo.services.MajorService;
import com.example.reactdemo.services.UserService;
import com.example.reactdemo.utils.Helper;
import com.example.reactdemo.utils.JwtUtils;
import com.example.reactdemo.utils.MessageUtil;

/**
 * 
 * @author binhtn1
 *
 */
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private UserRepository userRepository;
    private ClassRoomService classRoomService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private JwtUtils jwtUtils;
    private MajorService majorService;

    public UserServiceImpl(UserRepository userRepository, ClassRoomService classRoomService,
            BCryptPasswordEncoder bCryptPasswordEncoder, JwtUtils jwtUtils, MajorService majorService) {
        this.userRepository = userRepository;
        this.classRoomService = classRoomService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtUtils = jwtUtils;
        this.majorService = majorService;
    }

    /**
     * Get all users and search name
     *
     * @param search
     * @return ResponseEntity with data is a List<ApplicationUser>
     */
    @Override
    public ResponseEntity<?> findAll(String search) {
        ResponseCustom<?> response = new ResponseCustom<>(HttpStatus.OK, MessageUtil.getMessage("success"),
                userRepository.findAll(search));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * In Danh sách người dùng
     */
    @Override
    public ResponseEntity<?> print(IdsModel ids) {
        ResponseCustom<?> response = new ResponseCustom<>(HttpStatus.OK, MessageUtil.getMessage("success"),
                userRepository.getUserPrintData(ids.getIds()));
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    /**
     * Get user with username
     *
     * @param username
     * @return ApplicationUser
     */
    @Override
    public ApplicationUser findByUsername(String username) {
        ApplicationUser user = userRepository.findByUsernameAndDeleteFlagFalse(username);
        if (user != null) {
            user.setClassRooms(classRoomService.findAllByUsers_UsernameAndDeleteFlagFalse(username));
        }
        return user;
    }

    /**
     * Get user with username
     *
     * @param username
     * @return ResponseEntity with data is a ApplicationUser
     */
    @Override
    public ResponseEntity<?> getOne(String username) {
        ApplicationUser user = findByUsername(username);
        ResponseCustom<?> response = new ResponseCustom<>(HttpStatus.OK, MessageUtil.getMessage("User.found"), user);
        if (user == null) {
            response.setStatus(HttpStatus.NOT_FOUND);
            response.setMessage(MessageUtil.getMessage("User.notFound"));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Check phone is exists
     *
     * @param phone
     * @return boolean
     */
    @Override
    public boolean existsByPhone(String phone) {
        return userRepository.existsByPhoneAndDeleteFlagFalse(phone);
    }

    /**
     * Check user is exists
     *
     * @param username
     * @return boolean
     */
    @Override
    public boolean existByUsername(String username) {
        return userRepository.findByUsernameAndDeleteFlagFalse(username) != null;
    }

    /**
     * Add new user
     *
     * @param user
     * @return ResponseEntity with message of handle
     */
    @Override
    public ResponseEntity<?> save(ApplicationUser user) {
        ResponseCustom<?> response = new ResponseCustom<>(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("error"));
        if (user.getRole().getId() == 3) {
            String message = existsClassRooms(user.getClassRooms());
            if (message != null) {
                response.setMessage(message);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }

        if (user.getRole().getId() == 2) {
            String message = existsMajor(user.getMajor());
            if (message != null) {
                response.setMessage(message);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }

        String message = existsPhoneWhenSave(user.getPhone());
        if (message != null) {
            response.setMessage(message);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        setFullNameAndPhone(user);
        setAccountNameOfUser(user);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        ApplicationUser savedUser = userRepository.save(user);

        if (savedUser != null) {
            response.setMessage(MessageUtil.getMessage("User.addSuccess"));
            response.setStatus(HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Update user
     *
     * @param user
     * @return ResponseEntity with message of handle
     */
    @Override
    public ResponseEntity<?> saveEdit(ApplicationUser user) {
        ResponseCustom<?> response = new ResponseCustom<>(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("error"));

        ApplicationUser userInDB = findByUsername(user.getUsername());

        if (userInDB == null) {
            response.setMessage(MessageUtil.getMessage("User.notFound"));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        if (user.getRole().getId() == 3) {
            String message = existsClassRooms(user.getClassRooms());
            if (message != null) {
                response.setMessage(message);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }

        if (user.getRole().getId() == 2) {
            String message = existsMajor(user.getMajor());
            if (message != null) {
                response.setMessage(message);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }

        String message = existsPhoneWhenEdit(userInDB, user);
        if (message != null) {
            response.setMessage(message);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        user.setId(userInDB.getId());
        setFullNameAndPhone(user);
        if (!isUsernameChanged(userInDB, user)) {
            setAccountNameOfUser(user);
        } else {
            user.setAccountEmail(userInDB.getAccountEmail());
        }
        user.setPassword(userInDB.getPassword());
        ApplicationUser savedUser = userRepository.save(user);

        if (savedUser != null) {
            response.setMessage(MessageUtil.getMessage("User.updateSuccess"));
            response.setStatus(HttpStatus.OK);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Handle import file excel to add or edit user
     *
     * @param users
     * @return ResponseEntity with data is a List<UserImportResponse>
     */
    @Override
    public ResponseEntity<?> importFileExcel(List<ApplicationUser> users) {
        List<UserImportResponse> response = new ArrayList<>();
        users.forEach(user -> {
            UserImportResponse userResponse = new UserImportResponse();
            userResponse.setFirstName(user.getFirstName());
            userResponse.setLastName(user.getLastName());
            userResponse.setAddress(user.getAddress());
            userResponse.setBirthday(user.getBirthday());
            userResponse.setGender(user.isGender());
            userResponse.setRole(user.getRole().getId());
            userResponse.setPhone(user.getPhone());
            if (user.getUsername() == null) {
                // Case add new
                if ((user.getRole().getId() == 3 && existsClassRooms(user.getClassRooms()) != null)
                        || (user.getRole().getId() == 2 && existsMajor(user.getMajor()) != null)
                        || (existsPhoneWhenSave(user.getPhone()) != null)) {
                    userResponse.setStatus(0);
                } else {
                    setFullNameAndPhone(user);
                    setAccountNameOfUser(user);
                    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                    ApplicationUser savedUser = userRepository.save(user);
                    if (savedUser != null) {
                        userResponse.setStatus(1);
                        userResponse.setAccountMail(savedUser.getAccountEmail());
                    }
                }
            } else {
                // Case edit
                ApplicationUser userInDB = findByUsername(user.getUsername());
                if (userInDB == null || (user.getRole().getId() == 3 && existsClassRooms(user.getClassRooms()) != null)
                        || (user.getRole().getId() == 2 && existsMajor(user.getMajor()) != null)
                        || (existsPhoneWhenEdit(userInDB, user) != null)) {
                    userResponse.setStatus(0);
                } else {
                    user.setId(userInDB.getId());
                    setFullNameAndPhone(user);
                    if (!isUsernameChanged(userInDB, user)) {
                        setAccountNameOfUser(user);
                    } else {
                        user.setAccountEmail(userInDB.getAccountEmail());
                    }
                    user.setPassword(userInDB.getPassword());
                    ApplicationUser savedUser = userRepository.save(user);

                    if (savedUser != null) {
                        userResponse.setStatus(2);
                        userResponse.setAccountMail(savedUser.getAccountEmail());
                    }
                }
            }
            response.add(userResponse);
        });
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Register student to class room
     *
     * @param username
     * @param classRoom
     * @return ApplicationUser
     */
    @Override
    public ApplicationUser registerStudentToClassroom(String username, ClassRoom classRoom) {
        ApplicationUser user = userRepository.findByUsernameAndDeleteFlagFalse(username);
        user.getClassRooms().add(classRoom);
        return userRepository.save(user);
    }

    /**
     * Check password from request and hash password in db
     *
     * @param oldPassword
     * @param oldPasswordInDB
     * @return boolean
     */
    @Override
    public boolean isOldPassword(String oldPassword, String oldPasswordInDB) {
        return BCrypt.checkpw(oldPassword, oldPasswordInDB);
    }

    /**
     * Change password of user
     *
     * @param header
     * @param user
     * @return ResponseEntity with message of handle
     */
    @Override
    public ResponseEntity<?> saveChangePassword(String header, UserDTO user) {
        String username = jwtUtils.getUsernameFromBearerToken(header);

        ResponseCustom<?> response = new ResponseCustom<>(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("error"));

        ApplicationUser userInDB = findByUsername(username);

        if (userInDB == null) {
            response.setMessage(MessageUtil.getMessage("User.notFound"));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        if (!isOldPassword(user.getPassword(), userInDB.getPassword())) {
            response.setMessage(MessageUtil.getMessage("User.oldPasswordIncorrect"));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        userInDB.setPassword(bCryptPasswordEncoder.encode(user.getNewPassword()));
        ApplicationUser savedUser = userRepository.save(userInDB);

        if (savedUser != null) {
            response.setMessage(MessageUtil.getMessage("User.changePasswordSuccess"));
            response.setStatus(HttpStatus.OK);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Delete user
     *
     * @param username
     * @return ResponseEntity with message of handle
     */
    @Override
    public ResponseEntity<?> deleteByUsername(String username) {
        ResponseCustom<?> response = new ResponseCustom<>(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("error"));

        ApplicationUser userInDB = findByUsername(username);

        if (userInDB == null) {
            response.setStatus(HttpStatus.NOT_FOUND);
            response.setMessage(MessageUtil.getMessage("User.notFound"));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        if (userRepository.deleteUserByUsername(username) > 0) {
            response.setMessage(MessageUtil.getMessage("User.removeSuccess"));
            response.setStatus(HttpStatus.OK);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Delete multiple user
     * 
     * @param usernames
     * @return
     */
    public ProcessStatusModel deletes(IdsModel ids) {
        ProcessStatusModel model = new ProcessStatusModel();
        List<Integer> listIds = ids.getIds();
        List<Integer> usernameNotFound = new ArrayList<>();
        List<ApplicationUser> user = userRepository.findAllByIds(ids.getIds());
        List<Integer> usernameExits = user.stream().map(ApplicationUser::getId).collect(Collectors.toList());

//        user.stream().map(ApplicationUser::getUsername).forEach(System.out::println);

        for (ApplicationUser id : user) {
            if (!CollectionUtil.isDelete(id)) {
                model.getSuccess().add(id.getId());
            } else {
                model.getFailed().add(id.getId());
            }
        }
        // NOT FOUND
        usernameNotFound = listIds.stream().filter(e -> !usernameExits.contains(e)).collect(Collectors.toList());

        if (!model.getSuccess().isEmpty()) {
            userRepository.deleteUserByIds(model.getSuccess());

        }
        model.getNotFound().addAll(usernameNotFound);

        return model;
    }

    /**
     * Load user by user name for spring security
     *
     * @param username
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser applicationUser = userRepository.findByUsernameAndDeleteFlagFalse(username);
        if (applicationUser == null) {
            throw new UsernameNotFoundException(username);
        }
        return UserDetailsImpl.build(applicationUser);
    }

    /**
     * Check username change
     *
     * @param userInDB
     * @param newUser
     * @return boolean
     */
    private boolean isUsernameChanged(ApplicationUser userInDB, ApplicationUser newUser) {
        String accountNameInDB = Helper.getAccountName(userInDB.getFirstName(), userInDB.getLastName());
        String accountName = Helper.getAccountName(newUser.getFirstName(), newUser.getLastName());
        return accountNameInDB.equals(accountName);
    }

    /**
     * Set full name and phone for user
     *
     * @param user
     */
    private void setFullNameAndPhone(ApplicationUser user) {
        user.setFirstName(Helper.normalizeName(user.getFirstName()));
        user.setLastName(Helper.normalizeName(user.getLastName()));
        user.setPhone(Helper.getPhoneNumberWithZeroFirst(user.getPhone()));
    }

    /**
     * Set account name for user
     *
     * @param user
     */
    private void setAccountNameOfUser(ApplicationUser user) {
        String EMAIL_DOMAIN = "@fpt.edu.vn";
        String accountName = Helper.getAccountName(user.getFirstName(), user.getLastName());
        Integer lastIndexOfAccount = userRepository.getLastNumberOfAccountName(accountName);
        if (lastIndexOfAccount != null) {
            accountName = accountName + ++lastIndexOfAccount;
        }
        user.setAccountEmail(accountName + EMAIL_DOMAIN);
        user.setUsername(accountName);
    }

    /**
     * Check class room is exists
     *
     * @param classRooms
     * @return string is a message of class room not exists
     */
    private String existsClassRooms(List<ClassRoom> classRooms) {
        String classRoomNotExist = classRoomService.existsClassRoomsByIdIn(classRooms);
        if (classRoomNotExist != null) {
            return MessageUtil.getMessageWithParam("Classroom.notFoundWithParam", classRoomNotExist);
        }
        return null;
    }

    /**
     * Check major is exist
     *
     * @param major
     * @return string is a message of major not exists
     */
    private String existsMajor(Major major) {
        if (major != null && !majorService.existsById(major.getId())) {
            return MessageUtil.getMessage("Major.notFound");
        }
        return null;
    }

    /**
     * Check phone is exist when add new
     *
     * @param phone
     * @return string is a message of phone is exists
     */
    private String existsPhoneWhenSave(String phone) {
        if (existsByPhone(Helper.getPhoneNumberWithZeroFirst(phone))) {
            return MessageUtil.getMessage("User.phoneExists");
        }
        return null;
    }

    /**
     * Check phone is exist when edit
     *
     * @param userInDB
     * @param editUser
     * @return string is a message of phone is exists
     */
    private String existsPhoneWhenEdit(ApplicationUser userInDB, ApplicationUser editUser) {
        if (!userInDB.getPhone().equals(Helper.getPhoneNumberWithZeroFirst(editUser.getPhone()))
                && existsByPhone(Helper.getPhoneNumberWithZeroFirst(editUser.getPhone()))) {
            return MessageUtil.getMessage("User.phoneExists");
        }
        return null;
    }
}
