package persistenceTests;

import store.Store;

import javax.persistence.*;
import java.util.List;

public class StoreDAO{
    private static EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("TradingSystem");

    public Store getById(int id) throws Exception {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        String query = "select c from Store c where c.id = :id";
        TypedQuery<Store> tq = em.createQuery(query, Store.class);
        tq.setParameter("id", id);
        Store store = null;
        try{
            store = tq.getSingleResult();
            return store;
        }
        catch (NoResultException e){
            e.printStackTrace();
        }
        finally {
            em.close();
        }
        return null;
    }

    public void add(Store store) throws Exception {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
        try{
            et = em.getTransaction();
            et.begin();
            em.persist(store);
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

    public List<Store> getAll() throws Exception {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
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
            em.close();
        }
        return null;
    }

    public void deleteById(int id) throws Exception {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
        Store store = null;
        try{
            et = em.getTransaction();
            et.begin();
            store = em.find(Store.class, id);
            em.remove(store);
            et.commit();
        }
        catch (Exception e){
            if(et!=null){
                et.rollback();
            }
            e.printStackTrace();
        }
        finally {
            em.close();
        }
    }

}
