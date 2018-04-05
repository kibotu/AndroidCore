package com.exozet.android.core.time;

import com.exozet.android.core.utils.JUnitExtensions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


/**
 * Utilities for converting to/from the HTTP 1.&nbsp;0 timestamp formats.
 * They are:
 * <pre>
 * <ul>
 * <li>Sun, 06 Nov 1994 08:49:37 GMT  ; RFC 822, updated by RFC 1123</li>
 * <li>Sunday, 06-Nov-94 08:49:37 GMT ; RFC 850, obsoleted by RFC 1036</li>
 * <li>Sun Nov  6 08:49:37 1994       ; ANSI C's asctime() format</li>
 * <li>2016-09-19T15:22:21+02:00      ; iso8601() format</li>
 * </ul>
 * </pre>
 * HTTP 1.1 requires clients and servers to read these formats, though they
 * should only write RFC 1123 format.
 * <p>
 * <p>Source: http://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html
 * (via Google search "http timestamp format").
 */
public class TimestampConvert {

    /**
     * EEE, dd MMM yyyy HH:mm:ss zzz
     */
    private static final SimpleDateFormat rfc1123Formatter;

    /**
     * EEEEEEEEE, dd-MMM-yy HH:mm:ss zzz
     */
    private static final SimpleDateFormat rfc1036Formatter;

    /**
     * EEE MMM dd HH:mm:ss yyyy
     */
    private static final SimpleDateFormat asctimeFormatter;

    /**
     * yyyy-MM-dd'T'HH:mm:ssXXX
     */

    /**
     * Source: <a href="https://en.wikipedia.org/wiki/ISO_8601">https://en.wikipedia.org/wiki/ISO_8601</a>
     * <pre>
     *   expressed according to ISO 8601:
     *   Date: 2018-01-31
     *   Combined date and time in UTC: 2018-01-31T18:13:41+00:00
     *   2018-01-31T18:13:41Z
     *   20180131T181341Z
     *   Week: 2018-W05
     *   Date with week number: 2018-W05-3
     *   Ordinal date: 2018-031
     * </pre>
     */
    private static final SimpleDateFormat iso8601Formatter;

    public static final String EEE_DD_MMM_YYYY_HH_MM_SS_ZZZ = "EEE, dd MMM yyyy HH:mm:ss zzz";

    public static final String EEEEEEEEE_DD_MMM_YY_HH_MM_SS_ZZZ = "EEEEEEEEE, dd-MMM-yy HH:mm:ss zzz";

    public static final String EEE_MMM_DD_HH_MM_SS_YYYY = "EEE MMM dd HH:mm:ss yyyy";

    public static final String YYYY_MM_DD_T_HH_MM_SSXXX = "yyyy-MM-dd'T'HH:mm:ssXXX"; // supported only in SDK >= 24 and plain java

    public static final String YYYY_MM_DD_T_HH_MM_SSZZZZZ = "yyyy-MM-dd'T'HH:mm:ssZZZZZ"; // works on all android versions but not in unit tests

    public static String iso8601Format() {
        return isJUnitTest() ? YYYY_MM_DD_T_HH_MM_SSXXX : YYYY_MM_DD_T_HH_MM_SSZZZZZ;
    }

    private static boolean isJUnitTest() {
        return JUnitExtensions.isJUnitTest();
    }

    static {
        rfc1123Formatter = new SimpleDateFormat(EEE_DD_MMM_YYYY_HH_MM_SS_ZZZ, Locale.ENGLISH);
        rfc1123Formatter.setTimeZone(TimeZone.getTimeZone("GMT"));

        rfc1036Formatter = new SimpleDateFormat(EEEEEEEEE_DD_MMM_YY_HH_MM_SS_ZZZ, Locale.ENGLISH);
        rfc1036Formatter.setTimeZone(TimeZone.getTimeZone("GMT"));

        asctimeFormatter = new SimpleDateFormat(EEE_MMM_DD_HH_MM_SS_YYYY, Locale.ENGLISH);
        asctimeFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));

//        iso8601Formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        iso8601Formatter = new SimpleDateFormat(iso8601Format(), Locale.ENGLISH);
        // asctimeFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    /**
     * Converts an ISO-8601-style timestamp to a Date.
     */
    public static Date iso8601ToDate(String timestamp) throws ParseException {
        return iso8601Formatter.parse(timestamp);
    }

    /**
     * Converts a Date to an ISO-8601-style timestamp.
     */
    public static String dateToIso8601(Date date) {
        return iso8601Formatter.format(date);
    }

    /**
     * Converts an RFC-1123-style timestamp to a Date.
     */
    public static Date rfc1123ToDate(String timestamp) throws ParseException {
        return rfc1123Formatter.parse(timestamp);
    }

    /**
     * Converts a Date to an RFC-1123-style timestamp.
     */
    public static String dateToRfc1123(Date date) {
        return rfc1123Formatter.format(date);
    }

    /**
     * Converts an RFC-1036-style timestamp to a Date.
     */
    public static Date rfc1036ToDate(String timestamp) throws ParseException {
        return rfc1036Formatter.parse(timestamp);
    }

    /**
     * Converts a Date to an RFC-1036-style timestamp.
     */
    public static String dateToRfc1036(Date date) {
        return rfc1036Formatter.format(date);
    }

    /**
     * Converts an asctime-style timestamp to a Date.
     */
    public static Date asctimeToDate(String timestamp) throws ParseException {
        return asctimeFormatter.parse(timestamp);
    }

    /**
     * Converts a Date to an asctime-style timestamp.
     */
    public static String dateToAsctime(Date date) {
        return asctimeFormatter.format(date);
    }

    /**
     * Converts an HTTP-style timestamp to a Date. The timestamp could be any of
     * RFC 1123, RFC 1036, or C's asctime().
     *
     * @throws ParseException if none of the formats apply.
     */
    public static Date httpToDate(String timestamp) throws ParseException {
        Date date;

        // Try the different formats in order of preference
        try {
            date = rfc1123ToDate(timestamp);
        } catch (ParseException e) {
            try {
                date = rfc1036ToDate(timestamp);
            } catch (ParseException e1) {
                date = asctimeToDate(timestamp);
            }
        }

        return date;
    }

    private static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";

    public static Date GetUTCDateTimeAsDate(Date date) {
        //note: doesn't check for null
        return StringDateToDate(GetUTCDateTimeAsString(date));
    }

    public static String GetUTCDateTimeAsString(Date date) {
        final SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        return sdf.format(date);
    }

    public static Date StringDateToDate(String StrDate) {
        Date dateToReturn = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            dateToReturn = dateFormat.parse(StrDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateToReturn;
    }

}