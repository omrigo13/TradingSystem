package externalServices;

public interface PaymentSystem {
    boolean pay(PaymentData data);

    void payBack(PaymentData data);
}
