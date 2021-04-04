package exceptions;

public class TargetIsNotStoreManagerException extends Exception {

    private final String userName;
    private final String name;

    public TargetIsNotStoreManagerException(String userName, String name) {
        this.userName = userName;
        this.name = name;
    }

    @Override
    public String toString() {
        return "TargetIsNotStoreManagerException{" +
                "userName='" + userName + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
