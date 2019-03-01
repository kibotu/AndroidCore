package com.exozet.android.core.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import net.kibotu.logger.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

/**
 * Created by tim.wienrich on 06.10.17.
 */

public class MathHelper {

    public static String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("SHA-256");  //TODO test if working
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            Logger.e(e);
            return s;
        }
    }

    //vvvvvvvvvvvv image processing vvvvvvvvvvvv

    private static int decodeYUV420SPtoRedSum(byte[] yuv420sp, int width, int height) {
        if (yuv420sp == null) {
            return 0;
        }

        final int frameSize = width * height;
        int sum = 0;

        for (int heightIndex = 0, pixelIndex = 0; heightIndex < height; heightIndex++) {
            int uvp = frameSize + (heightIndex >> 1) * width, u = 0, v = 0;

            for (int widthIndex = 0; widthIndex < width; widthIndex++, pixelIndex++) {
                int y = (0xff & yuv420sp[pixelIndex]) - 16;

                if (y < 0) {
                    y = 0;
                }

                if ((widthIndex & 1) == 0) {
                    v = (0xff & yuv420sp[uvp++]) - 128;
                    u = (0xff & yuv420sp[uvp++]) - 128;
                }

                int y1192 = 1192 * y;

                int redPixel = (y1192 + 1634 * v);
                if (redPixel < 0) {
                    redPixel = 0;
                } else if (redPixel > 262143) {
                    redPixel = 262143;
                }

                int greenPixel = (y1192 - 833 * v - 400 * u);
                if (greenPixel < 0) {
                    greenPixel = 0;
                } else if (greenPixel > 262143) {
                    greenPixel = 262143;
                }

                int bluePixel = (y1192 + 2066 * u);
                if (bluePixel < 0) {
                    bluePixel = 0;
                } else if (bluePixel > 262143) {
                    bluePixel = 262143;
                }

                int pixel = 0xff000000 | ((redPixel << 6) & 0xff0000) | ((greenPixel >> 2) & 0xff00)
                        | ((bluePixel >> 10) & 0xff);

                int red = (pixel >> 16) & 0xff;
                sum += red;
            }
        }

        return sum;
    }

    public static int decodeYUV420SPtoRedAvg(byte[] yuv420sp, int width, int height) {
        if (yuv420sp == null) {
            return 0;
        }

        final int frameSize = width * height;

        int sum = decodeYUV420SPtoRedSum(yuv420sp, width, height);

        return (sum / frameSize);
    }

    //^^^^^^^^^^^^ image processing ^^^^^^^^^^^^

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static int convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public static Point getScreenSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static int count(Collection<?> collection) {
        return collection == null
                ? 0
                : collection.size();
    }

    /**
     * Calculates the percentage of a value in a given range.
     *
     * @param value Input value.
     * @param min   - Min Range Value.
     * @param max   - Max Range Value.
     * @return Percentage of a given value in a range. If within range, returned values are between [0f,1f]
     * @deprecated use Float.percentToValueOfRange(min,max)
     */
    @Deprecated
    public static float getPercentInRange(float value, float min, float max) {
        return (value - min) / (max - min);
    }

    /**
     * Calculates a value in a given range by percentage.
     *
     * @param percent - Percentage. Expecting a value [0f,1f] if expected to be in range.
     * @param min     - Min Range Value.
     * @param max     - Max Range Value.
     * @return Concrete value by a given percentage. If percentage value [0f, 1f] then the returned value will be in [min, max] range.
     * @deprecated use Float.valueToPercentOfRange(min,max)
     */
    @Deprecated
    public static float getValueFromPercentInRange(float percent, float min, float max) {
        return min + percent * (max - min);
    }
}
