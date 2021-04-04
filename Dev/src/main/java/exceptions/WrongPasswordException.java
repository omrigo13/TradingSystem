package exceptions;

import java.util.Arrays;

public class WrongPasswordException extends Exception {
    final String userName;
    final String password;
    final String maskedPassword;

    public WrongPasswordException(String userName, String password) {
        this.userName = userName;
        this.password = password;

        char[] maskedPasswordArray = new char[password.length()];
        Arrays.fill(maskedPasswordArray, '*');
        this.maskedPassword = new String(maskedPasswordArray);
    }

    @Override
    public String toString() {
        return "WrongPasswordException{" +
                "userName='" + userName + '\'' +
                ", password='" + maskedPassword + '\'' +
                '}';
    }
}
