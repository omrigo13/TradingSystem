package persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import store.Item;
import store.Store;
import user.Basket;
import user.Subscriber;

import javax.persistence.*;
import java.util.List;

public class Repo {

    private static Repo repo_instance = null;
    private static EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("TradingSystem");
    private static EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();

    private static SessionFactory sessionFactory;


    private Repo() {
    }


    private static Session getSession() {
        if (sessionFactory == null) {
            sessionFactory = new DatabaseConfigBuilder()
                    .buildConfiguration()
                    .buildSessionFactory();
        }

        return sessionFactory.getCurrentSession();
    }

    public List<Item> getItems() {
        String query = "select c from Item c where c.id is not null";
        TypedQuery<Item> tq = em.createQuery(query, Item.class);
        List<Item> list;
        try{
            list = tq.getResultList();
            return list;
        }
        catch(NoResultException e){
            e.printStackTrace();
        }
        finally {
//            em.close();
        }
        return null;
    }

    public List<Subscriber> getSubscribers() {
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
//            em.close();
        }
        return null;
    }

    public List<Store> getStores() {
        String query = "select c from Store c where c.id is not null";
        TypedQuery<Store> tq = em.createQuery(query, Store.class);
        List<Store> list;
        try{
            list = tq.getResultList();
            return list;
        }
        catch(NoResultException e){
            e.printStackTrace();
        }
        finally {
//            em.close();
        }
        return null;
    }

    public List<Basket> getBaskets() {
        String query = "select c from Basket c where c.id is not null";
        TypedQuery<Basket> tq = em.createQuery(query, Basket.class);
        List<Basket> list;
        try{
            list = tq.getResultList();
            return list;
        }
        catch(NoResultException e){
            e.printStackTrace();
        }
        finally {
//            em.close();
        }
        return null;
    }

    public static Repo getInstance(){
        if(repo_instance == null)
            repo_instance = new Repo();
        return repo_instance;
    }

    public static EntityManager getEm() {
        return em;
    }

    public static <T> void merge(T obj){
        EntityTransaction et = null;
        try{
            et = em.getTransaction();
            et.begin();
            em.merge(obj);
            et.commit();
        }
        catch (Exception e){
            if(et != null){
                et.rollback();
            }
            e.printStackTrace();
        }
        finally {
//            em.close();
        }
    }

    public static <T> void persist(T obj){
        EntityTransaction et = null;
        try{
            et = em.getTransaction();
            et.begin();
            em.persist(obj);
            et.commit();
        }
        catch (Exception e){
            if(et != null){
                et.rollback();
            }
            e.printStackTrace();
        }
        finally {
//            em.close();
        }
    }
}
