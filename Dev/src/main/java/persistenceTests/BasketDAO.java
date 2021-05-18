package persistenceTests;

import user.Basket;

import javax.persistence.*;

public class BasketDAO {
    private static EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("TradingSystem");

//    public Basket getById(int id) throws Exception {
//        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
//        String query = "select c from Basket c where c.id = :id";
//        TypedQuery<Basket> tq = em.createQuery(query, Basket.class);
//        tq.setParameter("id", id);
//        Basket basket = null;
//        try{
//            basket = tq.getSingleResult();
//            return basket;
//        }
//        catch (NoResultException e){
//            e.printStackTrace();
//        }
//        finally {
//            em.close();
//        }
//        return null;
//    }

    public void add(Basket basket) throws Exception {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
        try{
            et = em.getTransaction();
            et.begin();
            em.persist(basket);
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

//    public List<Basket> getAll() throws Exception {
//        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
//        String query = "select c from Basket c where c.id is not null";
//        TypedQuery<Basket> tq = em.createQuery(query, Basket.class);
//        List<Basket> list;
//        try{
//            list = tq.getResultList();
//            return list;
//        }
//        catch(NoResultException e){
//            e.printStackTrace();
//        }
//        finally {
//            em.close();
//        }
//        return null;
//    }
}
