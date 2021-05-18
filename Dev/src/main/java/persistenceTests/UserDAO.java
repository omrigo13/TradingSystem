package persistenceTests;

import user.User;

import javax.persistence.*;
import java.util.List;

public class UserDAO {
    private static EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("TradingSystem");

    public User getById(int id) throws Exception {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        String query = "select c from User c where c.id = :id";
        TypedQuery<User> tq = em.createQuery(query, User.class);
        tq.setParameter("id", id);
        User user = null;
        try{
            user = tq.getSingleResult();
            return user;
        }
        catch (NoResultException e){
            e.printStackTrace();
        }
        finally {
            em.close();
        }
        return null;
    }

    public void add(User user) throws Exception {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
        try{
            et = em.getTransaction();
            et.begin();
            em.persist(user);
            et.commit();
        }
        catch (Exception e){
            if(et != null){
                et.rollback();
            }
            e.printStackTrace();
        }
        finally {
            em.close();
        }
    }

    public List<User> getAll() throws Exception {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        String query = "select c from User c where c.id is not null";
        TypedQuery<User> tq = em.createQuery(query, User.class);
        List<User> list;
        try{
            list = tq.getResultList();
            return list;
        }
        catch(NoResultException e){
            e.printStackTrace();
        }
        finally {
            em.close();
        }
        return null;
    }
}
