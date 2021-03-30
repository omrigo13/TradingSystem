package authentication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserAuthenticationTest {

    private final String userName = "Lidor";
    private final String password = "lidor12345";
    private final String password1 = "jfh746";
    private UserAuthentication auth;

    @Test
    void register() throws RegistrationException {
        auth.register(userName, password);
    }

    @Test
    void registerExistingUser() throws RegistrationException {
        auth.register(userName, password);
        assertThrows(UserAlreadyExistsException.class, () -> auth.register(userName, password));
    }

    @Test
    void login() throws UserAlreadyExistsException, LoginException {
        assertThrows(UserDoesNotExistException.class, () -> auth.login(userName, password));
        auth.register(userName, password);
        assertThrows(WrongPasswordException.class, () -> auth.login(userName, password1));
        auth.login(userName, password);
    }

    @BeforeEach
    void setUp() {
            auth = new UserAuthentication();
    }
}