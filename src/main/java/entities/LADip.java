package entities;

import org.json.JSONArray;
import org.json.JSONObject;
import enums.Units;

import javax.persistence.*;
import java.util.*;

/**
 * Created by Julian Hautzmayer on 19.08.2017.
 * The dip represents an unusual pattern in the light curve of a star which could be a transiting exoplanet.
 * Multiple properties can be determined from the dip like deltaF, tT or tF.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "LADip.findAll", query = "select d from LADip d"),
        @NamedQuery(name = "LADip.findById", query = "select d from LADip d where d.id=:id")
})
public class LADip {
    private static final String JSON_ID = "id";
    private static final String JSON_START_POINT = "startPoint";
    private static final String JSON_END_POINT = "endPoint";
    private static final String JSON_DELTA_FLUX = "deltaFlux";
    private static final String JSON_FULL_TIME = "fullTime";
    private static final String JSON_TOTAL_TIME = "totalTime";
    private static final String JSON_IS_MODIFIED = "isModified";
    private static final String JSON_CHAR_LIST = "charList";
    private static final String JSON_XS_IN = "r_x";
    private static final String JSON_XS_OUT = "xs";
    private static final String JSON_FLUXES_IN = "r_y";
    private static final String JSON_FLUXES_OUT = "fluxes";
    private static final String JSON_GLOBAL_START_INDEX = "globalStartIndex";
    private static final String JSON_GLOBAL_END_INDEX = "globalEndIndex";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Double startPoint;       //starting x of the dip
    Double endPoint;         //ending x of the dip
    Integer globalStartIndex;
    Integer globalEndIndex;
    Double deltaFlux;   //the average flux blocked by an object
    Double fullTime;    //the time between point 2 to 3
    Double totalTime;   //the time between point 1 and 4
    boolean isModified;

    @Transient
    private List<String> charList;
    @Transient
    private List<Double> xs;
    @Transient
    private List<Double> fluxes;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JoinColumn(name = "la_dip_id")
    List<LADataPoint> dataPoints;

    public static final Comparator<LADip> ID_COMPERATOR = new Comparator<LADip>() {
        @Override
        public int compare(LADip d1, LADip d2) {
            return Long.compare(d1.getId(), d2.getId());
        }
    };

    public LADip() {
        charList = new LinkedList<>();
        xs = new LinkedList<>();
        fluxes = new LinkedList<>();
        dataPoints = new LinkedList<>();
        this.isModified = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Double startPoint) {
        this.startPoint = startPoint;
    }

    public Double getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Double endPoint) {
        this.endPoint = endPoint;
    }

    public Double getDeltaFlux() {
        return deltaFlux;
    }

    public void setDeltaFlux(Double deltaFlux) {
        this.deltaFlux = deltaFlux;
    }

    public List<String> getCharList() {
        return charList;
    }

    public void setCharList(List<String> charList) {
        this.charList = charList;
    }

    public Double getFullTime() {
        return fullTime;
    }

    public void setFullTime(Double fullTime) {
        this.fullTime = fullTime;
    }

    public Double getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Double totalTime) {
        this.totalTime = totalTime;
    }

    public List<Double> getXs() {
        return xs;
    }

    public void setXs(List<Double> xs) {
        this.xs = xs;
    }

    public List<Double> getFluxes() {
        return fluxes;
    }

    public void setFluxes(List<Double> fluxes) {
        this.fluxes = fluxes;
    }

    public boolean isModified() {
        return isModified;
    }

    public void setModified(boolean modified) {
        isModified = modified;
    }

    public List<LADataPoint> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(List<LADataPoint> dataPoints) {
        this.dataPoints = dataPoints;
    }

    public Integer getGlobalStartIndex() {
        return globalStartIndex;
    }

    public void setGlobalStartIndex(Integer globalStartIndex) {
        this.globalStartIndex = globalStartIndex;
    }

    public Integer getGlobalEndIndex() {
        return globalEndIndex;
    }

    public void setGlobalEndIndex(Integer globalEndIndex) {
        this.globalEndIndex = globalEndIndex;
    }

    public void preparePersistenceOfLists() {
        dataPoints = new LinkedList<>();
        for(int i = xs.size() - 1, j = charList.size() - 1; i >= 0; i--, j--) {
            LADataPoint dp = new LADataPoint();
            dp.setX(xs.get(i));
            dp.setFlux(fluxes.get(i));
            dp.setFlag((j >= 0) ? charList.get(j) : null);
            dataPoints.add(0, dp);
        }
    }

    public void reCreatePersistenceOfList() {
        xs = new LinkedList<>();
        fluxes = new LinkedList<>();
        charList = new LinkedList<>();
        for (LADataPoint dp : dataPoints) {
            xs.add(dp.getX());
            fluxes.add(dp.getFlux());
            if (dp.getFlag() != null) {
                charList.add(dp.getFlag());
            }
        }
    }

    public JSONObject parseToJson() {
        JSONObject json = new JSONObject();

        reCreatePersistenceOfList();
        json.put(JSON_ID, id);
        json.put(JSON_START_POINT, startPoint);
        json.put(JSON_END_POINT, endPoint);
        json.put(JSON_DELTA_FLUX, deltaFlux);
        json.put(JSON_FULL_TIME, fullTime);
        json.put(JSON_TOTAL_TIME, totalTime);
        json.put(JSON_IS_MODIFIED, isModified);
        json.put(JSON_CHAR_LIST, charList);
        json.put(JSON_GLOBAL_START_INDEX, globalStartIndex);
        json.put(JSON_GLOBAL_END_INDEX, globalEndIndex);
        try {
            json.put(JSON_XS_OUT, getXs());
            json.put(JSON_FLUXES_OUT, getFluxes());
        } catch (Exception e) {
            json.put(JSON_XS_OUT, new LinkedList<>());
            json.put(JSON_FLUXES_OUT, new LinkedList<>());
        }

        return json;
    }

    public static LADip createFromJson(JSONObject json) {
        Long id = -1L;
        Double startPoint = -1.;
        Double endPoint = -1.;
        Double deltaFlux = -1.;
        Double fullTime = -1.;
        Double totalTime = -1.;
        boolean isModified = false;
        Integer globalStartIndex = -1;
        Integer globalEndIndex = -1;
        List<Double> xs = new LinkedList<>();
        List<Double> fluxes = new LinkedList<>();
        List<String> charList = new LinkedList<>();

        if (!json.isNull(JSON_ID))
            id = json.getLong(JSON_ID);
        if (!json.isNull(JSON_START_POINT))
            startPoint = json.getDouble(JSON_START_POINT);
        if (!json.isNull(JSON_END_POINT))
            endPoint = json.getDouble(JSON_END_POINT);
        if (!json.isNull(JSON_DELTA_FLUX))
            deltaFlux = json.getDouble(JSON_DELTA_FLUX);
        if (!json.isNull(JSON_FULL_TIME))
            fullTime = json.getDouble(JSON_FULL_TIME);
        if (!json.isNull(JSON_TOTAL_TIME))
            totalTime = json.getDouble(JSON_TOTAL_TIME);
        if (!json.isNull(JSON_IS_MODIFIED))
            isModified = json.getBoolean(JSON_IS_MODIFIED);
        if (!json.isNull(JSON_GLOBAL_START_INDEX))
             globalStartIndex = json.getInt(JSON_GLOBAL_START_INDEX);
        if (!json.isNull(JSON_GLOBAL_END_INDEX))
            globalEndIndex = json.getInt(JSON_GLOBAL_END_INDEX);

        JSONArray arr = null;
        if (!json.isNull(JSON_XS_IN)) {
            arr = json.getJSONArray(JSON_XS_IN);
            for (int i = 0; i < arr.length(); i++) {
                xs.add(arr.getDouble(i));
            }
        }
        if (!json.isNull(JSON_FLUXES_IN)) {
            arr = json.getJSONArray(JSON_FLUXES_IN);
            for (int i = 0; i < arr.length(); i++) {
                fluxes.add(arr.getDouble(i));
            }
        }
        if (!json.isNull(JSON_CHAR_LIST)) {
            arr = json.getJSONArray(JSON_CHAR_LIST);
            for (int i = 0; i < arr.length(); i++) {
                charList.add(arr.getString(i));
            }
        }

        LADip dip = new LADip();
        dip.setId(id);
        dip.setStartPoint(startPoint);
        dip.setEndPoint(endPoint);
        dip.setDeltaFlux(deltaFlux);
        dip.setFullTime(fullTime);
        dip.setTotalTime(totalTime);
        dip.setModified(isModified);
        dip.setGlobalStartIndex(globalStartIndex);
        dip.setGlobalEndIndex(globalEndIndex);
        dip.setXs(xs);
        dip.setFluxes(fluxes);
        dip.setCharList(charList);

        return dip;
    }

    @Override
    public String toString() {
        return "LADip{" +
                "id=" + id +
                ", startPoint=" + startPoint +
                ", endPoint=" + endPoint +
                ", deltaFlux=" + deltaFlux +
                ", fullTime=" + fullTime +
                ", totalTime=" + totalTime +
                '}';
    }
}
