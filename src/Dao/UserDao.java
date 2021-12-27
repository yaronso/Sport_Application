package Dao;

import java.sql.SQLException;
import Models.User;

/**
 *  The following interface is a part of DAO design pattern for the object User under the package Models.
 *  Each method demonstrates different database's queries, updates & responses for the table users in our database schema.
 *  Each of the below methods is triggered by the class UserController which triggered by the client side inside GUI package.
 */
public interface UserDao {
    int insertNewUser(User user) throws SQLException;
    boolean userLogin(String userName, String password) throws SQLException;
    int updateChangePass(String pstr, String userName) throws SQLException;
    String validPassword(String userName) throws SQLException;
}
