package exceptions;

public class GetStoreItemException extends Exception {
    private final String name;
    private final String itemName;
    private final String category;
    private final String subCategory;

    public GetStoreItemException(String name, String itemName, String category, String subCategory, Exception cause) {
        super(cause);
        this.name = name;
        this.itemName = itemName;
        this.category = category;
        this.subCategory = subCategory;
    }

    @Override
    public String toString() {
        return "GetStoreItemException{" +
                "name='" + name + '\'' +
                ", itemName='" + itemName + '\'' +
                ", category='" + category + '\'' +
                ", subCategory='" + subCategory + '\'' +
                "} " + super.toString();
    }
}
