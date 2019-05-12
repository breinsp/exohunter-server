package entities;

import org.json.JSONArray;
import org.json.JSONObject;
import utils.DipUtils;
import utils.MathUtils;
import enums.Units;

import javax.persistence.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Julian Hautzmayer on 19.08.2017.
 * Candidates are an conclusion of all dips which intercommunicate with each other.
 * The LACandidate holds all important properties and end-results concerning a exo-planet or unidentified object.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "LACandidate.findAll", query = "select c from LACandidate c"),
        @NamedQuery(name = "LACandidate.findById", query = "select c from LACandidate c where c.id=:id"),
        @NamedQuery(name = "LACandidate.findCandidateMatchingByEvalId", query = "select c from LACandidate c join fetch c.k2LACandidateMatchingResults where c.evaluation.id=:evalId")
})
public class LACandidate {
    private static final String JSON_ID = "id";
    private static final String JSON_DIPS = "dips";
    private static final String JSON_AVG_TOTAL_TIME = "avgTotalTime";
    private static final String JSON_AVG_FULL_TIME = "avgFullTime";
    private static final String JSON_ORBITAL_VELOCITY = "orbitalVelocity";
    private static final String JSON_SEMI_MAJOR_AXIS = "semiMajorAxis";
    private static final String JSON_IMPACT_PARAMETER = "impactParameter";
    private static final String JSON_AVG_DELTA_FLUX = "avgDeltaFlux";
    private static final String JSON_AVG_PLANET_RADIUS = "avgPlanetRadius";
    private static final String JSON_AVG_PlANET_RADIUS_ERR1 = "avgPlanetRadiusErr1";
    private static final String JSON_AVG_PlANET_RADIUS_ERR2 = "avgPlanetRadiusErr2";
    private static final String JSON_EQU_TEMP_K_BB01 = "equTempKBB01";
    private static final String JSON_EQU_TEMP_K_BB03 = "equTempKBB03";
    private static final String JSON_EQU_TEMP_K_BB07 = "equTempKBB07";
    private static final String JSON_AVG_PERIOD = "periodAvg";
    private static final String JSON_UNITS = "units";
    private static final String JSON_IS_EQUILIBRIUM_TEMP_VALID = "isEquilibriumTempValid";
    private static final String JSON_IS_PERIOD_VALID = "isPeriodValid";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JoinColumn(name = "la_candidate_id")
    List<LADip> dips;         //all the dips respective to a candidate
    Double avgTotalTime;    //average total time of all dips(in days)
    Double avgFullTime;     //average full time of all dips(in days)
    Double orbitalVelocity;     //average orbital velocity of the star(in m/s)
    Double semiMajorAxis;   //average distance between candidate and the star(in AU)
    Double impactParameter; //impact parameter b
    Double avgDeltaFlux;    //average delta flux of all dips
    Double avgPlanetRadius;    //radius of the planet(in km)
    Double avgPlanetRadiusErr1;   //radius of the planet with radius of star error(in km)
    Double avgPlanetRadiusErr2;   //radius of the planet with radius of star error(in km)

    Double equTempKBB01;    //equilibrium temperature of a black body sphere with reflection value 0.1 in kelvin
    Double equTempKBB03;    //equilibrium temperature of a black body sphere with reflection value 0.3 in kelvin
    Double equTempKBB07;    //equilibrium temperature of a black body sphere with reflection value 0.7 in kelvin

    Double periodAvg;      //average period determined by multiple dips or calculated by a single dip(in days)
    @ManyToOne
    Star star;             //referenced star for the candidate
    @ManyToOne
    LAEvaluation evaluation;
    boolean isEquilibriumTempValid;
    boolean isPeriodValid;
    @OneToMany(mappedBy = "match", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    List<K2LACandidateMatchingResult> k2LACandidateMatchingResults;

    @ElementCollection(fetch = FetchType.EAGER)
    Map<String, Units> units;

    public LACandidate() {
        this.k2LACandidateMatchingResults = new LinkedList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<LADip> getDips() {
        return dips;
    }

    public void setDips(List<LADip> dips) {
        this.dips = dips;
    }

    public Double getOrbitalVelocity() {
        return orbitalVelocity;
    }

    public void setOrbitalVelocity(Double orbitalVelocity) {
        this.orbitalVelocity = orbitalVelocity;
    }

    public Double getSemiMajorAxis() {
        return semiMajorAxis;
    }

    public void setSemiMajorAxis(Double semiMajorAxis) {
        this.semiMajorAxis = semiMajorAxis;
    }

    public Double getImpactParameter() {
        return impactParameter;
    }

    public void setImpactParameter(Double impactParameter) {
        this.impactParameter = impactParameter;
    }

    public Double getAvgDeltaFlux() {
        return avgDeltaFlux;
    }

    public void setAvgDeltaFlux(Double avgDeltaFlux) {
        this.avgDeltaFlux = avgDeltaFlux;
    }

    public Double getAvgPlanetRadius() {
        return avgPlanetRadius;
    }

    public void setAvgPlanetRadius(Double avgPlanetRadius) {
        this.avgPlanetRadius = avgPlanetRadius;
    }

    public Double getPeriodAvg() {
        return periodAvg;
    }

    public void setPeriodAvg(Double periodAvg) {
        this.periodAvg = periodAvg;
    }

    public Double getAvgTotalTime() {
        return avgTotalTime;
    }

    public void setAvgTotalTime(Double avgTotalTime) {
        this.avgTotalTime = avgTotalTime;
    }

    public Double getAvgFullTime() {
        return avgFullTime;
    }

    public void setAvgFullTime(Double avgFullTime) {
        this.avgFullTime = avgFullTime;
    }

    public Double getAvgPlanetRadiusErr1() {
        return avgPlanetRadiusErr1;
    }

    public void setAvgPlanetRadiusErr1(Double avgPlanetRadiusErr1) {
        this.avgPlanetRadiusErr1 = avgPlanetRadiusErr1;
    }

    public Double getAvgPlanetRadiusErr2() {
        return avgPlanetRadiusErr2;
    }

    public void setAvgPlanetRadiusErr2(Double avgPlanetRadiusErr2) {
        this.avgPlanetRadiusErr2 = avgPlanetRadiusErr2;
    }

    public Star getStar() {
        return star;
    }

    public void setStar(Star star) {
        this.star = star;
    }

    public boolean isEquilibriumTempValid() {
        return isEquilibriumTempValid;
    }

    public void setEquilibriumTempValid(boolean equilibriumTempValid) {
        isEquilibriumTempValid = equilibriumTempValid;
    }

    public boolean isPeriodValid() {
        return isPeriodValid;
    }

    public void setPeriodValid(boolean periodValid) {
        isPeriodValid = periodValid;
    }

    public Double getEquTempKBB01() {
        return equTempKBB01;
    }

    public void setEquTempKBB01(Double equTempKBB01) {
        this.equTempKBB01 = equTempKBB01;
    }

    public Double getEquTempKBB03() {
        return equTempKBB03;
    }

    public void setEquTempKBB03(Double equTempKBB03) {
        this.equTempKBB03 = equTempKBB03;
    }

    public Double getEquTempKBB07() {
        return equTempKBB07;
    }

    public void setEquTempKBB07(Double equTempKBB07) {
        this.equTempKBB07 = equTempKBB07;
    }

    public Map<String, Units> getUnits() {
        return units;
    }

    public LAEvaluation getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(LAEvaluation evaluation) {
        this.evaluation = evaluation;
    }

    public List<K2LACandidateMatchingResult> getK2LACandidateMatchingResults() {
        return k2LACandidateMatchingResults;
    }

    public void setK2LACandidateMatchingResults(List<K2LACandidateMatchingResult> k2LACandidateMatchingResults) {
        this.k2LACandidateMatchingResults = k2LACandidateMatchingResults;
    }

    public void setUnits(Map<String, Units> units) {
        this.units = units;
    }

    private void initUnits() {
        units = new HashMap<>();
        units.put(JSON_AVG_TOTAL_TIME, Units.TIME_BJD_2454833);
        units.put(JSON_AVG_FULL_TIME, Units.TIME_BJD_2454833);
        units.put(JSON_ORBITAL_VELOCITY, Units.VELOCITY_M_PER_S);
        units.put(JSON_SEMI_MAJOR_AXIS, Units.DISTANCE_AU);
        units.put(JSON_IMPACT_PARAMETER, Units.DISTANCE_KM);
        units.put(JSON_AVG_DELTA_FLUX, Units.OTHER_FLUX);
        units.put(JSON_AVG_PLANET_RADIUS, Units.DISTANCE_JUPITER_RAD);
        units.put(JSON_AVG_PlANET_RADIUS_ERR1, Units.DISTANCE_JUPITER_RAD);
        units.put(JSON_AVG_PlANET_RADIUS_ERR2, Units.DISTANCE_JUPITER_RAD);
        units.put(JSON_EQU_TEMP_K_BB01, Units.TEMP_KELVIN);
        units.put(JSON_EQU_TEMP_K_BB03, Units.TEMP_KELVIN);
        units.put(JSON_EQU_TEMP_K_BB07, Units.TEMP_KELVIN);
        units.put(JSON_AVG_PERIOD, Units.TIME_DAY);
    }

    /**
     * Determine the average planet radius, together with the planet radius and the plus and minus error of the star radius.
     */
    private void determineRadius() {
        List<Double> planetRadii = new LinkedList<>();
        List<Double> planetRadiiErr1 = new LinkedList<>();
        List<Double> planetRadiiErr2 = new LinkedList<>();
        for (LADip dip : dips) {
            double rad_err1 = calculateRadiusWithError(dip, star, star.getRad_err1());
            double rad_err2 = calculateRadiusWithError(dip, star, star.getRad_err2());
            double normal_radius = calculateRadiusWithError(dip, star, 0);
            planetRadiiErr1.add(rad_err1);
            planetRadiiErr2.add(rad_err2);
            planetRadii.add(normal_radius);
        }
        this.avgPlanetRadius = MathUtils.calculateAverage(planetRadii);
        this.avgPlanetRadiusErr1 = MathUtils.calculateAverage(planetRadiiErr1);
        this.avgPlanetRadiusErr2 = MathUtils.calculateAverage(planetRadiiErr2);
    }

    /**
     * Calculate the planet radius with a certain star radius error.
     *
     * @return calculated planet radius
     */
    private double calculateRadiusWithError(LADip dip, Star star, double error) {
        double mainFlux = (dip.getFluxes().get(0) + dip.getFluxes().get(dip.getFluxes().size() - 1)) / 2;
        double planetRadius = Math.sqrt((dip.getDeltaFlux() / mainFlux) * Math.pow((star.getRadius() + error) * MathUtils.RADIUS_SUN, 2)) / MathUtils.RADIUS_JUPITER;
        return planetRadius;
    }

    /**
     * Calculate the semi-major axis of the planet in respect to the star.
     *
     * @return calculated semi-major axis
     */
    private Double determineSemiMajorAxis() {
        if (this.periodAvg == null) {
            return null;
        }
        double a = Math.pow(this.periodAvg * MathUtils.DAY_TO_SECOND, 2) * MathUtils.GRAVITATIONAL_CONSTANT * star.getMass() * MathUtils.MASS_SUN;
        a = Math.pow(a / (4 * Math.PI * Math.PI), 1D / 3);
        a = a / MathUtils.ASTRONOMICAL_UNIT;
        return a;
    }

    /**
     * Calculate the average, orbital velocity of the planet.
     * (it is taken for granted that the planet has a perfect circular orbit, a more accurate determination of
     * the average velocity is not possible with the value we can get out of the light curve)
     *
     * @return average, orbital velocity of the planet
     */
    private Double determineVelocity() {
        if (this.periodAvg == null) {
            return null;
        }
        double v = (2 * Math.PI * this.semiMajorAxis * MathUtils.ASTRONOMICAL_UNIT) / (this.periodAvg * MathUtils.DAY_TO_SECOND);
        return v;
    }

    /**
     * Calculate the equilibrium temperature for a spherical black body with reflection values of 0.1, 0.3 and 0.7.
     */
    private void determineEquilibriumTemperature() {
        if (star.getEffectiveTemp() <= -1 || semiMajorAxis == null) {
            this.equTempKBB01 = null;
            this.equTempKBB03 = null;
            this.equTempKBB07 = null;
            return;
        }
        this.isEquilibriumTempValid = true;
        this.equTempKBB01 = star.getEffectiveTemp() * Math.pow(1 - 0.1, 1D / 4) * Math.sqrt((star.getRadius() * MathUtils.RADIUS_SUN) / (2 * semiMajorAxis * MathUtils.ASTRONOMICAL_UNIT / 1000));
        this.equTempKBB03 = star.getEffectiveTemp() * Math.pow(1 - 0.3, 1D / 4) * Math.sqrt((star.getRadius() * MathUtils.RADIUS_SUN) / (2 * semiMajorAxis * MathUtils.ASTRONOMICAL_UNIT / 1000));
        this.equTempKBB07 = star.getEffectiveTemp() * Math.pow(1 - 0.7, 1D / 4) * Math.sqrt((star.getRadius() * MathUtils.RADIUS_SUN) / (2 * semiMajorAxis * MathUtils.ASTRONOMICAL_UNIT / 1000));
    }

    /**
     * Calculate the average deltaFlux value of all dips.
     *
     * @return avg deltaFlux value
     */
    private double calculateAvgDeltaFlux() {
        List<Double> list = new LinkedList<>();
        for (LADip dip : this.dips) {
            list.add(dip.getDeltaFlux());
        }
        return MathUtils.calculateAverage(list);
    }

    /**
     * Calculate the average totalTime value of all dips.
     *
     * @return avg totalTime value
     */
    private double calculateAvgTotalTime() {
        List<Double> list = new LinkedList<>();
        for (LADip dip : this.dips) {
            list.add(dip.getTotalTime());
        }
        return MathUtils.calculateAverage(list);
    }

    /**
     * Calculate the average fullTime value of all dips.
     *
     * @return avg fullTime value
     */
    private double calculateAvgFullTime() {
        List<Double> list = new LinkedList<>();
        for (LADip dip : this.dips) {
            list.add(dip.getFullTime());
        }
        return MathUtils.calculateAverage(list);
    }

    /**
     * Determine the average period of all dips. If there are at least two dips, the period can be determined by the ending / starting points of the dips.
     * Else the period must be calculated by models which will come in the future TODO.
     *
     * @return determined or calculated avg period of dips
     */
    private Double determineOrCalculatePeriod() {
        if (this.dips.size() > 1) {
            this.isPeriodValid = true;
            return DipUtils.calculateAveragePeriodOfDips(this.dips);
        } else {
            //calculate
            /*double period = (star.getMass() / Math.pow(star.getRadius(), 3)) *
                    (LightCurveUtils.GRAVITATIONAL_CONSTANT * Math.PI / 32) *
                    (Math.pow(getAvgTotalTime(), 2) / Math.pow(getAvgDeltaFlux(), 3/4));*/
            return null;
        }
    }

    public JSONObject parseToJson() {
        JSONObject json = new JSONObject();

        JSONArray arr = new JSONArray();
        for (LADip dip : dips) {
            arr.put(dip.parseToJson());
        }

        json.putOnce(JSON_DIPS, arr);

        json.putOnce(JSON_ID, id);
        json.putOnce(JSON_AVG_TOTAL_TIME, avgTotalTime);
        json.putOnce(JSON_AVG_FULL_TIME, avgFullTime);

        json.putOnce(JSON_AVG_DELTA_FLUX, avgDeltaFlux);
        json.putOnce(JSON_AVG_PLANET_RADIUS, avgPlanetRadius);
        json.putOnce(JSON_AVG_PlANET_RADIUS_ERR1, avgPlanetRadiusErr1);
        json.putOnce(JSON_AVG_PlANET_RADIUS_ERR2, avgPlanetRadiusErr2);

        json.putOnce(JSON_AVG_PERIOD, periodAvg);

        json.putOnce(JSON_ORBITAL_VELOCITY, orbitalVelocity);
        json.putOnce(JSON_SEMI_MAJOR_AXIS, semiMajorAxis);
        json.putOnce(JSON_IMPACT_PARAMETER, impactParameter);

        json.putOnce(JSON_EQU_TEMP_K_BB01, equTempKBB01);
        json.put(JSON_EQU_TEMP_K_BB03, equTempKBB03);
        json.put(JSON_EQU_TEMP_K_BB07, equTempKBB07);

        json.put(JSON_UNITS, Units.parseUnitMap(units));
        json.put(JSON_IS_EQUILIBRIUM_TEMP_VALID, isEquilibriumTempValid);
        json.put(JSON_IS_PERIOD_VALID, isPeriodValid);

        try {
        if(k2LACandidateMatchingResults != null) {
            JSONArray newArr = new JSONArray();
            for (K2LACandidateMatchingResult match : k2LACandidateMatchingResults) {
                newArr.put(match.parseToJson());
            }
            json.put("matches", newArr);
        }} catch (Exception e) {

        }
        return json;
    }

    public static LACandidate createFromJson(JSONObject json, Star star) {
        Long id = null;
        List<LADip> dips = new LinkedList<>();
        Double avgTotalTime = null;
        Double avgFullTime = null;
        Double orbitalVelocity = null;
        Double semiMajorAxis = null;
        Double impactParameter = null;
        Double avgDeltaFlux = null;
        Double avgPlanetRadius = null;
        Double avgPlanetRadiusErr1 = null;
        Double avgPlanetRadiusErr2 = null;

        Double equTempKBB01 = null;
        Double equTempKBB03 = null;
        Double equTempKBB07 = null;

        Double avgPeriod = null;

        boolean isEquilibriumTempValid = false;
        boolean isPeriodValid = false;

        if (json.has(JSON_ID))
            id = json.getLong(JSON_ID);
        if (json.has(JSON_AVG_TOTAL_TIME))
            avgTotalTime = json.getDouble(JSON_AVG_TOTAL_TIME);
        if (json.has(JSON_AVG_FULL_TIME))
            avgFullTime = json.getDouble(JSON_AVG_FULL_TIME);
        if (json.has(JSON_ORBITAL_VELOCITY))
            orbitalVelocity = json.getDouble(JSON_ORBITAL_VELOCITY);
        if (json.has(JSON_SEMI_MAJOR_AXIS))
            semiMajorAxis = json.getDouble(JSON_SEMI_MAJOR_AXIS);
        if (json.has(JSON_IMPACT_PARAMETER))
            impactParameter = json.getDouble(JSON_IMPACT_PARAMETER);
        if (json.has(JSON_AVG_DELTA_FLUX))
            avgDeltaFlux = json.getDouble(JSON_AVG_DELTA_FLUX);
        if (json.has(JSON_AVG_PLANET_RADIUS))
            avgPlanetRadius = json.getDouble(JSON_AVG_PLANET_RADIUS);
        if (json.has(JSON_AVG_PlANET_RADIUS_ERR1))
            avgPlanetRadiusErr1 = json.getDouble(JSON_AVG_PlANET_RADIUS_ERR1);
        if (json.has(JSON_AVG_PlANET_RADIUS_ERR2))
            avgPlanetRadiusErr2 = json.getDouble(JSON_AVG_PlANET_RADIUS_ERR2);
        if (json.has(JSON_EQU_TEMP_K_BB01))
            equTempKBB01 = json.getDouble(JSON_EQU_TEMP_K_BB01);
        if (json.has(JSON_EQU_TEMP_K_BB03))
            equTempKBB03 = json.getDouble(JSON_EQU_TEMP_K_BB03);
        if (json.has(JSON_EQU_TEMP_K_BB07))
            equTempKBB07 = json.getDouble(JSON_EQU_TEMP_K_BB07);
        if (json.has(JSON_AVG_PERIOD))
            avgPeriod = json.getDouble(JSON_AVG_PERIOD);
        if (json.has(JSON_IS_EQUILIBRIUM_TEMP_VALID))
            isEquilibriumTempValid = json.getBoolean(JSON_IS_EQUILIBRIUM_TEMP_VALID);
        if (json.has(JSON_IS_PERIOD_VALID))
            isPeriodValid = json.getBoolean(JSON_IS_PERIOD_VALID);

        JSONArray arr = json.getJSONArray(JSON_DIPS);
        for (int i = 0; i < arr.length(); i++) {
            LADip dip = LADip.createFromJson(arr.getJSONObject(i));
            dips.add(dip);
        }

        LACandidate candidate = new LACandidate();
        candidate.setId(id);
        candidate.setAvgTotalTime(avgTotalTime);
        candidate.setAvgFullTime(avgFullTime);

        candidate.setOrbitalVelocity(orbitalVelocity);
        candidate.setSemiMajorAxis(semiMajorAxis);
        candidate.setImpactParameter(impactParameter);
        candidate.setAvgDeltaFlux(avgDeltaFlux);
        candidate.setAvgPlanetRadius(avgPlanetRadius);
        candidate.setAvgPlanetRadiusErr1(avgPlanetRadiusErr1);
        candidate.setAvgPlanetRadiusErr2(avgPlanetRadiusErr2);

        candidate.setEquTempKBB01(equTempKBB01);
        candidate.setEquTempKBB03(equTempKBB03);
        candidate.setEquTempKBB07(equTempKBB07);

        candidate.setEquilibriumTempValid(isEquilibriumTempValid);
        candidate.setPeriodValid(isPeriodValid);

        candidate.setPeriodAvg(avgPeriod);
        candidate.setDips(dips);
        candidate.setStar(star);

        Map<String, Units> units = Units.convertToUnitMap(json.getJSONObject(JSON_UNITS));
        candidate.setUnits(units);

        return candidate;
    }

    @Override
    public String toString() {
        return "LACandidate{" +
                "avgTimeTotal=" + avgTotalTime +
                ", avgTimeFull=" + avgFullTime +
                ", orbitalVelocity=" + orbitalVelocity +
                ", semiMajorAxis=" + semiMajorAxis +
                ", impactParameter=" + impactParameter +
                ", avgDeltaFlux=" + avgDeltaFlux +
                ", avgPlanetRadius=" + avgPlanetRadius +
                ", periodAvg=" + periodAvg +
                '}';
    }
}
