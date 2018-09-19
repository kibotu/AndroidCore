package com.exozet.android.core.utils;


import java.io.UnsupportedEncodingException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static android.text.TextUtils.isEmpty;
import static com.exozet.android.core.utils.ByteExtensions.validUTF8;

/**
 * Created by Jan Rabe on 14/10/15.
 */
final public class StringExtensions {

    private StringExtensions() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    @NonNull
    public static String clamp(@Nullable final String s, final int maxLength) {
        if (isEmpty(s))
            return "";

        return s.substring(0, MathExtensions.clamp(s.length(), 0, maxLength));
    }

    /**
     * http://stackoverflow.com/a/1447720
     */
    public static String fixEncoding(String latin1) {
        try {
            byte[] bytes = latin1.getBytes("ISO-8859-1");
            if (!validUTF8(bytes))
                return latin1;
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // Impossible, throw unchecked
            throw new IllegalStateException("No Latin1 or UTF-8: " + e.getMessage());
        }
    }

    @NonNull
    public static String capitalize(final String line) {
        return isEmpty(line)
                ? ""
                : Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    public static String surroundWithQuotes(String msg) {
        return String.format("\"%s\"", "" + msg);
    }

    private static String escape(String s) {
        return isEmpty(s)
                ? ""
                : s.replace("\n", "\\n").replace("\r", "\\r");
    }

    public static int length(@Nullable final String string) {
        return isEmpty(string) ? 0 : string.length();
    }
}
