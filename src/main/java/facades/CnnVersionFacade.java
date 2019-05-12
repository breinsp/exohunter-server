package facades;

import entities.CnnVersion;
import org.json.JSONArray;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class CnnVersionFacade {
    @PersistenceContext
    EntityManager em;

    public void save(CnnVersion cv) {
        em.persist(cv);
    }

    public List<CnnVersion> findAll() {
        return em.createNamedQuery("CnnVersion.findAll").getResultList();
    }

    public CnnVersion findById(Long id) {
        List<CnnVersion> cvs = em.createNamedQuery("CnnVersion.findById").setParameter("id", id).getResultList();

        if(cvs.size() > 0) {
            return cvs.get(0);
        } else {
            return null;
        }
    }

    public CnnVersion findLatestVersion() {
        CnnVersion cv = (CnnVersion)em.createNamedQuery("CnnVersion.findLatestVersion").getSingleResult();
        return cv;
    }

    public JSONArray listOfObjectToJSON(List<CnnVersion> list) {
        JSONArray arr = new JSONArray();
        for (CnnVersion cv : list) {
            arr.put(cv.parseToJson());
        }
        return arr;
    }
}
