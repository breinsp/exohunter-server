package facades;

import entities.Star;
import org.json.JSONArray;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class StarFacade {
    @PersistenceContext
    EntityManager em;

    public List<Star> findAll() {
        List<Star> stars = em.createNamedQuery("Star.findAll").getResultList();
        return stars;
    }

    public Star findById(Long id) {
        List<Star> stars = em.createNamedQuery("Star.findById").setParameter("id", id).getResultList();
        if (stars.size() <= 0) {
            return null;
        } else {
            return stars.get(0);
        }
    }

    public Star save(Star s) {
        List<Star> oldStars = em.createNamedQuery("Star.findByEpic").setParameter("epic", s.getEpic()).getResultList();
        if (oldStars.size() <= 0) {
            em.persist(s);
            return s;
        } else {
            return oldStars.get(0);
        }
    }

    public void saveRange(List<Star> stars) {
        for (Star s : stars) {
            em.persist(s);
        }
    }

    public JSONArray listOfObjectToJSON(List<Star> list) {
        JSONArray arr = new JSONArray();
        for (Star s : list) {
            arr.put(s.parseToJson());
        }
        return arr;
    }

}
