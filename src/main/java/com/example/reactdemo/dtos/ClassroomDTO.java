package com.example.reactdemo.dtos;

import com.example.reactdemo.utils.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
/**
 * 
 * @author binhtn1
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassroomDTO {
    private int id;

    // Name of class room
    private String name;

    // Current amount student joined class room
    private long currentAmountStudent;

    // Max amount student of class room
    private int amountStudent;

    // Start date class room
    private Date startDate;

    // End date class room
    private Date endDate;

    // Flag check delete
    private boolean deleteFlag;

    /**
     * Constructor
     * 
     * @param id
     * @param name
     * @param amountStudent
     * @param startDate
     * @param endDate
     */
    public ClassroomDTO(int id, String name, int amountStudent, String startDate, String endDate, boolean deleteFlag) {
        this.id = id;
        this.name = name;
        this.amountStudent = amountStudent;
        this.startDate = DateUtil.stringToDate(startDate);
        this.endDate = DateUtil.stringToDate(endDate);
        this.deleteFlag = deleteFlag;
    }
}
