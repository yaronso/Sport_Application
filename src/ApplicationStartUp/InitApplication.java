package ApplicationStartUp;

import ClientServer.ThreadPooledServer;
import ClientServer.WorkerRunnable;
import DataBase.Database;
import DataBase.IDataBase;
import GUI.FindGame;
import GUI.UserHome;
import GUI.UserLogin;
import jdk.jshell.execution.Util;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * The main application initializer.
 */
public class InitApplication extends JFrame {
    public static void main(String[] args) throws SQLException, IOException {
        IDataBase db = Database.getInstance(); // Dependency Injection
        ((Database) db).dataBaseConfig((Database) db);
        Connection connection = db.getDBConnection();
        String[] transportProps = Utils.PropertiesReaders.getTransportProperties();
        ThreadPooledServer server = new ThreadPooledServer(Integer.parseInt(transportProps[1]));
        new Thread(server).start();
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(Long.MAX_VALUE);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error was occured, in InitApplication.");
                } finally {
                    db.closeDBConnection(connection);
                    server.stop();
                }
            }
        });
    }
}
