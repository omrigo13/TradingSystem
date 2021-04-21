package acceptanceTests;

import externalServices.DeliveryData;
import externalServices.DeliverySystem;

public class DeliverySystemMock implements DeliverySystem {
    @Override
    public boolean deliver(DeliveryData data) {
        return false;
    }
}
