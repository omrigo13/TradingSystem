package store;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {

    private static Inventory inventory = new Inventory();

    @BeforeEach
    void setUp() {
        inventory = new Inventory();
    }
//    @Test
//    void addItemAndRating() throws Exception{
//        //checks that item name cannot be null
//        assertThrows(WrongName.class, () -> inventory.addItem(null, 20, "vegetables", "red", 3, 5));
//
//        //checks that item name cannot be with only white spaces
//        assertThrows(WrongName.class, () -> inventory.addItem("   ", 20, "vegetables", "red", 3, 5));
//
//        //checks that item cannot be with negative rating
//        assertThrows(WrongRating.class, () -> inventory.addItem("tomato", 20, "vegetables", "red", -1, 5));
//
//        inventory.addItem("tomato", 20, "vegetables", "red", 2, 5);
//        assertEquals(inventory.getItems().size(), 1);
//        assertEquals(inventory.getItems().keys().nextElement().getId(), 1);
//        inventory.addItem("cucumber", 15, "vegetables", "green", 2, 10);
//        assertEquals(inventory.getItems().size(), 2);
//        assertEquals(inventory.getItems().keys().nextElement().getId(), 2);
//    }

    @Test
    void addItemWithoutRating() throws Exception{
        //checks that item name cannot start with a number
        assertThrows(WrongNameException.class, () -> inventory.addItem("12tomato", 20, "vegetables", "red", 5));

        //checks that item has a positive price
        assertThrows(WrongPriceException.class, () -> inventory.addItem("tomato", -5, "vegetables", "red", 5));

        //checks that item has a positive amount
        assertThrows(WrongAmountException.class, () -> inventory.addItem("tomato", 17, "vegetables", "red", -2));

        //checks that we cannot add an item that already exists
        inventory.addItem("tomato", 20, "vegetables", "red", 5);

        assertThrows(ItemAlreadyExistsException.class, () -> inventory.addItem("tomato", 20, "vegetables", "red", 5));
        assertEquals(inventory.getItems().size(), 1);
    }

    @Test
    void searchItemByName() throws Exception{
        inventory.addItem("tomato", 20, "vegetables", "red", 5);
        inventory.addItem("cucumber", 15, "vegetables", "green", 10);
        assertThrows(ItemNotFoundException.class, () -> inventory.searchItemByName("carrot"));
        inventory.addItem("tomato", 20, "vegetables", "blue", 5);
        assertEquals(inventory.searchItemByName("tomato").size(), 2);
    }

    @Test
    void searchItemByCategory() throws Exception{
        inventory.addItem("tomato", 20, "vegetables", "red", 5);
        inventory.addItem("cucumber", 15, "vegetables", "green", 10);
        assertThrows(ItemNotFoundException.class, () -> inventory.searchItemByCategory("camera"));
        inventory.addItem("GoPro", 1200, "camera", "black", 5);
        assertEquals(inventory.searchItemByCategory("vegetables").size(), 2);
    }

    @Test
    void searchItemByKeyWord() throws Exception{
        inventory.addItem("tomato", 20, "vegetables", "red", 5);
        inventory.addItem("cucumber", 15, "vegetables", "green", 10);
        assertThrows(ItemNotFoundException.class, () -> inventory.searchItemByKeyWord("blue"));
        inventory.addItem("RedGoPro", 1200, "camera", "black", 5);
        assertEquals(inventory.searchItemByKeyWord("red").size(), 2);
    }

    @Test
    void searchItem() throws Exception{
        inventory.addItem("tomato", 20, "vegetables", "red", 5);
        inventory.addItem("cucumber", 15, "vegetables", "green", 10);
        inventory.addItem("tomato", 20, "vegetables", "blue", 5);
        assertThrows(ItemNotFoundException.class, () -> inventory.searchItem("tomato", "fruits","orange"));
        assertEquals(inventory.searchItem("tomato", "vegetables","blue").getId(), 3);
    }

    @Test
    void filterByPrice() throws Exception{
        inventory.addItem("tomato", 20, "vegetables", "red", 5);
        inventory.addItem("cucumber", 15, "vegetables", "green", 10);
        assertThrows(ItemNotFoundException.class, () -> inventory.filterByPrice(30, 100));
        inventory.addItem("tomato", 20, "vegetables", "blue", 5);
        assertEquals(inventory.filterByPrice(15, 20).size(), 3);
    }

    @Test
    void filterByRating() throws Exception{
        inventory.addItem("tomato", 20, "vegetables", "red", 5);
        Item tomato = inventory.searchItem("tomato", "vegetables", "red");
        tomato.setRating(2);
        inventory.addItem("cucumber", 15, "vegetables", "green", 10);
        Item cucumber = inventory.searchItem("cucumber", "vegetables", "green");
        cucumber.setRating(3);
        assertThrows(ItemNotFoundException.class, () -> inventory.filterByRating(4));
        assertEquals(inventory.filterByRating(2).size(), 2);
        assertEquals(inventory.filterByRating(3).size(), 1);
    }

    @Test
    void changeQuantity() throws Exception{
        inventory.addItem("tomato", 20, "vegetables", "red", 5);
        inventory.addItem("cucumber", 15, "vegetables", "green", 10);
        Item tomato = inventory.searchItem("tomato", "vegetables","red");

        //checks that the quantity must be 0 or greater
        assertThrows(WrongAmountException.class, () -> inventory.changeQuantity("tomato", "vegetables","red", -1));

        inventory.changeQuantity("tomato", "vegetables","red", 8);
        assertEquals(inventory.getItems().get(tomato), 8);
        inventory.changeQuantity("tomato", "vegetables","red", 2);
        assertEquals(inventory.getItems().get(tomato), 2);
    }

    @Test
    void decreaseByQuantity() throws Exception{
        inventory.addItem("cucumber", 15, "vegetables", "green", 10);
        inventory.addItem("carrot", 20, "vegetables", "orange", 0);
        Item cucumber = inventory.searchItem("cucumber", "vegetables","green");

        //checks that the quantity must be 0 or greater
        assertThrows(WrongAmountException.class, () -> inventory.decreaseByQuantity("carrot", "vegetables","orange",4));

        inventory.decreaseByQuantity("cucumber",  "vegetables", "green",1);
        assertEquals(inventory.getItems().get(cucumber), 9);
        inventory.decreaseByQuantity("cucumber",  "vegetables", "green",2);
        assertEquals(inventory.getItems().get(cucumber), 7);
    }

    @Test
    void removeItem() throws Exception{
        inventory.addItem("cucumber", 15, "vegetables", "green", 10);
        inventory.addItem("carrot", 20, "vegetables", "orange", 0);
        assertEquals(inventory.getItems().size(), 2);

        //checks that only an existing item can be removed
        assertThrows(ItemNotFoundException.class, () -> inventory.removeItem("tomato", "vegetables","orange"));

        inventory.removeItem("carrot", "vegetables","orange");
        assertEquals(inventory.getItems().size(), 1);
        assertThrows(ItemNotFoundException.class, () -> inventory.removeItem("carrot", "vegetables","orange"));

    }

    @Test
    void updateItemPrice() throws Exception{
        inventory.addItem("cucumber", 15, "vegetables", "green", 10);
        inventory.addItem("carrot", 20, "vegetables", "orange", 0);

        //set a negative price for an item
        assertThrows(WrongPriceException.class, () -> inventory.setItemPrice("carrot", "vegetables","orange", -20));

        inventory.setItemPrice("carrot", "vegetables","orange", 50);
        assertEquals(inventory.searchItem("carrot", "vegetables","orange").getPrice(), 50);
        inventory.setItemPrice("carrot", "vegetables","orange", 34);
        assertEquals(inventory.searchItem("carrot", "vegetables","orange").getPrice(), 34);
    }
}
