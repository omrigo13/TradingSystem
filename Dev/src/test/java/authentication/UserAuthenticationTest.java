package authentication;

import exceptions.SubscriberAlreadyExistsException;
import exceptions.SubscriberDoesNotExistException;
import exceptions.WrongPasswordException;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import persistence.RepoMock;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertThrows;
import static org.testng.AssertJUnit.assertEquals;

public class UserAuthenticationTest {

    private UserAuthentication auth;

    private MessageDigest digest;

    @Spy private ConcurrentHashMap<String, Record> records;

    private final String userName = "Jones";
    private final String password = "jones12345";
    private final SecureRandom random = new SecureRandom();

    @BeforeClass
    public void beforeClass() {
        RepoMock.enable();
    }

    @BeforeMethod
    void setUp() throws NoSuchAlgorithmException {
        MockitoAnnotations.openMocks(this);
        digest = spy(MessageDigest.getInstance("SHA-256"));
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
        String hash = records.get(userName).getHash();
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
        Record record = new Record(userName, salt, hash);
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
        Record record = new Record(userName, salt, "SomeHash");
        when(records.get(userName)).thenReturn(record);
        when(digest.digest()).thenReturn("SomeOtherHash".getBytes());
        assertThrows(WrongPasswordException.class, () -> auth.authenticate(userName, password));
    }}