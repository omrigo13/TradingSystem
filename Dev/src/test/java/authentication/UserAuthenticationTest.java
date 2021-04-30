package authentication;

import exceptions.SubscriberAlreadyExistsException;
import exceptions.SubscriberDoesNotExistException;
import exceptions.WrongPasswordException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAuthenticationTest {

    private UserAuthentication auth;

    private final MessageDigest digest;

    @Spy private ConcurrentHashMap<String, UserAuthentication.Record> records;

    private final String userName = "Jones";
    private final String password = "jones12345";
    private final SecureRandom random = new SecureRandom();

    UserAuthenticationTest() throws NoSuchAlgorithmException {

        digest = spy(MessageDigest.getInstance("SHA-256"));
    }

    @BeforeEach
    void setUp() {

        auth = spy(new UserAuthentication(records, digest, random));
    }

    @Test
    void register() throws SubscriberAlreadyExistsException {

        AtomicReference<byte[]> ref = new AtomicReference<>();

        doAnswer(invocation -> {
            byte[] result = (byte[])invocation.callRealMethod();
            ref.set(result);
            return result;
        }).when(digest).digest();

        auth.register(userName, password);
        verify(digest).reset();
        String hash = records.get(userName).hash();
        assertEquals(new String(ref.get()), hash);
    }

    @Test
    void register_existingUser() {

        doReturn(null).when(records).computeIfAbsent(same(userName), any());
        assertThrows(SubscriberAlreadyExistsException.class, () -> auth.register(userName, password));
    }

    @Test
    void authenticate() throws WrongPasswordException, SubscriberDoesNotExistException {

        byte[] salt = "12345".getBytes();
        String hash = "SomeHash";
        UserAuthentication.Record record = new UserAuthentication.Record(salt, hash);
        when(records.get(userName)).thenReturn(record);
        when(digest.digest()).thenReturn(hash.getBytes());
        auth.authenticate(userName, password);
    }

    @Test
    void authenticate_subscriberDoesNotExist() {

        assertThrows(SubscriberDoesNotExistException.class, () -> auth.authenticate(userName, password));
    }

    @Test
    void authenticate_wrongPassword() {

        byte[] salt = "12345".getBytes();
        UserAuthentication.Record record = new UserAuthentication.Record(salt, "SomeHash");
        when(records.get(userName)).thenReturn(record);
        when(digest.digest()).thenReturn("SomeOtherHash".getBytes());
        assertThrows(WrongPasswordException.class, () -> auth.authenticate(userName, password));
    }
}