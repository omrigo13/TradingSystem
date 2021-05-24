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
    
    state file -  java class that will be compiled during runtime
    the state file contains a series of instructions of use cases and arguments for them.
    the state file will be loaded and the system should be in that state after the initialization finished.
