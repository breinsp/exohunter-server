package facades;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import entities.ConfirmedExoStat;
import entities.ExoplanetStat;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Stateless
public class ConfirmedExoStatFacade {

    @PersistenceContext
    EntityManager em;

    public void saveRange(List<ConfirmedExoStat> list) {
        for (ConfirmedExoStat e: list) {
            em.persist(e);
        }
    }

    public void save(ConfirmedExoStat c) {
        em.persist(c);
    }

    public static List<ConfirmedExoStat> loadAndPersistData() {
        Client client = Client.create();
        System.out.println(ConfirmedExoStat.URL_GET_DATA);
        WebResource resource = client.resource(ConfirmedExoStat.URL_GET_DATA);
        ClientResponse response = resource.accept("application/json").get(ClientResponse.class);
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        String str = response.getEntity(String.class);
        String[] lines = str.split("\n");
        String[] parts = null;
        List<ConfirmedExoStat> confirmedExoStatList = new LinkedList<>();

        for (int i = 1; i < lines.length; i++) {

            try {
                System.out.println(lines[i]);
                parts = lines[i].split("[,]");
                LocalDateTime timeStamp = LocalDateTime.parse(LocalDateTime.now().format(ConfirmedExoStat.dtf), ConfirmedExoStat.dtf);
                String hostName = parts[0];
                String letter = parts[1];
                String discoveryMethod = parts[2];
                Integer planetNumber = parseCheckValInteger(parts[3]);
                Double orbitalPeriod = parseCheckValDouble(parts[4]);
                Double jupiterMass = parseCheckValDouble(parts[5]);
                Double jupiterRadius = parseCheckValDouble(parts[6]);
                Integer keplerFlag = parseCheckValInteger(parts[7]);
                Integer k2Flag = parseCheckValInteger(parts[8]);
                String bodyName = parts[9];
                Double equiTemp = null;
                if (parts.length >= 11)
                    equiTemp = parseCheckValDouble(parts[10]);
                Double earthRadius = null;
                if (parts.length >= 12)
                    earthRadius = parseCheckValDouble(parts[11]);

                ConfirmedExoStat c = new ConfirmedExoStat(
                        timeStamp,
                        hostName,
                        letter,
                        discoveryMethod,
                        planetNumber,
                        orbitalPeriod,
                        jupiterMass,
                        jupiterRadius,
                        keplerFlag,
                        k2Flag,
                        bodyName,
                        equiTemp,
                        earthRadius);
                confirmedExoStatList.add(c);
                System.out.println("OK");
            } catch (Exception ex) {
                System.out.println(parts);
                System.err.println("ERROR!!!");
            }
        }
        return confirmedExoStatList;
    }

    public static Double parseCheckValDouble(String val) {
        if (!val.isEmpty()) {
            return new Double(val);
        }
        return null;
    }

    public static Integer parseCheckValInteger(String val) {
        if (!val.isEmpty()) {
            return new Integer(val);
        }
        return null;
    }
}
