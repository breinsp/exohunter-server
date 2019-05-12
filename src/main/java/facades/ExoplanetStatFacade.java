package facades;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import entities.ExoplanetStat;
import entities.ExoplanetStatRow;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.MathUtils;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Stateless
public class ExoplanetStatFacade {

    @PersistenceContext
    EntityManager em;

    public void save(ExoplanetStat es) {
        em.persist(es);
        for(ExoplanetStatRow r: es.getExoplanetStatRows()) {
            r.setExoplanetStat(es);
            em.persist(r);
        }
    }

    public int findCntOfConfirmed(ExoplanetStat es) {
        Long id = (Long) em.createNamedQuery("ExoplanetStatRow.findCntOfConfirmed").setParameter("id", es.getId()).getSingleResult();
        return id.intValue();
    }

    public JSONArray findCntPerYear(ExoplanetStat es) {
        JSONArray arr = new JSONArray();
        List<Object[]> res = em.createNamedQuery("ExoplanetStatRow.findCntPerYear").setParameter("id", es.getId()).getResultList();
        for(Object[] row: res) {
            int year = (int)row[0];
            Long count = (Long)row[1];
            JSONObject json = new JSONObject();
            json.put("year", year);
            json.put("count", count.intValue());
            arr.put(json);
        }
        return arr;
    }

    public JSONArray findCntPerYearAndMethod(ExoplanetStat es) {
        JSONArray arr = new JSONArray();
        List<Object[]> res = em.createNamedQuery("ExoplanetStatRow.findCntPerYearAndMethod").setParameter("id", es.getId()).getResultList();
        for(Object[] row: res) {
            int year = (int)row[0];
            String method = (String)row[1];
            Long count = (Long)row[2];
            JSONObject json = new JSONObject();
            json.put("year", year);
            json.put("method", method);
            json.put("count", count.intValue());
            arr.put(json);
        }
        return arr;
    }

    private ExoplanetStat findLatestExoplanetStat() {
        List<ExoplanetStat> es = em.createNamedQuery("ExoplanetStat.findLatest").getResultList();

        if(es.size() > 0) {
            return es.get(0);
        } else {
            return null;
        }
    }

    public ExoplanetStat findLatestExoplanetStatAndUpdate() {
        ExoplanetStat latest = findLatestExoplanetStat();
        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDate today = LocalDate.now();
        LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);

        if(latest == null || todayMidnight.isAfter(latest.getTimeStamp())) {
            latest = updateExoplanetStat();
            save(latest);
            return latest;
        }

        return latest;
    }

    public ExoplanetStat updateExoplanetStat() {
        Client client = Client.create();
        System.out.println(ExoplanetStat.URL_GET_WITH_YEAR);
        WebResource resource = client.resource(ExoplanetStat.URL_GET_WITH_YEAR);
        ClientResponse response = resource.accept("application/json").get(ClientResponse.class);
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        String str = response.getEntity(String.class);
        String[] lines = str.split("\n");

        Map<Integer, Map<String, Integer>> meta = new HashMap<>();
        List<String> usedExos = new LinkedList<>();

        for(int i = 0; i < lines.length; i++) {
            if(i==0) continue;

            lines[i] = lines[i].replaceAll("(\\.,)","");
            String[] parts = lines[i].split("[,]");

            if(usedExos.contains(parts[0] + ";" + parts[1])) {
                continue;
            } else {
                usedExos.add(parts[0] + ";" + parts[1]);
            }

            String yearString = parts[2].replace(">", "#").replace("<", "#");
            yearString = yearString.replace("\"", "");
            String[] yearParts = yearString.split("[#]");

            Pattern p = Pattern.compile("(\\s*)(\\d+)(\\s*)");
            Matcher m = p.matcher(yearParts[yearParts.length - 2]);
            while(m.find()) {
                yearString = m.group();
            }
            yearString = yearString.trim();

            int year = Integer.parseInt(yearString);
            String transitMethod = parts[3];

            System.out.println(i + " - " + year);

            if(meta.containsKey(year)) {
                Map<String, Integer> curMap = meta.get(year);

                if(curMap.containsKey(transitMethod)) {
                    curMap.replace(transitMethod, curMap.get(transitMethod) + 1);
                } else {
                    curMap.put(transitMethod, 1);
                }
            } else {
                Map<String, Integer> newMap = new HashMap<>();
                newMap.put(transitMethod, 1);

                meta.put(year, newMap);
            }
        }

        List<ExoplanetStatRow> esr = new LinkedList<>();
        for(int year: meta.keySet()) {
            for(String method: meta.get(year).keySet()) {
                ExoplanetStatRow row = new ExoplanetStatRow(year, method, meta.get(year).get(method));
                esr.add(row);
            }
        }
        ExoplanetStat es = new ExoplanetStat();
        es.setTimeStamp(LocalDateTime.parse(LocalDateTime.now().format(ExoplanetStat.dtf), ExoplanetStat.dtf));
        es.setExoplanetStatRows(esr);
        return es;
    }

    public JSONObject findCntPerYearAndMethodPerc(ExoplanetStat es) {
        Object[] result = (Object[]) em.createNativeQuery("select (select sum(count) from ExoplanetStatRow where upper(method) like upper('transit') and exoplanetStat_id=:id) as transit_cnt, " +
                "(select sum(count) from ExoplanetStatRow where upper(method) like upper('radial velocity') and exoplanetStat_id=:id) as radial_velocity_cnt, " +
                "(select sum(count) from ExoplanetStatRow where upper(method) like upper('imaging') and exoplanetStat_id=:id) as imaging_cnt, " +
                "(select sum(count) from ExoplanetStatRow where upper(method) not in (upper('transit'), upper('radial velocity'), upper('imaging')) and exoplanetStat_id=:id) as other_cnt " +
                "from dual").setParameter("id", es.getId()).getSingleResult();

        Long cnt = (Long) em.createNamedQuery("ExoplanetStatRow.findCntOfConfirmed").setParameter("id", es.getId()).getSingleResult();

        JSONObject json = new JSONObject();

        double percentageTransit = MathUtils.round(((BigDecimal)result[0]).intValue() / cnt.doubleValue() * 100, 2) ;
        double percentageRadialVelocity = MathUtils.round(((BigDecimal)result[1]).intValue() / cnt.doubleValue() * 100, 2) ;
        double percentageImaging = MathUtils.round(((BigDecimal)result[2]).intValue() / cnt.doubleValue() * 100, 2) ;
        double percentageOther =  MathUtils.round(((BigDecimal)result[3]).intValue() / cnt.doubleValue() * 100, 2) ;

        json.put("percentageTransit", percentageTransit);
        json.put("percentageRadialVelocity", percentageRadialVelocity);
        json.put("percentageImaging", percentageImaging);
        json.put("percentageOther", percentageOther);
        return json;
    }
}
