package externalServices;

public class PaymentSystemBasicImpl implements PaymentSystem {

    @Override
    public boolean pay(PaymentData data) {
        return true;
    }

    @Override
    public void payBack(PaymentData data) {

    }
}
