# TradingSystem

### system startup:
    in order to run the system you should go inside package main and run Main.main
    in the next stage if you want to run the system with secure communication you need to click on the:
        https://localhost:443/
    in the next stage if you want to run the system without secure communication you need to click on the:
        http://localhost:80/
                
    the system will load the state file and you should be able to use the system with the next information:
      1. registered users: Tal, Omri, Noa, Admin and their password is: 123
      2. there is a store called eBay with id 0 and the founder of that store is: Tal
      3. there are two items in the store: Bamba, Chips
      4. Tal assigned Omri to be Store Owner and Omri assigned Noa to be Store Owner
      5. Noa purchased 10 Bambas and 5 Chips from the eBay store
    
### formats: 

    configuration file - resource bundle file which defines the parameters for the initialization of the system.
    the parameters should be the resources or services that the system will use during runtime.
    
   the configuration file can be edited on this file: [config properties](https://github.com/omrigo13/TradingSystem/blob/main/Dev/config/config.properties)  
   these are the fields that you can edit:  
   >1. system.admin.name - this field sets the system admin's username
   >2. system.admin.password - this field sets the system admin's password
   >3. port - this field sets the unsecured port for system connection
   >4. sslPort - this field sets the secured port for system connection
   >5. stateFileAddress - this field sets the address of the script file, the path should be like 🔴**Dev/config/ScriptFileName.java**❗🔴 and you can locate your script file [here](https://github.com/omrigo13/TradingSystem/tree/main/Dev/config)
   >6. startupScript - this field sets the script file name, for example: if you added 🔴**Dev/config/ScriptFileName.java**❗🔴 as state file you should sets this field to: 🔴**ScriptFileName**❗🔴
   >7. paymentSystem - this field sets the external payment system that will work with the system, the payment system should follow this implementation
   ```java
   void connect();
   void pay(PaymentData data);
   void cancel(PaymentData data);
   
   data contains: card number, month, year, holder, ccv, id
   ```
   >8. deliverySystem - this field sets the external delivery system that will work with the system, the delivery system should follow this implementation
   ```java
   void connect();
   void deliver(DeliveryData data);
   void cancel(DeliveryData data);
   
   data contains: name, address, city, country, zip
   ```
   <br><br>
   
    state file - java class that will be compiled during runtime
    the state file contains a series of instructions of use cases and arguments for them.
    the state file will be loaded and the system should be in that state after the initialization finished.
    
   the state file you create should follow the service of the system, the script instructions should be written using [these methods](https://github.com/omrigo13/TradingSystem/blob/main/Dev/src/main/java/service/TradingSystemService.java)
   
   ```java
   public class ScriptFileName {

    public static void run(TradingSystemService tradingSystemService) throws InvalidActionException {
        code lines here...
        for example:
            tradingSystemService.register(username, password);
    }
}
```
