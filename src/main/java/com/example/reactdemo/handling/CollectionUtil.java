package com.example.reactdemo.handling;

import com.example.reactdemo.models.ApplicationUser;

/**
 * 
 * @author binhtn1
 *
 */
public class CollectionUtil {


    /**
     * Hàm check điều kiện xóa
     * @param <T>
     * @param t
     * @return
     */
    public static <T extends ApplicationUser> boolean isDelete(T t) {
        return t.isDeleteFlag();
    }
}
