package ApplicationStartUp;

import DataBase.Database;
import DataBase.IDataBase;
import GUI.FindGame;
import GUI.UserHome;
import GUI.UserLogin;
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
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    //FindGame findGame = new FindGame(connection);
                    //findGame.setVisible(true);
                    UserLogin loginFrame = new UserLogin();
                    loginFrame.setVisible(true);
                    //UserHome userHome = new UserHome("yasofer", connection);
                    //userHome.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error was occured, in InitApplication.");
                } finally {
                    db.closeDBConnection(connection);
                }
            }
        });
    }
}
