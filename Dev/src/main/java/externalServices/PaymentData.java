package externalServices;

public class PaymentData {


    private final String card_number;
    private final int month;
    private final int year;
    private final String holder;
    private final String ccv;
    private final String id;
    private double paymentValue;
    private int transactionId;

    public PaymentData() { //TODO should be removed after fix purchase cart
        card_number = "";
        month = 0;
        year = 0;
        holder = "";
        ccv = "";
        id = "";
    }

    public PaymentData(String card_number, int month, int year, String holder, String ccv, String id) {
        this.card_number = card_number;
        this.month = month;
        this.year = year;
        this.holder = holder;
        this.ccv = ccv;
        this.id = id;
        this.paymentValue = 0;
    }

    public double getPaymentValue() {
        return paymentValue;
    }

    public String getCard_number() {
        return card_number;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public String getHolder() {
        return holder;
    }

    public String getCcv() {
        return ccv;
    }

    public String getId() {
        return id;
    }

    public void setPaymentValue(double paymentValue) { this.paymentValue = paymentValue; }

    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }

    public int getTransactionId() { return transactionId; }
}
