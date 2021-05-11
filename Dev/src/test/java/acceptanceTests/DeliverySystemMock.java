package acceptanceTests;

import exceptions.DeliverySystemException;
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
    public boolean connect() throws DeliverySystemException {
        return false;
    }

    @Override
    public boolean deliver(DeliveryData data) throws DeliverySystemException {

        if(!deliveries.containsKey(data.getName()))
            deliveries.put(data.getName(), new LinkedList<>());
        deliveries.get(data.getName()).add(data.getAddress());
        return true;
    }
}
