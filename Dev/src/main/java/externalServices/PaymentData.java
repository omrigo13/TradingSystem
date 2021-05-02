package externalServices;

public class PaymentData {

    private double paymentValue;
    private String username;

    public String getUsername() {
        return username;
    }

    public double getPaymentValue() {
        return paymentValue;
    }

    public PaymentData(double paymentValue, String username) {
        this.paymentValue = paymentValue;
        this.username = username;
    }
}
