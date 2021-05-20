package persistence;

import user.ManagerPermission;
import user.OwnerPermission;

import javax.persistence.*;
import java.util.List;

public class ManagerPermissionDAO {
    private static EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("TradingSystem");

    public static ManagerPermission getById(int id) throws Exception {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        String query = "select c from ManagerPermission c where c.id = :id";
        TypedQuery<ManagerPermission> tq = em.createQuery(query, ManagerPermission.class);
        tq.setParameter("id", id);
        ManagerPermission permission = null;
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

    public static void add(ManagerPermission permission) throws Exception {
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

    public static List<ManagerPermission> getAll() throws Exception {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        String query = "select c from ManagerPermission c where c.id is not null";
        TypedQuery<ManagerPermission> tq = em.createQuery(query, ManagerPermission.class);
        List<ManagerPermission> list;
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
        ManagerPermission permission = null;
        try{
            et = em.getTransaction();
            et.begin();
            permission = em.find(ManagerPermission.class, id);
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
