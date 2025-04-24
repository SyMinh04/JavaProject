package com.example.stu.Core.Utils;

/**
 * Utility class for converting between different case formats
 */
public class CaseConverter {

    /**
     * Converts a camelCase string to snake_case
     * @param camelCase String in camelCase format
     * @return String in snake_case format
     */
    public static String camelToSnakeCase(String camelCase) {
        if (camelCase == null || camelCase.isEmpty()) {
            return camelCase;
        }
        
        StringBuilder result = new StringBuilder();
        result.append(Character.toLowerCase(camelCase.charAt(0)));
        
        for (int i = 1; i < camelCase.length(); i++) {
            char ch = camelCase.charAt(i);
            if (Character.isUpperCase(ch)) {
                result.append('_');
                result.append(Character.toLowerCase(ch));
            } else {
                result.append(ch);
            }
        }
        
        return result.toString();
    }

    /**
     * Converts a snake_case string to camelCase
     * @param snakeCase String in snake_case format
     * @return String in camelCase format
     */
    public static String snakeToCamelCase(String snakeCase) {
        if (snakeCase == null || snakeCase.isEmpty()) {
            return snakeCase;
        }
        
        StringBuilder result = new StringBuilder();
        boolean nextUpper = false;
        
        for (int i = 0; i < snakeCase.length(); i++) {
            char ch = snakeCase.charAt(i);
            if (ch == '_') {
                nextUpper = true;
            } else {
                if (nextUpper) {
                    result.append(Character.toUpperCase(ch));
                    nextUpper = false;
                } else {
                    result.append(ch);
                }
            }
        }
        
        return result.toString();
    }
}