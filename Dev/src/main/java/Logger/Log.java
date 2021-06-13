package Logger;

import main.Main;
import org.apache.log4j.Logger;
import service.TradingSystemServiceImpl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

public class Log {

    private static final Logger eventLog = Logger.getLogger(TradingSystemServiceImpl.class);
    private static final Logger errorLog = Logger.getLogger(Main.class);

    public void errorToLogger(String description){
        errorLog.error(description);
    }

    public void writeToLogger(String description){
        eventLog.info(description);
    }

    public Collection<String> getEventLog() throws IOException {
        Collection<String> eventLog = new LinkedList<>();
        BufferedReader reader = new BufferedReader(new FileReader("Dev/logging.log"));
        String line = null;
        String ls = System.getProperty("line.separator");
        while ((line = reader.readLine()) != null) {
            if(line.contains("INFO"))
            eventLog.add(line);
        }
        reader.close();
        return eventLog;
    }

    public Collection<String> getErrorLog() throws IOException {
        Collection<String> errorLog = new LinkedList<>();
        BufferedReader reader = new BufferedReader(new FileReader("Dev/logging.log"));
        String line = null;
        String ls = System.getProperty("line.separator");
        while ((line = reader.readLine()) != null) {
            if(line.contains("ERROR"))
                errorLog.add(line);
        }
        reader.close();
        return errorLog;
    }

}
