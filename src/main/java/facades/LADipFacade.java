package facades;

import entities.LADip;
import org.json.JSONArray;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class LADipFacade {
    @PersistenceContext
    EntityManager em;

    @Inject
    LADataPointFacade laDataPointFacade;

    public List<LADip> findAll() {
        List<LADip> list = em.createNamedQuery("LADip.findAll").getResultList();
        //list.stream().forEach((LADip d) -> d.reCreatePersistenceOfList());
        return list;
    }

    public LADip findById(Long id) {
        List<LADip> dips = em.createNamedQuery("LADip.findById").setParameter("id", id).getResultList();
        if (dips.size() <= 0) {
            return null;
        } else {
            //dips.get(0).reCreatePersistenceOfList();
            return dips.get(0);
        }
    }

    public void save(LADip d) {
        d.setId(null);
        d.preparePersistenceOfLists();
        laDataPointFacade.saveRange(d.getDataPoints());
        em.persist(d);
    }

    public void saveRange(List<LADip> dips) {
        for (LADip d : dips) {
            save(d);
        }
    }

    public JSONArray listOfObjectToJSON(List<LADip> list) {
        JSONArray arr = new JSONArray();
        for (LADip d : list) {
            arr.put(d.parseToJson());
        }
        return arr;
    }
}
