package com.example.reactdemo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
public class ClassRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "nvarchar(50)")
    @Length(max = 50, message = "Class name length must be between 0 and 50")
    // Name of class
    private String name;

    @Max(value = 40, message = "Amount Student must be less than or equal to 40")
    @Min(value = 10, message = "Amount Student must be greater than or equal to 10")
    // Amount max student of class room
    private int amountStudent;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    // Start date of class room
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    // End date of class room
    private Date endDate;

    @ManyToMany(mappedBy = "classRooms")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    // List users of class room
    private List<ApplicationUser> users;

    //@JsonIgnore
    // Delete flag
    private boolean deleteFlag = false;
}
