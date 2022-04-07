package com.example.reactdemo.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * 
 * @author binhtn1
 *
 */

@Component
public class MessageUtil {

    private static ResourceBundleMessageSource messageSource;

    @Autowired
    MessageUtil(ResourceBundleMessageSource messageSource) {
        MessageUtil.messageSource = messageSource;
    }

    /**
     * Get message from message bundle
     *
     * @param msgCode
     * @return message
     */
    public static String getMessage(String msgCode) {
        return messageSource.getMessage(msgCode, null, Locale.US);
    }

    /**
     * Get message from message bundle with param
     *
     * @param msgCode
     * @param params
     * @return message
     */
    public static String getMessageWithParam(String msgCode, String... params) {
        StringBuilder message = new StringBuilder(messageSource.getMessage(msgCode, null, Locale.US));
        for (String param : params) {
            int firstIndex = message.indexOf("{");
            message.replace(firstIndex, firstIndex + 7, param);
        }
        return message.toString();
    }
}
