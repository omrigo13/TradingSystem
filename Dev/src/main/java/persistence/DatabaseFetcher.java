package persistence;

import policies.DiscountPolicy;
import policies.PurchasePolicy;
import store.Store;
import user.Subscriber;
import user.User;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class DatabaseFetcher {

    private ConcurrentHashMap<String, Subscriber> subscribers;
    private ConcurrentHashMap<Integer, Store> stores;
    private AtomicInteger subscriberIdCounter;
    private ConcurrentHashMap<Integer, PurchasePolicy> purchasePolicies;
    private ConcurrentHashMap<Integer, DiscountPolicy> discountPolicies;
    private ConcurrentHashMap<Store, Collection<Integer>> storesPurchasePolicies; //todo: why collection? store has only one purchase policy
    private ConcurrentHashMap<Store, Collection<Integer>> storesDiscountPolicies; //todo: same as with purchase policy
//    private ConcurrentHashMap<String, Map<String, Integer>> visitors; //todo - needed?


    public DatabaseFetcher() {
        Repo.getEm(); //initializing Entity Manager
        subscribers = new ConcurrentHashMap<>();
        stores = new ConcurrentHashMap<>();
        subscriberIdCounter = new AtomicInteger();
        purchasePolicies = new ConcurrentHashMap<>();
        discountPolicies = new ConcurrentHashMap<>();
        storesPurchasePolicies = new ConcurrentHashMap<>();
        storesDiscountPolicies = new ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<String, Subscriber> getSubscribers() {
        List<Subscriber> list = Repo.getSubscribers();
        for (Subscriber s:list ) {
            if(s.isLoggedIn() == true)
                s.setLoggedIn(false);
            subscribers.put(s.getUserName(), s);
        }
        return subscribers;
    }

    public ConcurrentHashMap<Integer, Store> getStores() {
        List<Store> list = Repo.getStores();
        for (Store s:list ) {
            stores.put(s.getId(), s);
        }
        return stores;
    }

    public AtomicInteger getSubscriberIdCounter() {
        List<Subscriber> list = Repo.getSubscribers();
        subscriberIdCounter.set(list.size());
        return subscriberIdCounter;
    }

    public ConcurrentHashMap<Store, Collection<Integer>> getStoresPurchasePolicies() {
        List<Store> list = Repo.getStores();
        for (Store s:list ) {
            storesPurchasePolicies.put(s, new LinkedList<>());
            storesPurchasePolicies.get(s).add(s.getPurchasePolicy().getPurchase_id());
        }
        return storesPurchasePolicies;
    }

    public ConcurrentHashMap<Store, Collection<Integer>> getStoresDiscountPolicies() {
        List<Store> list = Repo.getStores();
        for (Store s:list ) {
            storesDiscountPolicies.put(s, new LinkedList<>());
            storesDiscountPolicies.get(s).add(s.getDiscountPolicy().getDiscount_id());
        }
        return storesDiscountPolicies;
    }

    public ConcurrentHashMap<Integer, PurchasePolicy> getPurchasePolicies() {
        return purchasePolicies;
    }

    public ConcurrentHashMap<Integer, DiscountPolicy> getDiscountPolicies() {
        return discountPolicies;
    }
}
