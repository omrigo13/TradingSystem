package persistenceTests;

import user.Subscriber;

import javax.persistence.*;
import java.util.List;

public class SubscriberDAO {
    private static EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("TradingSystem");

    public Subscriber getById(int id) throws Exception {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        String query = "select c from Subscriber c where c.id = :id";
        TypedQuery<Subscriber> tq = em.createQuery(query, Subscriber.class);
        tq.setParameter("id", id);
        Subscriber subscriber = null;
        try{
            subscriber = tq.getSingleResult();
            return subscriber;
        }
        catch (NoResultException e){
            e.printStackTrace();
        }
        finally {
            em.close();
        }
        return null;
    }

    public void add(Subscriber subscriber) throws Exception {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
        try{
            et = em.getTransaction();
            et.begin();
            em.persist(subscriber);
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

    public List<Subscriber> getAll() throws Exception {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        String query = "select c from Subscriber c where c.id is not null";
        TypedQuery<Subscriber> tq = em.createQuery(query, Subscriber.class);
        List<Subscriber> list;
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
