package persistence;

import user.AppointerPermission;

import javax.persistence.*;
import java.util.List;

public class AppointerPermissionDAO {
    private static EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("TradingSystem");

    public static AppointerPermission getById(int id) throws Exception {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        String query = "select c from AppointerPermission c where c.id = :id";
        TypedQuery<AppointerPermission> tq = em.createQuery(query, AppointerPermission.class);
        tq.setParameter("id", id);
        AppointerPermission permission = null;
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

    public static void add(AppointerPermission permission) throws Exception {
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

    public static List<AppointerPermission> getAll() throws Exception {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        String query = "select c from ManagerPermission c where c.id is not null";
        TypedQuery<AppointerPermission> tq = em.createQuery(query, AppointerPermission.class);
        List<AppointerPermission> list;
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
        AppointerPermission permission = null;
        try{
            et = em.getTransaction();
            et.begin();
            permission = em.find(AppointerPermission.class, id);
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
