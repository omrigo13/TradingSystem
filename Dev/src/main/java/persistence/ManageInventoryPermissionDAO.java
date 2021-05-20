package persistence;


import user.ManageInventoryPermission;

import javax.persistence.*;
import java.util.List;

public class ManageInventoryPermissionDAO {
    private static EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("TradingSystem");

    public static ManageInventoryPermission getById(int id) throws Exception {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        String query = "select c from AppointerPermission c where c.id = :id";
        TypedQuery<ManageInventoryPermission> tq = em.createQuery(query, ManageInventoryPermission.class);
        tq.setParameter("id", id);
        ManageInventoryPermission permission = null;
        try{
            permission = tq.getSingleResult();
            return permission;
        }
        catch (NoResultException e){
            e.printStackTrace();
        }
        finally {
            em.close();
        }
        return null;
    }

    public static void add(ManageInventoryPermission permission) throws Exception {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
        try{
            et = em.getTransaction();
            et.begin();
            em.persist(permission);
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

    public static List<ManageInventoryPermission> getAll() throws Exception {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        String query = "select c from ManagerPermission c where c.id is not null";
        TypedQuery<ManageInventoryPermission> tq = em.createQuery(query, ManageInventoryPermission.class);
        List<ManageInventoryPermission> list;
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
        ManageInventoryPermission permission = null;
        try{
            et = em.getTransaction();
            et.begin();
            permission = em.find(ManageInventoryPermission.class, id);
            em.remove(permission);
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
