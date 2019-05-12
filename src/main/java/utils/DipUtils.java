package utils;

import entities.LADip;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DipUtils {
    /**
     * Calculate the average period in a list of dips.
     * @return average period
     */
    public static double calculateAveragePeriodOfDips(List<LADip> dips) {
        Collections.sort(dips, LADip.ID_COMPERATOR);

        List<Double> periods = new LinkedList<>();
        for (int i = 1; i < dips.size(); i++) {
            periods.add(dips.get(i).getEndPoint() - dips.get(i - 1).getEndPoint());
        }
        return MathUtils.calculateAverage(periods);
    }
}
