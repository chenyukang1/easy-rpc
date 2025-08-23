package com.cyk.easy.rpc.common.utils;

public class Assert {

    private Assert() {
        // Prevent instantiation
    }

    /**
     * Asserts that the given condition is true.
     *
     * @param condition the condition to check
     * @param message   the message to include in the exception if the condition is false
     */
    public static void isTrue(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Asserts that the given object is not null.
     *
     * @param object  the object to check
     * @param message the message to include in the exception if the object is null
     */
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Asserts that the given string is not null or empty.
     *
     * @param str     the string to check
     * @param message the message to include in the exception if the string is null or empty
     */
    public static void notEmpty(String str, String message) {
        if (StringUtils.isEmpty(str)) {
            throw new IllegalArgumentException(message);
        }
    }
}
