package externalServices;

import exceptions.DeliverySystemException;

public class DeliverySystemBasicImpl implements DeliverySystem {

    @Override
    public boolean connect() throws DeliverySystemException {
        return false;
    }

    @Override
    public boolean deliver(DeliveryData data) {
        return true;
    }
}
