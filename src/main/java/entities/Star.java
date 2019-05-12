package entities;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.json.JSONArray;
import org.json.JSONObject;
import enums.Units;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@NamedQueries({
        @NamedQuery(name = "Star.findAll", query = "select s from Star s"),
        @NamedQuery(name = "Star.findById", query = "select s from Star s where s.id=:id"),
        @NamedQuery(name = "Star.findByEpic", query = "select s from Star s where s.epic=:epic")
})
public class Star {
    private static final String JSON_ID = "id";
    private static final String JSON_EPIC = "epic";
    private static final String JSON_CAMPAIGN = "campaign";
    private static final String JSON_MASS = "mass";
    private static final String JSON_RADIUS = "radius";
    private static final String JSON_RAD_ERR1 = "rad_err1";
    private static final String JSON_RAD_ERR2 = "rad_err2";
    private static final String JSON_EFFECTIVE_TEMP = "effectiveTemp";
    private static final String JSON_DISTANCE = "distance";
    private static final String JSON_UNITS = "units";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    int epic;
    String campaign;    //TODO: campaign
    double mass;
    double radius;
    double rad_err1;
    double rad_err2;
    double effectiveTemp;
    double distance;

    @ElementCollection(fetch = FetchType.EAGER)
    Map<String, Units> units;

    public Star() {}

    public Star(int epic, double mass, double radius, double rad_err1, double rad_err2, double effectiveTemp, double distance) {
        initUnits();
        this.epic = epic;
        this.mass = mass;
        this.radius = radius;
        this.rad_err1 = rad_err1;
        this.rad_err2 = rad_err2;
        this.effectiveTemp = effectiveTemp;
        this.distance = distance;
    }

    public int getEpic() {
        return epic;
    }

    public void setEpic(int epic) {
        this.epic = epic;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getRad_err1() {
        return rad_err1;
    }

    public void setRad_err1(double rad_err1) {
        this.rad_err1 = rad_err1;
    }

    public double getRad_err2() {
        return rad_err2;
    }

    public void setRad_err2(double rad_err2) {
        this.rad_err2 = rad_err2;
    }

    public double getEffectiveTemp() {
        return effectiveTemp;
    }

    public void setEffectiveTemp(double effectiveTemp) {
        this.effectiveTemp = effectiveTemp;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getCampaign() {
        return campaign;
    }

    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }

    public Map<String, Units> getUnits() {
        return units;
    }

    public void setUnits(Map<String, Units> units) {
        this.units = units;
    }

    public void initUnits() {
        units = new HashMap<>();
        units.put(JSON_MASS, Units.WEIGHT_SUN);
        units.put(JSON_RADIUS, Units.DISTANCE_SUN_RAD);
        units.put(JSON_RAD_ERR1, Units.DISTANCE_SUN_RAD);
        units.put(JSON_RAD_ERR2, Units.DISTANCE_SUN_RAD);
        units.put(JSON_EFFECTIVE_TEMP, Units.TEMP_KELVIN);
        units.put(JSON_DISTANCE, Units.DISTANCE_PC);
    }


    public JSONObject parseToJson() {
        JSONObject json = new JSONObject();
        json.putOnce(JSON_ID, id);
        json.putOnce(JSON_EPIC, epic);
        json.putOnce(JSON_MASS, mass);
        json.putOnce(JSON_RADIUS, radius);
        json.putOnce(JSON_RAD_ERR1, rad_err1);
        json.putOnce(JSON_RAD_ERR2, rad_err2);
        json.putOnce(JSON_EFFECTIVE_TEMP, effectiveTemp);
        json.putOnce(JSON_DISTANCE, distance);
        json.put(JSON_UNITS, Units.parseUnitMap(units));

        return json;
    }

    public static Star createFromJson(JSONObject json) {
        int epic = -1;
        double mass = -1;
        double radius = -1;
        double rad_err1 = -1;
        double rad_err2 = -1;
        double effectiveTemp = -1;
        double distance = -1;
        Long id = null;

        if (json.has(JSON_ID)) {
            id = json.getLong(JSON_ID);
        }
        if(!json.isNull(JSON_EPIC))
            epic = json.getInt(JSON_EPIC);
        if(!json.isNull(JSON_MASS))
            mass = json.getDouble(JSON_MASS);
        if(!json.isNull(JSON_RADIUS))
            radius = json.getDouble(JSON_RADIUS);
        if(!json.isNull(JSON_RAD_ERR1))
            rad_err1 = json.getDouble(JSON_RAD_ERR1);
        if(!json.isNull(JSON_RAD_ERR2))
            rad_err2 = json.getDouble(JSON_RAD_ERR2);
        if(!json.isNull(JSON_EFFECTIVE_TEMP))
            effectiveTemp = json.getDouble(JSON_EFFECTIVE_TEMP);
        if(!json.isNull(JSON_DISTANCE))
            distance = json.getDouble(JSON_DISTANCE);

        Star star = new Star(epic, mass, radius, rad_err1, rad_err2, effectiveTemp, distance);

        Map<String, Units> units = Units.convertToUnitMap(json.getJSONObject(JSON_UNITS));
        star.setUnits(units);

        return star;
    }

    public static Map<String, Object> getMetaDataStar(int epic) {
        Map<String, Object> result = new HashMap<>();

        Client client = Client.create();
        WebResource resource = client.resource("https://exoplanetarchive.ipac.caltech.edu/cgi-bin/nstedAPI/nph-nstedAPI?table=k2targets&format=json&order=epic_number&select=epic_number,k2_mass,k2_rad,k2_type,k2_raderr1,k2_raderr2,k2_teff,k2_dist&where=epic_number='" + epic + "'");
        ClientResponse response = resource.accept("application/data.lightcurves.json").get(ClientResponse.class);
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        String str = response.getEntity(String.class);
        JSONArray arr = new JSONArray(str);
        JSONObject json = (JSONObject) arr.get(0);

        double starMass = json.getDouble("k2_mass");
        double starRadius = json.getDouble("k2_rad");
        double starRadiusError1 = json.getDouble("k2_raderr1");
        double starRadiusError2 = json.getDouble("k2_raderr2");
        double starEffectiveTemperature = -1;
        if (!json.isNull("k2_teff")) {
            starEffectiveTemperature = json.getDouble("k2_teff");
        }
        double starDistance = -1;
        if (!json.isNull("k2_dist")) {
            starDistance = json.getDouble("k2_dist");
        }

        result.put("epic", epic);
        result.put("mass", starMass);
        result.put("radius", starRadius);
        result.put("radius_err1", starRadiusError1);
        result.put("radius_err2", starRadiusError2);
        result.put("effectiveTemp", starEffectiveTemperature);
        result.put("distance", starDistance);

        return result;
    }
}
