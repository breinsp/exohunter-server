package facades;

import entities.LADataPoint;
import org.json.JSONArray;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class LADataPointFacade {
    @PersistenceContext
    EntityManager em;

    public List<LADataPoint> findAll() {
        return em.createNamedQuery("LADataPoint.findAll").getResultList();
    }

    public LADataPoint findById(Long id) {
        List<LADataPoint> dps = em.createNamedQuery("LADataPoint.findById").setParameter("id", id).getResultList();
        if (dps.size() >= 0) {
            return dps.get(0);
        } else {
            return null;
        }
    }

    public void save(LADataPoint dp) {
        em.persist(dp);
    }

    public void saveRange(List<LADataPoint> dps) {
        for (LADataPoint dp : dps) {
            em.persist(dp);
        }
    }

    public JSONArray listOfObjectToJSON(List<LADataPoint> list) {
        JSONArray arr = new JSONArray();
        for (LADataPoint dp : list) {
            arr.put(dp.parseToJson());
        }
        return arr;
    }
}
