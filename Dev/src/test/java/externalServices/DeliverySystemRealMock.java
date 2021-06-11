package externalServices;

import exceptions.DeliverySystemException;

public class DeliverySystemRealMock implements DeliverySystem {

    private DeliverySystemBasicImpl deliverySystem;
    private long startTime;
    private long endTime;
    private boolean fake;

    public DeliverySystemRealMock() throws DeliverySystemException {
        deliverySystem = new DeliverySystemBasicImpl();
        fake = false;
    }
    @Override
    public void connect() throws DeliverySystemException {
        startTime = System.nanoTime();
    }

    @Override
    public void deliver(DeliveryData data) throws DeliverySystemException {
        if(fake)
            throw new DeliverySystemException();
        deliverySystem.deliver(data);
        endTime = System.nanoTime();
    }

    @Override
    public void cancel(DeliveryData date) throws DeliverySystemException {
        if(!fake) {
            deliverySystem.cancel(date);
            endTime = System.nanoTime();
        }
    }

    public long getTime() { return (this.endTime - this.startTime) / 1000000; }

    public void setFake(boolean fake) {this.fake = fake;}
}
