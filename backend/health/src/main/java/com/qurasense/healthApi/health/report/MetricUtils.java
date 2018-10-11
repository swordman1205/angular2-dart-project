package com.qurasense.healthApi.health.report;


import com.qurasense.healthApi.health.model.UnitType;

public class MetricUtils {

    public static String weightToString(Long weight, UnitType weightUnit) {
        if (weightUnit == UnitType.METRIC) {
            return String.format("%s kg", weight);
        } else {
            int lbs = (int) Math.round(weight / 0.45359237);
            return String.format("%s lbs", lbs);
        }
    }

    public static String heightToString(Long height, UnitType heightUnit) {
        if (heightUnit == UnitType.METRIC) {
            return String.format("%s cm", height);
        } else {
            int totalInches = (int) Math.round(height / 2.54);
            int feet = totalInches / 12;
            int inch = totalInches % 12;
            return String.format("%s feet, %s inch", feet, inch);
        }
    }
}
