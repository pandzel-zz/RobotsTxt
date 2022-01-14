package com.panforge.robotstxt;

class StringUtils {

    /**
     * Trim text and return empty String if provided {@code text} is {@code null}.
     *
     * @param text - text to trim
     * @return trimmed text or empty String
     */
    public static String trimToEmpty(String text) {
        if (text == null) {
            return "";
        }
        return text.trim();
    }

    /**
     * Trim text and return {@code null} if provided {@code text} is {@code null} or empty.
     *
     * @param text - text to trim
     * @return trimmed text or {@code null}
     */
    public static String trimToNull(String text) {
        String s = trimToEmpty(text);
        return s.isEmpty() ? null : s;
    }

    /**
     * Find the longest common prefix between {@code a} and {@code b} strings.
     * @param a - text to compare
     * @param b - text to compare with
     * @return longest common prefix
     */
    public static String greatestCommonPrefix(String a, String b) {
        int minLength = Math.min(a.length(), b.length());
        for (int i = 0; i < minLength; i++) {
            if (a.charAt(i) != b.charAt(i)) {
                return a.substring(0, i);
            }
        }
        return a.substring(0, minLength);
    }
}
