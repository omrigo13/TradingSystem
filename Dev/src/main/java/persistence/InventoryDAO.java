package persistence;

import store.Inventory;
import store.Item;

import javax.persistence.*;
import java.util.List;

public class InventoryDAO {
    private static EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("TradingSystem");

    public static Inventory getById(int id) throws Exception {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        String query = "select c from Inventory c where c.id = :id";
        TypedQuery<Inventory> tq = em.createQuery(query, Inventory.class);
        tq.setParameter("id", id);
        Inventory inventory = null;
        try{
            inventory = tq.getSingleResult();
            return inventory;
        }
        catch (NoResultException e){
            e.printStackTrace();
        }
        finally {
            em.close();
        }
        return null;
    }

    public static void add(Inventory inventory) throws Exception {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
        try{
            et = em.getTransaction();
            et.begin();
            em.persist(inventory);
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

    public static List<Inventory> getAll() throws Exception {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        String query = "select c from Inventory c where c.id is not null";
        TypedQuery<Inventory> tq = em.createQuery(query, Inventory.class);
        List<Inventory> list;
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

    public static void deleteById(int id) throws Exception {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
        Inventory inventory = null;
        try{
            et = em.getTransaction();
            et.begin();
            inventory = em.find(Inventory.class, id);
            em.remove(inventory);
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
//    public void addItem(int inventoryId, Item newItem, Integer amount){
    public static void addItem(Inventory inventory){
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
//        Inventory inventory = null;
        try{
            et = em.getTransaction();
            et.begin();
            em.find(Inventory.class, inventory.getId());
//            inventory.getItems().put(newItem, amount);
//            inventory.updateItemsCounter(inventory.getItemsCounterValue());
            em.merge(inventory);
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

    public static void deleteItem(int inventoryId, Item item){
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
        Inventory inventory = null;
        try{
            et = em.getTransaction();
            et.begin();
            inventory = em.find(Inventory.class, inventoryId);
            Item itemData = em.find(Item.class, item.getId());
            inventory.getItems().remove(itemData);
            inventory.setItemsCounterValue(inventory.getItemsCounterValue()-1);
            em.persist(inventory);
            em.persist(itemData);
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