package externalServices;

import exceptions.DeliverySystemException;

public class DeliverySystemFake implements DeliverySystem {

    private boolean fake;

    public DeliverySystemFake() {
        fake = false;
    }

    @Override
    public void connect() throws DeliverySystemException { }

    @Override
    public void deliver(DeliveryData data) throws DeliverySystemException {
        if(fake)
            throw new DeliverySystemException();
    }

    @Override
    public void cancel(DeliveryData date) throws DeliverySystemException { }

    public void setFake(boolean fake) {this.fake = fake;}
}
