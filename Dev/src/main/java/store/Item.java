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

    public void setPrice(double price) throws Exception {
        if(price < 0)
            throw new WrongPrice("item price must be positive");
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String newSubCategory){ this.subCategory=newSubCategory;}

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) throws Exception {
        if(rating < 0)
            throw new WrongRating("rating must be positive");
        this.rating = rating;
    }

    public String toString() { return "id:" + id +
            "\nname:" + name +
            "\nprice:" + price +
            "\ncategory:" + category +
            "\nsub category:" + subCategory +
            "\nrating:" + rating + '\n';}
}
