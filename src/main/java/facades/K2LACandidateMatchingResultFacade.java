package facades;


import entities.K2LACandidateMatchingResult;
import org.json.JSONArray;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class K2LACandidateMatchingResultFacade {
    @PersistenceContext
    EntityManager em;

    public List<K2LACandidateMatchingResult> findAll() {
        List<K2LACandidateMatchingResult> res = em.createNamedQuery("K2LACandidateMatchingResult.findAll").getResultList();
        return res;
    }

    public K2LACandidateMatchingResult findById(Long id) {
        List<K2LACandidateMatchingResult> candidates = em.createNamedQuery("K2LACandidateMatchingResult.findById").setParameter("id", id).getResultList();
        if (candidates.size() <= 0) {
            return null;
        } else {
            return candidates.get(0);
        }
    }

    public K2LACandidateMatchingResult findByCandidateName(String candidateName) {
        List<K2LACandidateMatchingResult> candidates = em.createNamedQuery("K2LACandidateMatchingResult.findByCandidateName").setParameter("candidateName", candidateName).getResultList();
        if (candidates.size() <= 0) {
            return null;
        } else {
            return candidates.get(0);
        }
    }

    public List<K2LACandidateMatchingResult> findAllByStar(int epic) {
        return em.createNamedQuery("K2LACandidateMatchingResult.findByStar").setParameter("epic", epic).getResultList();
    }

    public void save(K2LACandidateMatchingResult r) {
        em.persist(r);
    }

    public JSONArray listOfObjectToJSON(List<K2LACandidateMatchingResult> list) {
        JSONArray arr = new JSONArray();
        for (K2LACandidateMatchingResult c : list) {
            arr.put(c.parseToJson());
        }
        return arr;
    }

    public void merge(K2LACandidateMatchingResult c) {
        em.merge(c);
    }

    public void remove(K2LACandidateMatchingResult r) {
        em.remove(r);
    }

}
