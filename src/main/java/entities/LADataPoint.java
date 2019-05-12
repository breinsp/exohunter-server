package entities;

import org.json.JSONObject;

import javax.persistence.*;

@Entity
@NamedQueries({
        @NamedQuery(name = "LADataPoint.findAll", query = "select dp from LADataPoint dp"),
        @NamedQuery(name = "LADataPoint.findById", query = "select dp from LADataPoint dp where dp.id=:id")
})
public class LADataPoint {
    public static final String JSON_ID = "id";
    public static final String JSON_X = "x";
    public static final String JSON_FLUX = "flux";
    public static final String JSON_FLAG = "flag";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Double x;
    Double flux;
    String flag;

    public LADataPoint() {
    }

    public LADataPoint(Double x, Double flux, String flag) {
        this.x = x;
        this.flux = flux;
        this.flag = flag;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getFlux() {
        return flux;
    }

    public void setFlux(Double flux) {
        this.flux = flux;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "LADataPoint{" +
                "x=" + x +
                ", flux=" + flux +
                ", flag='" + flag + '\'' +
                '}';
    }

    public JSONObject parseToJson() {
        JSONObject json = new JSONObject();
        json.put(JSON_ID, id);
        json.put(JSON_X, x);
        json.put(JSON_FLUX, flux);
        json.put(JSON_FLAG, flag);

        return json;
    }
}
