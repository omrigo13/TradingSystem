package robustnessTests;

import exceptions.DeliverySystemException;
import exceptions.PaymentSystemException;
import externalServices.DeliveryData;
import externalServices.DeliverySystem;

import java.util.HashMap;
import java.util.LinkedList;

public class DeliverySystemMock2 implements DeliverySystem {

    private HashMap<String, LinkedList<String>> deliveries = new HashMap<>();


    public HashMap<String, LinkedList<String>> getDeliveries() {
        return deliveries;
    }

    @Override
    public void connect() throws DeliverySystemException {
        throw new DeliverySystemException();
    }

    @Override
    public void deliver(DeliveryData data) throws DeliverySystemException {
        throw new DeliverySystemException();

//        if(!deliveries.containsKey(data.getName()))
//            deliveries.put(data.getName(), new LinkedList<>());
//        deliveries.get(data.getName()).add(data.getAddress());
    }

    @Override
    public void cancel(DeliveryData date) throws DeliverySystemException {
        throw new DeliverySystemException();

    }
}
