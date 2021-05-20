package persistence;


import user.GetHistoryPermission;

import javax.persistence.*;
import java.util.List;

public class GetHistoryPermissionDAO {
    private static EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("TradingSystem");

    public static GetHistoryPermission getById(int id) throws Exception {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        String query = "select c from AppointerPermission c where c.id = :id";
        TypedQuery<GetHistoryPermission> tq = em.createQuery(query, GetHistoryPermission.class);
        tq.setParameter("id", id);
        GetHistoryPermission permission = null;
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

    public static void add(GetHistoryPermission permission) throws Exception {
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

    public static List<GetHistoryPermission> getAll() throws Exception {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        String query = "select c from ManagerPermission c where c.id is not null";
        TypedQuery<GetHistoryPermission> tq = em.createQuery(query, GetHistoryPermission.class);
        List<GetHistoryPermission> list;
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
        GetHistoryPermission permission = null;
        try{
            et = em.getTransaction();
            et.begin();
            permission = em.find(GetHistoryPermission.class, id);
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
