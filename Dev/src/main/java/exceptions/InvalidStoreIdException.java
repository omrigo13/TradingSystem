package exceptions;

public class InvalidStoreIdException extends Exception {
    private int storeId;

    public InvalidStoreIdException(int storeId) {
        this.storeId = storeId;
    }

    @Override
    public String toString() {
        return "InvalidStoreIdException{" +
                "storeName='" + storeId + '\'' +
                '}';
    }
}
