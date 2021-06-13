package persistence;

import org.testng.annotations.Test;

public class SystemInitializeFromDBTest {

    @Test
    public void initializeFromFB(){
        DatabaseFetcher fetcher = new DatabaseFetcher();
        fetcher.getSubscribers();
        fetcher.getStores();
        fetcher.getDiscountPolicies();
        fetcher.getPurchasePolicies();
        fetcher.getStoresDiscountPolicies();
        fetcher.getStoresPurchasePolicies();
        fetcher.getSubscriberIdCounter();

        System.out.println();

    }
}
