package persistenceTests;

import store.Inventory;
import store.Item;
import user.OwnerPermission;

import javax.persistence.*;
import java.util.List;

public class OwnerPermissionDAO {
    private static EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("TradingSystem");

    public OwnerPermission getById(int id) throws Exception {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        String query = "select c from OwnerPermission c where c.id = :id";
        TypedQuery<OwnerPermission> tq = em.createQuery(query, OwnerPermission.class);
        tq.setParameter("id", id);
        OwnerPermission ownerPermission = null;
        try{
            ownerPermission = tq.getSingleResult();
            return ownerPermission;
        }
        catch (NoResultException e){
            e.printStackTrace();
        }
        finally {
            em.close();
        }
        return null;
    }

    public void add(OwnerPermission ownerPermission) throws Exception {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
        try{
            et = em.getTransaction();
            et.begin();
            em.persist(ownerPermission);
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

    public List<OwnerPermission> getAll() throws Exception {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        String query = "select c from OwnerPermission c where c.id is not null";
        TypedQuery<OwnerPermission> tq = em.createQuery(query, OwnerPermission.class);
        List<OwnerPermission> list;
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
        OwnerPermission ownerPermission = null;
        try{
            et = em.getTransaction();
            et.begin();
            ownerPermission = em.find(OwnerPermission.class, id);
            em.remove(ownerPermission);
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
