package exceptions;

public class TargetIsNotManagerException extends InvalidActionException {

    private final String userName;
    private final String name;

    public TargetIsNotManagerException(String userName, String name) {
        this.userName = userName;
        this.name = name;
    }

    @Override
    public String toString() {
        return "TargetIsNotManagerException{" +
                "userName='" + userName + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
