package com.panforge.robotstxt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A factory class used to generate Matcher instances that will
 * throw if in use after their timeout.
 * @author : Credit goes to https://www.exratione.com/2017/06/preventing-unbounded-regular-expression-operations-in-java/
 */
abstract class TimeLimitedMatcherFactory {
    // If a regular expression requires more than a couple of seconds
    // to complete, then it has no place in polite society.
    private static final long DEFAULT_TIMEOUT_MS = 2000L;
    private static final long timeoutMs;
    
    static {
      timeoutMs = determineTimeout();
    }

    /**
     * Generate a Matcher instance that will throw if used or still
     * in use more than timeoutInMilliseconds after its instantiation.
     * <p>
     * Use the instance immediately and then discard it.
     *
     * @param pattern               The Pattern instance.
     * @param charSequence          The CharSequence to operate on.
     * @param timeoutInMilliseconds Throw after this timeout is reached.
     * @return a matcher
     */
    public static Matcher matcher(
            Pattern pattern,
            CharSequence charSequence,
            long timeoutInMilliseconds
    ) {
        // Substitute in our exploding CharSequence implementation.
        if (!(charSequence instanceof TimeLimitedCharSequence)) {
            charSequence = new TimeLimitedCharSequence(
                    charSequence,
                    timeoutInMilliseconds,
                    pattern,
                    charSequence
            );
        }

        return pattern.matcher(charSequence);
    }

    /**
     * Generate a Matcher instance that will throw if used or still
     * in use more than 2 seconds after its instantiation.
     * <p>
     * Use the instance immediately and then discard it.
     *
     * @param pattern      The Pattern instance.
     * @param charSequence The CharSequence to operate on.
     * @return a matcher
     */
    public static Matcher matcher(
            Pattern pattern,
            CharSequence charSequence
    ) {
        return matcher(pattern, charSequence, timeoutMs);
    }

    /**
     * Determine the timeout used.
     *
     * @return timeout
     */
    private static Long determineTimeout() {
        String str = System.getProperty("com.panforge.robotstxt.timeout");
        if (str == null) {
            return DEFAULT_TIMEOUT_MS;
        }
        try {
            return Long.parseLong(str);
        } catch (final NumberFormatException nfe) {
            return DEFAULT_TIMEOUT_MS;
        }
    }

    /**
     * An exception to indicate that a regular expression operation
     * timed out.
     */
    public static class RegExpTimeoutException extends RuntimeException {
        public RegExpTimeoutException(String message) {
            super(message);
        }
    }

    /**
     * A CharSequence implementation that throws when charAt() is called
     * after a given timeout.
     * <p>
     * Since charAt() is invoked frequently in regular expression operations
     * on a string, this gives a way to abort long-running regular expression
     * operations.
     */
    private static class TimeLimitedCharSequence implements CharSequence {
        private final CharSequence inner;
        private final long timeoutInMilliseconds;
        private long timeoutAfterTimestamp = -1;//-1 indicates not yet initialized to proper value.
        private final Pattern pattern;
        private final CharSequence originalCharSequence;

        /**
         * Default constructor.
         *
         * @param inner                 The CharSequence to wrap. This may be a subsequence of the original.
         * @param timeoutInMilliseconds How long before calls to charAt() throw.
         * @param pattern               The Pattern instance; only used for logging purposes.
         * @param originalCharSequence  originalCharSequence The original sequence, used for logging purposes.
         */
        public TimeLimitedCharSequence(
                CharSequence inner,
                long timeoutInMilliseconds,
                Pattern pattern,
                CharSequence originalCharSequence
        ) {
            super();
            this.inner = inner;
            this.timeoutInMilliseconds = timeoutInMilliseconds;
            // Carry out this calculation here, once, rather than every time
            // charAt() is invoked. Little optimizations make the world turn.
            timeoutAfterTimestamp = System.currentTimeMillis() + timeoutInMilliseconds;
            this.pattern = pattern;
            this.originalCharSequence = originalCharSequence;
        }

        public char charAt(int index) {
            // This is an unavoidable slowdown, but what can you do?
            if (System.currentTimeMillis() > timeoutAfterTimestamp) {
                // Note that we add the original charsequence to the exception
                // message. This condition can be met on a subsequence of the
                // original sequence, and the subsequence string is rarely
                // anywhere near as helpful.
                throw new RegExpTimeoutException(
                        "Regular expression timeout after " + timeoutInMilliseconds
                                + "ms for [ " + pattern.pattern() + " ] operating on [ "
                                + originalCharSequence + " ]"
                );
            }
            return inner.charAt(index);
        }

        public int length() {
            return inner.length();
        }

        public CharSequence subSequence(int start, int end) {
            // Ensure that any subsequence generated during a regular expression
            // operation is still going to explode on time.
            return new TimeLimitedCharSequence(
                    inner.subSequence(start, end),
                    timeoutAfterTimestamp - System.currentTimeMillis(),
                    pattern,
                    originalCharSequence
            );
        }

        @Override
        public String toString() {
            return inner.toString();
        }
    }
}
