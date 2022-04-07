package com.example.reactdemo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
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
public class Major {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Major code of major
    private String majorCode;

    // Name of major
    private String name;

    @OneToMany(mappedBy = "major", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    // List user of major
    private List<ApplicationUser> users;
}
