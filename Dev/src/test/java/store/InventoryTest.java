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
    @Test
    void addItemAndRating() throws Exception{
        //checks that item name cannot be null
        assertThrows(WrongName.class, () -> inventory.addItem(null, 20, "vegetables", "red", 3, 5));

        //checks that item name cannot be with only white spaces
        assertThrows(WrongName.class, () -> inventory.addItem("   ", 20, "vegetables", "red", 3, 5));

        //checks that item cannot be with negative rating
        assertThrows(WrongRating.class, () -> inventory.addItem("tomato", 20, "vegetables", "red", -1, 5));

        inventory.addItem("tomato", 20, "vegetables", "red", 2, 5);
        assertEquals(inventory.getItems().size(), 1);
        assertEquals(inventory.getItems().keys().nextElement().getId(), 1);
        inventory.addItem("cucumber", 15, "vegetables", "green", 2, 10);
        assertEquals(inventory.getItems().size(), 2);
        assertEquals(inventory.getItems().keys().nextElement().getId(), 2);
    }

    @Test
    void addItemWithoutRating() throws Exception{
        //checks that item name cannot start with a number
        assertThrows(WrongName.class, () -> inventory.addItem("12tomato", 20, "vegetables", "red", 5));

        //checks that item has a positive price
        assertThrows(WrongPrice.class, () -> inventory.addItem("tomato", -5, "vegetables", "red", 5));

        //checks that item has a positive amount
        assertThrows(WrongAmount.class, () -> inventory.addItem("tomato", 17, "vegetables", "red", 2, -2));

        //checks that we cannot add an item that already exists
        inventory.addItem("tomato", 20, "vegetables", "red", 2, 5);

        assertThrows(ItemAlreadyExists.class, () -> inventory.addItem("tomato", 20, "vegetables", "red", 2, 5));
        assertEquals(inventory.getItems().size(), 1);
    }

    @Test
    void searchItemByName() throws Exception{
        inventory.addItem("tomato", 20, "vegetables", "red", 2, 5);
        inventory.addItem("cucumber", 15, "vegetables", "green", 2, 10);
        assertThrows(ItemNotFound.class, () -> inventory.searchItemByName("carrot"));
        inventory.addItem("tomato", 20, "vegetables", "blue", 2, 5);
        assertEquals(inventory.searchItemByName("tomato").size(), 2);
    }

    @Test
    void searchItem() throws Exception{
        inventory.addItem("tomato", 20, "vegetables", "red", 2, 5);
        inventory.addItem("cucumber", 15, "vegetables", "green", 2, 10);
        inventory.addItem("tomato", 20, "vegetables", "blue", 2, 5);
        assertThrows(ItemNotFound.class, () -> inventory.searchItem("tomato", "fruits","orange"));
        assertEquals(inventory.searchItem("tomato", "vegetables","blue").getId(), 3);
    }

    @Test
    void changeQuantity() throws Exception{
        inventory.addItem("tomato", 20, "vegetables", "red", 2, 5);
        inventory.addItem("cucumber", 15, "vegetables", "green", 2, 10);
        Item tomato = inventory.searchItem("tomato", "vegetables","red");

        //checks that the quantity must be 0 or greater
        assertThrows(WrongAmount.class, () -> inventory.changeQuantity("tomato", "vegetables","red", -1));

        inventory.changeQuantity("tomato", "vegetables","red", 8);
        assertEquals(inventory.getItems().get(tomato), 8);
        inventory.changeQuantity("tomato", "vegetables","red", 2);
        assertEquals(inventory.getItems().get(tomato), 2);
    }

    @Test
    void decreaseByOne() throws Exception{
        inventory.addItem("cucumber", 15, "vegetables", "green", 2, 10);
        inventory.addItem("carrot", 20, "vegetables", "orange", 2, 0);
        Item cucumber = inventory.searchItem("cucumber", "vegetables","green");

        //checks that the quantity must be 0 or greater
        assertThrows(WrongAmount.class, () -> inventory.decreaseByOne("carrot", "vegetables","orange"));

        inventory.decreaseByOne("cucumber",  "vegetables", "green");
        assertEquals(inventory.getItems().get(cucumber), 9);
        inventory.decreaseByOne("cucumber",  "vegetables", "green");
        inventory.decreaseByOne("cucumber",  "vegetables", "green");
        assertEquals(inventory.getItems().get(cucumber), 7);
    }

    @Test
    void removeItem() throws Exception{
        inventory.addItem("cucumber", 15, "vegetables", "green", 2, 10);
        inventory.addItem("carrot", 20, "vegetables", "orange", 2, 0);
        assertEquals(inventory.getItems().size(), 2);

        //checks that only an existing item can be removed
        assertThrows(ItemNotFound.class, () -> inventory.removeItem("tomato", "vegetables","orange"));

        inventory.removeItem("carrot", "vegetables","orange");
        assertEquals(inventory.getItems().size(), 1);
        assertThrows(ItemNotFound.class, () -> inventory.removeItem("carrot", "vegetables","orange"));

    }
}
