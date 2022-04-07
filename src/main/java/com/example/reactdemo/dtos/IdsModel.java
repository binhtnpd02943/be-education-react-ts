package com.example.reactdemo.dtos;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;
/**
 * 
 * @author binhtn1
 *
 */
@Data
public class IdsModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "ids can't null")
    private List<Integer> ids;

}