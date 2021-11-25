package Utils;

import java.io.*;
import java.util.Properties;

// Utils class include utilities functions.
public class PropertiesReaders {

    public static String[] getJDBCProperties() throws IOException {
        String[] propertiesArray = new String[5];
        Properties props = new Properties();
        String dbSettingsPropertyFile = "src/Config/jdbc.properties";
        FileReader fReader = new FileReader(dbSettingsPropertyFile);
        props.load(fReader);
        propertiesArray[0] = props.getProperty("db.driver.class");
        propertiesArray[1] = props.getProperty("db.conn.url");
        propertiesArray[2] = props.getProperty("db.username");
        propertiesArray[3] = props.getProperty("db.password");
        return propertiesArray;
    }

    public static String[] getTransportProperties() throws IOException {
        String[] propertiesArray = new String[5];
        Properties props = new Properties();
        String dbSettingsPropertyFile = "src/Config/transport.properties";
        FileReader fReader = new FileReader(dbSettingsPropertyFile);
        props.load(fReader);
        propertiesArray[0] = props.getProperty("server.host");
        propertiesArray[1] = props.getProperty("server.port");
        propertiesArray[2] = props.getProperty("clients.queue.size");
        return propertiesArray;
    }

    public static boolean isAnyObjectNull(Object... objects) {
        for (Object o : objects) {
            if (o.equals("")){
                return true;
            }
        }
        return false;
    }
}
