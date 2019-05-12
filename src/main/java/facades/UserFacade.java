package facades;

import entities.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class UserFacade {
    @PersistenceContext
    EntityManager em;

    public long createNewUser() {
        User user = new User("Bob");
        em.persist(user);
        return user.getId();
    }

    public void save(User testUser) {
        em.persist(testUser);
    }

    public User findById(Long userId) {
        List<User> user = em.createNamedQuery("User.findById").setParameter("userId", userId).getResultList();
        if (user.size() <= 0) {
            return null;
        } else {
            return user.get(0);
        }
    }
}
