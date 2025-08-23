package com.cyk.easy.rpc.common.utils;

public final class StringUtils {

    private StringUtils() {
        // Prevent instantiation
    }

    /**
     * Checks if a string is empty or null.
     *
     * @param str the string to check
     * @return true if the string is empty or null, false otherwise
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * Checks if a string is not empty and not null.
     *
     * @param str the string to check
     * @return true if the string is not empty and not null, false otherwise
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
}
