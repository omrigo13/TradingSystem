package externalServices;

import exceptions.PaymentSystemException;

import java.io.IOException;
import java.time.LocalDate;

public class PaymentSystemBasicImpl implements PaymentSystem {

    private HttpConnection httpConnection;
    private final String url = "https://cs-bgu-wsep.herokuapp.com/";
    private final String method = "POST";
    private String params;
    private String msg;
    private String result;
    private int transactionId;

    @Override
    public void connect() throws PaymentSystemException {
        params = "action_type=handshake";
        msg = "";
        httpConnection = new HttpConnection();

        try {
            result = httpConnection.send(url, method, params, msg);
            if(result == null || result.compareTo("OK") != 0)
                throw new PaymentSystemException();
        }
        catch (IOException e) {
            throw new PaymentSystemException();
        }
    }

    @Override
    public void pay(PaymentData data) throws PaymentSystemException {
        params = "action_type=pay";
        LocalDate now = LocalDate.now();
        params += "&card_number=" + data.getCard_number();
        params += "&month=" + now.getMonthValue();
        params += "&year=" + now.getYear();
        params += "&holder=" + data.getHolder();
        params += "&ccv=" + data.getCcv();
        params += "&id=" + data.getId();
        msg = "";
        httpConnection = new HttpConnection();

        try {
            result = httpConnection.send(url, method, params, msg);
            transactionId = Integer.parseInt(result);
            if(transactionId == -1)
                throw new PaymentSystemException();
            data.setTransactionId(transactionId);
            data.setPaid();
        } catch (IOException e) {
            throw new PaymentSystemException();
        }
    }

    @Override
    public void cancel(PaymentData data) throws PaymentSystemException {
        params = "action_type=cancel_pay";
        params += "&transaction_id=" + data.getTransactionId();
        msg = "";
        httpConnection = new HttpConnection();

        try {
            result = httpConnection.send(url,method,params,msg);
            transactionId = Integer.parseInt(result);
            if(transactionId == -1)
                throw new PaymentSystemException();
            data.setNotPaid();
        }
        catch (IOException e){
            throw new PaymentSystemException();
        }
    }
}
