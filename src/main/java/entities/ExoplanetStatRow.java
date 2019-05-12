package entities;

import org.json.JSONObject;

import javax.persistence.*;

@Entity
@NamedQueries({
        @NamedQuery(name = "ExoplanetStatRow.findCntPerYear", query = "select esr.year, sum(esr.count) from ExoplanetStatRow esr  where esr.exoplanetStat.id=:id group by esr.year"),
        @NamedQuery(name = "ExoplanetStatRow.findCntPerYearAndMethod", query = "select esr.year, esr.method, sum(esr.count) from ExoplanetStatRow esr  where esr.exoplanetStat.id=:id group by esr.year, esr.method"),
        @NamedQuery(name = "ExoplanetStatRow.findCntOfConfirmed", query = "select sum(esr.count) from ExoplanetStatRow esr where esr.exoplanetStat.id=:id")
})
public class ExoplanetStatRow {
    private static final String JSON_ID = "id";
    private static final String JSON_YEAR = "year";
    private static final String JSON_METHOD = "method";
    private static final String JSON_COUNT = "count";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    int year;
    String method;
    int count;
    @ManyToOne
    ExoplanetStat exoplanetStat;

    public ExoplanetStatRow() {
    }

    public ExoplanetStatRow(int year, String method, int count) {
        this.year = year;
        this.method = method;
        this.count = count;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ExoplanetStat getExoplanetStat() {
        return exoplanetStat;
    }

    public void setExoplanetStat(ExoplanetStat exoplanetStat) {
        this.exoplanetStat = exoplanetStat;
    }

    public JSONObject parseToJson() {
        JSONObject json = new JSONObject();

        json.put(JSON_ID, id);
        json.put(JSON_YEAR, year);
        json.put(JSON_METHOD, method);
        json.put(JSON_COUNT, count);

        return json;
    }
}
