package authentication;

import exceptions.SubscriberAlreadyExistsException;
import exceptions.SubscriberDoesNotExistException;
import exceptions.WrongPasswordException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAuthenticationTest {

    private UserAuthentication auth;

    @Mock
    private Map<String, String> userNamesAndPasswords;

    private final String userName = "Jones";
    private final String password = "jones12345";
    private final String password1 = "jfh746";

    @BeforeEach
    void setUp() {
        auth = new UserAuthentication(userNamesAndPasswords);
    }

    @Test
    void register() throws SubscriberAlreadyExistsException {
        auth.register(userName, password);
        verify(userNamesAndPasswords).putIfAbsent(userName, password);
    }

    @Test
    void register_existingUser() {
        when(userNamesAndPasswords.putIfAbsent(userName, password)).thenReturn("password1234");
        assertThrows(SubscriberAlreadyExistsException.class, () -> auth.register(userName, password));
    }

    @Test
    void authenticate() throws WrongPasswordException, SubscriberDoesNotExistException {
        when(userNamesAndPasswords.get(userName)).thenReturn(password);
        auth.authenticate(userName, password);
    }

    @Test
    void authenticate_subscriberDoesNotExist() {
        assertThrows(SubscriberDoesNotExistException.class, () -> auth.authenticate(userName, password));
    }

    @Test
    void authenticate_wrongPassword() {
        when(userNamesAndPasswords.get(userName)).thenReturn("a different password");
        assertThrows(WrongPasswordException.class, () -> auth.authenticate(userName, password1));
    }
}