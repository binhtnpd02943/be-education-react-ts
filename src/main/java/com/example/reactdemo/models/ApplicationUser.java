package com.example.reactdemo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author binhtn1
 *
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ApplicationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // User name of user
    @Length(max = 20)
    private String username;

    // Password of user, default is 123456
    @Length(max = 150)
    @JsonIgnore
    private String password = "123456";

    // Role of user
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    // Class rooms of user
    @ManyToMany(fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinTable(name = "user_classroom",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "classroom_id")
    )
    private List<ClassRoom> classRooms;

    // Major of user
    @ManyToOne
    @JoinColumn(name = "major_id")
    private Major major;

    // First name of user
    @NotNull
    @Length(max = 30)
    @Column(columnDefinition = "nvarchar(30)")
    private String firstName;

    // Last name of user
    @NotNull
    @Length(max = 10)
    @Column(columnDefinition = "nvarchar(10)")
    private String lastName;

    // Account mail of user
    @Length(max = 30)
    private String accountEmail;

    // Gender of user
    @NotNull
    private boolean gender;

    // Phone of user
    @NotNull
    @Pattern(regexp = "^(\\+84|84|0)([3|5|7|8|9])([0-9]{8})$\\b", message = "Phone number is not valid")
    @Length(max = 12)
    private String phone;
    
    // Address of user
    @NotNull
    @Length(max = 100)
    @Column(columnDefinition = "nvarchar(100)")
    private String address;
    
    // Birthday of user
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date birthday;

    // Delete flag
    @JsonIgnore
    private boolean deleteFlag = false;

}
