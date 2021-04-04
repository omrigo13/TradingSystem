package exceptions;

public class NewStoreException extends Exception {
    
    final String storeName; 
    
    public NewStoreException(String storeName, Exception cause) {
        this.storeName = storeName;
    }

    @Override
    public String toString() {
        return "NewStoreException{" +
                "storeName='" + storeName + '\'' +
                "} " + super.toString();
    }
}
