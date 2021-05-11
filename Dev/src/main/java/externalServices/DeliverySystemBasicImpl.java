package externalServices;

import exceptions.DeliverySystemException;

import java.io.IOException;

public class DeliverySystemBasicImpl implements DeliverySystem {

    private HttpConnection httpConnection;
    private final String url = "https://cs-bgu-wsep.herokuapp.com/";
    private final String method = "POST";
    private String params;
    private String msg;
    private String result;
    private int transactionId;

    @Override
    public void connect() throws DeliverySystemException {
        params = "action_type=handshake";
        msg = "";
        httpConnection = new HttpConnection();

        try {
            result = httpConnection.send(url, method, params, msg);
            if(result == null || result.compareTo("OK") != 0)
                throw new DeliverySystemException();
        }
        catch (IOException e) {
            throw new DeliverySystemException();
        }
    }

    @Override
    public void deliver(DeliveryData data) throws DeliverySystemException {
        params = "action_type=supply";
        params += "&name=" + data.getName();
        params += "&address=" + data.getAddress();
        params += "&city=" + data.getCity();
        params += "&country=" + data.getCountry();
        params += "&zip=" + data.getZip();
        msg = "";
        httpConnection = new HttpConnection();

        try {
            result = httpConnection.send(url,method,params,msg);
            transactionId = Integer.parseInt(result);
            if(transactionId == -1)
                throw new DeliverySystemException();
            data.setTransactionId(transactionId);
        }
        catch (IOException e){
            throw new DeliverySystemException();
        }
    }
}
