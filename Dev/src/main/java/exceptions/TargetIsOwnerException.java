package exceptions;

public class TargetIsOwnerException extends InvalidActionException {

    private final String userName;
    private final String storeName;

    public TargetIsOwnerException(String userName, String storeName) {
        this.userName = userName;
        this.storeName = storeName;
    }

    @Override
    public String toString() {
        return "TargetIsOwnerException{" +
                "userName='" + userName + '\'' +
                ", name='" + storeName + '\'' +
                '}';
    }
}
