package com.example.reactdemo.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * 
 * @author binhtn1
 *
 */

public class Helper {

    /**
     * Get phone number with zero first
     *
     * @param phoneNumber
     * @return string
     */
    public static String getPhoneNumberWithZeroFirst(String phoneNumber) {
        if (phoneNumber.length() == 11) {
            return phoneNumber.replaceFirst("84", "0");
        }

        if (phoneNumber.length() == 12) {
            return phoneNumber.replaceFirst("\\+84", "0");
        }
        return phoneNumber;
    }

    /**
     * Remove space unnecessary
     *
     * @param str
     * @return String
     */
    public static String removeSpaceUnnecessary(String str) {
        return str.trim().replaceAll("\\s+", " ");
    }

    /**
     * Normalize name
     *
     * @param name
     * @return String
     */
    public static String normalizeName(String name) {
        name = removeSpaceUnnecessary(name).toLowerCase();
        String temp[] = name.split(" ");
        name = "";
        for (int i = 0; i < temp.length; i++) {
            name += String.valueOf(temp[i].charAt(0)).toUpperCase() + temp[i].substring(1);
            if (i < temp.length - 1)
                name += " ";
        }
        return name;
    }

    /**
     * Remove accent
     *
     * @param s
     * @return String
     */
    public static String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        temp = pattern.matcher(temp).replaceAll("");
        return temp.replaceAll("đ", "d").replaceAll("Đ", "D");
    }

    /**
     * Get account name from first name and last name
     *
     * @param firstName
     * @param lastName
     * @return string of account name
     */
    public static String getAccountName(String firstName, String lastName) {
        StringBuilder sb = new StringBuilder(lastName);
        String temp[] = firstName.split(" ");
        for (String i : temp) {
            sb.append(i.charAt(0));
        }
        return removeAccent(sb.toString());
    }

}
