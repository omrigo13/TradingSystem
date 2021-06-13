package user;

import store.Store;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class HistoryPurchaseId implements Serializable {
    private Store store;
    private String userName;

    public HistoryPurchaseId() {
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoryPurchaseId that = (HistoryPurchaseId) o;
        return Objects.equals(store, that.store) && Objects.equals(userName, that.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(store, userName);
    }
}
