package facades;

import entities.TrainData;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class TrainDataFacade {

    @PersistenceContext
    EntityManager em;

    public TrainData findByStar(int epic) {
        List<TrainData> tds = em.createNamedQuery("TrainData.findByEpic").setParameter("epic", epic).getResultList();

        if(tds.size() > 0) {
            return tds.get(0);
        } else {
            return null;
        }
    }

    public void save(TrainData td) {
        em.persist(td);
    }

    public void merge(TrainData td) {
        em.merge(td);
    }
}
