package entities;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@NamedQueries({
        @NamedQuery(name = "ExoplanetStat.findLatest", query="select es from ExoplanetStat es where es.timeStamp=(select max(esl.timeStamp) from ExoplanetStat esl)")
})
public class ExoplanetStat {
    private static final String JSON_ID = "id";
    private static final String JSON_TIMESTAMP = "timeStamp";
    private static final String JSON_EXOPLANET_STAT_ROWS = "exoplanetStatRows";
    public static final String URL_GET_WITH_YEAR = "https://exoplanetarchive.ipac.caltech.edu/cgi-bin/nstedAPI/nph-nstedAPI?table=multiexopars&format=csv&select=mpl_hostname,mpl_letter,mpl_reflink,mpl_discmethod";
    public static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    LocalDateTime timeStamp;
    @OneToMany(mappedBy = "exoplanetStat", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    List<ExoplanetStatRow> exoplanetStatRows;

    public ExoplanetStat() {
    }

    public ExoplanetStat(LocalDateTime timeStamp, List<ExoplanetStatRow> exoplanetStatRows) {
        this.timeStamp = timeStamp;
        this.exoplanetStatRows = exoplanetStatRows;
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

    public List<ExoplanetStatRow> getExoplanetStatRows() {
        return exoplanetStatRows;
    }

    public void setExoplanetStatRows(List<ExoplanetStatRow> exoplanetStatRows) {
        this.exoplanetStatRows = exoplanetStatRows;
    }

    public JSONObject parseToJson() {
        JSONObject json = new JSONObject();

        json.put(JSON_ID, id);
        json.put(JSON_TIMESTAMP, timeStamp.format(dtf));

        JSONArray arr = new JSONArray();
        for(ExoplanetStatRow r: exoplanetStatRows) {
            arr.put(r.parseToJson());
        }
        json.put(JSON_EXOPLANET_STAT_ROWS, arr);

        return json;
    }
}
