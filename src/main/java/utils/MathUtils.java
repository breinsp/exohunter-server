package utils;

import java.util.List;

public class MathUtils {
    public static final double RADIUS_SUN = 695700; //km
    public static final double RADIUS_JUPITER = 69911; //km
    public static final double RADIUS_EARTH = 6371; //km
    public static final double RADIUS_MARS = 3390; //km
    public static final double RADIUS_VENUS = 6052; //km
    public static final double RADIUS_24622 = 69911; //km

    public static final double MASS_SUN = 1.98855E30; //kg

    public static final double ASTRONOMICAL_UNIT =1.49597870700E11; //AU -> m
    public static final double GRAVITATIONAL_CONSTANT = 6.6740831E-11; //m^3 / kg * s^2

    public static final double DAY_TO_SECOND = 24*60*60; //day -> sec
    public static final double HOUR_TO_SECOND = 24*60;   //hour -> sec
    public static final double MINUTE_TO_SECOND = 60;    //minute -> sec

    public static final double KELVIN_TO_CELSIUS = 273.15;

    /**
     * Calculate the standard deviation from a list of doubles and the pre-calculated average.
     * @return the standard deviation
     */
    public static double calculateStandardDeviation(double avg, List<Double> elements) {
        double val = elements.stream().mapToDouble(d -> Math.pow(avg - d, 2)).sum();
        return Math.sqrt(val / elements.size());
    }

    /**
     * Calculate the average from a list of doubles.
     * @return the average
     */
    public static double calculateAverage(List<Double> elements) {
        if (elements.size() == 0) {
            return 0;
        }
        double sum = elements.stream().mapToDouble(d -> d).sum();
        return (double) (sum / elements.size());
    }

    /**
     * Round a decimal number to a certain decimal point.
     * @return rounded value
     */
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    /**
     * Convert a certain number between 0 and 1.
     * @return
     */
    public static double normalizeZeroToOne(double number, double min, double max) {
        return (number - min) / (max - min);
    }

    /**
     * The average percentage difference between two numbers.
     * @return percentage difference
     */
    public static double percentageAvgDifference(double one, double two) {
        return Math.abs(one - two) / ((one + two) / 2);
    }
}
