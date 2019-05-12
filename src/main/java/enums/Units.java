package enums;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public enum Units {
    DISTANCE_AU("au"),
    DISTANCE_KM("km"),
    DISTANCE_M("m"),
    DISTANCE_CM("cm"),
    DISTANCE_JUPITER_RAD("R_Jup"),
    DISTANCE_EARTH_RAD("R_Earth"),
    DISTANCE_SUN_RAD("R_Sun"),
    DISTANCE_PC("pc"),

    WEIGHT_KG("kg"),
    WEIGHT_SUN("M_Sun"),

    TIME_SEC("s"),
    TIME_MIN("min"),
    TIME_HOUR("h"),
    TIME_DAY("days"),
    TIME_BJD_2454833("BJD-2454833"),

    VELOCITY_KM_PER_H("km/h"),
    VELOCITY_M_PER_S("m/s"),

    TEMP_KELVIN("K"),
    TEMP_CELSIUS("°C"),

    OTHER_FLUX("flux"),
    OTHER_UNKNOWN("");

    private final String text;

    private Units(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static Units findUnitsByString(String text) {
        switch (text) {
            case "au":
                return DISTANCE_AU;
            case "km":
                return DISTANCE_KM;
            case "m":
                return DISTANCE_M;
            case "cm":
                return DISTANCE_CM;
            case "R_Jup":
                return DISTANCE_JUPITER_RAD;
            case "R_Earth":
                return DISTANCE_EARTH_RAD;
            case "R_Sun":
                return DISTANCE_SUN_RAD;
            case "pc":
                return DISTANCE_PC;
            case "kg":
                return WEIGHT_KG;
            case "M_Sun":
                return WEIGHT_SUN;
            case "s":
                return TIME_SEC;
            case "min":
                return TIME_MIN;
            case "h":
                return TIME_HOUR;
            case "days":
                return TIME_DAY;
            case "BJD-2454833":
                return TIME_BJD_2454833;
            case "km/h":
                return VELOCITY_KM_PER_H;
            case "m/s":
                return VELOCITY_M_PER_S;
            case "K":
                return TEMP_KELVIN;
            case "°C":
                return TEMP_CELSIUS;
            case "flux":
                return OTHER_FLUX;
            case "":
                return OTHER_UNKNOWN;
            default:
                return OTHER_UNKNOWN;
        }
    }

    public static JSONObject parseUnitMap(Map<String, Units> map) {
        JSONObject json = new JSONObject();
        for (String k : map.keySet()) {
            json.put(k, map.get(k).toString());
        }

        return json;
    }

    public static Map<String, Units> convertToUnitMap(JSONObject json) {
        Map<String, Units> map = new HashMap<>();
        for (String k : json.keySet()) {
            map.put(k, findUnitsByString(json.getString(k)));
        }

        return map;
    }
}
