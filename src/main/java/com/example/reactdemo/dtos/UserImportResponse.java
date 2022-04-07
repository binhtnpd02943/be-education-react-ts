package com.example.reactdemo.dtos;

import lombok.Data;

import java.util.Date;

/**
 * 
 * @author binhtn1
 *
 */

@Data
public class UserImportResponse {

    // First name of user
    private String firstName;

    // Last name of user
    private String lastName;

    // Gender of user
    private boolean gender;

    // Account mail of user
    private String accountMail;

    // Birthday of user
    private Date birthday;

    // Phone of user
    private String phone;

    // Address of user
    private String address;

    // Status of handle
    private int status;
    
    //Role of user
    private int role;
}
