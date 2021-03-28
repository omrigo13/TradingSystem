package store;

public class Item {

    private int id;
    private String name;
    private double price;
    private String category;
    private String subCategory;
    private double rating;

    public Item(int id, String name, double price, String category, String subCategory, double rating) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.subCategory = subCategory;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public double getRating() {
        return rating;
    }
}
