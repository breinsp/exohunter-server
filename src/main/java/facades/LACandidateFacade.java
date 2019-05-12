package facades;

import entities.K2LACandidateMatchingResult;
import entities.LACandidate;
import org.json.JSONArray;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class LACandidateFacade {
    @PersistenceContext
    EntityManager em;
    @Inject
    LADipFacade dipFacade;

    public List<LACandidate> findAll() {
        return em.createNamedQuery("LACandidate.findAll").getResultList();
    }

    public LACandidate findById(Long id) {
        List<LACandidate> candidates = em.createNamedQuery("LACandidate.findById").setParameter("id", id).getResultList();
        if (candidates.size() <= 0) {
            return null;
        } else {
            return candidates.get(0);
        }
    }

    public void save(LACandidate c) {
        dipFacade.saveRange(c.getDips());
        c.setId(null);
        em.persist(c);
    }

    public void saveRange(List<LACandidate> candidates) {
        for (LACandidate c : candidates) {
            save(c);
        }
    }

    public JSONArray listOfObjectToJSON(List<LACandidate> list) {
        JSONArray arr = new JSONArray();
        for (LACandidate c : list) {
            arr.put(c.parseToJson());
        }
        return arr;
    }

    public void merge(LACandidate c) {
        em.merge(c);
    }
}
