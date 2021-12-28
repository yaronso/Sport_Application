package Dao;

import Models.User;
import java.io.IOException;
import java.sql.*;

/**
 * The following class is a concrete implementation of the interface UserDao.
 * This class access the database by using the JDBC API of Java.
 */
public class UserDaoImpl implements UserDao {
    // Fields:
    private final String DB_DRIVER;
    private final String DB_URL;
    private final String DB_USER;
    private final String DB_PASSWORD;
    private final String DB_NAME;
    // SQL statements:
    private static final String INSERT_TO_USERS = "INSERT INTO users(first_name, last_name, user_name, password, email_id, mobile_number) VALUES(?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_CHANGE_PASSWORD = "UPDATE users set password=? where user_name=?";
    private static final String USER_LOGIN = "Select user_name, password from users where user_name=? and password=?";
    private static final String CHECK_PW = "Select password from users where user_name=?";

    /**
     * Class Constructor
     * @throws IOException
     */
    public UserDaoImpl() throws IOException {
        String[] propertiesArray = Utils.PropertiesReaders.getJDBCProperties(); // Read the database properties.
        DB_DRIVER = propertiesArray[0];
        DB_URL = propertiesArray[1];
        DB_USER = propertiesArray[2];
        DB_PASSWORD = propertiesArray[3];
        DB_NAME = propertiesArray[4];
    }

    /**
     * Receive the database connection.
     * @return Connection
     */
    private Connection getConnection() {
        try {
            Class.forName(DB_DRIVER);
            return DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * DB Connection close.
     * @param con
     */
    private static void close(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * Query Statement close.
     * @param stmt
     */
    private static void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The following function insert a new user record to users table (Registration).
     * @param user
     * @return
     * @throws SQLException
     */
    public int insertNewUser(User user) throws SQLException {
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

    /**
     * The following function change the user's password according his new input.
     * @param pstr
     * @param userName
     * @return int
     */

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

    /**
     * The following function performs the user's login to the application (if the user was already registered).
     * @param userName
     * @param password
     * @return boolean
     * @throws SQLException
     */
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

    /**
     *  The following function perform a user's password validation in case of change password by the user.
     * @param userName
     * @return String
     */
    @Override
    public String validPassword(String userName) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String currPassword = null;
        try {
            connection = getConnection();
            stmt = connection.prepareStatement(CHECK_PW);
            stmt.setString(1, userName);
            rs = stmt.executeQuery();
            while (rs.next()) {
                currPassword = rs.getString(1);
                System.out.println(currPassword);
            }
            return currPassword;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return currPassword;
        } finally {
            close(stmt);
            close(connection);
            assert rs != null;
            rs.close();
        }
    }
}
