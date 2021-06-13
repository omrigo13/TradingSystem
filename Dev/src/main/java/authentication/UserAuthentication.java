package authentication;

import java.security.MessageDigest;
import exceptions.SubscriberAlreadyExistsException;
import exceptions.SubscriberDoesNotExistException;
import exceptions.WrongPasswordException;
import persistence.Repo;

import javax.persistence.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Entity
public class UserAuthentication {

    @OneToMany(cascade = CascadeType.ALL)
    private final Map<String, Record> records;
    @Transient
    private final MessageDigest digest;
    @Transient
    private final SecureRandom random;
    @Id
    private Integer id;

    public UserAuthentication() {
        this(new ConcurrentHashMap<>(), createMessageDigest(), new SecureRandom());
        this.id = 1;
    }

    private static MessageDigest createMessageDigest() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public UserAuthentication(ConcurrentHashMap<String, Record> records, MessageDigest digest, SecureRandom random) {

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
            return new Record(userName, salt, computeHash(password, salt));
        });

        if (ref.absent)
            throw new SubscriberAlreadyExistsException(userName);

        Repo.merge(this);
    }

    synchronized String computeHash(String password, byte[] salt) {
        digest.reset();
        digest.update(password.getBytes());
        digest.update(salt);
        return new String(digest.digest());
    }

    public void authenticate(String userName, String password) throws SubscriberDoesNotExistException, WrongPasswordException {
        Record record = records.get(userName);
        if (record == null)
            throw new SubscriberDoesNotExistException(userName);
        if (!record.getHash().equals(computeHash(password, record.getSalt())))
            throw new WrongPasswordException(userName, password);
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
