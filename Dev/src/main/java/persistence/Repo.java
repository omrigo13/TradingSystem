package persistence;

import authentication.UserAuthentication;
import policies.DiscountPolicy;
import policies.PurchasePolicy;
import store.Item;
import store.Store;
import user.Basket;
import user.Subscriber;
import user.Visitors;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Repo {

    private static Repo repo = null;
    private static EntityManager em;
    private static String persistence_unit = "TradingSystemTest";

    public static void setPersistence_unit(String persistence_unit) {
        Repo.persistence_unit = persistence_unit;
    }

    private Repo() {
    }

    public static void set(Repo repo, EntityManager em, EntityTransaction et) {
        Repo.repo = repo;
        Repo.em = em;
    }

    public static List<Item> getItems() {
        String query = "select c from Item c where c.id is not null";
        TypedQuery<Item> tq = em.createQuery(query, Item.class);
        List<Item> list;
        try{
            list = tq.getResultList();
            return list;
        }
        catch(NoResultException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Subscriber> getSubscribers() {
        String query = "select c from Subscriber c where c.id is not null";
        TypedQuery<Subscriber> tq = em.createQuery(query, Subscriber.class);
        List<Subscriber> list;
        try{
            list = tq.getResultList();
            return list;
        }
        catch(NoResultException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Store> getStores() {
        String query = "select c from Store c where c.id is not null";
        TypedQuery<Store> tq = em.createQuery(query, Store.class);
        List<Store> list;
        try{
            list = tq.getResultList();
            return list;
        }
        catch(NoResultException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Basket> getBaskets() {
        String query = "select c from Basket c where c.id is not null";
        TypedQuery<Basket> tq = em.createQuery(query, Basket.class);
        List<Basket> list;
        try{
            list = tq.getResultList();
            return list;
        }
        catch(NoResultException e) {
            throw new RuntimeException(e);
        }
    }

    public static Repo getInstance(){
        if(repo == null) {
            repo = new Repo();
        }
        return repo;
    }

    public static EntityManager getEm() {
        if (em == null)
            em = Persistence.createEntityManagerFactory(persistence_unit).createEntityManager();

        return em;
    }

    public static <T> void merge(T obj){
        EntityTransaction et = null;
        try{
            et = getEm().getTransaction();
            et.begin();
            getEm().merge(obj);
            et.commit();
        }
        catch (Exception e) {
            if(et != null)
                et.rollback();
            throw new RuntimeException(e);
        }
    }

    public static <T> void persist(T obj){
        EntityTransaction et = null;
        try{
            et = getEm().getTransaction();
            et.begin();
            getEm().persist(obj);
            et.commit();
        }
        catch (Exception e) {
            if(et != null)
                et.rollback();
            throw new RuntimeException(e);
        }
    }

    public static boolean isDBEmpty(){
        getInstance();
        getEm();
        String query = "select c from Store c where c.id is not null";
        TypedQuery<Store> tq = em.createQuery(query, Store.class);
        List<Store> list;
        try{
            list = tq.getResultList();
            if(list.size() == 0)
                return true;
            return false;
        }
        catch(NoResultException e) {
            return true;
        }
    }

    public static UserAuthentication getAuthentication() {
        String query = "select c from UserAuthentication c where c.id is not null";
        TypedQuery<UserAuthentication> tq = em.createQuery(query, UserAuthentication.class);
        UserAuthentication ua = null;
        try{
            ua = tq.getSingleResult();
            return ua;
        }
        catch(NoResultException e) {
            throw new RuntimeException(e);
        }
    }
    public static Visitors getVisitors() {
        String query = "select c from Visitors c where c.id is not null";
        TypedQuery<Visitors> tq = em.createQuery(query, Visitors.class);
        Visitors vis = null;
        try{
            vis = tq.getSingleResult();
            return vis;
        }
        catch(NoResultException e) {
            throw new RuntimeException(e);
        }
    }

    public static ConcurrentHashMap<Integer, PurchasePolicy> getPurchasePolicies() {
        String query = "select c from PurchasePolicy c where c.id is not null";
        TypedQuery<PurchasePolicy> tq = em.createQuery(query, PurchasePolicy.class);
        List<PurchasePolicy> list = new LinkedList<>();
        ConcurrentHashMap<Integer, PurchasePolicy> map = new ConcurrentHashMap<>();
        try{
            list = tq.getResultList();
            for (PurchasePolicy p: list) {
                map.put(p.getPurchase_id(), p);
            }
            return map;
        }
        catch(NoResultException e) {
            throw new RuntimeException(e);
        }
    }

    public static ConcurrentHashMap<Integer, DiscountPolicy> getDiscountPolicies() {
        String query = "select c from DiscountPolicy c where c.id is not null";
        TypedQuery<DiscountPolicy> tq = em.createQuery(query, DiscountPolicy.class);
        List<DiscountPolicy> list = new LinkedList<>();
        ConcurrentHashMap<Integer, DiscountPolicy> map = new ConcurrentHashMap<>();
        try{
            list = tq.getResultList();
            for (DiscountPolicy p: list) {
                map.put(p.getDiscount_id(), p);
            }
            return map;
        }
        catch(NoResultException e) {
            throw new RuntimeException(e);
        }
    }
}
