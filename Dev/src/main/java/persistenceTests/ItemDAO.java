package persistenceTests;

import store.Item;

import javax.persistence.*;
import java.util.List;

public class ItemDAO {
    private static EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("TradingSystem");

    public Item getById(int id) throws Exception {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        String query = "select c from Item c where c.id = :id";
        TypedQuery<Item> tq = em.createQuery(query, Item.class);
        tq.setParameter("id", id);
        Item item = null;
        try{
            item = tq.getSingleResult();
            return item;
        }
        catch (NoResultException e){
            e.printStackTrace();
        }
        finally {
            em.close();
        }
        return null;
    }

    public void add(Item item) throws Exception {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
        try{
            et = em.getTransaction();
            et.begin();
            em.persist(item);
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

    public List<Item> getAll() throws Exception {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
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
            em.close();
        }
        return null;
    }
}
