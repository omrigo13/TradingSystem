package store;

import java.io.Serializable;
import java.util.Objects;

public class ItemId implements Serializable {
    private int item_id;
    private int store_id;


    public ItemId() {

    }

    public ItemId(int item_id, int store_id) {
        this.item_id = item_id;
        this.store_id = store_id;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int id) {
        this.item_id = id;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int storeId) {
        this.store_id = storeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemId itemId = (ItemId) o;
        return item_id == itemId.item_id && store_id == itemId.store_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(item_id, store_id);
    }
}