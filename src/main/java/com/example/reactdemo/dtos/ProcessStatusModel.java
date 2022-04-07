package com.example.reactdemo.dtos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * @author binhtn1
 *
 */
@Data
public class ProcessStatusModel implements Serializable {

    private static final long serialVersionUID = 1L;

    //Danh sach id thanh cong
    private List<Integer> success = new ArrayList<Integer>();

    //Danh sach id that bai
    private List<Integer> failed = new ArrayList<Integer>();

    //Danh sach id khong ton tai
    private List<Integer> notFound = new ArrayList<Integer>();
}
