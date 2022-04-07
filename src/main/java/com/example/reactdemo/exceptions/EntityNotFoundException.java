package com.example.reactdemo.exceptions;

import com.example.reactdemo.utils.MessageUtil;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * 
 * @author binhtn1
 *
 */
public class EntityNotFoundException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public EntityNotFoundException(Class<?> clazz, String... searchParamsMap) {
        super(EntityNotFoundException.generateMessage(clazz.getSimpleName(), toMap(String.class, String.class, searchParamsMap)));
    }

    /**
     * Generate message of error
     * @param entity
     * @param searchParams
     * @return String is a message
     */
    private static String generateMessage(String entity, Map<String, String> searchParams) {
        return MessageUtil.getMessageWithParam("entityNotFound", StringUtils.capitalize(entity), searchParams.toString());
    }

    /**
     * Map values is error
     * @param keyType
     * @param valueType
     * @param entries
     * @param <K>
     * @param <V>
     * @return Map object and value error
     */
    private static <K, V> Map<K, V> toMap(
            Class<K> keyType, Class<V> valueType, Object... entries) {
        if (entries.length % 2 == 1)
            throw new IllegalArgumentException("Invalid entries");
        return IntStream.range(0, entries.length / 2).map(i -> i * 2)
                .collect(HashMap::new,
                        (m, i) -> m.put(keyType.cast(entries[i]), valueType.cast(entries[i + 1])),
                        Map::putAll);
    }

}