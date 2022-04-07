package com.example.reactdemo.apiresponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @author binhtn1
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class ApiValidationError extends ApiSubError {
    // Object name with error
    private String object;

    // Field of object with error
    private String field;

    // Value error
    private Object rejectedValue;

    // Message of error
    private String message;

    /**
     * Constructor
     * @param object
     * @param message
     */
    ApiValidationError(String object, String message) {
        this.object = object;
        this.message = message;
    }
}
