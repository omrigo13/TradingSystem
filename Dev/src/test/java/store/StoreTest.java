package store;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StoreTest {

    private static Store store;

    static {
        try {
            store = new Store(1,"ebay","www.ebay.com online shopping");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        store = new Store(1,"ebay","www.ebay.com online shopping");
    }
    @Test
    void createNewStore() throws Exception{
        //checks that store name cannot be null
        assertThrows(WrongName.class, () -> store = new Store(1, null, "www.ebay.com online shopping"));

        //checks that store name cannot be with only white spaces
        assertThrows(WrongName.class, () -> store = new Store(1, "   ", "www.ebay.com online shopping"));

        //checks that store name cannot start with a number
        assertThrows(WrongName.class, () -> store = new Store(1, "95ebay", "www.ebay.com online shopping"));
    }

//    @Test
//    void addItemAndRating() throws Exception{
//        //checks that item name cannot be null
//        assertThrows(WrongName.class, () -> store.addItem(null, 20, "vegetables", "red", 3, 5));
//
//        //checks that item name cannot be with only white spaces
//        assertThrows(WrongName.class, () -> store.addItem("   ", 20, "vegetables", "red", 3, 5));
//
//        //checks that item cannot be with negative rating
//        assertThrows(WrongRating.class, () -> store.addItem("tomato", 20, "vegetables", "red", -1, 5));
//
//        store.addItem("tomato", 20, "vegetables", "red", 2, 5);
//        assertEquals(store.getItems().size(), 1);
//        assertEquals(store.getItems().keys().nextElement().getId(), 1);
//        store.addItem("cucumber", 15, "vegetables", "green", 2, 10);
//        assertEquals(store.getItems().size(), 2);
//        assertEquals(store.searchItemByName("cucumber").peek().getId(), 2);
//    }

    @Test
    void addItemWithoutRating() throws Exception{
        //checks that item name cannot start with a number
        assertThrows(WrongName.class, () -> store.addItem("12tomato", 20, "vegetables", "red", 5));

        //checks that item has a positive price
        assertThrows(WrongPrice.class, () -> store.addItem("tomato", -5, "vegetables", "red", 5));

        //checks that item has a positive amount
        assertThrows(WrongAmount.class, () -> store.addItem("tomato", 17, "vegetables", "red", -2));

        //checks that we cannot add an item that already exists
        store.addItem("tomato", 20, "vegetables", "red", 5);

        assertThrows(ItemAlreadyExists.class, () -> store.addItem("tomato", 20, "vegetables", "red", 5));
        assertEquals(store.getItems().size(), 1);
    }

    @Test
    void searchItemByName() throws Exception{
        store.addItem("tomato", 20, "vegetables", "red", 5);
        store.addItem("cucumber", 15, "vegetables", "green", 10);
        assertThrows(ItemNotFound.class, () -> store.searchItemByName("carrot"));
        store.addItem("tomato", 20, "vegetables", "blue", 5);
        assertEquals(store.searchItemByName("tomato").size(), 2);
    }

    @Test
    void searchItemByCategory() throws Exception{
        store.addItem("tomato", 20, "vegetables", "red", 5);
        store.addItem("cucumber", 15, "vegetables", "green", 10);
        assertThrows(ItemNotFound.class, () -> store.searchItemByCategory("camera"));
        store.addItem("GoPro", 1200, "camera", "black", 5);
        assertEquals(store.searchItemByCategory("vegetables").size(), 2);
    }

    @Test
    void searchItemByKeyWord() throws Exception{
        store.addItem("tomato", 20, "vegetables", "red", 5);
        store.addItem("cucumber", 15, "vegetables", "green", 10);
        assertThrows(ItemNotFound.class, () -> store.searchItemByKeyWord("blue"));
        store.addItem("RedGoPro", 1200, "camera", "black", 5);
        assertEquals(store.searchItemByKeyWord("red").size(), 2);
    }

    @Test
    void searchItem() throws Exception{
        store.addItem("tomato", 20, "vegetables", "red", 5);
        store.addItem("cucumber", 15, "vegetables", "green", 10);
        store.addItem("tomato", 20, "vegetables", "blue", 5);
        assertThrows(ItemNotFound.class, () -> store.searchItem("tomato", "fruits","orange"));
        assertEquals(store.searchItem("tomato", "vegetables","blue").getId(), 3);
    }

    @Test
    void filterByPrice() throws Exception{
        store.addItem("tomato", 20, "vegetables", "red", 5);
        store.addItem("cucumber", 15, "vegetables", "green", 10);
        assertThrows(ItemNotFound.class, () -> store.filterByPrice(30, 100));
        store.addItem("tomato", 20, "vegetables", "blue", 5);
        assertEquals(store.filterByPrice(15, 20).size(), 3);
    }

    @Test
    void filterByRating() throws Exception{
        store.addItem("tomato", 20, "vegetables", "red", 5);
        Item tomato = store.searchItem("tomato", "vegetables", "red");
        tomato.setRating(2);
        store.addItem("cucumber", 15, "vegetables", "green", 10);
        Item cucumber = store.searchItem("cucumber", "vegetables", "green");
        cucumber.setRating(3);
        assertThrows(ItemNotFound.class, () -> store.filterByRating(4));
        assertEquals(store.filterByRating(2).size(), 2);
        assertEquals(store.filterByRating(3).size(), 1);
    }

    @Test
    void changeQuantity() throws Exception{
        store.addItem("tomato", 20, "vegetables", "red", 5);
        store.addItem("cucumber", 15, "vegetables", "green", 10);
        Item tomato = store.searchItem("tomato", "vegetables","red");

        //checks that the quantity must be 0 or greater
        assertThrows(WrongAmount.class, () -> store.changeQuantity("tomato", "vegetables","red", -1));

        store.changeQuantity("tomato", "vegetables","red", 8);
        assertEquals(store.getItems().get(tomato), 8);
        store.changeQuantity("tomato", "vegetables","red", 2);
        assertEquals(store.getItems().get(tomato), 2);
    }

    @Test
    void decreaseByOne() throws Exception{
        store.addItem("cucumber", 15, "vegetables", "green", 10);
        store.addItem("carrot", 20, "vegetables", "orange", 0);
        Item cucumber = store.searchItem("cucumber", "vegetables","green");

        //checks that the quantity must be 0 or greater
        assertThrows(WrongAmount.class, () -> store.decreaseByOne("carrot", "vegetables","orange"));

        store.decreaseByOne("cucumber",  "vegetables", "green");
        assertEquals(store.getItems().get(cucumber), 9);
        store.decreaseByOne("cucumber",  "vegetables", "green");
        store.decreaseByOne("cucumber",  "vegetables", "green");
        assertEquals(store.getItems().get(cucumber), 7);
    }

    @Test
    void removeItem() throws Exception{
        store.addItem("cucumber", 15, "vegetables", "green", 10);
        store.addItem("carrot", 20, "vegetables", "orange", 0);
        assertEquals(store.getItems().size(), 2);

        //checks that only an existing item can be removed
        assertThrows(ItemNotFound.class, () -> store.removeItem("tomato", "vegetables","orange"));

        store.removeItem("carrot", "vegetables","orange");
        assertEquals(store.getItems().size(), 1);
        assertThrows(ItemNotFound.class, () -> store.removeItem("carrot", "vegetables","orange"));

    }

    @Test
    void updateItemPrice() throws Exception{
        store.addItem("cucumber", 15, "vegetables", "green", 10);
        store.addItem("carrot", 20, "vegetables", "orange", 0);

        //set a negative price for an item
        assertThrows(WrongPrice.class, () -> store.setItemPrice("carrot", "vegetables","orange", -20));

        store.setItemPrice("carrot", "vegetables","orange", 50);
        assertEquals(store.searchItem("carrot", "vegetables","orange").getPrice(), 50);
        store.setItemPrice("carrot", "vegetables","orange", 34);
        assertEquals(store.searchItem("carrot", "vegetables","orange").getPrice(), 34);
    }
}
