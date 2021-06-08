package user;

import store.Store;

import javax.persistence.MapsId;
import java.io.Serializable;
import java.util.Objects;

public class BasketId implements Serializable {

    private String username;

    private int store_id;

    public BasketId() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasketId basketId = (BasketId) o;
        return store_id == basketId.store_id && Objects.equals(username, basketId.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, store_id);
    }

    public BasketId(String username, int store_id) {
        this.username = username;
        this.store_id = store_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }
}
