package Dao;

import Models.User;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class UserDaoImpl implements UserDao {
    // Fields:
    private final String DB_DRIVER;
    private final String DB_URL;
    private final String DB_USER;
    private final String DB_PASSWORD;
    // SQL statements:
    private static final String INSERT_TO_USERS = "INSERT INTO users(first_name, last_name, user_name, password, email_id, mobile_number) VALUES(?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_CHANGE_PASSWORD = "UPDATE users set password=? where user_name=?";
    private static final String USER_LOGIN = "Select user_name, password from users where user_name=? and password=?";

    // CTR
    public UserDaoImpl() throws IOException {
        String[] propertiesArray = getJDBCProperties();
        DB_DRIVER = propertiesArray[0];
        DB_URL = propertiesArray[1];
        DB_USER = propertiesArray[2];
        DB_PASSWORD = propertiesArray[3];
    }


    private Connection getConnection() {
        try {
            Class.forName(DB_DRIVER);
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void close(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String[] getJDBCProperties() throws IOException {
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

    public int insert(User user) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            stmt = connection.prepareStatement(INSERT_TO_USERS, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getUserName());
            stmt.setString(4, user.getPassword());
            stmt.setString(5, user.getEmail());
            stmt.setString(6, user.getMobile());
            int result = stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                user.setFirstName(rs.getString(1));
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(stmt);
            close(connection);
            rs.close();
        }
    }

    public int updateChangePass(String pstr, String userName) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = getConnection();
            stmt = connection.prepareStatement(UPDATE_CHANGE_PASSWORD);
            stmt.setString(1, pstr);
            stmt.setString(2, userName);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(stmt);
            close(connection);
        }
    }

    @Override
    public boolean userLogin(String userName, String password) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            stmt = connection.prepareStatement(USER_LOGIN);
            stmt.setString(1, userName);
            stmt.setString(2, password);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            close(connection);
            close(stmt);
            rs.close();
        }
        return false;
    }


}
