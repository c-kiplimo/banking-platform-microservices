package com.collicode.card.util;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.relational.core.query.Criteria;

import java.util.Map;
import java.util.Set;

public class QueryUtil {

    public static Criteria createCriteria(Map<String, String> map, Set<String> allowedFields) {
        Criteria criteria = Criteria.empty();
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (allowedFields.contains(key)) {
                    criteria = criteria.and(key).is(value);
                }
            }
        }
        return criteria;
    }

    public static PageRequest pageRequest(Map<String, String> map) {
        int page = 0;
        int size = 100;
        if (map != null) {
            try {
                page = Integer.parseInt(map.getOrDefault("page", "0"));
                size = Integer.parseInt(map.getOrDefault("size", "10"));
            } catch (NumberFormatException e) {

            }
        }
        return PageRequest.of(page, size);
    }
}

