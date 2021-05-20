package persistence;

import user.AdminPermission;
import user.ManagerPermission;

import javax.persistence.*;
import java.util.List;

public class AdminPermissionDAO {
    private static EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("TradingSystem");

    public static AdminPermission getById(int id) throws Exception {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        String query = "select c from AdminPermission c where c.id = :id";
        TypedQuery<AdminPermission> tq = em.createQuery(query, AdminPermission.class);
        tq.setParameter("id", id);
        AdminPermission permission = null;
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

    public static void add(AdminPermission permission) throws Exception {
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

    public static List<AdminPermission> getAll() throws Exception {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        String query = "select c from ManagerPermission c where c.id is not null";
        TypedQuery<AdminPermission> tq = em.createQuery(query, AdminPermission.class);
        List<AdminPermission> list;
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
        AdminPermission permission = null;
        try{
            et = em.getTransaction();
            et.begin();
            permission = em.find(AdminPermission.class, id);
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
