package store;

import exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import policies.defaultDiscountPolicy;
import policies.defaultPurchasePolicy;
import tradingSystem.TradingSystem;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class StoreTest {

    @Mock private TradingSystem tradingSystem;
    private Store store;

    @BeforeEach
    void setUp() throws Exception {
        store = new Store( 1,"ebay","www.ebay.com online shopping", new defaultPurchasePolicy(), new defaultDiscountPolicy());
    }
    @Test
    void createNewStore() throws Exception{
        //checks that store name cannot be null
        assertThrows(WrongNameException.class, () -> store = new Store( 1, null, "www.ebay.com online shopping", new defaultPurchasePolicy(), new defaultDiscountPolicy()));

        //checks that store name cannot be with only white spaces
        assertThrows(WrongNameException.class, () -> store = new Store( 1, "   ", "www.ebay.com online shopping", new defaultPurchasePolicy(), new defaultDiscountPolicy()));

        //checks that store name cannot start with a number
        assertThrows(WrongNameException.class, () -> store = new Store( 1, "95ebay", "www.ebay.com online shopping", new defaultPurchasePolicy(), new defaultDiscountPolicy()));
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
    void addItemWithoutRating() throws ItemException {
        //checks that item name cannot start with a number
        assertThrows(WrongNameException.class, () -> store.addItem("12tomato", 20, "vegetables", "red", 5));

        //checks that item has a positive price
        assertThrows(WrongPriceException.class, () -> store.addItem("tomato", -5, "vegetables", "red", 5));

        //checks that item has a positive amount
        assertThrows(WrongAmountException.class, () -> store.addItem("tomato", 17, "vegetables", "red", -2));

        //checks that we cannot add an item that already exists
        store.addItem("tomato", 20, "vegetables", "red", 5);

        assertThrows(ItemAlreadyExistsException.class, () -> store.addItem("tomato", 20, "vegetables", "red", 5));
        assertEquals(store.getItems().size(), 1);
    }



    @Test
    void searchItemById() throws ItemException{
        int tomatoId= store.addItem("tomato", 20, "vegetables", "red", 5);
        int cucumberId= store.addItem("cucumber", 15, "vegetables", "green", 10);
        int tomato2Id= store.addItem("tomato", 20, "vegetables", "blue", 5);
        assertThrows(ItemNotFoundException.class, () -> store.searchItemById(6));
        assertEquals(store.searchItemById(tomato2Id).getId(), 2);
    }

    @Test
    void filterWithoutItemsByPrice() throws ItemException{
        store.addItem("tomato", 20, "vegetables", "red", 5);
        store.addItem("cucumber", 15, "vegetables", "green", 10);
        assertTrue( store.filterByPrice(null,30, 100).isEmpty());
        store.addItem("tomato", 20, "vegetables", "blue", 5);
        assertEquals(store.filterByPrice(null,15, 20).size(), 3);
    }

    @Test
    void filterWithItemsByPrice() throws ItemException{
        Collection<Item> list=new LinkedList<>();
        int tomatoId= store.addItem("tomato", 20, "vegetables", "red", 5);
        list.add(store.searchItemById(tomatoId));
        int cucmberId= store.addItem("cucumber", 15, "vegetables", "green", 10);
        list.add(store.searchItemById(cucmberId));
        Collection<Item> filteredItems=store.filterByPrice(list,15,30);
        assertFalse(filteredItems.isEmpty());
        assertEquals(filteredItems.size(),2);
        filteredItems=store.filterByPrice(list,30,100);
        assertTrue(filteredItems.isEmpty());
    }


    @Test
    void filterWithoutItemsByRating() throws ItemException{
        int tomatoId=store.addItem("tomato", 20, "vegetables", "red", 5);
        Item tomato = store.searchItemById(tomatoId);
        tomato.setRating(2);
        int cucumberid=store.addItem("cucumber", 15, "vegetables", "green", 10);
        Item cucumber = store.searchItemById(cucumberid);
        cucumber.setRating(3);

        assertEquals(store.filterByRating(null,2).size(), 2);
        assertEquals(store.filterByRating(null,3).size(), 1);
    }
    @Test
    void filterWithItemsByRating() throws ItemException{
        Collection<Item> list=new LinkedList<>();
        int tomatoId=store.addItem("tomato", 20, "vegetables", "red", 5);
        Item tomato = store.searchItemById(tomatoId);
        tomato.setRating(2);
        list.add(tomato);
        int cucumberid=store.addItem("cucumber", 15, "vegetables", "green", 10);
        Item cucumber = store.searchItemById(cucumberid);
        cucumber.setRating(3);
        list.add(cucumber);

        assertEquals(store.filterByRating(list,2).size(), 2);
        assertEquals(store.filterByRating(list,3).size(), 1);
        assertTrue(store.filterByRating(list,5).isEmpty());
    }

//    @Test
//    void changeQuantity() throws ItemException{
//        int tomatoId=store.addItem("tomato", 20, "vegetables", "red", 5);
//        int cucumberId=store.addItem("cucumber", 15, "vegetables", "green", 10);
//        Item tomato = store.searchItem(tomatoId);
//
//        //checks that the quantity must be 0 or greater
//        assertThrows(WrongAmountException.class, () -> store.changeQuantity(tomatoId, -1));
//
//        store.changeQuantity("tomato", "vegetables","red", 8);
//        assertEquals(store.getItems().get(tomato), 8);
//        store.changeQuantity("tomato", "vegetables","red", 2);
//        assertEquals(store.getItems().get(tomato), 2);
//    }

//    @Test
//    void decreaseByQuantity() throws ItemException{
//        int cucumberId= store.addItem("cucumber", 15, "vegetables", "green", 10);
//        int carrotId= store.addItem("carrot", 20, "vegetables", "orange", 0);
//        Item cucumber = store.searchItemById(cucumberId);
//
//        //checks that the quantity must be 0 or greater
//        assertThrows(WrongAmountException.class, () -> store.decreaseByQuantity(carrotId,5));
//
//        store.decreaseByQuantity(cucumberId,1);
//        assertEquals(store.getItems().get(cucumber), 9);
//        store.decreaseByQuantity(cucumberId,2);
//        assertEquals(store.getItems().get(cucumber), 7);
//    }

    @Test
    void removeItem() throws ItemException{
        store.addItem("cucumber", 15, "vegetables", "green", 10);
        int carrotId= store.addItem("carrot", 20, "vegetables", "orange", 0);
        assertEquals(store.getItems().size(), 2);

        //checks that only an existing item can be removed
        assertThrows(ItemNotFoundException.class, () -> store.removeItem(5));

        store.removeItem(carrotId);
        assertEquals(store.getItems().size(), 1);
        assertThrows(ItemNotFoundException.class, () -> store.removeItem(carrotId));

    }

//    @Test
//    void updateItemPrice() throws ItemException{
//        store.addItem("cucumber", 15, "vegetables", "green", 10);
//        store.addItem("carrot", 20, "vegetables", "orange", 0);
//
//        //set a negative price for an item
//        assertThrows(WrongPriceException.class, () -> store.setItemPrice("carrot", "vegetables","orange", -20));
//
//        store.setItemPrice("carrot", "vegetables","orange", 50);
//        assertEquals(store.searchItem("carrot", "vegetables","orange").getPrice(), 50);
//        store.setItemPrice("carrot", "vegetables","orange", 34);
//        assertEquals(store.searchItem("carrot", "vegetables","orange").getPrice(), 34);
//    }

    @Test
    void searchItems() throws ItemException {
        store.addItem("carrot", 20, "vegetables", "orange", 8);
        store.addItem("cucumber", 15, "vegetables", "green", 10);
        store.addItem("onion",8.9,"vegetables","white",70);

        assertEquals(store.searchItems(null,null,"vegetables").size(),3);
        assertEquals(store.searchItems("r",null,null).size(),2);
        assertEquals(store.searchItems(null,"onion",null).size(),1);
        assertEquals(store.searchItems("rot",null,"vegetables").size(),1);
        assertTrue(store.searchItems("cucumber","onion",null).isEmpty());

    }

    @Test
    void filterItems() throws ItemException {
        Collection<Item> list=new LinkedList<>();

        int carrotId= store.addItem("carrot", 20, "vegetables", "orange", 8);
        int cucumberId= store.addItem("cucumber", 15, "vegetables", "green", 10);
        int onionId= store.addItem("onion",8.9,"vegetable","white",70);
        Item carrot=store.searchItemById(carrotId);
        carrot.setRating(3);
        list.add(carrot);
        Item cucumber=store.searchItemById(cucumberId);
        cucumber.setRating(2);
        list.add(cucumber);
        Item onion=store.searchItemById(onionId);
        onion.setRating(3.5);
        list.add(onion);
        store.setRating(3);
        assertEquals(store.filterItems(list,2.0,null,null,null).size(),3);
        assertEquals(store.filterItems(list,null,3.0,null,null).size(),3);
        assertEquals(store.filterItems(list,null,null,100.0,10.0).size(),2);
        assertEquals(store.filterItems(list,3.0,null,15.0,5.0).size(),1);
        assertTrue(store.filterItems(list,null,4.0,17.0,8.0).isEmpty());
    }


    @Test
    void searchAndFilter() throws ItemException {
        Collection<Item> list = new LinkedList<>(){};

        int carrotId= store.addItem("carrot", 20, "vegetables", "orange", 8);
        int cucumberId= store.addItem("cucumber", 15, "vegetables", "green", 10);
        int onionId= store.addItem("onion",8.9,"vegetables","white",70);
        Item carrot=store.searchItemById(carrotId);
        carrot.setRating(3);
        list.add(carrot);
        Item cucumber=store.searchItemById(cucumberId);
        cucumber.setRating(2);
        list.add(cucumber);
        Item onion=store.searchItemById(onionId);
        onion.setRating(3.5);
        list.add(onion);
        store.setRating(3);
        assertEquals(store.searchAndFilter("c"," ","vegetables",2.5,null,null,null).size(),1);
        assertEquals(store.searchAndFilter(null,null,"vegetables",2.5,2.0,null,null).size(),2);
        assertEquals(store.searchAndFilter(""," ","games",2.5,null,12.0,4.0).size(),0);

    }

    @Test
    void checkAmount() throws ItemException {
        int carrotId= store.addItem("carrot", 20, "vegetables", "orange", 8);
        int cucumberId= store.addItem("cucumber", 15, "vegetables", "green", 10);
        int onionId= store.addItem("onion",8.9,"vegetable","white",70);

        assertThrows(WrongAmountException.class, ()->store.checkAmount(carrotId,10));
        assertThrows(WrongAmountException.class,()-> store.checkAmount(cucumberId,-3));
        assertTrue(store.checkAmount(onionId,27));
    }

    @Test
    void changeItem() throws ItemException {
        int tomatoId= store.addItem("tomato", 20, "vegetables", "red", 5);
        int cucumberId=store.addItem("cucumber", 15, "vegetables", "green", 10);
        int tomato2Id=store.addItem("tomato", 20, "vegetables", "blue", 5);

        assertThrows(ItemNotFoundException.class, () -> store.changeItem(5,"hello",null,30.0));
        assertNotEquals(store.searchItemById(tomatoId).getSubCategory(),"hello");
        assertNotEquals(store.searchItemById(tomatoId).getPrice(),30);
        store.changeItem(tomatoId,"hamama",null,null);
        assertEquals(store.searchItemById(tomatoId).getSubCategory(),"hamama");

        assertThrows(WrongAmountException.class,() -> store.changeItem(cucumberId,null,-5,10.0));
        store.changeItem(cucumberId," ",30,null);
        assertEquals(store.getItems().get(store.searchItemById(cucumberId)),30);

        assertThrows(WrongPriceException.class,()-> store.changeItem(tomato2Id,"hi",null,-4.0));
        assertNotEquals(store.searchItemById(tomatoId).getSubCategory(),"hi");
        store.changeItem(tomato2Id,null,null,25.8);
        assertEquals(store.searchItemById(tomato2Id).getPrice(),25.8);

        store.changeItem(tomatoId," ",10,13.0);
        assertEquals(store.searchItemById(tomatoId).getPrice(),13.0);
        assertEquals(store.getItems().get(store.searchItemById(tomatoId)),10);

        store.changeItem(cucumberId,"oldItem",null,8.9);
        assertEquals(store.searchItemById(cucumberId).getPrice(),8.9);
        assertEquals(store.searchItemById(cucumberId).getSubCategory(),"oldItem");
    }

    @Test
    void calculate() throws ItemException, Exception {
        int tomatoId= store.addItem("tomato", 20, "vegetables", "red", 5);
        int cucumberID= store.addItem("cucumber", 15, "vegetables", "green", 10);
        int carrotId= store.addItem("carrot", 20, "vegetables", "orange", 8);
        store.searchItemById(carrotId).lock();
        Map<Item, Integer> items = new HashMap<>();
        items.put(store.searchItemById(tomatoId), 2);
        items.put(store.searchItemById(cucumberID), 2);
        items.put(store.searchItemById(carrotId), 2);
        StringBuilder details = new StringBuilder();
//        assertThrows(Exception.class, () -> store.processBasketAndCalculatePrice(items, details));
        assertEquals(store.getItems().get(store.searchItemById(tomatoId)), 5);
        store.searchItemById(carrotId).unlock();
        assertEquals(store.processBasketAndCalculatePrice(items, details), 110);
        assertEquals(store.getItems().get(store.searchItemById(tomatoId)), 3);
        store.searchItemById(tomatoId).unlock();
        store.searchItemById(cucumberID).unlock();
        store.searchItemById(carrotId).unlock();
        items.clear();
        items.put(store.searchItemById(tomatoId), 2);
        items.put(store.searchItemById(cucumberID), 2);
        items.put(store.searchItemById(carrotId), 8);
        assertThrows(WrongAmountException.class, () -> store.processBasketAndCalculatePrice(items, details));
    }

    @Test
    void unlockItems() throws ItemException{
        int tomatoId= store.addItem("tomato", 20, "vegetables", "red", 5);
        int cucumberID= store.addItem("cucumber", 15, "vegetables", "green", 10);
        int carrotId= store.addItem("carrot", 20, "vegetables", "orange", 8);
        store.searchItemById(carrotId).lock();
        store.searchItemById(tomatoId).lock();
        store.searchItemById(cucumberID).lock();
        Set<Item> items = new HashSet<>();
        items.add(store.searchItemById(tomatoId));
        items.add(store.searchItemById(carrotId));
        items.add(store.searchItemById(cucumberID));
        store.unlockItems(items);
        assertFalse(store.searchItemById(carrotId).isLocked());
        assertFalse(store.searchItemById(cucumberID).isLocked());
    }

    @Test
    void rollBack() throws ItemException{
        int tomatoId= store.addItem("tomato", 20, "vegetables", "red", 5);
        int cucumberID= store.addItem("cucumber", 15, "vegetables", "green", 10);
        int carrotId= store.addItem("carrot", 20, "vegetables", "orange", 8);
        store.searchItemById(carrotId).lock();
        store.searchItemById(tomatoId).lock();
        store.searchItemById(cucumberID).lock();
        Map<Item, Integer> items = new HashMap<>();
        items.put(store.searchItemById(tomatoId), 2);
        items.put(store.searchItemById(cucumberID), 2);
        items.put(store.searchItemById(carrotId), 8);
        store.rollBack(items);
        assertEquals(store.getItems().get(store.searchItemById(tomatoId)), 7);
        assertEquals(store.getItems().get(store.searchItemById(carrotId)), 16);
        assertFalse(store.searchItemById(cucumberID).isLocked());
        assertFalse(store.searchItemById(carrotId).isLocked());
    }
}
