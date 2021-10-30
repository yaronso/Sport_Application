package ApplicationStartUp;

import DataBase.Database;
import DataBase.IDataBase;
import GUI.UserLogin;
import GUI.UserRegistration;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * The main application initializer.
 */
public class InitApplication extends JFrame {
    public static void main(String[] args) throws SQLException {
        IDataBase db = new Database(); // Dependency Injection
        ((Database) db).dataBaseConfig();
        Connection connection = db.getDBConnection();
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UserLogin loginFrame = new UserLogin(connection);
                    loginFrame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
