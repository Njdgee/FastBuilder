package com.njdge.fastbuilder.utils;

import java.text.DecimalFormat;

public class TimeUtil {
    private static DecimalFormat df = new DecimalFormat("0.000");

    public static String formatTime(Long time) {
        if (time == null) {
            return CC.GRAY + "-.---";
        }
        double seconds = (double) time / 1000;
        return df.format(seconds);
    }
}
