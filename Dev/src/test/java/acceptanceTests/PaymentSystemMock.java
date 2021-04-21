package acceptanceTests;

import exceptions.PaymentSystemException;
import externalServices.PaymentData;
import externalServices.PaymentSystem;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;

public class PaymentSystemMock implements PaymentSystem {

    private double amount;
//    private boolean isSucceed;
    private Collection<String> payments = new LinkedList<>();

    public double getAmount() {
        return amount;
    }

//    public void setSucceed(boolean succeed) {
//        isSucceed = succeed;
//    }

    @Override
    public boolean pay(PaymentData data) throws PaymentSystemException {
//        if(!isSucceed)
//            throw  new PaymentSystemException();
        amount = data.getPaymentValue();
        payments.add(data.getUsername());
        return true;
    }

    public Collection<String> getPayments() {
        return payments;
    }

    @Override
    public void payBack(PaymentData data) {

    }
}