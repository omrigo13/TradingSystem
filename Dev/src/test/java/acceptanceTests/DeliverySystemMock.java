package acceptanceTests;

import externalServices.DeliveryData;
import externalServices.DeliverySystem;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

public class DeliverySystemMock implements DeliverySystem {

//    private Collection<String> deliveries = new LinkedList<>();
//    private String address; //last address delivered to

    private HashMap<String, LinkedList<String>> deliveries = new HashMap<>();


    public HashMap<String, LinkedList<String>> getDeliveries() {
        return deliveries;
    }

    @Override
    public boolean deliver(DeliveryData data) throws Exception{

        if(!deliveries.keySet().contains(data.getUsername()))
            deliveries.put(data.getUsername(), new LinkedList<>());
        deliveries.get(data.getUsername()).add(data.getAddress());
        return true;
    }
}
