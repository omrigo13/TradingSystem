package externalServices;

import exceptions.DeliverySystemException;

public interface DeliverySystem {

    boolean connect() throws DeliverySystemException;

    boolean deliver(DeliveryData data) throws DeliverySystemException;
}
