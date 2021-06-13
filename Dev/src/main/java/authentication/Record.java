package authentication;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;
@Entity
public class Record {
    private byte[] salt;
    private String hash;
//    @Id
//    @GeneratedValue
//    private Integer id;
    @Id
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    Record(String userName, byte[] salt, String hash) {
        this.salt = salt;
        this.hash = hash;
        this.userName = userName;
    }

    public Record() {

    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Record) obj;
        return Objects.equals(this.salt, that.salt) &&
                Objects.equals(this.hash, that.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(salt, hash);
    }

    @Override
    public String toString() {
        return "Record[" +
                "salt=" + salt + ", " +
                "hash=" + hash + ']';
    }

//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public Integer getId() {
//        return id;
//    }
}
