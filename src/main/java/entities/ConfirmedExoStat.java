package entities;

import org.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
public class ConfirmedExoStat {
    private static final String JSON_ID = "id";
    private static final String JSON_TIMESTAMP = "timeStamp";
    private static final String JSON_HOST_NAME = "hostName";
    private static final String JSON_LETTER = "letter";
    private static final String JSON_DISCOVERY_METHOD = "discoverMethod";
    private static final String JSON_PLANET_NUMBER = "planetNumber";
    private static final String JSON_ORBITAL_PERIOD = "orbitalPeriod";
    private static final String JSON_JUPITER_MASS = "jupiterMass";
    private static final String JSON_JUPITER_RADIUS = "jupiterRadius";
    private static final String JSON_KEPLER_FLAG = "keplerFlag";
    private static final String JSON_K2_FLAG = "k2Flag";
    private static final String JSON_BODY_NAME = "bodyName";
    private static final String JSON_EQUI_TEMP = "equiTemp";
    private static final String JSON_EARTH_RADIUS = "earthRadius";
    public static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    public static final String URL_GET_DATA =
            "https://exoplanetarchive.ipac.caltech.edu/cgi-bin/nstedAPI/nph-nstedAPI?table=exoplanets&format=csv&" +
                    "select=" +
                    "pl_hostname," +
                    "pl_letter," +
                    "pl_discmethod," +
                    "pl_pnum," +
                    "pl_orbper," +
                    "pl_bmassj," +
                    "pl_radj," +
                    "pl_kepflag," +
                    "pl_k2flag," +
                    "pl_name," +
                    "pl_eqt," +
                    "pl_rade";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    LocalDateTime timeStamp;
    String hostName;
    String letter;
    String discoveryMethod;
    Integer planetNumber;
    Double orbitalPeriod;
    Double jupiterMass;
    Double jupiterRadius;
    Integer keplerFlag;
    Integer k2Flag;
    String bodyName;
    Double equiTemp;
    Double earthRadius;

    public ConfirmedExoStat() {
    }

    public ConfirmedExoStat(LocalDateTime timeStamp, String hostName, String letter, String discoveryMethod, Integer planetNumber, Double orbitalPeriod, Double jupiterMass, Double jupiterRadius, Integer keplerFlag, Integer k2Flag, String bodyName, Double equiTemp, Double earthRadius) {
        this.timeStamp = timeStamp;
        this.hostName = hostName;
        this.letter = letter;
        this.discoveryMethod = discoveryMethod;
        this.planetNumber = planetNumber;
        this.orbitalPeriod = orbitalPeriod;
        this.jupiterMass = jupiterMass;
        this.jupiterRadius = jupiterRadius;
        this.keplerFlag = keplerFlag;
        this.k2Flag = k2Flag;
        this.bodyName = bodyName;
        this.equiTemp = equiTemp;
        this.earthRadius = earthRadius;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getDiscoveryMethod() {
        return discoveryMethod;
    }

    public void setDiscoveryMethod(String discoveryMethod) {
        this.discoveryMethod = discoveryMethod;
    }

    public Integer getPlanetNumber() {
        return planetNumber;
    }

    public void setPlanetNumber(Integer planetNumber) {
        this.planetNumber = planetNumber;
    }

    public Double getOrbitalPeriod() {
        return orbitalPeriod;
    }

    public void setOrbitalPeriod(Double orbitalPeriod) {
        this.orbitalPeriod = orbitalPeriod;
    }

    public Double getJupiterMass() {
        return jupiterMass;
    }

    public void setJupiterMass(Double jupiterMass) {
        this.jupiterMass = jupiterMass;
    }

    public Double getJupiterRadius() {
        return jupiterRadius;
    }

    public void setJupiterRadius(Double jupiterRadius) {
        this.jupiterRadius = jupiterRadius;
    }

    public Integer getKeplerFlag() {
        return keplerFlag;
    }

    public void setKeplerFlag(Integer keplerFlag) {
        this.keplerFlag = keplerFlag;
    }

    public Integer getK2Flag() {
        return k2Flag;
    }

    public void setK2Flag(Integer k2Flag) {
        this.k2Flag = k2Flag;
    }

    public String getBodyName() {
        return bodyName;
    }

    public void setBodyName(String bodyName) {
        this.bodyName = bodyName;
    }

    public Double getEquiTemp() {
        return equiTemp;
    }

    public void setEquiTemp(Double equiTemp) {
        this.equiTemp = equiTemp;
    }

    public Double getEarthRadius() {
        return earthRadius;
    }

    public void setEarthRadius(Double earthRadius) {
        this.earthRadius = earthRadius;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        json.put(JSON_ID, id);
        json.put(JSON_TIMESTAMP, timeStamp.format(dtf));
        json.put(JSON_HOST_NAME, hostName);
        json.put(JSON_LETTER, letter);
        json.put(JSON_DISCOVERY_METHOD, discoveryMethod);
        json.put(JSON_PLANET_NUMBER, planetNumber);
        json.put(JSON_ORBITAL_PERIOD, orbitalPeriod);
        json.put(JSON_JUPITER_MASS, jupiterMass);
        json.put(JSON_JUPITER_RADIUS, jupiterRadius);
        json.put(JSON_KEPLER_FLAG, keplerFlag);
        json.put(JSON_K2_FLAG, k2Flag);
        json.put(JSON_BODY_NAME, bodyName);
        json.put(JSON_EQUI_TEMP, equiTemp);
        json.put(JSON_EARTH_RADIUS, earthRadius);

        return json;
    }
}
