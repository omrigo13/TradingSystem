package Logger;

import org.apache.log4j.Logger;
import service.TradingSystemServiceImpl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

public class EventLog {

    private static final Logger logger = Logger.getLogger(TradingSystemServiceImpl.class);

    public void writeToLogger(String description){
        logger.info(description);
    }

    public Collection<String> getLog() throws IOException {
        Collection<String> eventLog = new LinkedList<>();
        BufferedReader reader = new BufferedReader(new FileReader("Dev/logging.log"));
        String line = null;
        String ls = System.getProperty("line.separator");
        while ((line = reader.readLine()) != null) {
            eventLog.add(line);
        }
        reader.close();
        return eventLog;
    }

}
