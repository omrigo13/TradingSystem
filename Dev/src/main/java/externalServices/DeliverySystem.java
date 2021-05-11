package externalServices;

import exceptions.DeliverySystemException;

public interface DeliverySystem {

    void connect() throws DeliverySystemException;

    void deliver(DeliveryData data) throws DeliverySystemException;
}
