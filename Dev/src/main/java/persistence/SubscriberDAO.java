package persistence;

import store.Inventory;
import user.Subscriber;

import javax.persistence.*;
import java.util.List;

public class SubscriberDAO {
    private static EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("TradingSystem");

    public static Subscriber getById(String username) throws Exception {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        String query = "select c from Subscriber c where c.username = :username";
        TypedQuery<Subscriber> tq = em.createQuery(query, Subscriber.class);
        tq.setParameter("username", username);
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

    public static void add(Subscriber subscriber) throws Exception {
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

    public static List<Subscriber> getAll() throws Exception {
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

    public static void updateSubscriber(Subscriber subscriber){
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
        try{
            et = em.getTransaction();
            et.begin();
            em.find(Subscriber.class, subscriber.getUsername());
            em.merge(subscriber);
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

    public static void setLoginStatus(String username, boolean isLoggedIn){
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
        Subscriber subscriber = null;
        try{
            et = em.getTransaction();
            et.begin();
            subscriber = em.find(Subscriber.class, username);
            subscriber.setLoggedIn(isLoggedIn);
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
}
