package authentication;

import java.security.MessageDigest;
import exceptions.SubscriberAlreadyExistsException;
import exceptions.SubscriberDoesNotExistException;
import exceptions.WrongPasswordException;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;

public class UserAuthentication {

    record Record(byte[] salt, String hash) {
    }

    private final ConcurrentHashMap<String, Record> records;
    private final MessageDigest digest;
    private final SecureRandom random;

    public UserAuthentication() {
        this(new ConcurrentHashMap<>(), createMessageDigest(), new SecureRandom());
    }

    private static MessageDigest createMessageDigest() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    UserAuthentication(ConcurrentHashMap<String, Record> records, MessageDigest digest, SecureRandom random) {

        this.records = records;
        this.digest = digest;
        this.random = random;
    }

    public void register(String userName, String password) throws SubscriberAlreadyExistsException {

        var ref = new Object() {
            boolean absent = true;
        };

        records.computeIfAbsent(userName, (k) -> {
            ref.absent = false;
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            return new Record(salt, computeHash(password, salt));
        });

        if (ref.absent)
            throw new SubscriberAlreadyExistsException(userName);
    }

    String computeHash(String password, byte[] salt) {
        digest.reset();
        digest.update(password.getBytes());
        digest.update(salt);
        return new String(digest.digest());
    }

    public void authenticate(String userName, String password) throws SubscriberDoesNotExistException, WrongPasswordException {
        Record record = records.get(userName);
        if (record == null)
            throw new SubscriberDoesNotExistException(userName);
        if (!record.hash.equals(computeHash(password, record.salt)))
            throw new WrongPasswordException(userName, password);
    }
}
