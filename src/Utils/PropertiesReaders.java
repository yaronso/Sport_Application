package Utils;

import java.io.*;
import java.util.Properties;

/**
 * The following class is a utilities class that include different utilities functions.
 */
public class PropertiesReaders {
    /**
     * The following function reads the content of the config file: jdbc.properties for the database connection.
     * @return String[] array with the database details.
     * @throws IOException
     */
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
        propertiesArray[4] = props.getProperty("db.name");
        return propertiesArray;
    }

    /**
     * The following function reads the content of the config file: transport.properties for the thread pool connection.
     * @return String[] array with the transport details.
     * @throws IOException
     */
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

    /**
     * The following function verify that an input of objects are not null.
     * @param objects
     * @return true if any object is null, otherwise false.
     */
    public static boolean isAnyObjectNull(Object... objects) {
        for (Object o : objects) {
            if (o.equals("")){
                return true;
            }
        }
        return false;
    }
}
