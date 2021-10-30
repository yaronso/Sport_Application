package ApplicationStartUp;

import DataBase.Database;
import DataBase.IDataBase;
import GUI.UserRegistration;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class InitApplication extends JFrame {
    public static void main(String[] args) throws SQLException {
        IDataBase db = new Database(); // Dependency Injection
        ((Database) db).dataBaseConfig();
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UserRegistration frame = new UserRegistration();  // Set the register window to true
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
