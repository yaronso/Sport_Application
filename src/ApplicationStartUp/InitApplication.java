package ApplicationStartUp;

import ClientServer.ThreadPooledServer;
import DataBase.Database;
import DataBase.IDataBase;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * The main application initializer, the following class serve as the server class of our application.
 * Its responsibility is to configure the database & the thread pool server that listens to coming clients.
 */
public class InitApplication extends JFrame {
    public static void main(String[] args) throws SQLException, IOException {
        IDataBase db = Database.getInstance(); // Using the Singleton Design Pattern.
        ((Database) db).dataBaseConfig((Database) db); // Configure the database
        Connection connection = db.getDBConnection();
        String[] transportProps = Utils.PropertiesReaders.getTransportProperties();
        ThreadPooledServer server = new ThreadPooledServer(Integer.parseInt(transportProps[1])); // Configure the thread pool.
        new Thread(server).start();
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(Long.MAX_VALUE);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error was occurred in InitApplication.");
                } finally {
                    db.closeDBConnection(connection);
                    server.stop();
                }
            }
        });
    }
}
